package com.business.service;

import com.business.entity.SoldProducts;
import com.business.entity.WalmartProduct;
import com.util.constants.SearchBy;
import com.util.constants.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.primefaces.model.SortOrder;
import java.util.logging.Logger;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;

@Stateless
@DeclareRoles({"adminRole", "userRole"})
public class WalmartProductService {

    private final static Logger LOGGER = Logger.getLogger(WalmartProductService.class.getName());

    // ======== connecting to a database ========= //
    @PersistenceContext(unitName = "itrPU")
    private EntityManager em;

    @RolesAllowed({"adminRole", "userRole"})
    public WalmartProduct findProduct(long id) {
        return em.find(WalmartProduct.class, id);
    }

    public void activateProduct(List<WalmartProduct> products, String status) {
        products.forEach((wp) -> {
            em.createQuery("UPDATE WalmartProduct SET status = '" + status + "' WHERE id = " + wp.getId()).executeUpdate();
        });
    }

    @RolesAllowed({"adminRole"})
    public List<WalmartProduct> findProductsByStatus(String status) {

        TypedQuery<WalmartProduct> query;
        if ("SCHEDULED".equals(status) || (status) == null) {
            query = em.createQuery("SELECT p FROM WalmartProduct p WHERE status LIKE 'SCHEDULED' OR status IS NULL", WalmartProduct.class);
        } else {
            query = em.createQuery("SELECT p FROM WalmartProduct p WHERE status = :status", WalmartProduct.class);
            query.setParameter("status", status);
        }
        return query.getResultList();
    }

    @RolesAllowed({"adminRole"})
    public void updateStore(List<WalmartProduct> products, String store) {
        products.forEach((wp) -> {
            em.createQuery("UPDATE WalmartProduct SET store = '" + store + "' WHERE id = " + wp.getId()).executeUpdate();
        });
    }

    @RolesAllowed({"adminRole"})
    public void addProduct(WalmartProduct product) {
        if (product.getId() == null) {
            em.persist(product);
            LOGGER.info("product successfully saved...");
        }
    }

    @RolesAllowed({"adminRole"})
    public void deleteProduct(WalmartProduct product) {
        if (product.getId() != null) {
            em.remove((product));
            LOGGER.log(Level.INFO, "WalmartProduct with ID {0} successfully removed...", product.getId());
        }
    }

    @RolesAllowed({"adminRole"})
    public void update(WalmartProduct product) {
        em.merge(product);
        LOGGER.log(Level.INFO, "walmartProduct with ID {0} has been successfully updated", product.getId());
    }

    @RolesAllowed({"adminRole"})
    public void addSoldProduct(Long productId, String soldBy, Date soldDate) {
        LocalDateTime fromDate = soldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        SoldProducts soldProduct = new SoldProducts(productId, soldBy, fromDate);
        em.persist(soldProduct);
        LOGGER.log(Level.INFO, "Sold informations for walmartProduct with ID {0} successfully saved.", productId);
    }

    public long countProductsWithoutStatus() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM WalmartProduct c WHERE status IS NULL", Long.class);
        return query.getSingleResult();
    }

    public long countSubproducts(String status) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM WalmartProduct c WHERE status = :status", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }

    @RolesAllowed({"adminRole", "userRole"})
    public List<WalmartProduct> findPage(int first, int pageSize, String status, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        System.out.println("==========================================");
        System.out.println("first = " + first + " pageSize = " + pageSize + " sortField = " + sortField + " sortOrder = " + sortOrder + " filters = " + filters);
        System.out.println("==========================================");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WalmartProduct> cq = cb.createQuery(WalmartProduct.class);
        Root<WalmartProduct> product = cq.from(WalmartProduct.class);

        // selecting
        cq.select(product);

        // ordering
        if (sortOrder != null) {
            if (sortField != null) {
                if (sortOrder.equals(SortOrder.ASCENDING)) {
                    cq.orderBy(cb.asc(product.get(sortField)));
                } else if (sortOrder.equals(SortOrder.DESCENDING)) {
                    cq.orderBy(cb.asc(product.get(sortField)));
                }
            } else {
                cq.orderBy(cb.asc(product.get("id")));
            }
        }

        // filtering
        Predicate filterCondition = cb.conjunction();
        for (Map.Entry<String, Object> filter : filters.entrySet()) {
            if (!filter.getKey().equals("globalFilter")) { // if you really need a global filter then implement it here
                Path<Object> pathFilter = product.get(filter.getKey());
                if (!filter.getValue().equals("")) {
                    if (pathFilter != null && pathFilter.getClass().isPrimitive()) {
                        filterCondition = cb.and(filterCondition, cb.equal(pathFilter, filter.getValue()));
                    } else {
                        filterCondition = cb.and(filterCondition, cb.like(pathFilter.as(String.class), filter.getValue() + "%"));
                    }
                }
            }
        }
        cq.where(filterCondition);

        if (status == null) {
            // Find ACTIVE and SOLD for user roles
            cq.where(cb.or(cb.equal(product.get("status"), Status.ACTIVE), cb.equal(product.get("status"), Status.SOLD)));
        } else {
            cq.where(cb.equal(product.get("status"), status));
        }

        //pagination
        TypedQuery<WalmartProduct> tq = em.createQuery(cq);
        if (pageSize >= 0) {
            tq.setMaxResults(pageSize);
        }
        if (first >= 0) {
            tq.setFirstResult(first);
        }
        return tq.getResultList();
    }

    @RolesAllowed({"adminRole"})
    public List<WalmartProduct> findAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WalmartProduct> cq = cb.createQuery(WalmartProduct.class);
        Root<WalmartProduct> product = cq.from(WalmartProduct.class);

        // selecting
        cq.select(product);

        TypedQuery<WalmartProduct> tq = em.createQuery(cq);
        return tq.getResultList();
    }

    public List<WalmartProduct> findAllForUserRoles() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WalmartProduct> cq = cb.createQuery(WalmartProduct.class);
        Root<WalmartProduct> product = cq.from(WalmartProduct.class);

        // selecting only products with status ACTIVE or SOLD for employees with user role
        cq.select(product).where(cb.or(
                cb.equal(product.get("status"), Status.ACTIVE), cb.equal(product.get("status"), Status.SOLD)));

        TypedQuery<WalmartProduct> tq = em.createQuery(cq);
        return tq.getResultList();
    }

    public List<WalmartProduct> findAllActive(int page, int pageSize, String filter, String searchWords) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WalmartProduct> cq = cb.createQuery(WalmartProduct.class);
        Root<WalmartProduct> product = cq.from(WalmartProduct.class);

        cq.select(product).where(cb.equal(product.get("status"), Status.ACTIVE));

        String pattern;
        if ((searchWords != null) && !(searchWords.trim().isEmpty())) {
            pattern = "%" + searchWords + "%";

            switch (filter) {
                case SearchBy.CATEGORY:
                    cq.select(product).where(cb.equal(product.get("status"), Status.ACTIVE), cb.like(product.get("category"), pattern));
                    break;
                case SearchBy.PRODUCT_NAME:
                    cq.select(product).where(cb.equal(product.get("status"), Status.ACTIVE), cb.like(product.get("productName"), pattern));
                    break;
                //                    +++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                case SearchBy.DEPARTMENT:
                    cq.select(product).where(cb.equal(product.get("status"), Status.ACTIVE), cb.like(product.get("department"), pattern));
                    System.out.println("service side search by department +++++++++++++++++++++++++++++" + filter);
                    break;
                default:
                    cq.select(product).where(cb.equal(product.get("status"), Status.ACTIVE),
                            cb.or(cb.like(product.get("category"), pattern),
                                    cb.like(product.get("productName"), pattern)));
                    break;
            }
        }

        // pagination
        TypedQuery<WalmartProduct> tq = em.createQuery(cq);
        if (pageSize >= 0) {
            tq.setMaxResults(pageSize);
        }
        if (page > 0) {
            tq.setFirstResult(page * pageSize);
        }
        return tq.getResultList();
    }

    @RolesAllowed({"adminRole"})
    public void persistUploadedData(List<Future<List<WalmartProduct>>> resultList) {

        resultList.forEach((future) -> {
            if (future.isDone()) {
                try {
                    (future.get()).forEach(t -> {
                        em.persist(t);
                    });
                } catch (InterruptedException | ExecutionException ex) {
                    Logger.getLogger(WalmartProductService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}

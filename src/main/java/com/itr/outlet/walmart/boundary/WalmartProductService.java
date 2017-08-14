package com.itr.outlet.walmart.boundary;

import com.itr.entity.SoldProducts;
import com.itr.entity.WalmartProduct;
import com.itr.enums.Status;
import java.time.LocalDate;
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

        WalmartProduct updatedWalmartProduct = em.merge(product);

        //em.persist(updatedWalmartProduct);
        LOGGER.log(Level.INFO, "walmartProduct with ID {0} has been successfully updated", product.getId());
    }

    @RolesAllowed({"adminRole"})
    public void addSoldProduct(Long productId, String soldBy, Date soldDate) {
        LocalDate fromDate = soldDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        SoldProducts soldProduct = new SoldProducts(productId, soldBy, fromDate);
        em.persist(soldProduct);
        LOGGER.log(Level.INFO, "Sold informations for walmartProduct with ID {0} successfully saved.", productId);
    }

    @RolesAllowed({"adminRole", "userRole"})
    // in case you need to count all products
    public long countProducts() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM WalmartProduct c WHERE status IS NULL", Long.class);
        return query.getSingleResult();
    }

    @RolesAllowed({"adminRole", "userRole"})
    public long countSubproducts(String status) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM WalmartProduct c WHERE status = :status", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }

    @RolesAllowed({"adminRole", "userRole"})
    public List<WalmartProduct> findPage(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

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

    @RolesAllowed({"adminRole", "userRole"})
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

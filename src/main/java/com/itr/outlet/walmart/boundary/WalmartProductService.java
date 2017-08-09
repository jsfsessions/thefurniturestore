package com.itr.outlet.walmart.boundary;

import com.itr.entity.WalmartProduct;
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

@Stateless
public class WalmartProductService {

    private final static Logger LOGGER = Logger.getLogger(WalmartProductService.class.getName());

    // ======== connecting to a database ========= //    
    @PersistenceContext(unitName = "itrPU")
    private EntityManager em;

    public WalmartProduct findProduct(long id) {
        return em.find(WalmartProduct.class, id);
    }
    
    public void activateProduct(List<WalmartProduct> products, String status) { 
        products.forEach((wp) -> {
            em.createQuery("UPDATE WalmartProduct SET status = '" + status + "' WHERE id = " + wp.getId()).executeUpdate();
        });
    }
    
    public void updateStore(List<WalmartProduct> products, String store) {
        products.forEach((wp) -> {
            em.createQuery("UPDATE WalmartProduct SET store = '" + store + "' WHERE id = " + wp.getId()).executeUpdate();
        });
    }

    public void addProduct(WalmartProduct product) {
        if (product.getId() == null) {
            em.persist(product);
            LOGGER.info("product successfully saved...");
        }
    }

    public void deleteProduct(WalmartProduct product) {
        if (product.getId() != null) {
            em.remove((product));
            LOGGER.log(Level.INFO, "WalmartProduct with ID {0} successfully removed...", product.getId());
        }
    }

    public void update(WalmartProduct product) {
        
        WalmartProduct updatedWalmartProduct = em.merge(product);
        
        //em.persist(updatedWalmartProduct);
        LOGGER.log(Level.INFO, "walmartProduct with ID {0} has been successfully updated", product.getId());
    }

    // in case you need to count all products
    public long countProducts() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM WalmartProduct c WHERE status IS NULL", Long.class);
        return query.getSingleResult();
    }

    public long countSubproducts(String status) {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM WalmartProduct c WHERE status = :status", Long.class);
        query.setParameter("status", status);
        return query.getSingleResult();
    }
    
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

    public List<WalmartProduct> findAll() {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<WalmartProduct> cq = cb.createQuery(WalmartProduct.class);
        Root<WalmartProduct> product = cq.from(WalmartProduct.class);

        // selecting
        cq.select(product);

        TypedQuery<WalmartProduct> tq = em.createQuery(cq);
        return tq.getResultList();
    }

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

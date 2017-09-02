package com.business.service;

import java.time.LocalDateTime;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class SalesReportService {

    @PersistenceContext(unitName = "itrPU")
    private EntityManager em;

    public List<Object> getReportBetweenDays(LocalDateTime startDate, LocalDateTime endDate) {
        Query query;

        if (endDate == null) {
            query = em.createQuery("SELECT p.productName, p.upc, p.salePrice, p.store, s.soldBy, s.soldDate "
                    + "FROM SoldProducts s JOIN WalmartProduct p ON s.productId = p.id "
                    + "WHERE s.soldDate >= :startDate");
        } else {
            query = em.createQuery("SELECT p.productName, p.upc, p.salePrice, p.store, s.soldBy, s.soldDate "
                    + "FROM SoldProducts s JOIN WalmartProduct p ON s.productId = p.id "
                    + "WHERE s.soldDate >= :startDate AND s.soldDate < :endDate");
            query.setParameter("endDate", endDate);
        }
        query.setParameter("startDate", startDate);

        return query.getResultList();
    }
}

package com.business.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class SoldProducts implements Serializable {

    private static final long serialVersionUID = 1960928695578629995L;

    @Id
    @Column(name = "ID", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "productId", nullable = false)
    private Long productId;

    @NotNull
    @Column(name = "soldBy", nullable = false)
    private String soldBy;

    @NotNull
    @Column(name = "soldDate", nullable = false)
    private LocalDateTime soldDate;

    public SoldProducts() {
    }

    public SoldProducts(Long productId, String soldBy, LocalDateTime soldDate) {
        this.productId = productId;
        this.soldBy = soldBy;
        this.soldDate = soldDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getSoldBy() {
        return soldBy;
    }

    public void setSoldBy(String soldBy) {
        this.soldBy = soldBy;
    }

    public LocalDateTime getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDateTime soldDate) {
        this.soldDate = soldDate;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.productId);
        hash = 47 * hash + Objects.hashCode(this.soldBy);
        hash = 47 * hash + Objects.hashCode(this.soldDate);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SoldProducts other = (SoldProducts) obj;
        if (!Objects.equals(this.soldBy, other.soldBy)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        return Objects.equals(this.soldDate, other.soldDate);
    }

    @Override
    public String toString() {
        return "SoldProducts{" + "id=" + id + ", productId=" + productId + ", soldBy=" + soldBy + ", soldDate=" + soldDate + '}';
    }
}

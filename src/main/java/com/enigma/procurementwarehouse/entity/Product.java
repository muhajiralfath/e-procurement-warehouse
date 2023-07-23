package com.enigma.procurementwarehouse.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "m_product")
public class Product {
    @GenericGenerator(strategy = "uuid2", name = "system-uuid")
    @GeneratedValue(generator = "system-uuid")
    @Id
    private String id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "is_active")
    private Boolean isDeleted;

    @Column(name = "created_at", updatable = false)
    @CreatedDate
    private Date createdAt;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedDate
    private String updatedBy;

    @Column(name = "updated_at")
    @LastModifiedDate
    private Date updatedAt;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference
    private List<ProductPrice> productPrices;

    public List<ProductPrice> getProductPrices() {
        return Collections.unmodifiableList(productPrices);
    }

    public void addProductPrice(ProductPrice productPrice) {
        productPrices.add(productPrice);
    }
}

package com.mvpmatch.vendingmachine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Data
@Table(name="products")
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_generator")
    @SequenceGenerator(name="product_generator", sequenceName = "products_seq_id", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private Integer amountAvailable;

    @Column(nullable = false)
    private Integer cost;

    @Column(nullable = false)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

}


package com.mvpmatch.vendingmachine.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@Table(name="products")
@EqualsAndHashCode(callSuper = true)
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


package com.mvpmatch.vendingmachine.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name="coin_inventory")
public class CoinInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coin_inventory_generator")
    @SequenceGenerator(name="coin_inventory_generator", sequenceName = "coin_inventory_seq_id", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private Integer coinValue;

    @Column(nullable = false)
    private Integer amount;

}

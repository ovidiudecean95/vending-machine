package com.mvpmatch.vendingmachine.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="coin_inventory")
public class CoinInventory {

    public static final List<Integer> VALID_COINS = List.of(5, 10, 20, 50, 100);

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coin_inventory_generator")
    @SequenceGenerator(name="coin_inventory_generator", sequenceName = "coin_inventory_seq_id", allocationSize=1)
    private Integer id;

    @Column(nullable = false)
    private Integer coinValue;

    @Column(nullable = false)
    private Integer amount;

    public void incrementAmount() {
        ++amount;
    }

}

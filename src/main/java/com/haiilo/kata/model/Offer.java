package com.haiilo.kata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "offers")
public class Offer {

    @Id
    private Long itemId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @OneToOne
    @MapsId
    @JoinColumn(name = "item_id")
    private Item item;
}

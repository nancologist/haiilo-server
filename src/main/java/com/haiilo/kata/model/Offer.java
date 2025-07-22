package com.haiilo.kata.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JsonBackReference
    private Item item;
}

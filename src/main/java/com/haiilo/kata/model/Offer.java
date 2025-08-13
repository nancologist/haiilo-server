package com.haiilo.kata.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "offers")
public class Offer {

    @Id
    @JsonIgnore
    private Long itemId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "item_id")
    @JsonBackReference
    private Item item;
}

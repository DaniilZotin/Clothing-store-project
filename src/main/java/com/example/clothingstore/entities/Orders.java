package com.example.clothingstore.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

@Entity(name = "orders")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "customer_id")
    long customerId;

    @Column(name = "clothes_id")
    long clothesId;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "price")
    Double price;

}

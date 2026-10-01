package com.trainosys.shopapi.cart;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long cartItemId;
    long userId;
    long productId;
    int quantity;
}

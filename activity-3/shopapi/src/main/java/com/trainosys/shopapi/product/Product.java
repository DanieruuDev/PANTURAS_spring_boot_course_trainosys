package com.trainosys.shopapi.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    int id;
    String name;
    double price;
    String category;
    int stock;

}

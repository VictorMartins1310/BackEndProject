package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Table(schema = "AppTodo")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    productID;
    private String  name;
    private String  brand;
    private BigDecimal price;
    private int      qty;
    private ProductType type;
    /** True means products bought, False means to be bought */
    private Boolean bought = false;

    public Product(String name, String brand, BigDecimal price, int qty, ProductType type) {
        this.name   = name;
        this.brand  = brand;
        this.price  = price;
        this.qty    = qty;
        this.type   = type;
    }

    public Product productBought(int qty){
        this.qty -= qty;
        this.bought = this.qty <= 0;
        return this;
    }

    public BigDecimal totalPrice(){ return new BigDecimal(qty).multiply(price); }
}
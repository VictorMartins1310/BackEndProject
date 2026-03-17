package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@Table(schema = "AppTodo")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long    productID;
    private String  name;
    private String  brand;
    private BigDecimal price;
    private int      qty;
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "app_todo.product_type")
    private ProductType type = ProductType.Other;
    /** True means products bought, False means to be bought */
    private Boolean bought = false;

    public Product(String name, String brand, BigDecimal price, int qty, ProductType type) {
        setName(name);
        setBrand(brand);
        setPrice(price);
        setQty(qty);
        setType(type);
    }

    public Product productBought(int qty){
        this.qty -= qty;
        this.bought = this.qty <= 0;
        return this;
    }

    public BigDecimal totalPrice(){ return new BigDecimal(qty).multiply(price); }
}
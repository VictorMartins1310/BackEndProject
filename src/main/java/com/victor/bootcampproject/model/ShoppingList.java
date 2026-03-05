package com.victor.bootcampproject.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
//@DynamicUpdate
@NoArgsConstructor
//@RequiredArgsConstructor
@AllArgsConstructor
@Table(schema = "AppTodo")
public class ShoppingList extends TodoItem{
    private String marketName;
    @OneToMany
    private List<Product> products  = new ArrayList<>();
    public void addProduct(Product product) {
        products.add(product);
    }
    public BigDecimal getTotal(){
        BigDecimal total = new BigDecimal("0.00");
        for (Product prod: products)
            total = total.add(prod.totalPrice());
        return total;
    }
    public ShoppingList(AppUser user, String marketName) {
        this.marketName = marketName;
        super.setUser(user);
    }
}
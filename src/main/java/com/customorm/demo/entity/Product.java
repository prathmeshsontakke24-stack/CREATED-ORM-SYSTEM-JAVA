package com.customorm.demo.entity;

import com.customorm.annotations.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "in_stock")
    private Boolean inStock;

    public Product(){}

    public Product(String productName, Double price, Integer quantity, Boolean inStock){
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.inStock = inStock;
    }

    // getters setters

    public Long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Boolean getInStock() {
        return inStock;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @Override
    public String toString() {
        return "Product{id=" + id +
                ", name='" + productName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity + '}';
    }
}
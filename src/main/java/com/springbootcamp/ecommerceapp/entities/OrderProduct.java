//package com.springbootcamp.ecommerceapp.entities;
//
//import javax.persistence.*;
//import java.util.LinkedHashSet;
//import java.util.Set;
//
//@Entity
//public class OrderProduct {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//    private Integer Quantity;
//    private Double price;
//    private String productDetails;
//
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;
//
//
//    @ManyToOne
//    @JoinColumn(name = "product_variation_id")
//    private ProductVariation productVariation;
//
//
//    @OneToMany(mappedBy = "orderProduct", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private Set<OrderStatus> lifecycle;
//
//    public OrderProduct() {
//    }
//
//    public OrderProduct(Integer quantity, Double price, String productDetails) {
//        Quantity = quantity;
//        this.price = price;
//        this.productDetails = productDetails;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Integer getQuantity() {
//        return Quantity;
//    }
//
//    public void setQuantity(Integer quantity) {
//        Quantity = quantity;
//    }
//
//    public Double getPrice() {
//        return price;
//    }
//
//    public void setPrice(Double price) {
//        this.price = price;
//    }
//
//    public String getProductDetails() {
//        return productDetails;
//    }
//
//    public void setProductDetails(String productDetails) {
//        this.productDetails = productDetails;
//    }
//
//    public Order getOrder() {
//        return order;
//    }
//
//    public void setOrder(Order order) {
//        this.order = order;
//    }
//
//    public ProductVariation getProductVariation() {
//        return productVariation;
//    }
//
//    public void setProductVariation(ProductVariation productVariation) {
//        this.productVariation = productVariation;
//    }
//
//    public Set<OrderStatus> getLifecycle() {
//        return lifecycle;
//    }
//
//    public void setLifecycle(Set<OrderStatus> lifecycle) {
//        this.lifecycle = lifecycle;
//    }
//
//
//    @Override
//    public String toString() {
//        return "Order_Product{" +
//                "id=" + id +
//                ", Quantity=" + Quantity +
//                ", price=" + price +
//                ", productDetails='" + productDetails + '\'' +
//                ", order=" + order +
//                '}';
//    }
//
//    public void addStatus(OrderStatus status){
//        if(status != null){
//            if(lifecycle == null)
//                lifecycle = new LinkedHashSet<>();
//            lifecycle.add(status);
//            status.setOrderProduct(this);
//        }
//    }
//}

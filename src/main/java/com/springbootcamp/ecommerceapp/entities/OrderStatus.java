//package com.springbootcamp.ecommerceapp.entities;
//
//import javax.persistence.*;
//
//@Entity
//public class OrderStatus {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    String from;
//    String to;
//    String transitionNotes;
//
//    @ManyToOne
//    @JoinColumn(name = "order_product_id")
//    private OrderProduct orderProduct;
//
//
//    public OrderStatus() {
//    }
//
//    public OrderStatus(String from, String to, String transitionNotes) {
//        this.from = from;
//        this.to = to;
//        this.transitionNotes = transitionNotes;
//    }
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getFrom() {
//        return from;
//    }
//
//    public void setFrom(String from) {
//        this.from = from;
//    }
//
//    public String getTo() {
//        return to;
//    }
//
//    public void setTo(String to) {
//        this.to = to;
//    }
//
//    public String getTransitionNotes() {
//        return transitionNotes;
//    }
//
//    public void setTransitionNotes(String transitionNotes) {
//        this.transitionNotes = transitionNotes;
//    }
//
//    public OrderProduct getOrderProduct() {
//        return orderProduct;
//    }
//
//    public void setOrderProduct(OrderProduct orderProduct) {
//        this.orderProduct = orderProduct;
//    }
//
//    @Override
//    public String toString() {
//        return "OrderStatus{" +
//                "id=" + id +
//                ", from='" + from + '\'' +
//                ", to='" + to + '\'' +
//                ", transitionNotes='" + transitionNotes + '\'' +
//                ", orderProduct=" + orderProduct +
//                '}';
//    }
//}

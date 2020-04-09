//package com.springbootcamp.ecommerceapp.entities;
//
//import javax.persistence.*;
//import java.util.Date;
//import java.util.HashSet;
//import java.util.Set;
//
//@Entity
//public class Order {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    private Double amountPaid;
//
//    @Temporal(TemporalType.DATE)
//    private Date dateCreated;
//    private String paymentMethod;
//
//    @Embedded
//    private OrderAddress deliveryAddress;
//
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "customerUserId")
//    private Customer customer;
//
//
//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private Set<OrderProduct> items;
//
//    public Order() {
//    }
//
//    public Order(Double amountPaid, Date dateCreated, String paymentMethod) {
//        this.amountPaid = amountPaid;
//        this.dateCreated = dateCreated;
//        this.paymentMethod = paymentMethod;
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
//    public Double getAmountPaid() {
//        return amountPaid;
//    }
//
//    public void setAmountPaid(Double amountPaid) {
//        this.amountPaid = amountPaid;
//    }
//
//    public Date getDateCreated() {
//        return dateCreated;
//    }
//
//    public void setDateCreated(Date dateCreated) {
//        this.dateCreated = dateCreated;
//    }
//
//    public String getPaymentMethod() {
//        return paymentMethod;
//    }
//
//    public void setPaymentMethod(String paymentMethod) {
//        this.paymentMethod = paymentMethod;
//    }
//
//    public OrderAddress getDeliveryAddress() {
//        return deliveryAddress;
//    }
//
//    public void setDeliveryAddress(OrderAddress deliveryAddress) {
//        this.deliveryAddress = deliveryAddress;
//    }
//
//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }
//
//    public Set<OrderProduct> getItems() {
//        return items;
//    }
//
//    public void setItems(Set<OrderProduct> items) {
//        this.items = items;
//    }
//
//    @Override
//    public String toString() {
//        return "Order{" +
//                "id=" + id +
//                ", amountPaid=" + amountPaid +
//                ", dateCreated=" + dateCreated +
//                ", paymentMethod='" + paymentMethod + '\'' +
//                ", deliveryAddress=" + deliveryAddress +
//                ", customer=" + customer +
//                '}';
//    }
//
//    public void addOrderProduct(OrderProduct item){
//        if(item!=null){
//            if(items==null)
//                items = new HashSet<>();
//
//            items.add(item);
//            item.setOrder(this);
//        }
//    }
//}

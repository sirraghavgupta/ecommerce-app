package com.springbootcamp.ecommerceapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Double amountPaid;

    @Temporal(TemporalType.DATE)
    private Date dateCreated;
    private String paymentMethod;

    @Embedded
    private OrderAddress deliveryAddress;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "customerUserId")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<OrderProduct> items;

    public Orders(Date dateCreated, String paymentMethod) {
        this.amountPaid = amountPaid;
        this.dateCreated = dateCreated;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", amountPaid=" + amountPaid +
                ", dateCreated=" + dateCreated +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", deliveryAddress=" + deliveryAddress +
                ", customer=" + customer +
                '}';
    }

    public void addOrderProduct(OrderProduct item) {
        if (item != null) {
            if (items == null)
                items = new HashSet<>();

            items.add(item);
            item.setOrder(this);
        }
    }

    public void calculateBillAmount(){
        Double amount = 0D;
        for(OrderProduct item : items){
            amount+=item.getPrice();
        }
        setAmountPaid(amount);
    }
}

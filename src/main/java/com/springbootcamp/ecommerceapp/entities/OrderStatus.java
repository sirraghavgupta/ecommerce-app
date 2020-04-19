package com.springbootcamp.ecommerceapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderStatus extends AuditInformation{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    String fromStatus;
    String toStatus;
    String transitionNotes;

    @ManyToOne
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    public OrderStatus(String fromStatus, String toStatus, String transitionNotes) {
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
        this.transitionNotes = transitionNotes;
    }


    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "OrderStatus{" +
                "id=" + id +
                ", from='" + fromStatus + '\'' +
                ", to='" + toStatus + '\'' +
                ", transitionNotes='" + transitionNotes + '\'' +
                ", orderProduct=" + orderProduct.getId() +
                '}';
    }
}

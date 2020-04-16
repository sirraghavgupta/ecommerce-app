package com.springbootcamp.ecommerceapp.entities;

import com.springbootcamp.ecommerceapp.utils.HashMapConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer Quantity=1;
    private Double price;

    @Convert(converter = HashMapConverter.class)
    private Map<String, String> productAttributes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Orders order;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "productVariationId")
    private ProductVariation productVariation;


    @OneToMany(mappedBy = "orderProduct", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OrderStatus> lifecycle;

    public void setProductVariation(ProductVariation productVariation) {
        this.productVariation = productVariation;
        this.price = productVariation.getPrice();
        this.productAttributes = productVariation.getProductAttributes();
        productVariation.addOrderProduct(this);
    }

    @Override
    public String toString() {
        return "OrderProduct{" +
                "id=" + id +
                ", Quantity=" + Quantity +
                ", price=" + price +
                ", productAttributes=" + productAttributes +
                ", order=" + order.getId() +
                ", productVariation=" + productVariation.getId() +
                '}';
    }

    public void addStatus(OrderStatus status){
        if(status != null){
            if(lifecycle == null)
                lifecycle = new LinkedHashSet<>();
            lifecycle.add(status);
            status.setOrderProduct(this);
        }
    }
}

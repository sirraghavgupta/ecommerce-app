package com.springbootcamp.ecommerceapp.entities;

import com.springbootcamp.ecommerceapp.utils.HashMapConverter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ProductVariation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer quantityAvailable;
    private Double price;
    private String primaryImageName;

    @Convert(converter = HashMapConverter.class)
    private Map<String, Object> productAttributes;

    private boolean isDeleted = false;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;


    @OneToMany(mappedBy = "productVariation", fetch = FetchType.EAGER)
    private Set<OrderProduct> orderedProducts;


    public ProductVariation(Integer quantityAvailable, Double price) {
        this.quantityAvailable = quantityAvailable;
        this.price = price;
    }

    @Override
    public String toString() {
        return "ProductVariation{" +
                "id=" + id +
                ", quantityAvailable=" + quantityAvailable +
                ", price=" + price +
                ", primaryImageName='" + primaryImageName + '\'' +
                ", productAttributes=" + productAttributes +
                ", isDeleted=" + isDeleted +
                '}';
    }


    public void addOrderProduct(OrderProduct orderProduct){
        if(orderProduct != null){
            if(orderedProducts == null)
                orderedProducts = new LinkedHashSet<>();
            orderedProducts.add(orderProduct);
        }
    }
}

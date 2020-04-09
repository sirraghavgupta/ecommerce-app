package com.springbootcamp.ecommerceapp.entities;

import com.springbootcamp.ecommerceapp.utils.HashMapConverter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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


//    @OneToMany(mappedBy = "productVariation", fetch = FetchType.EAGER)
//    private Set<OrderProduct> orderedProducts;

    public ProductVariation() {
    }

    public ProductVariation(Integer quantityAvailable, Double price) {
        this.quantityAvailable = quantityAvailable;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Integer quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getPrimaryImageName() {
        return primaryImageName;
    }

    public void setPrimaryImageName(String primaryImageName) {
        this.primaryImageName = primaryImageName;
    }

    public Map<String, Object> getProductAttributes() {
        return productAttributes;
    }

    public void setProductAttributes(Map<String, Object> productAttributes) {
        this.productAttributes = productAttributes;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

//    public Set<OrderProduct> getOrderedProducts() {
//        return orderedProducts;
//    }

//    public void setOrderedProducts(Set<OrderProduct> orderedProducts) {
//        this.orderedProducts = orderedProducts;
//    }

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


//    public void addOrderProduct(OrderProduct orderProduct){
//        if(orderProduct != null){
//            if(orderedProducts == null)
//                orderedProducts = new LinkedHashSet<>();
//            orderedProducts.add(orderProduct);
//            orderProduct.setProductVariation(this);
//        }
//    }
}

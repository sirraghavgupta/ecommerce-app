package com.springbootcamp.ecommerceapp.entities;

import com.springbootcamp.ecommerceapp.utils.CartId;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Cart extends AuditInformation{

    @EmbeddedId
    private CartId id = new CartId();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId("customerUserId")
    private Customer owner;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @MapsId("productVariationId")
    private ProductVariation item;

    private Integer quantity;
    private boolean isWishlisted;

    public Cart(Integer quantity, boolean isWishlisted) {
        this.quantity = quantity;
        this.isWishlisted = isWishlisted;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", owner=" + owner.getEmail() +
                ", item=" + item.getId() +
                ", quantity=" + quantity +
                ", isWishlisted=" + isWishlisted +
                '}';
    }
}

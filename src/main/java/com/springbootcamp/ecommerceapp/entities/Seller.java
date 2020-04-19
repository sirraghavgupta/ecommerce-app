package com.springbootcamp.ecommerceapp.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Where(clause = "is_deleted=false")
public class Seller extends User{

    private String GST;
    private String companyName;
    private String companyContact;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private Set<Product> products;


    public Seller() {
        this.addRole(new Role(2, "ROLE_SELLER"));
    }

    public Seller(String email, String firstName, String middleName, String lastName, String GST, String companyName, String companyContact) {
        super(email, firstName, middleName, lastName);
        this.GST = GST.toUpperCase();
        this.companyName = companyName;
        this.companyContact = companyContact;
        this.addRole(new Role(2, "ROLE_SELLER"));
    }

    @Override
    public String toString() {
        return "Seller{" +
                super.toString() +
                "GST='" + GST + '\'' +
                ", companyName='" + companyName + '\'' +
                ", companyContact='" + companyContact + '\'' +
                '}';
    }

    public void addProduct(Product product){
        if(product != null){
            if(products == null)
                products = new HashSet<Product>();

            products.add(product);

            product.setSeller(this);
        }
    }

    @Override
    public void addAddress(Address address) {
        if(address !=null){
            Set<Address> addresses = new HashSet<Address>();
            addresses.add(address);
            this.setAddresses(addresses);
            address.setUser(this);
        }
    }
}

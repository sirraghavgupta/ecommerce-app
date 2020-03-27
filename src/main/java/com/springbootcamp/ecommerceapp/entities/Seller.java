package com.springbootcamp.ecommerceapp.entities;

import sun.text.UCompactIntArray;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
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

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
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
}

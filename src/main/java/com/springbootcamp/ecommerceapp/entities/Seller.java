//package com.springbootcamp.ecommerceapp.entities;
//
//import sun.text.UCompactIntArray;
//
//import javax.persistence.*;
//
//@Entity
//public class Seller {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long user_id;
//
//    @Column(name = "GST")
//    private String GST;
//
////    @ForeignKey
//    private String companyAddressId;
//    private String companyContact;
//    private String companyName;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn
//    private User user;
//
//    public Seller() {
//    }
//
//    public Seller(String GST, String companyAddressId, String companyContact, String companyName) {
//        this.GST = GST;
//        this.companyAddressId = companyAddressId;
//        this.companyContact = companyContact;
//        this.companyName = companyName;
//    }
//
//    public String getGST() {
//        return GST;
//    }
//
//    public void setGST(String GST) {
//        this.GST = GST;
//    }
//
//    public String getCompanyAddressId() {
//        return companyAddressId;
//    }
//
//    public void setCompanyAddressId(String companyAddressId) {
//        this.companyAddressId = companyAddressId;
//    }
//
//    public String getCompanyContact() {
//        return companyContact;
//    }
//
//    public void setCompanyContact(String companyContact) {
//        this.companyContact = companyContact;
//    }
//
//    public String getCompanyName() {
//        return companyName;
//    }
//
//    public void setCompanyName(String companyName) {
//        this.companyName = companyName;
//    }
//
//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }
//
//    @Override
//    public String toString() {
//        return "Seller{" +
//                "GST='" + GST + '\'' +
//                ", companyAddressId='" + companyAddressId + '\'' +
//                ", companyContact='" + companyContact + '\'' +
//                ", companyName='" + companyName + '\'' +
//                '}';
//    }
//}

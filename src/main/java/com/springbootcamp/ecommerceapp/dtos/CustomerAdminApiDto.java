package com.springbootcamp.ecommerceapp.dtos;

public class CustomerAdminApiDto {

    private Long id;
    private String fullName;
    private String email;
    private boolean isActive;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String firstName, String middleName, String lastName){
        this.fullName = firstName + " " + middleName + " " +lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

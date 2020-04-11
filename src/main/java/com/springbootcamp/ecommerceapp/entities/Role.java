package com.springbootcamp.ecommerceapp.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Role implements GrantedAuthority {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private boolean isDeleted = false;

    @Column(unique = true)
    private String authority;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;



    public Role(Integer id, String authority) {
        this.id = id;
        this.authority = authority;
    }

    public void addUser(User user){
        if(users == null)
            users = new HashSet<>();

        users.add(user);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", authority='" + authority + '\'' +
                ", users=" + users +
                '}';
    }
}


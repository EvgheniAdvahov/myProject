package com.myProject.myProject.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String username;
    private String password;
    private String fullName;

    @OneToMany(mappedBy = "user")
    private List<MyLog> myLogs;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles = new ArrayList<>();

    public String getStringRoles() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Role role : roles) {
            stringBuilder.append(role.getName()).append(", ");
        }
        return stringBuilder.toString();
    }

}

package com.example.model

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.validator.constraints.NotEmpty
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.annotation.Transactional

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name = "slack_login")
class SlackUser {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private int id;
    @Column(name = "name")
    @NotEmpty(message = "*Please provide your name")
    private String name;
    @Column(name = "email")
    @NotEmpty(message = "*Please provide your email")
    private String email
    @Column(name = "reg_date")
    @CreationTimestamp
    private Date regDate;
    @Column(name = "token")
    private String token
    /*@ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
*/
    int getId() {
        return id
    }

    void setId(int id) {
        this.id = id
    }

    String getToken() {
        return token
    }

    void setToken(String token) {
        this.token = token
    }

    String getName() {
        return name
    }

    String getDate(){
        return regDate
    }

    void setDate(String regDate){
        this.regDate = regDate
    }

    void setName(String name) {
        this.name = name
    }


    String getEmail() {
        return email
    }

    void setEmail(String email) {
        this.email = email
    }


/*
    Set<Role> getRoles() {
        return roles
    }

    void setRoles(Set<Role> roles) {
        this.roles = roles
    }
*/
}
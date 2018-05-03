package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "date")
    private String date;
    @Column(name = "enddate")
    private String enddate;
    @JsonIgnore
    @ManyToMany(mappedBy = "events", cascade = CascadeType.ALL)
    private Set<Team> teams = new HashSet<>();
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
    @Column(name = "description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public void addTeam(Team team){
        teams.add(team);
    }

    public void setTeams(Set<Team> teams) {
        this.teams = teams;
    }

    public void removeTeam(Team team){
        this.teams.remove(team);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

}

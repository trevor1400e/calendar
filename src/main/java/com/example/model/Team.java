package com.example.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "teamname")
    private String teamname;
    @Column(name = "teamcolor")
    private String teamcolor;
    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "posts_teams",
            joinColumns = { @JoinColumn(name = "teams_id") },
            inverseJoinColumns = { @JoinColumn(name = "events_post_id") })
    private List<Event> events = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamname() {
        return teamname;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public String getTeamcolor() {
        return teamcolor;
    }

    public void setTeamcolor(String teamcolor) {
        this.teamcolor = teamcolor;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.addTeam(this);
    }

    public void removeEvent(Event event) {
        events.removeIf((e) -> e.getId() == event.getId());
        event.setTeams(null);
    }

}

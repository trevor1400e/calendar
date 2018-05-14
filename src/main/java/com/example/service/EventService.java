package com.example.service;

import com.example.model.Event;

import java.util.List;

public interface EventService {

    public Event getEvent(int id);

    public List<Event> searchEventsByTitle(String title);

    public List<Event> findByTeam(String team);

    public void removeEvent(int id);
}

package com.example.service;

import com.example.model.Event;
import com.example.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    EventRepository eventRepo;

    @Override
    public Event getEvent(int id) {
        return eventRepo.findById(id);
    }

    @Override
    public List<Event> searchEventsByTitle(String title) {
        return eventRepo.findByTitleContaining(title);
    }

    @Override
    public List<Event> findByTeam(String team) {
        return eventRepo.findByTeamTeamname(team);
    }

}

package com.example.service;

import java.util.List;

import com.example.model.Event;

public interface EventService {

	public Event getEvent(int id);
	
	public List<Event> searchEventsByTitle(String title);

	public List<Event> findByTeam(String team);
}

package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.Event;
import com.example.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService{

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
	
}

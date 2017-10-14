package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.Event;

@Repository("eventRepository")
public interface EventRepository extends JpaRepository<Event, Integer>{
	
	Event findById(int id);
	
	List<Event> findByTitleContaining(String title);

}

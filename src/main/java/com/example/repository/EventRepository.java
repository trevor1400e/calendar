package com.example.repository;

import com.example.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("eventRepository")
public interface EventRepository extends JpaRepository<Event, Integer> {

    Event findById(int id);

    List<Event> findAll();

    List<Event> findByTitleContaining(String title);

    List<Event> findByTeamTeamname(String team);

}

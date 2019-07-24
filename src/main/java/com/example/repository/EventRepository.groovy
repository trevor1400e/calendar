package com.example.repository;

import com.example.model.Event;
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("eventRepository")
 interface EventRepository extends JpaRepository<Event, Integer> {

    Event findById(int id);

    List<Event> findAll();

    List<Event> findByTitleContaining(String title);

    @Query("""
            SELECT DISTINCT e FROM Event e
            LEFT OUTER JOIN e.teams t
            WHERE t.teamname = :teamName
            """)
    List<Event> findByTeam(@Param("teamName") String teamName);

    @Transactional
    @Modifying
    @Query("""
            DELETE FROM Event e WHERE e.id = :id
            """)
    void removeEvent(@Param("id") int id);
}

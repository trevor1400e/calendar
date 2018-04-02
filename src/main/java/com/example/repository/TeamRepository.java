package com.example.repository;

import com.example.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("teamRepository")
public interface TeamRepository extends JpaRepository<Team, Integer> {

    Team findById(int id);

    Team findByteamname(String teamname);


}

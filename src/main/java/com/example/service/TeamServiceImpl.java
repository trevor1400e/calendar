package com.example.service;

import com.example.model.Team;
import com.example.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    TeamRepository teamRepo;

    @Override
    public Team findById(int id) {
        return teamRepo.findById(id);
    }

    @Override
    public Team findByteamname(String teamname) {
        return teamRepo.findByteamname(teamname);
    }


}

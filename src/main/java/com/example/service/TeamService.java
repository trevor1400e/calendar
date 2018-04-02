package com.example.service;

import com.example.model.Team;


public interface TeamService {

    public Team findById(int id);

    public Team findByteamname(String teamname);

}

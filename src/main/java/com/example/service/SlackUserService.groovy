package com.example.service;

import com.example.model.SlackUser;
import com.example.model.User;

public interface SlackUserService {
	public SlackUser findUserByEmail(String email);
	public SlackUser findUserById(int id);
	public void saveSlackUser(SlackUser slackUser);
}

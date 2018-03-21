package com.example.repository

import com.example.model.SlackUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository("slackUserRepository")
interface SlackUserRepository extends JpaRepository<SlackUser, Long> {
    SlackUser findByEmail(String email);
    SlackUser findById(int id);
}
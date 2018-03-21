package com.example.model;

import javax.persistence.*;

@Entity
@Table(name = "team")
public class Team {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="teamname")
	private String teamname;
	@Column(name="teamcolor")
	private String teamcolor;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTeamname() {
		return teamname;
	}
	public void setTeamname(String teamname) {
		this.teamname = teamname;
	}
	public String getTeamcolor() {
		return teamcolor;
	}
	public void setTeamcolor(String teamcolor) {
		this.teamcolor = teamcolor;
	}
	
	
}

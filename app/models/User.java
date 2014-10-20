package models;

import javax.persistence.Entity;
import play.db.ebean.*;

/**
 * Created by jakeesmael on 10/20/14.
 */
@Entity
public class User {
	private String username;
	private String name;
	private int points;
	private int wins;
	private int losses;
	private int level;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public int getWins() {
		return wins;
	}

	public void setWins(int wins) {
		this.wins = wins;
	}

	public int getLosses() {
		return losses;
	}

	public void setLosses(int losses) {
		this.losses = losses;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public static Model.Finder<Long,User> find = new Model.Finder<Long,User>(
		Long.class, User.class
	);
}

package models;

import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.ebean.*;

import java.math.BigInteger;

/**
 * Created by jakeesmael on 10/20/14.
 */
@Entity
@Table(name = "users")
public class User extends Model{
	private String username;
	private String password;
	private String name;
	private Integer points;
	private Integer wins;
	private Integer losses;
	private Integer level;
	private String email;
    private BigInteger facebookId;

	public User() {}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
		this.points = 20;
		this.wins = 0;
		this.losses = 0;
		this.level = 1;
        this.facebookId = null;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getWins() {
		return wins;
	}

	public void setWins(Integer wins) {
		this.wins = wins;
	}

	public Integer getLosses() {
		return losses;
	}

	public void setLosses(Integer losses) {
		this.losses = losses;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

    public void setFacebookId(BigInteger facebookId) { this.facebookId = facebookId; }

    public BigInteger getFacebookId() {
    	return facebookId;
    }

	public static Model.Finder<Long,User> find = new Model.Finder<Long,User>(
		Long.class, User.class
	);
}

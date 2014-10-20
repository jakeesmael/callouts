package models;

import play.db.ebean.Model;

import javax.persistence.Entity;
import java.sql.Timestamp;

/**
 * Created by jakeesmael on 10/20/14.
 */
@Entity
public class Challenge {
	private int challengeId;
	private String challengerUsername;
	private String challengedUsername;
	private int wager;
	private int odds;
	private String location;
	private Timestamp time;
	private String subject;
	private String winner;

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

	public String getChallengerUsername() {
		return challengerUsername;
	}

	public void setChallengerUsername(String challengerUsername) {
		this.challengerUsername = challengerUsername;
	}

	public String getChallengedUsername() {
		return challengedUsername;
	}

	public void setChallengedUsername(String challengedUsername) {
		this.challengedUsername = challengedUsername;
	}

	public int getWager() {
		return wager;
	}

	public void setWager(int wager) {
		this.wager = wager;
	}

	public int getOdds() {
		return odds;
	}

	public void setOdds(int odds) {
		this.odds = odds;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public static Model.Finder<Long,Challenge> find = new Model.Finder<Long,Challenge>(
		Long.class, Challenge.class
	);
}

package models;

import com.avaje.ebean.annotation.Sql;
import controllers.UserController;
import play.db.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Created by jakeesmael on 10/20/14.
 */
@Entity
@Table(name="challenges")
public class Challenge extends Model {
	private Integer challengeId;
	private String challengerUsername;
	private String challengedUsername;
	private Integer wager;
	private Integer odds;
	private String location;
	private Timestamp time;
	private String subject;
	private String winner;

	public Challenge(int challengeId, String challengerUsername, String challengedUsername,
									 int odds, Timestamp time, String subject, String winner) {
		this.challengeId = challengeId;
		this.challengerUsername = challengerUsername;
		this.challengedUsername = challengedUsername;
		this.odds = odds;
		this.time = time;
		this.subject = subject;
		this.winner = winner;
	}

	public Integer getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(Integer challengeId) {
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

	public Integer getWager() {
		return wager;
	}

	public void setWager(Integer wager) {
		this.wager = wager;
	}

	public Integer getOdds() {
		return odds;
	}

	public void setOdds(Integer odds) {
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

	public String getTimeRemaining() {
		int timeRemaining =  (int)((time.getTime() - System.currentTimeMillis())/1000);
		int hours = timeRemaining / 3600;
		int minutes = (timeRemaining % 3600) / 60;
		int seconds = timeRemaining % 60;
		String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		return timeString;
	}

    public User getChallenger() { return UserController.getUserByUsername(getChallengerUsername()); }

    public User getChallenged() { return UserController.getUserByUsername(getChallengedUsername()); }

}

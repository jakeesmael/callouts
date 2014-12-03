package models;

import play.db.ebean.Model;
import javax.persistence.Entity;
import java.sql.Timestamp;
import com.avaje.ebean.annotation.Sql;

/**
 * Created by jakeesmael on 10/20/14.
 */
@Entity
@Sql
public class BetData {
	private String challengerUsername;
	private String challengedUsername;
	private String subject;
	private Timestamp time;
	private String winner;
	private String predictedWinner;
	private int wager;

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

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getPredictedWinner() {
		return predictedWinner;
	}

	public void setPredictedWinner(String predictedWinner) {
		this.predictedWinner = predictedWinner;
	}

	public int getWager() {
		return wager;
	}

	public void setWager(int wager) {
		this.wager = wager;
	}

	public static Model.Finder<Long,BetData> find = new Model.Finder<Long,BetData>(
		Long.class, BetData.class
	);
}
package models;

import play.db.ebean.Model;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by jakeesmael on 10/20/14.
 */
@Entity
public class Bet {
	private int betId;
	private String winner;
	private int wager;
	private int challengeId;
	private String bettor;
	private String subject;
	private Challenge challenge;

	public Bet(int betId, String predictedWinner, int wager, int challengeId, String bettor,
						String challengerUsername, String challengedUsername, int odds, Timestamp time, String subject, String winner) {
		this.betId = betId;
		this.winner = predictedWinner;
		this.wager = wager;
		this.challengeId = challengeId;
		this.bettor = bettor;
		this.subject = subject;
		this.challenge = new Challenge(challengeId, challengerUsername, challengedUsername, odds, time, subject, winner);
	}

	public int getBetId() {
		return betId;
	}

	public void setBetId(int betId) {
		this.betId = betId;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

	public int getWager() {
		return wager;
	}

	public void setWager(int wager) {
		this.wager = wager;
	}

	public int getChallengeId() {
		return challengeId;
	}

	public void setChallengeId(int challengeId) {
		this.challengeId = challengeId;
	}

	public String getBettor() {
		return bettor;
	}

	public void setBettor(String bettor) {
		this.bettor = bettor;
	}

	public void setChallenge(Challenge challenge) {
		this.challenge = challenge;
	}

	public Challenge getChallenge() {
		return challenge;
	}

	public static Model.Finder<Long,Bet> find = new Model.Finder<Long,Bet>(
		Long.class, Bet.class
	);
}

package models;

import play.db.ebean.Model;
import javax.persistence.Entity;

/**
 * Created by jakeesmael on 10/20/14.
 */
@Entity
public class Bet {
	private int betId;
	private String winner;
	private int wager;
	private int challengeId;

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

	public static Model.Finder<Long,Bet> find = new Model.Finder<Long,Bet>(
		Long.class, Bet.class
	);
}

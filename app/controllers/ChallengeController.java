/**
 * Created by KenYHT on 10/20/2014.
 */
package controllers;

import com.avaje.ebean.*;
import models.Challenge;
import play.mvc.Controller;

import java.sql.Timestamp;
import java.util.List;

import static controllers.Callouts.getCurrentUsername;
import static controllers.UserController.updatePoints;

public class ChallengeController extends Controller {

	/**
	 * Adds a challenge, or issues it. Returns true if it was successful, false if it wasn't
 	 * @return boolean - whether or not it was successful.
	 */
	public static boolean addChallenge(Callouts.ChallengeForm challenge) {
		String fromUsername = getCurrentUsername();

		boolean wasSuccessful = true;
		/* Checks that we have all the necessary info and that the person is sending
		 * a challenge from themselves (not hacking and sending a challenge from someone
		 * else). */
		if (challenge.challengerUsername == null || challenge.challengerUsername.isEmpty() ||
			challenge.challengedUsername == null || challenge.challengedUsername.isEmpty() ||
			challenge.wager <= 0 ||
			challenge.time == null ||
			challenge.subject == null || challenge.subject.isEmpty() ||
			!fromUsername.equals(challenge.challengerUsername) ||
			!isValidChallenge(challenge.challengerUsername, challenge.challengedUsername, challenge.time))
			wasSuccessful = false;
		else {
			if (challenge.odds <= 0)
				challenge.odds = 1;

			String time = challenge.time.toString().split("\\.")[0];
			String sql = "insert into challenges(challenger_username, challenged_username, " +
				"wager, odds, location, time, subject) values(\"" +
				challenge.challengerUsername + "\", \"" +
				challenge.challengedUsername + "\", " +
				challenge.wager + ", " +
				challenge.odds + ", \"" +
				challenge.location + "\", \"" +
				time + "\", \"" +
				challenge.subject + "\");";
			SqlUpdate insert = Ebean.createSqlUpdate(sql);
			insert.execute();
		}

		return wasSuccessful;
	}

	/**
	 * Gets a number of the most recent challenges
	 * @param number, the number of most recent challenges you want to fetch
	 * @return
	 */
	public static List<Challenge> getMostRecentChallenges(int number) {
		String sql = "select * " +
			"from challenges ORDER BY challenge_id DESC LIMIT " + number + ";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("challenge_id", "challengeId")
			.columnMapping("challenger_username", "challengerUsername")
			.columnMapping("challenged_username", "challengedUsername")
			.columnMapping("wager", "wager")
			.columnMapping("odds", "odds")
			.columnMapping("location", "location")
			.columnMapping("time", "time")
			.columnMapping("subject", "subject")
			.columnMapping("winner", "winner")
			.create();
		Query<Challenge> query = Ebean.find(Challenge.class).setRawSql(rawSql);
		List<Challenge> challengeList = query.findList();

		return challengeList;
	}


	/**
	 * Gets the challenges sent by the user identified by username
	 * @param username
	 * @return
	 */
	public static List<Challenge> getSentChallengesByUsername(String username) {
		String sql = "select * " +
			"from challenges where challenger_username = \"" + username + "\";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("challenge_id", "challengeId")
			.columnMapping("challenger_username", "challengerUsername")
			.columnMapping("challenged_username", "challengedUsername")
			.columnMapping("wager", "wager")
			.columnMapping("odds", "odds")
			.columnMapping("location", "location")
			.columnMapping("time", "time")
			.columnMapping("subject", "subject")
			.columnMapping("winner", "winner")
			.create();
		Query<Challenge> query = Ebean.find(Challenge.class).setRawSql(rawSql);
		List<Challenge> challengeList = query.findList();

		return challengeList;
	}

	/**
	 * Gets the challenges received by the user identified by username
	 * @param username
	 * @return
	 */
	public static List<Challenge> getReceivedChallengesByUsername(String username) {
		String sql = "select * from challenges where challenged_username = \"" + username + "\";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("challenge_id", "challengeId")
			.columnMapping("challenger_username", "challengerUsername")
			.columnMapping("challenged_username", "challengedUsername")
			.columnMapping("wager", "wager")
			.columnMapping("odds", "odds")
			.columnMapping("location", "location")
			.columnMapping("time", "time")
			.columnMapping("subject", "subject")
			.columnMapping("winner", "winner")
			.create();
		Query<Challenge> query = Ebean.find(Challenge.class).setRawSql(rawSql);
		List<Challenge> challengeList = query.findList();

		return challengeList;
	}

	/**
	 * Returns whether or not the challenge we want to add is valid.
	 * Since the key for a challenge is the challenger, the challenged, and the time,
	 * we make sure there is no other challenge with the same parameters.
	 * @param challenger
	 * @param challenged
	 * @param time
	 * @return boolean
	 */
	public static boolean isValidChallenge(String challenger, String challenged, Timestamp time) {
		return getChallenge(challenger, challenged, time) == null;
	}

	/**
	 * Returns a specific challenge based on challenger, challenged, and time
	 * @param challenger
	 * @param challenged
	 * @param time
	 * @return challenge
	 */
	public static Challenge getChallenge(String challenger, String challenged, Timestamp time) {
		Challenge challenge;

		String sql = "select * from challenges where challenger_username = \"" +
			challenger + "\" and challenged_username = \"" +
			challenged + "\" and time = \"" +
			time + "\";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("challenge_id", "challengeId")
			.columnMapping("challenger_username", "challengerUsername")
			.columnMapping("challenged_username", "challengedUsername")
			.columnMapping("wager", "wager")
			.columnMapping("odds", "odds")
			.columnMapping("location", "location")
			.columnMapping("time", "time")
			.columnMapping("subject", "subject")
			.columnMapping("winner", "winner")
			.create();
		Query<Challenge> query = Ebean.find(Challenge.class).setRawSql(rawSql);
		List<Challenge> challengeList = query.findList();
		if (challengeList.isEmpty())
			challenge = null;
		else
			challenge = challengeList.get(0);

		return challenge;
	}

    public static Challenge getChallengeById(int challengeId) {
        Challenge challenge;

        String sql = "select * from challenges where challenge_id = \"" + challengeId + "\";";
        RawSql rawSql = RawSqlBuilder.unparsed(sql)
                .columnMapping("challenge_id", "challengeId")
                .columnMapping("challenger_username", "challengerUsername")
                .columnMapping("challenged_username", "challengedUsername")
                .columnMapping("wager", "wager")
                .columnMapping("odds", "odds")
                .columnMapping("location", "location")
                .columnMapping("time", "time")
                .columnMapping("subject", "subject")
                .columnMapping("winner", "winner")
                .create();
        Query<Challenge> query = Ebean.find(Challenge.class).setRawSql(rawSql);
        List<Challenge> challengeList = query.findList();
        if (challengeList.isEmpty())
            challenge = null;
        else
            challenge = challengeList.get(0);

        return challenge;
    }

	/**
	 * Deletes a challenge based on the challenger, challenged, and time
	 * @param challenger
	 * @param challenged
	 * @param time
	 */
	public static void deleteChallenge(String challenger, String challenged, Timestamp time) {
		String sql = "delete from challenges where challenger_username = \"" +
			challenger + "\" and challenged_username = \"" +
			challenged + "\" and time = \"" +
			time + "\";";
		SqlUpdate delete = Ebean.createSqlUpdate(sql);
		delete.execute();
	}

	/**
	 * Updates the time of a challenge
	 * @param challenger
	 * @param challenged
	 * @param oldTime
	 * @param newTime
	 */
	public static void updateChallengeTime(String challenger, String challenged, Timestamp oldTime,
																				 Timestamp newTime) {
		String sql = "update challenges set time = \"" + newTime + "\"" +
			" where challenger_username = \"" + challenger + "\"" +
			" and challenged_username = \"" + challenged + "\"" +
			" and time = \"" + oldTime + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	/**
	 * Updates the location of a challenge
	 * @param challenger
	 * @param challenged
	 * @param time
	 * @param newLocation
	 */
	public static void updateChallengeLocation(String challenger, String challenged, Timestamp time,
																				 String newLocation) {
		String sql = "update challenges set location = \"" + newLocation + "\"" +
			" where challenger_username = \"" + challenger + "\"" +
			" and challenged_username = \"" + challenged + "\"" +
			" and time = \"" + time + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	/**
	 * Updates the subject of a challenge
	 * @param challenger
	 * @param challenged
	 * @param time
	 * @param newSubject
	 */
	public static void updateChallengeSubject(String challenger, String challenged, Timestamp time,
																						 String newSubject) {
		String sql = "update challenges set subject = \"" + newSubject + "\"" +
			" where challenger_username = \"" + challenger + "\"" +
			" and challenged_username = \"" + challenged + "\"" +
			" and time = \"" + time + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	public static void declareWinner(int challengeId, String winner) {
		String sql = "update challenges set winner = \"" + winner + "\"" +
			" where challenge_id = \"" + challengeId + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	public static void distributePoints(int challengeId, String winner) {
		Challenge challenge = getChallengeById(challengeId);
		int points = challenge.getWager();
		if (winner.equals(challenge.getChallengedUsername())) {
			points *= challenge.getOdds();
			updatePoints(challenge.getChallengerUsername(), -1*points);
			updatePoints(challenge.getChallengedUsername(), points);
		}
		else {
			updatePoints(challenge.getChallengerUsername(), points);
			updatePoints(challenge.getChallengedUsername(), -1*points);
		}
	}

}
package controllers;
/**
 * Created by KenYHT on 10/20/2014
 */

import com.avaje.ebean.*;
import models.Bet;
import models.Challenge;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import java.util.List;

import static controllers.Callouts.getCurrentUsername;
import static controllers.ChallengeController.getChallenge;

public class BetController extends Controller {

	/**
	 * Adds a bet
	 * @param bet - form containing bet information, including key for the challenge
	 * @return - whether the bet was successfully issued or not
	 */
	public static boolean addBet(Callouts.BetForm bet) {
		String bettor = getCurrentUsername();

		boolean wasSuccessful = true;
		/* Make sure they don't bet on a challenge involving themselves. */
		if (bet.wager <= 0 || bet.winner == null || bet.winner.isEmpty() || bet.winner.isEmpty() ||
			bet.challenger.equals(bettor) || bet.challenged.equals(bettor))
			wasSuccessful = false;
		else {
			Challenge challenge = getChallenge(bet.challenger, bet.challenged, bet.time);
			int challengeId = challenge.getChallengeId();
			String sql = "insert into bets(winner, wager, challenge_id, bettor) " +
				"values(\"" + bet.winner + "\", " +
				"\"" + bet.wager + "\", " +
				"\"" + challengeId + "\", " +
				"\"" + bettor + "\");";
			SqlUpdate insert = Ebean.createSqlUpdate(sql);
			insert.execute();
		}

		return wasSuccessful;
	}

    public static void deleteBet(int betId) {
        String sql = "delete from bets where bet_id = \"" + betId + "\";";
        SqlUpdate delete = Ebean.createSqlUpdate(sql);
        delete.execute();
    }

	/**
	 * Gets all bets that a user has placed
	 * @param username
	 * @return betList - the list of bets made by a user
	 */
	public static List<Bet> getPlacedBets(String username) {
		String sql = "select * from bets where bettor = \"" + username + "\";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("bet_id", "betId")
			.columnMapping("winner", "winner")
			.columnMapping("wager", "wager")
			.columnMapping("challenge_id", "challengeId")
			.columnMapping("bettor", "bettor")
			.create();
		Query<Bet> query = Ebean.find(Bet.class).setRawSql(rawSql);
		List<Bet> betList = query.findList();

		return betList;
	}

	/**
	 * Gets all bets that a user has placed
	 * @param username
	 * @return betList - the list of bets made by a user
	 */
	public static List<Bet> getPlacedBetsChallenges(String username) {
		String sql = "select b.bet_id, b.winner, b.wager, b.challenge_id, b.bettor, c.challenge_id, c.challenger_username, c.challenged_username, c.wager, c.odds, c.location, c.time, c.subject, c.winner from bets b, challenges c where c.challenge_id = 1 and bettor = \"" + username + "\";";
		RawSql rawSql = RawSqlBuilder.parse(sql)
			.columnMapping("b.bet_id", "betId")
			.columnMapping("b.winner", "winner")
			.columnMapping("b.wager", "wager")
			.columnMapping("b.challenge_id", "challengeId")
			.columnMapping("b.bettor", "bettor")
			.columnMapping("c.subject", "subject")
			.columnMapping("c.challenge_id", "challenge.challengeId")
			.columnMapping("c.challenger_username", "challenge.challengerUsername")
			.columnMapping("c.challenged_username", "challenge.challengedUsername")
			.columnMapping("c.wager", "challenge.wager")
			.columnMapping("c.odds", "challenge.odds")
			.columnMapping("c.location", "challenge.location")
			.columnMapping("c.time", "challenge.time")
			.columnMapping("c.subject", "challenge.subject")
			.columnMapping("c.winner", "challenge.winner")
			.create();
		Query<Bet> query = Ebean.find(Bet.class).setRawSql(rawSql);
		List<Bet> betList = query.findList();
		System.out.println(betList.size());
		Challenge cunt = betList.get(0).getChallenge();
		Bet b = betList.get(0);
		System.out.println("bet fields: " + b.getBetId() + " " + b.getWager() + " " + b.getChallengeId());
		System.out.println(b.getSubject());

		return betList;
	}
}
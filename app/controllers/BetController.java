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
}
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

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static controllers.Callouts.getCurrentUsername;
import static controllers.ChallengeController.getChallenge;

public class BetController extends Controller {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/callouts";

	//  Database credentials
	static final String USER = "calloutsuser";
	static final String PASS = "copw";

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

		Connection con = null;
		try {
			con = DriverManager.getConnection(
				DB_URL,
				USER,
				PASS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = con.prepareStatement("select b.bet_id, b.winner, b.wager, b.challenge_id, b.bettor, c.challenge_id, c.challenger_username, c.challenged_username, c.wager, c.odds, c.location, c.time, c.subject, c.winner from bets b, challenges c where b.challenge_id = c.challenge_id and bettor = ? ;");
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			/* For each row */
			while (resultSet.next()) {
				int betId = resultSet.getInt("b.bet_id");
				String winner = resultSet.getString("b.winner");
				int wager = resultSet.getInt("b.wager");
				int challengeId = resultSet.getInt("b.challenge_id");
				String bettor = resultSet.getString("b.bettor");
				String challengerUsername = resultSet.getString("c.challenger_username");
				String challengedUsername = resultSet.getString("c.challenged_username");
				int odds = resultSet.getInt("c.odds");
				Timestamp time = resultSet.getTimestamp("c.time");
				String subject = resultSet.getString("c.subject");
				Bet bet = new Bet(betId, winner, wager, challengeId, bettor, challengerUsername, challengedUsername,
					odds, time, subject);
				/* add each bet to the list here */
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
//		return betList;
	}
}
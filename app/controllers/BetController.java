package controllers;
/**
 * Created by KenYHT on 10/20/2014
 */
import com.avaje.ebean.*;
import models.Bet;
import models.Challenge;
import play.Play;
import play.mvc.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static controllers.Callouts.getCurrentUsername;
import static controllers.ChallengeController.getChallenge;

public class BetController extends Controller {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String dbUrl = "jdbc:mysql://engr-cpanel-mysql.engr.illinois.edu/esmael2_callouts";
//	static final String dbUrl="jdbc:mysql://localhost:3306/callouts";

	// Database credentials
	static final String user = "esmael2_callouts";
	static final String pass = "C@lloutspw";
//	static final String user="calloutsuser";
//	static final String pass="copw";


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
		String sql = "select b.bet_id, b.winner, b.wager, b.challenge_id, b.bettor, c.challenge_id, c.challenger_username, c.challenged_username, c.wager, c.odds, c.location, c.time, c.subject, c.winner from bets b join challenges c on (b.challenge_id=c.challenge_id) where b.bettor=\"" + username + "\";";
		System.out.println(sql);
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("b.bet_id", "betId")
			.columnMapping("b.winner", "winner")
			.columnMapping("b.wager", "wager")
			.columnMapping("b.challenge_id", "challengeId")
			.columnMapping("b.bettor", "bettor")
			/*.columnMapping("c.challenge_id", "challenge.challengeId")
			.columnMapping("c.challenger_username", "challenge.challengerUsername")
			.columnMapping("c.challenged_username", "challenge.challengedUsername")
			.columnMapping("c.wager", "challenge.wager")
			.columnMapping("c.odds", "challenge.odds")
			.columnMapping("c.location", "challenge.location")
			.columnMapping("c.time", "challenge.time")
			.columnMapping("c.subject", "challenge.subject")
			.columnMapping("c.winner", "challenge.winner")*/
			.create();

		Query<Bet> query = Ebean.find(Bet.class).setRawSql(rawSql);
		List<Bet> betList = query.findList();

		return betList;
	}

	 /* Gets all bets that a user has placed
	 * @param username
	 * @return betList - the list of bets made by a user
	 */
	public static List<Bet> getPlacedBetsChallenges(String username) {

		List<Bet> betList = new ArrayList<Bet>();
		Connection con = null;
		/*String dbUrl = Play.application().configuration().getString("db.default.url");
		String user = Play.application().configuration().getString("db.default.user");
		String pass = Play.application().configuration().getString("db.default.password");*/
		try {
			con = DriverManager.getConnection(
				dbUrl,
				user,
				pass);
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
				String predictedWinner = resultSet.getString("b.winner");
				int wager = resultSet.getInt("b.wager");
				int challengeId = resultSet.getInt("b.challenge_id");
				String bettor = resultSet.getString("b.bettor");
				String challengerUsername = resultSet.getString("c.challenger_username");
				String challengedUsername = resultSet.getString("c.challenged_username");
				int odds = resultSet.getInt("c.odds");
				Timestamp time = resultSet.getTimestamp("c.time");
				String subject = resultSet.getString("c.subject");
				String winner = resultSet.getString("c.winner");
				Bet bet = new Bet(betId, predictedWinner, wager, challengeId, bettor, challengerUsername, challengedUsername,
					odds, time, subject, winner);
				betList.add(bet);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return betList;
	}
}
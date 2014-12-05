package controllers;

/**
 * Created by KenYHT on 10/20/2014
 */

import com.avaje.ebean.*;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Crypto;
import play.mvc.*;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.math.BigInteger;

import static controllers.Callouts.getCurrentUsername;

public class UserController extends Controller {

	/**
	 * Gets the user specified by the username from the database
	 * @param username
	 * @return user - user specified by username, otherwise null
	 */
	public static User getUserByUsername(String username) {
		String sql = "select * from users where username = \"" + username + "\";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("username", "username")
			.columnMapping("password", "password")
			.columnMapping("name", "name")
			.columnMapping("points", "points")
			.columnMapping("wins", "wins")
			.columnMapping("losses", "losses")
			.columnMapping("level", "level")
			.columnMapping("email", "email")
			.create();
		Query<User> query = Ebean.find(User.class).setRawSql(rawSql);
		List<User> userList = query.findList();

		User user;
		if (userList.isEmpty())
			user = null;
		else
			user = userList.get(0);

		return user;
	}

	/**
	 * Checks if the password given is correct for that user
	 * @param username
	 * @param password
	 * @return
	 */
	public static boolean correctPassword(String username, String password) {
		String sql = "select * from users where username = \"" + username + "\";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql)
			.columnMapping("username", "username")
			.columnMapping("password", "password")
			.create();
		Query<User> query = Ebean.find(User.class).setRawSql(rawSql);
		List<User> passwordList = query.findList();

		boolean correct = true;
		if (passwordList.isEmpty() || !passwordList.get(0).getPassword().equals(Crypto.sign(password)))
			correct = false;

		return correct;
	}

	/**
	 * Adds the user to the database after successful sign up
	 * @param username
	 * @param password
	 */
	public static void addUser(String username, String password, String email) {
		password = Crypto.sign(password);

		String sql;
		if (email == null || email.isEmpty()) {
			sql = "insert into users(username, password) values(\""
				+ username + "\", \""
				+ password + "\");";
		}
		else {
			sql = "insert into users(username, password, email) values(\""
				+ username + "\", \""
				+ password + "\", \""
				+ email + "\");";
		}
		SqlUpdate insert = Ebean.createSqlUpdate(sql);
		insert.execute();
	}

	/**
	 * Updates the name of a user
	 * @param name
	 */
	public static void updateUserName(String name) {
		String username = getCurrentUsername();
		String sql = "update users set name = \"" + name + "\" where username = \"" + username + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	/**
	 * Updates the email address of a user
	 * @param email - the user's new email address
	 */
	public static void updateEmail(String email) {
		String username = getCurrentUsername();
		String sql = "update users set email = \"" + email + "\" where username = \"" + username + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	public static void updatePassword(String password) {
		String username = getCurrentUsername();
		String sql = "update users set password = \"" + Crypto.sign(password) + "\" where username = \"" + username + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	public static void updatePoints(String username, int points) {
		String sql = "update users set points = points+" + points +
			" where username=\"" + username + "\";";
		SqlUpdate update = Ebean.createSqlUpdate(sql);
		update.execute();
	}

	/**
	 * Updates the Facebook user ID of a user.
	 * @param facebookId - the user's Facebook ID
	 */
	public static void updateFacebookId(BigInteger facebookId) {
        String username = getCurrentUsername();
        String sql = "update users set facebook_id = \"" + facebookId + "\" where username = \"" + username + "\";";
        SqlUpdate update = Ebean.createSqlUpdate(sql);
        update.execute();
	}

    /**
     * Updates the profile picture URL of a user.
     * @param pictureUrl - the user's profile picture URL
     */
    public static void updatePictureUrl(String pictureUrl) {
        String username = getCurrentUsername();
        String sql = "update users set picture_url = \"" + pictureUrl + "\" where username = \"" + username + "\";";
        SqlUpdate update = Ebean.createSqlUpdate(sql);
        update.execute();
    }
}
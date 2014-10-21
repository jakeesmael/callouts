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
			.columnMapping("name", "name")
			.columnMapping("points", "points")
			.columnMapping("wins", "wins")
			.columnMapping("losses", "losses")
			.columnMapping("level", "level")
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
		System.out.println("\n=====\n" + correct + "\n=======\n");

		return correct;
	}

	/**
	 * Adds the user to the database after successful sign up
	 * @param username
	 * @param password
	 */
	public static void addUser(String username, String password) {
		password = Crypto.sign(password);
		User newUser = new User(username, password);

		String sql = "insert into users(username, password) values(\""
			+ newUser.getUsername() + "\", \""
			+ newUser.getPassword() + "\");";
		SqlUpdate insert = Ebean.createSqlUpdate(sql);
		insert.execute();
	}
}
package controllers;

import com.avaje.ebean.*;
import models.User;
import play.data.Form;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

import static controllers.UserController.addUser;
import static controllers.UserController.correctPassword;
import static controllers.UserController.getUserByUsername;

/**
 * Created by jakeesmael on 10/19/14.
 */
public class Callouts extends Controller {

	public static class UserForm {
		public String username;
		public String password;
	}

	/**
	 * returns login page
	 * @return
	 */
	public static Result login() {
		return ok(views.html.login.render());
	}

	/**
	 * Called when someone goes to the sign up page (with GET)
	 * @return
	 */
	public static Result signUpGet() {
		return ok(views.html.signup.render(true));
	}

	/**
	 * Called on a POST request to sign up
	 * Should have a form with username and password.
	 * @return home page on successful sign up, sign up page if unsuccessful
	 */
	public static Result signUpPost() {
		Form<UserForm> userFormForm = Form.form(UserForm.class);
		UserForm userForm = userFormForm.bindFromRequest().get();

		boolean validSignUp = true;

		if (userForm.username == null || userForm.username.isEmpty()
			|| userForm.password == null || userForm.password.isEmpty()) {
			validSignUp = false;
		}
		else {
			String sql = "select * from users where username = \"" + userForm.username + "\";";
			RawSql rawSql = RawSqlBuilder.unparsed(sql).create();
			Query<User> query = Ebean.find(User.class).setRawSql(rawSql);
			List<User> userList = query.findList();
			if (!userList.isEmpty()) {
				validSignUp = false;
			}
		}
		if (validSignUp) {
			addUser(userForm.username, userForm.password);
			setSessionCookie(userForm.username);
			return redirect("/");
		}
		else {
			return ok(views.html.signup.render(validSignUp));
		}

	}

	/**
	 * Sets the session cookie for the user based on their username
	 * @param username
	 */
	private static void setSessionCookie(String username) {
		String sessionCookie = Crypto.encryptAES(username);
		response().setCookie("session_id", sessionCookie, Integer.MAX_VALUE);
	}

	/**
	 * Authenticates the user after logging in
	 * @return login page if unsuccessful, newsfeed otherwise
	 */
	public static Result authenticate() {
		Form<UserForm> userFormForm = Form.form(UserForm.class);
		UserForm userForm = userFormForm.bindFromRequest().get();

		boolean successful = true;
		User user = getUserByUsername(userForm.username);
		if (user == null)
			successful = false;
		successful = correctPassword(userForm.username, userForm.password);

		if (successful) {
			setSessionCookie(userForm.username);
			return redirect("/");
		}
		else {
			return redirect("/login");
		}
	}

	@Security.Authenticated(Secured.class)
	public static Result profile() {
		return ok(views.html.profile.render());
	}

	@Security.Authenticated(Secured.class)
	public static Result newsfeed() {
		return ok(views.html.newsfeed.render());
	}
}

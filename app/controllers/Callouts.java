package controllers;

import com.avaje.ebean.*;
import models.User;
import org.mindrot.jbcrypt.BCrypt;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;

import java.util.Map;
import java.util.Set;
import java.util.List;

/**
 * Created by jakeesmael on 10/19/14.
 */
public class Callouts extends Controller {

	public static class UserForm {
		public String username;
		public String password;
	}

	public static Result login() {
		return ok(views.html.login.render());
	}

	public static Result signUpGet() {
		return ok(views.html.signup.render(false));
	}

	/**
	 * Called on a POST request to sign up
	 * Should have a form with username and password.
	 * @return
	 */
	public static Result signUpPost() {
		Form<UserForm> userFormForm = Form.form(UserForm.class);
		UserForm userForm = userFormForm.bindFromRequest().get();

		boolean validSignUp = true;

		if (userForm.username == null || userForm.password == null)
			validSignUp = false;

		String sql = "select * from users where username = \"" + userForm.username + "\";";
		RawSql rawSql = RawSqlBuilder.unparsed(sql).create();
		Query<User> query = Ebean.find(User.class).setRawSql(rawSql);
		List<User> userList = query.findList();
		if (userList.isEmpty()) {
			addUser(userForm);
			return ok(views.html.test.render("created " + userForm.username + " " + userForm.password));
		}
		else {
			validSignUp = false;
			return ok(views.html.signup.render(validSignUp));
		}

	}

	/**
	 * Adds the user to the database after successful sign up
	 * @param user - form containing username and password to be added
	 */
	public static void addUser(UserForm user) {
		String username = user.username;
		String password = BCrypt.hashpw(user.password, BCrypt.gensalt());
		User newUser = new User(username, password);

		String sql = "insert into users(username, password) values(\""
			+ newUser.getUsername() + "\", \""
			+ newUser.getPassword() + "\");";
		SqlUpdate insert = Ebean.createSqlUpdate(sql);
		insert.execute();
	}

	public static Result authenticate() {
		return ok(views.html.test.render(""));
	}

    public static Result newsfeed() { return ok(views.html.newsfeed.render()); }
}

package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.RawSql;
import com.avaje.ebean.RawSqlBuilder;
import models.User;
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

	public static Result signUpPost() {
		Form<UserForm> userFormForm = Form.form(UserForm.class);
		UserForm userForm = userFormForm.bindFromRequest().get();

		boolean validSignUp = true;

		if (userForm.username == null || userForm.password == null) {
			validSignUp = false;
		}

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

	public static void addUser(UserForm user) {

	}

	public static Result authenticate() {
		return ok(views.html.test.render(""));
	}

    public static Result profile() { return ok(views.html.test.render(""));}
}

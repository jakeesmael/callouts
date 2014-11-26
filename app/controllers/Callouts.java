package controllers;

import com.avaje.ebean.*;
import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import models.User;
import models.Challenge;
import play.Play;
import play.data.Form;
import play.data.DynamicForm;
import play.libs.Crypto;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.mvc.Http;

import java.sql.Timestamp;
import java.util.List;
import java.util.Calendar;

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

	public static class SignupForm {
		public String username;
		public String password;
		public String email;
	}

	public static class ChallengeForm {
		public String challengerUsername;
		public String challengedUsername;
		public int wager;
		public int odds;
		public String location;
		public Timestamp time;
		public String subject;

		public ChallengeForm(String challengerUsername, String challengedUsername, int wager,
												 int odds, String location, Timestamp time, String subject) {
			this.challengerUsername = challengerUsername;
			this.challengedUsername = challengedUsername;
			this.wager = wager;
			this.odds = odds;
			this.location = location;
			this.time = time;
			this.subject = subject;
		}
	}

	public static class BetForm {
		public String winner;
		public int wager;
		public String challenger;
		public String challenged;
		public Timestamp time;
	}

	/**
	 * returns login page
	 * @return
	 */
	public static Result login() {
		return ok(views.html.login.render());
	}

	public static Result logout() {
		deleteSessionCookie();
		return redirect("/login");

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
		Form<SignupForm> signupFormForm = Form.form(SignupForm.class);
		SignupForm signupForm = signupFormForm.bindFromRequest().get();

		boolean validSignUp = true;

		if (signupForm.username == null || signupForm.username.isEmpty()
			|| signupForm.password == null || signupForm.password.isEmpty()) {
			validSignUp = false;
		}
		else {
			String sql = "select * from users where username = \"" + signupForm.username + "\";";
			RawSql rawSql = RawSqlBuilder.unparsed(sql).create();
			Query<User> query = Ebean.find(User.class).setRawSql(rawSql);
			List<User> userList = query.findList();
			if (!userList.isEmpty()) {
				validSignUp = false;
			}
		}
		if (validSignUp) {
			addUser(signupForm.username, signupForm.password, signupForm.email);
			setSessionCookie(signupForm.username);
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
	public static void setSessionCookie(String username) {
		String sessionCookie = Crypto.encryptAES(username);
		response().setCookie("session_id", sessionCookie, Integer.MAX_VALUE);
	}

	/**
	 * Discards the session cookie, effectively logging them out.
	 */
	public static void deleteSessionCookie() {
		response().discardCookie("session_id");
	}

	/**
	 * Gets the username of the currently logged in user
	 * @return username
	 */
	public static String getCurrentUsername() {
		return Crypto.decryptAES(request().cookies().get("session_id").value());
	}

	/**
	 * Gets the currently logged in user as a User
	 * @return user
	 */
	public static User getCurrentUser() {
		return getUserByUsername(getCurrentUsername());
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

	public static void sendEmail(ChallengeForm challengeForm) {
		User user = getUserByUsername(challengeForm.challengedUsername);
		String challenger = challengeForm.challengerUsername;

		if (user.getEmail() != null && !user.getEmail().isEmpty()) {
			MailerAPI mail = Play.application().plugin(MailerPlugin.class).email();
			mail.setSubject(challenger + " challenged you!");
			mail.setRecipient(user.getEmail());
			mail.setFrom("noreply@callouts.com");
			mail.send(challenger + " challenged you!\nChallenge: " + challengeForm.subject);
		}
	}

	/**
	 *
	 * @return
	 */
	@Security.Authenticated(Secured.class)
	public static Result challengePost() {
		DynamicForm requestData = Form.form().bindFromRequest();
		// get all of the form field inputs from the request
		String challengerUsername = requestData.get("challengerUsername");
		String challengedUsername = requestData.get("challengedUsername");
		int odds = 1;
		int wager = Integer.parseInt(requestData.get("wager"));
		String location = requestData.get("location");
		String subject = requestData.get("subject");
		// hard code the time and odds for now...
		Calendar calendar = Calendar.getInstance();
		Timestamp time = new Timestamp(calendar.getTime().getTime());

		ChallengeForm challengeForm = new ChallengeForm(challengerUsername, challengedUsername, wager, odds, location, time, subject);

		if (!ChallengeController.isValidChallenge(challengeForm.challengerUsername, challengeForm.challengedUsername, challengeForm.time)
						|| ChallengeController.getChallenge(challengeForm.challengerUsername, challengeForm.challengedUsername, challengeForm.time) != null) {
			return redirect("/");
		} else {
			ChallengeController.addChallenge(challengeForm);
			sendEmail(challengeForm);
			return redirect("/" + challengedUsername);
		}
	}

	@Security.Authenticated(Secured.class)
	public static Result challengeDelete(String challengerUsername, String challengedUsername, String time, String profileUsername) {
		ChallengeController.deleteChallenge(challengerUsername, challengedUsername, Timestamp.valueOf(time));
		return redirect("/" + profileUsername);
	}

	@Security.Authenticated(Secured.class)
	public static Result challengeUpdateTime(String challengerUsername, String challengedUsername, String time, String profileUsername) {
		Calendar calendar = Calendar.getInstance();
		Timestamp oldTime = Timestamp.valueOf(time);
		Timestamp currentTime = new Timestamp(calendar.getTime().getTime());
		ChallengeController.updateChallengeTime(challengerUsername, challengedUsername, oldTime, currentTime);
		return redirect("/" + profileUsername);
	}

	@Security.Authenticated(Secured.class)
	public static Result profile(String profileUsername) {
		Http.Cookie sessionCookie = request().cookies().get("session_id");
		String username = Crypto.decryptAES(sessionCookie.value());
		User user = UserController.getUserByUsername(username);
		User profileUser = UserController.getUserByUsername(profileUsername);
		List<Challenge> sentChallenges= ChallengeController.getSentChallengesByUsername(profileUsername);
		List<Challenge> receivedChallenges = ChallengeController.getReceivedChallengesByUsername(profileUsername);

		if (profileUser == null) {
			return ok(views.html.error.render(user));
		} else {
			return ok(views.html.profile.render(user, profileUser, sentChallenges, receivedChallenges));
		}
	}

	@Security.Authenticated(Secured.class)
	public static Result newsfeed() {
		Http.Cookie sessionCookie = request().cookies().get("session_id");
		String username = Crypto.decryptAES(sessionCookie.value());
		User user = UserController.getUserByUsername(username);
		return ok(views.html.newsfeed.render(user));
	}

	@Security.Authenticated(Secured.class)
	public static Result settings() {
		String username = getCurrentUsername();
		User user = UserController.getUserByUsername(username);

		return ok(views.html.settings.render(user));
	}
}

package controllers;

import models.User;
import play.libs.Crypto;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import static controllers.UserController.getUserByUsername;

/**
 * Created by jakeesmael on 10/21/14.
 */
public class Secured extends Security.Authenticator {
	@Override
	public String getUsername(Http.Context ctx) {
		String username;

		Http.Cookie sessionCookie = ctx.request().cookies().get("session_id");
		if (sessionCookie != null) {
			User user = getUserByUsername(Crypto.decryptAES(sessionCookie.value()));
			if (user != null && user.getUsername() != null && !user.getUsername().isEmpty())
				username = sessionCookie.value();
			else
				username = null;
		}
		else
			username = null;

		return username;
	}

	@Override
	public Result onUnauthorized(Http.Context ctx) {
		return redirect("/login");
	}
}

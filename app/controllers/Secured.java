package controllers;

import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Created by jakeesmael on 10/21/14.
 */
public class Secured extends Security.Authenticator {
	@Override
	public String getUsername(Http.Context ctx) {
		Http.Cookie sessionCookie = ctx.request().cookies().get("session_id");
		if (sessionCookie != null)
			return sessionCookie.value();
		return null;
	}

	@Override
	public Result onUnauthorized(Http.Context ctx) {
		return redirect("/login");
	}
}

package controllers;

import play.mvc.*;

import java.util.Map;
import java.util.Set;

/**
 * Created by jakeesmael on 10/19/14.
 */
public class Callouts extends Controller {
	public static Result login() {
		return ok(views.html.login.render());
	}

	public static Result authenticate() {
		return ok(views.html.test.render());
	}
}

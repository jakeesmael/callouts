import controllers.Callouts;
import org.junit.Test;

import java.sql.Timestamp;

import static controllers.ChallengeController.addChallenge;
import static controllers.ChallengeController.deleteChallenge;
import static org.junit.Assert.assertTrue;

/**
 * Created by jakeesmael on 10/21/14.
 */
public class ChallengeControllerTest {

	@Test
	public static void testAddChallenge() {
		String challenger = "Jake";
		String challenged = "Ken";
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Callouts.ChallengeForm challengeForm = new Callouts.ChallengeForm(challenger, challenged, 2, 2, "somewhere",
			time, "smash");
		boolean worked = addChallenge(challengeForm);
		assertTrue(worked);

		System.out.println("testing!!!");
		deleteChallenge(challenger, challenged, time);
	}
}

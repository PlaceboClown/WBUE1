package highscore;

import highscore.data.GenderType;
import highscore.data.HighScoreRequestType;
import highscore.data.UserDataType;
import highscore.data.UserType;
import models.JeopardyGame;
import models.Player;
import play.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;

/**
 * Author: Lukas
 * Date: 23.05.2015
 */
public class PostHighScore {

	private PostHighScore() {
	}

	public static PostHighScore create() {
		return new PostHighScore();
	}

	public String post(JeopardyGame game) throws Failure {
		HighScoreRequestType highScoreRequestType = new HighScoreRequestType();
		if (game.getWinner() != null) {
			try {
				highScoreRequestType.setUserData(getUserDataFromGame(game));
				highScoreRequestType.setUserKey("3ke93-gue34-dkeu9");

				PublishHighScoreEndpoint endpoint = new PublishHighScoreService().getPublishHighScorePort();
				highscore.data.ObjectFactory factory = new highscore.data.ObjectFactory();

				return endpoint.publishHighScore(factory.createHighScoreRequest(highScoreRequestType).getValue());
			} catch (Failure failure) {
				throw failure;
			} catch (Exception e) {
				Logger.error(e.getMessage());
				throw new Failure("Error", new FailureType());
			}
		} else {
			throw new Failure("no Winner", new FailureType());
		}
	}

	private UserDataType getUserDataFromGame(JeopardyGame game) throws DatatypeConfigurationException {
		UserDataType userDataType = new UserDataType();
		Player winner = game.getWinner();
		Player looser = game.getLoser();
		UserType userWinner = getUser(winner);
		UserType userLoser = getUser(looser);

		if (game.isComputerPlayer(winner.getUser())) {
			userWinner.setFirstName(winner.getUser().getAvatar().getName());
			userWinner.setLastName("the evil-genius KI");

		} else {
			userLoser.setFirstName(winner.getUser().getAvatar().getName());
			userLoser.setLastName("the evil-stupid KI");
		}
		userDataType.setWinner(userWinner);
		userDataType.setLoser(userLoser);

		return userDataType;
	}

	private UserType getUser(Player player) throws DatatypeConfigurationException {
		UserType user = new UserType();
		user.setPassword("");

		user.setFirstName(player.getUser().getFirstName() == null || player.getUser().getFirstName().isEmpty() ? player.getUser().getName() : player.getUser().getFirstName());
		user.setLastName(player.getUser().getLastName() == null || player.getUser().getLastName().isEmpty() ? "-" : player.getUser().getLastName());
		user.setGender(GenderType.fromValue(player.getUser().getGender().name()));
		user.setPoints(player.getProfit());

		if (player.getUser().getBirthDate() != null) {
			GregorianCalendar calendar = new GregorianCalendar();
			calendar.setTime(player.getUser().getBirthDate());
			user.setBirthDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar));
		} else {
			user.setBirthDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(new GregorianCalendar()));
		}
		return user;
	}

}

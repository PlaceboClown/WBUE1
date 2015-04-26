package at.ac.tuwien.big.we15.lab2.api.impl;

import java.util.LinkedList;
import java.util.List;

import at.ac.tuwien.big.we15.lab2.api.Game;
import at.ac.tuwien.big.we15.lab2.api.Player;
import at.ac.tuwien.big.we15.lab2.api.Question;

public class SimpleGame implements Game {

	private int round = 0;
	private Player userPlayer = new SimplePlayer(false);
	private Player computerPlayer = new SimplePlayer(true);

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Game#getRound()
	 */
	@Override
	public int getRound() {
		return round;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Game#setRound(int)
	 */
	@Override
	public void setRound(int round) {
		this.round = round;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Game#getUserPlayer()
	 */
	@Override
	public Player getUserPlayer() {
		return userPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.ac.tuwien.big.we15.lab2.api.impl.Game#setUserPlayer(at.ac.tuwien.big
	 * .we15.lab2.api.Player)
	 */
	@Override
	public void setUserPlayer(Player userPlayer) {
		this.userPlayer = userPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Game#getComputerPlayer()
	 */
	@Override
	public Player getComputerPlayer() {
		return computerPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.ac.tuwien.big.we15.lab2.api.impl.Game#setComputerPlayer(at.ac.tuwien
	 * .big.we15.lab2.api.Player)
	 */
	@Override
	public void setComputerPlayer(Player computerPlayer) {
		this.computerPlayer = computerPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Game#getFirstPlayer()
	 */
	@Override
	public Player getFirstPlayer() {
		return computerPlayer.getAcc() >= userPlayer.getAcc() ? computerPlayer
				: userPlayer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see at.ac.tuwien.big.we15.lab2.api.impl.Game#getSecoundPlayer()
	 */
	@Override
	public Player getSecoundPlayer() {
		return computerPlayer.getAcc() < userPlayer.getAcc() ? computerPlayer
				: userPlayer;
	}

	@Override
	public List<Question> getAllQuestions() {
		List<Question> list = new LinkedList<Question>(
				userPlayer.getAllQuestions());
		list.addAll(computerPlayer.getAllQuestions());
		return list;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((computerPlayer == null) ? 0 : computerPlayer.hashCode());
		result = prime * result + round;
		result = prime * result
				+ ((userPlayer == null) ? 0 : userPlayer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleGame other = (SimpleGame) obj;
		if (computerPlayer == null) {
			if (other.computerPlayer != null)
				return false;
		} else if (!computerPlayer.equals(other.computerPlayer))
			return false;
		if (round != other.round)
			return false;
		if (userPlayer == null) {
			if (other.userPlayer != null)
				return false;
		} else if (!userPlayer.equals(other.userPlayer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimpleGame [round=" + round + ", userPlayer=" + userPlayer
				+ ", computerPlayer=" + computerPlayer + "]";
	}
}

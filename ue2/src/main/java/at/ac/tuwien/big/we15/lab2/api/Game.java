package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;

public interface Game {

	public int getRound();

	public void setRound(int round);

	public Player getUserPlayer();

	public void setUserPlayer(Player userPlayer);

	public Player getComputerPlayer();

	public void setComputerPlayer(Player computerPlayer);

	public Player getFirstPlayer();

	public Player getSecoundPlayer();

	public List<Question> getAllQuestions();
	
	public boolean isFinished();
	
	public void setFinished(boolean finished);
}
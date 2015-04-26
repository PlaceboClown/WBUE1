package at.ac.tuwien.big.we15.lab2.api;

import java.util.List;

public interface Player {

	public void setAcc(int acc);

	public int getAcc();

	public void setLastQuestion(Question question);

	public Question getLastQuestion();

	public void setLastAnswer(boolean answer);

	public boolean getLastAnswer();

	public void setAvatar(Avatar avatar);

	public Avatar getAvatar();
	
	public List<Question> getAllQuestions();
	
	public boolean isComputed();
}

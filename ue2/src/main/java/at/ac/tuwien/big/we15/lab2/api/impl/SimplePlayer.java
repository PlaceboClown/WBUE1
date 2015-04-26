package at.ac.tuwien.big.we15.lab2.api.impl;

import java.util.LinkedList;
import java.util.List;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.Player;
import at.ac.tuwien.big.we15.lab2.api.Question;

public class SimplePlayer implements Player {

	private int acc = 0;
	private Question lastQuestion;
	private boolean lastAnswer = true;
	private Avatar avatar;

	private LinkedList<Question> questions = new LinkedList<>();

	private boolean computed = true;

	public SimplePlayer(boolean computed) {
		this.computed = computed;
	}

	@Override
	public int getAcc() {
		return acc;
	}

	@Override
	public void setAcc(int acc) {
		this.acc = acc;
	}

	@Override
	public Question getLastQuestion() {
		return lastQuestion;
	}

	@Override
	public void setLastQuestion(Question lastQuestion) {
		this.lastQuestion = lastQuestion;
		questions.add(lastQuestion);
	}

	@Override
	public boolean getLastAnswer() {
		return lastAnswer;
	}

	@Override
	public void setLastAnswer(boolean lastAnswer) {
		this.lastAnswer = lastAnswer;
	}

	@Override
	public Avatar getAvatar() {
		return avatar;
	}

	@Override
	public void setAvatar(Avatar avatar) {
		this.avatar = avatar;
	}

	@Override
	public List<Question> getAllQuestions() {
		return questions;
	}

	@Override
	public boolean isComputed() {
		return computed;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + acc;
		result = prime * result + ((avatar == null) ? 0 : avatar.hashCode());
		result = prime * result + (lastAnswer ? 1231 : 1237);
		result = prime * result
				+ ((lastQuestion == null) ? 0 : lastQuestion.hashCode());
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
		SimplePlayer other = (SimplePlayer) obj;
		if (acc != other.acc)
			return false;
		if (avatar != other.avatar)
			return false;
		if (lastAnswer != other.lastAnswer)
			return false;
		if (lastQuestion == null) {
			if (other.lastQuestion != null)
				return false;
		} else if (!lastQuestion.equals(other.lastQuestion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SimplePlayer [acc=" + acc + ", lastQuestion=" + lastQuestion
				+ ", lastAnswer=" + lastAnswer + ", avatar=" + avatar + "]";
	}

}

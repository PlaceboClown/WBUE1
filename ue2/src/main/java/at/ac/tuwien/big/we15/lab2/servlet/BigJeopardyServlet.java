package at.ac.tuwien.big.we15.lab2.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.ac.tuwien.big.we15.lab2.api.Answer;
import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.Game;
import at.ac.tuwien.big.we15.lab2.api.Player;
import at.ac.tuwien.big.we15.lab2.api.Question;
import at.ac.tuwien.big.we15.lab2.api.QuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.impl.ServletJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleGame;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleQuestion;

/**
 * Servlet implementation class BigJeopardyservlet
 */
@WebServlet(name = "BigJeopardyServlet", urlPatterns = { "/BigJeopardyServlet" })
public class BigJeopardyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<Category> information = new ArrayList<Category>();
	private int lastRound = -1;
	private boolean processed = false;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BigJeopardyServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext servletContext = config.getServletContext();
		ServletJeopardyFactory factory = new ServletJeopardyFactory(
				servletContext);
		QuestionDataProvider provider = factory.createQuestionDataProvider();
		information.addAll(provider.getCategoryData());

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// request from question.jsp
		RequestDispatcher dispatcher = processQuestionRequest(request, response);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher dispatcher;
		HttpSession session = request.getSession(true);
		Game game = (Game) session.getAttribute("game");

		if(game == null){
			// request from login.jsp
			dispatcher = processLoginToJeopardy(request, response);
		} else {
			if(game.isFinished()){
				// request from winner.jsp
				dispatcher = processWinnerToJeopardy(request, response);
			} else {
				// request from jeopardy.jsp
				dispatcher = processJeopardyToQuestion(request, response);
			}
		}
		dispatcher.forward(request, response);
	}

	private void initNewGame(HttpSession session) {
		Game game = new SimpleGame();
		game.getUserPlayer().setAvatar(Avatar.BLACK_WIDOW);
		game.getComputerPlayer().setAvatar(
				Avatar.getOpponent(game.getUserPlayer().getAvatar()));
		session.setAttribute("game", game);

	}

	private RequestDispatcher processLoginToJeopardy(HttpServletRequest request, HttpServletResponse response){
		// create session
		// setup game data in session

		RequestDispatcher dispatcher;
		HttpSession session = request.getSession(true);

		initNewGame(session);
		session.setAttribute("user", request.getParameter("username"));
		session.setAttribute("information", information);

		dispatcher = getServletContext().getRequestDispatcher("/jeopardy.jsp");
		return dispatcher;
	}

	private RequestDispatcher processJeopardyToQuestion(HttpServletRequest request, HttpServletResponse response){
		RequestDispatcher dispatcher;
		HttpSession session = request.getSession(true);
		Game game = (Game) session.getAttribute("game");

		String id = request.getParameter("question_selection");
		if (id != null) {
			Integer toFind = Integer.parseInt(id);
			int j = 0;
			Question question = null;
			for (Category c : information) {
				for (Question q : c.getQuestions()) {
					if (j == toFind) {
						question = q;
					}
					j++;
				}
			}

			game.setRound(game.getRound() + 1);

			// communicate player data
			game.getUserPlayer().setLastQuestion(question);

			// process KI choose and communicate it
			Question q_p2 = processKIQuestion(game);
			game.getComputerPlayer().setLastQuestion(q_p2);

			dispatcher = getServletContext().getRequestDispatcher(
					"/question.jsp");
			processed = false;
		} else {
			//no question selected, do nothing
			dispatcher = getServletContext().getRequestDispatcher(
					"/jeopardy.jsp");
		}
		return dispatcher;
	}

	private RequestDispatcher processQuestionRequest(HttpServletRequest request, HttpServletResponse response){
		RequestDispatcher dispatcher;
		HttpSession session = request.getSession(true);
		Game game = (Game) session.getAttribute("game");
		
		if(processed){
			dispatcher = getServletContext().getRequestDispatcher(
					"/jeopardy.jsp");
			return dispatcher;	
		}
		
		
		// process KI answer
		Question q_p2 = game.getComputerPlayer().getLastQuestion();
		game.getComputerPlayer().setLastAnswer(processKIAnswer(q_p2));
		calcAcc(game.getComputerPlayer());

		// process player answer
		Question q_p1 = game.getUserPlayer().getLastQuestion();
		List<Answer> corrects = q_p1.getCorrectAnswers();
		List<Integer> correctIDs = new LinkedList<Integer>();
		for (Answer a : corrects) {
			correctIDs.add(a.getId());
		}
		String[] answers = request.getParameterValues("answers");
		if (answers == null) {
			answers = new String[0];
		}
		boolean p1_answer = corrects.size() == answers.length;
		for (int i = 0; p1_answer && i < answers.length; i++) {
			if (!correctIDs.contains(Integer.parseInt(answers[i]))) {
				p1_answer = false;
			}
		}
		game.getUserPlayer().setLastAnswer(p1_answer);
		calcAcc(game.getUserPlayer());

		if (game.getRound() >= 10) {
			dispatcher = getServletContext()
					.getRequestDispatcher("/winner.jsp");
		} else {
			dispatcher = getServletContext().getRequestDispatcher(
					"/jeopardy.jsp");
		}
		
		processed = true;
		return dispatcher;
	}

	private RequestDispatcher processWinnerToJeopardy(HttpServletRequest request, HttpServletResponse response){
		// create new session
		// setup game data in session

		RequestDispatcher dispatcher;
		request.getSession().invalidate();
		HttpSession session = request.getSession(true);

		initNewGame(session);
		session.setAttribute("user", request.getParameter("username"));
		session.setAttribute("information", information);

		dispatcher = getServletContext().getRequestDispatcher("/jeopardy.jsp");
		return dispatcher;
	}

	private boolean processKIAnswer(Question q_p2) {
		Random random = new Random();
		return random.nextFloat() < 0.5;
	}

	private void calcAcc(Player p) {
		p.setAcc(p.getAcc() + p.getLastQuestion().getValue()
				* (p.getLastAnswer() ? 1 : -1));
	}

	private Question processKIQuestion(Game game) {
		List<Question> playedQuestion = game.getAllQuestions();

		boolean found = false;
		Random random = new Random();
		Question choose = null;

		List<Question> questions = new ArrayList<Question>();

		for (Category q : information) {
			questions.addAll(q.getQuestions());
		}

		while (!found) {
			int rand = random.nextInt(questions.size() - 1 - 1) + 0;
			choose = questions.get(rand);

			if (!playedQuestion.contains(choose)) {
				found = true;
			}
		}

		return choose;
	}

}

package at.ac.tuwien.big.we15.lab2.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import at.ac.tuwien.big.we15.lab2.api.Question;
import at.ac.tuwien.big.we15.lab2.api.QuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.impl.ServletJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleQuestion;

/**
 * Servlet implementation class BigJeopardyservlet
 */
@WebServlet(name = "BigJeopardyServlet", urlPatterns = { "/BigJeopardyServlet" })
public class BigJeopardyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<Category> information = new ArrayList<Category>();

	// used session attributes
	// round
	// p1_acc (achieved € value)
	// p2_acc
	// questions_played_p1 (list of choosed questions player 1)
	// questions_played_p2
	// last_p1_question
	// last_p2_question
	// last_p1_answerr (bool)
	// last_p2_answer (bool)
	// p1_avatar
	// p2_avatar

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
		RequestDispatcher dispatcher;
		HttpSession session = request.getSession(true);

		// process KI answer
		Question q_p2 = (Question) session.getAttribute("last_p2_question");
		boolean p2_answer = processKIAnswer(q_p2);
		session.setAttribute("last_p2_answer", q_p2);

		int p2_acc = (int) session.getAttribute("p2_acc");

		if (p2_answer) {
			session.setAttribute("p2_acc", p2_acc + q_p2.getValue());
		} else {
			session.setAttribute("p2_acc", p2_acc - q_p2.getValue());
		}

		// process player answer
		Question q_p1 = (Question) session.getAttribute("last_p1_question");

		List<Answer> corrects = q_p1.getCorrectAnswers();
		String[] answers = request.getParameterValues("answers");
		boolean p1_answer = corrects.size() == answers.length;
		for (int i = 0; p1_answer && i < answers.length; i++) {
			corrects.contains(Integer.parseInt(answers[i]));
		}

		int p1_acc = (int) session.getAttribute("p1_acc");
		if (p1_answer) {
			session.setAttribute("p1_acc", p1_acc + q_p1.getValue());
		} else {
			session.setAttribute("p1_acc", p1_acc - q_p1.getValue());
		}

		int rounds = (int) session.getAttribute("round");

		if (rounds >= 10) {
			processWinner(request);
			dispatcher = getServletContext()
					.getRequestDispatcher("/winner.jsp");
		} else {
			dispatcher = getServletContext().getRequestDispatcher(
					"/jeopardy.jsp");
		}

		dispatcher.forward(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher;
		String user = request.getParameter("username");

		// TODO: bessere parameter auswaehlen - eventuell ueber session id?
		if (user == null) {
			// request from jeopardy.jsp

			HttpSession session = request.getSession(true);

			String id = request.getParameter("question_selection");
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

			int rounds = (int) session.getAttribute("round");
			session.setAttribute("round", rounds + 1);

			// communicate player data
			((ArrayList<Question>) session.getAttribute("questions_played_p1"))
					.add(question);
			session.setAttribute("last_p1_question", question);

			// process KI choose and communicate it
			Question q_p2 = processKIQuestion(session);
			((ArrayList<Question>) session.getAttribute("questions_played_p2"))
					.add(q_p2);
			session.setAttribute("last_p2_question", q_p2);

			dispatcher = getServletContext().getRequestDispatcher(
					"/question.jsp");
		} else {
			// request from login.jsp
			// setup game data in session

			HttpSession session = request.getSession(true);
			initNewGame(session);
			session.setAttribute("user", user);
			session.setAttribute("information", information);

			dispatcher = getServletContext().getRequestDispatcher(
					"/jeopardy.jsp");
		}
		dispatcher.forward(request, response);

	}

	private void initNewGame(HttpSession session) {
		session.setAttribute("round", 0);
		session.setAttribute("p1_acc", 0);
		session.setAttribute("p2_acc", 0);
		session.setAttribute("questions_played_p2",
				new ArrayList<SimpleQuestion>());
		session.setAttribute("questions_played_p1",
				new ArrayList<SimpleQuestion>());

		Avatar p1_avatar = Avatar.BLACK_WIDOW;

		session.setAttribute("p1_avatar", p1_avatar);
		session.setAttribute("p2_avatar", Avatar.getOpponent(p1_avatar));
	}

	private boolean processKIAnswer(Question q_p2) {
		Random random = new Random();
		return random.nextFloat() < 0.5;
	}

	private Question processKIQuestion(HttpSession session) {
		List<Category> info = (List<Category>) session
				.getAttribute("information");
		ArrayList<Question> played_p1 = (ArrayList<Question>) session
				.getAttribute("questions_played_p1");
		ArrayList<Question> played_p2 = (ArrayList<Question>) session
				.getAttribute("questions_played_p2");
		boolean found = false;
		Random random = new Random();
		Question choose = null;

		List<Question> questions = new ArrayList<Question>();

		for (Category q : info) {
			questions.addAll(q.getQuestions());
		}

		while (!found) {
			int rand = random.nextInt(questions.size() - 1 - 1) + 0;
			choose = questions.get(rand);

			if (!played_p1.contains(choose) && !played_p2.contains(choose)) {
				found = true;
			}
		}

		return choose;
	}

	private void processWinner(HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	private void processPlace(HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

}

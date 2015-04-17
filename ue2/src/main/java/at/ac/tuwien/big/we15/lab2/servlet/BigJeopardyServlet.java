package at.ac.tuwien.big.we15.lab2.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import at.ac.tuwien.big.we15.lab2.api.Category;
import at.ac.tuwien.big.we15.lab2.api.QuestionDataProvider;
import at.ac.tuwien.big.we15.lab2.api.impl.ServletJeopardyFactory;

import com.google.common.reflect.Parameter;


/**
 * Servlet implementation class BigJeopardyservlet
 */
@WebServlet(name="BigJeopardyServlet", urlPatterns={"/BigJeopardyServlet"})
public class BigJeopardyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private List<Category> categories;

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
		ServletJeopardyFactory factory = new ServletJeopardyFactory(servletContext);
		QuestionDataProvider provider = factory.createQuestionDataProvider();
		categories = provider.getCategoryData();
		for(Category c : categories){
			System.out.println(c.getName());
		}
		
    }	


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
	}




	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		String pw = request.getParameter("password");
		RequestDispatcher dispatcher;
		if(pw==null){
			 dispatcher  = getServletContext().getRequestDispatcher("/qestion.jsp");
		}else{
			 dispatcher  = getServletContext().getRequestDispatcher("/jeopardy.jsp");
		}
		dispatcher.forward(request, response);

		
		
	}

}

import models.Answer;
import models.Category;
import models.JeopardyDAO;
import models.Question;
import org.junit.*;
import org.mockito.Mockito;
import play.db.jpa.JPA;
import play.db.jpa.JPAPlugin;
import play.test.FakeApplication;
import play.test.Helpers;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Author: Lukas
 * Date: 23.05.2015
 */
public class GameJPATest {

    protected static FakeApplication app;
    protected static EntityManager em;

    private static JeopardyDAO jeopardyDao;

    @BeforeClass
    public static void startApp() {
        app = Helpers.fakeApplication(Helpers.inMemoryDatabase());
        Helpers.start(app);

        em = app.getWrappedApplication().plugin(JPAPlugin.class).get().em("default");
        JPA.bindForCurrentThread(em);

        jeopardyDao = JeopardyDAO.INSTANCE;
    }

    @AfterClass
    public static void stopApp() {
        jeopardyDao = null;

        JPA.bindForCurrentThread(null);
        em.close();
        em = null;

        Helpers.stop(app);
    }

    @Before
    public void setUp() {
        em.getTransaction().begin();
    }

    @After
    public void tearDown() {
        em.getTransaction().rollback();
    }

    @Test
    public void createCategory() {
        Category categoryHistory = Mockito.mock(Category.class);
        categoryHistory.setNameDE("Geschichte");
        categoryHistory.setNameEN("History");

        jeopardyDao.persist(categoryHistory);

        em.flush();
        assertNotNull(categoryHistory.getId());
    }

    @Test
    public void createCategoryWithQuestion() {
        Category categoryHistory = new Category();
        categoryHistory.setNameDE("Geschichte");
        categoryHistory.setNameEN("History");

        Question question = new Question();
        question.setTextDE("Test DE");
        question.setTextEN("Test EN");
	    question.setValue(100);

        categoryHistory.addQuestion(question);

        jeopardyDao.persist(categoryHistory);

        em.flush();
        assertNotNull(categoryHistory.getId());
        assertNotNull(question.getId());
    }

    @Test
    public void createCategoryWithQuestionAndChoice() {
        Category categoryHistory = new Category();
        categoryHistory.setNameDE("Geschichte");
        categoryHistory.setNameEN("History");

        Question question = new Question();
        question.setTextDE("Test DE");
        question.setTextEN("Test EN");
	    question.setValue(100);

        Answer answerR = new Answer();
        answerR.setTextDE("Antwort 1");
        answerR.setTextEN("Answer 1");

        Answer answerF = new Answer();
        answerF.setTextDE("Antwort 1");
        answerF.setTextEN("Answer 1");

        question.addRightAnswer(answerR);
        question.addWrongAnswer(answerF);

        categoryHistory.addQuestion(question);

        jeopardyDao.persist(categoryHistory);

        em.flush();
        assertNotNull(categoryHistory.getId());
        assertNotNull(question.getId());
        assertNotNull(answerR.getId());
        assertNotNull(answerF.getId());
    }

    @Test
    public void findAllQuestions() {
        List<Question> questionList = jeopardyDao.findEntities(Question.class);
        assertEquals(35, questionList.size());
    }

    @Test
    public void findAllCategories() {
        List<Category> questionList = jeopardyDao.findEntities(Category.class);
        assertEquals(5, questionList.size());
    }

    @Test
    public void findQuestionWithId6() {
        Question question = jeopardyDao.findEntity(6L, Question.class);

        assertNotNull(question);
        assertEquals("JavaScript...", question.getTextDE());
    }

    @Test
    public void updateCategory() {
        Category categoryHistory = new Category();

        categoryHistory.setNameDE("Geschichte");
        categoryHistory.setNameEN("History");
        jeopardyDao.persist(categoryHistory);
        em.flush();

        categoryHistory.setNameDE("Test");
        jeopardyDao.persist(categoryHistory);

        Category categoryFind = jeopardyDao.findEntity(categoryHistory.getId(), Category.class);
        assertEquals(categoryHistory, categoryFind);
    }

    @Test(expected = PersistenceException.class)
    public void createDetachedCategoryThrowsException() {
        Category categoryHistory = new Category();

        categoryHistory.setNameDE("Geschichte");
        categoryHistory.setNameEN("History");
        jeopardyDao.persist(categoryHistory);
        em.flush();
        em.detach(categoryHistory);
        jeopardyDao.persist(categoryHistory);
    }

    @Test
    public void editDetachedQuestion() {
        Question question = jeopardyDao.findEntity(6L, Question.class);
        em.detach(question);
        question.setTextDE("Test Test");

        Question questionFind = jeopardyDao.findEntity(6L, Question.class);
        assertNotEquals(question.getTextDE(), questionFind.getTextDE());
    }

    @Test
    public void mergeQuestion() {
        Question question = jeopardyDao.findEntity(6L, Question.class);
        em.detach(question);
        question.setTextDE("Test Test");
        jeopardyDao.merge(question);

        Question questionFind = jeopardyDao.findEntity(6L, Question.class);
        assertEquals(question.getTextDE(), questionFind.getTextDE());
    }
}

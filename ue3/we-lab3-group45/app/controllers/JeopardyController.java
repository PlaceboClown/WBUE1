package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.User;
import at.ac.tuwien.big.we15.lab2.api.impl.*;
import at.ac.tuwien.big.we15.lab2.api.*;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JeopardyController extends Controller {

    @Security.Authenticated(JeopardySecurity.class)
    public static Result startGame() {
          return TODO;
    }

    @Security.Authenticated(JeopardySecurity.class)
    public static Result gameOver() {
        String uuid=session("uuid");
        JeopardyGame game = (JeopardyGame) Cache.get(uuid+"game");

        game.getWinner().getUser().getName();

        return ok(winner.render(game));

    }

    @Security.Authenticated(JeopardySecurity.class)
    public static Result roundover() {
        //set richtig falsch stack
        //set kontostand spieler
        //set verfuegbare kategorien
        //fragen auswerten
        String uuid=session("uuid");
        JeopardyGame game = (JeopardyGame) Cache.get(uuid+"game");


        Map<String, String[]> values = Controller.request().queryString();
        Question question = game.getHumanPlayer().getChosenQuestion();
        List<Integer> answerList = new ArrayList<>();

        if(values.containsKey("answers")){

            String[] str_answers = values.get("answers");

            for (String str_answer : str_answers) {
                answerList.add(Integer.parseInt(str_answer));
            }
        }
        game.answerHumanQuestion(answerList);

        System.out.println("Human choose: ");
        game.getHumanPlayer().getAnsweredQuestions().stream().filter(q -> q != null).forEach(q ->
                System.out.print(q.getId() + ","));

        System.out.println("\nMarvin choose: ");
        game.getMarvinPlayer().getAnsweredQuestions().stream().filter(q -> q != null).forEach(q ->
                System.out.print(q.getId() + ","));
        System.out.println("\n");

        if(game.isGameOver()){
            return gameOver();
        } else {
            return ok(jeopardy.render(game));
        }

    }

    @Security.Authenticated(JeopardySecurity.class)
    public static Result newRound() {
        String uuid=session("uuid");
        String postAction = request().body().asFormUrlEncoded().get("question_selection")[0];

        int id = Integer.parseInt(postAction);
        int i = 0;
        JeopardyGame game = (JeopardyGame) Cache.get(uuid+"game");

        game.chooseHumanQuestion(id);
        return ok(question.render(game, game.getHumanPlayer().getChosenQuestion()));
    }

    public static Result index() {
        //erter aufruf
        //noch keine history vorhanden

        // Generate a unique ID
        String uuid=session("uuid");
        if(uuid==null) {
            uuid=java.util.UUID.randomUUID().toString();
            session("uuid", uuid);
        }

        JeopardyFactory factory = new PlayJeopardyFactory(Messages.get("json.file"));
        User user = (User) Cache.get("user");
        JeopardyGame game = factory.createGame(user);

        Cache.set(uuid+"user", user);
        Cache.set(uuid + "game", game);

        return  ok(views.html.jeopardy.render(game));
    }

    @Security.Authenticated(JeopardySecurity.class)
    public static Result logout() {
        String uuid=session("uuid");
        Cache.remove(uuid + "game");
        return Application.authentication();
    }

    @Security.Authenticated(JeopardySecurity.class)
    public static Result newGame() {
        String uuid=session("uuid");
        Cache.remove(uuid+"game");
        return index();
    }
}

package controllers;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.User;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleJeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleQuestion;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleUser;
import at.ac.tuwien.big.we15.lab2.api.*;
import play.Play;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Elisabeth on 04.05.2014.
 */
public class QuizController extends Controller {


    private static int counter = 0;

    @Security.Authenticated(QuizSecurity.class)
    public static Result startGame() {
          return TODO;
    }

    @Security.Authenticated(QuizSecurity.class)
    public static Result gameOver() {
        JeopardyGame game = (JeopardyGame) Cache.get("game");

        game.getWinner().getUser().getName();

        return ok(winner.render(game));

    }

    @Security.Authenticated(QuizSecurity.class)
    public static Result roundover() {
        //set richtig falsch stack
        //set kontostand spieler
        //set verfuegbare kategorien
        //fragen auswerten
        JeopardyGame game = (JeopardyGame) Cache.get("game");
        if(counter==game.getMaxQuestions()){
            return gameOver();
        }
        Map<String, String[]> values = Controller.request().queryString();
        Question chosen = (Question) Cache.get("chosenQuestion");
       for(String s : values.keySet()){
           if(s.equals("answers")){
                    int chosenAnsers = values.get(s).length;

               //wrong amount of answers so definately wront
               if(chosenAnsers != chosen.getCorrectAnswers().size()){

               }else{//right amount of answers maybe right
                   for(int i = 0; i < values.get(s).length; i++) {

                   }
               }


           }
       }




        game.getHumanPlayer().getAnsweredQuestions();
        String answ = Controller.request().getQueryString("answers");
        return ok(jeopardy.render(game, ""+counter++));

    }

    @Security.Authenticated(QuizSecurity.class)
    public static Result newRound() {
        String postAction = request().body().asFormUrlEncoded().get("question_selection")[0];


        int id = Integer.parseInt(postAction);
        int i = 0;
        JeopardyGame game = (JeopardyGame) Cache.get("game");
        Question chosen = null;
        for(Category c : game.getCategories()){
            for(Question q : c.getQuestions()){
                if(i==id) {
                    chosen = q;
                    break;
                }
                i++;
            }
        }
        game.hasBeenChosen(chosen);
        Cache.set("chosenQuestion", chosen);
        return ok(question.render(game, chosen, ""+counter));
    }

    public static Result index() {
        //erter aufruf
        //noch keine history vorhanden

        JeopardyFactory factory = new PlayJeopardyFactory(Messages.get("json.file"));
        User user = (User) Cache.get("user");
        JeopardyGame game = factory.createGame(user);

        Cache.set("user", user);
        Cache.set("game", game);
        return  ok(jeopardy.render(game, ""+counter++));
    }
}

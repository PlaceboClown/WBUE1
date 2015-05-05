package controllers;

import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleJeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleUser;
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

/**
 * Created by Elisabeth on 04.05.2014.
 */
public class QuizController extends Controller {


    @Security.Authenticated(QuizSecurity.class)
    public static Result startGame() {
        String username = session("username");
        User userHuman = new SimpleUser();
        userHuman.setName(username);

        String path = "conf/data.de.json";
        JeopardyFactory factory = new PlayJeopardyFactory(path);

       JeopardyGame game = new SimpleJeopardyGame();
        return TODO;
    }

    @Security.Authenticated(QuizSecurity.class)
    public static Result quiz() {
        return null;

    }

    @Security.Authenticated(QuizSecurity.class)
    public static Result roundover() {
        //set fragen +1
        //set richtig falsch stack
        //set kontostand spieler
        //set verfuegbare kategorien

        return TODO;

    }

    @Security.Authenticated(QuizSecurity.class)
    public static Result newRound() {
        String username = session("username");
        JeopardyGame quizGame = (JeopardyGame) Cache.get(username + "Game");

        return ok(question.render());

    }

    @Security.Authenticated(QuizSecurity.class)
    public static Result index() {
        //erter aufruf
        //noch keine history vorhanden

        String username = session("username");
        String avatarLabel = session("avatarLabel");
        User userHuman = new SimpleUser();
        userHuman.setName(username);
        String pic = session("avatarName");
        return  ok(jeopardy.render(avatarLabel, pic));
    }
}

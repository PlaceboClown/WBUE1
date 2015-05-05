package controllers;

import play.*;
import play.mvc.*;
import models.User;
import play.cache.Cache;
import play.data.Form;
import play.data.format.Formatters;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import play.api.data.*;
import play.api.data.Forms.*;

import static play.data.Form.form;
import views.html.*;

public class Application extends Controller {

    @play.db.ebean.Transactional
    public static Result index() {
        return null;
    }

    public static Result authentication() {
        return  null;

//        return ok(authentication.render(form(User.class)));
    }

    @play.db.ebean.Transactional
    public static Result login() {
        return null;
       /* Form<User> formUser = Form.form(User.class).bindFromRequest();
        if (formUser.hasErrors()) {
            return  null;

//            return badRequest(authentication.render(formUser));
        } else {
            if (findUserByUsernameAndPassword(formUser.get().getUsername(), formUser.get().getPassword()) != null) {
                session().clear();
                session("username", formUser.get().getUsername());
                return null;
            } else {
                formUser.reject("formError", Messages.get("passwordUsernameWrong"));
                return  null;

//                return badRequest(authentication.render(formUser));
            }
        }*/
    }

    public static Result registration() {
        return  null;

//        return ok(registration.render(form(User.class)));
    }

    public static Result logout() {
       /* String username = session("username");
        Cache.remove(username + "Game");
        session().clear();*/
        return  null;
//        return redirect(routes.Application.authentication());
    }

    @play.db.ebean.Transactional
    public static Result newUser() {
            return  null;
    }

    private static User findUserByUsername(String userName) {
      return  null;
    }

    private static User findUserByUsernameAndPassword(String userName, String password) {
       return  null;
    }

}

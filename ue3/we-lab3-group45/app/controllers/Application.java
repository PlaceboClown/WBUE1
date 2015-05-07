package controllers;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleUser;
import models.User;
import play.cache.Cache;
import play.data.Form;
import play.data.format.Formatters;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import sun.security.x509.AVA;
import views.html.authentication;
import views.html.registration;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static play.data.Form.form;

public class Application extends Controller {

    public static Result authentication() {

        return ok(authentication.render(form(User.class)));
    }

    @play.db.jpa.Transactional
    public static Result login() {
        Form<User> formUser = Form.form(User.class).bindFromRequest();
        if (formUser.hasErrors()) {
            return badRequest(authentication.render(formUser));
        } else {
            User user = findUserByUsernameAndPassword(formUser.get().getUsername(), formUser.get().getPassword());
            if( user != null) {
                session().clear();
                session("username", formUser.get().getUsername());
                Avatar userAvatar = Avatar.valueOf(user.getAvatarName());
                session("avatarName", user.getAvatarName());
                session("avatarPic", userAvatar.getImageHead());
                String avatarLabel = userAvatar.getName() +" (DU)";;
                session("avatarLabel", avatarLabel);
                Avatar opponent =  userAvatar.getOpponent(userAvatar);
                session("opponentHead", opponent.getImageHead());
                session("oponentName", opponent.getName());
                at.ac.tuwien.big.we15.lab2.api.User neuerUser = new SimpleUser();
                neuerUser.setAvatar(userAvatar);
                neuerUser.setName(user.getFirstname());
                Cache.set("user", neuerUser);
                Cache.set("opponent", opponent);
                return redirect(routes.JeopardyController.index());
            } else {
                formUser.reject("formError", Messages.get("passwordUsernameWrong"));
                return badRequest(authentication.render(formUser));
            }
        }
    }

    public static Result registration() {
        return ok(registration.render(form(User.class)));
    }


    public static Result logout() {
        String username = session("username");
        Cache.remove(username + "Game");
        session().clear();
        return redirect(routes.Application.authentication());
    }


    @play.db.jpa.Transactional
    public static Result newUser() {

        Formatters.register(Date.class, new Formatters.SimpleFormatter<Date>() {
            private List<SimpleDateFormat> dateFormats = new ArrayList<SimpleDateFormat>() {
                {
                    add(new SimpleDateFormat("yyyy-MM-dd"));
                    add(new SimpleDateFormat("dd.MM.yyyy"));
                }
            };

            @Override
            public Date parse(String input, Locale l) throws ParseException {
                if (input == null || input.isEmpty()) {
                    return null;
                }

                Date date = null;

                for (SimpleDateFormat format : dateFormats) {
                    try {
                        format.setLenient(false);
                        date = format.parse(input);
                    } catch (ParseException e) {
                    }
                    if (date != null) {
                        break;
                    }
                }
                if (date == null) {
                    throw new ParseException(Messages.get("birthdateFormat"), 0);
                }
                return date;
            }

            @Override
            public String print(Date date, Locale l) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                return sdf.format(date);
            }
        });

        Form<User> formUser = Form.form(User.class).bindFromRequest();
        if (formUser.hasErrors()) {
            return badRequest(registration.render(formUser));
        } else {
            if(findUserByUsername(formUser.get().getUsername()) != null) {
                formUser.reject("formError", Messages.get("usernameAlreadyExists"));
                return badRequest(registration.render(formUser));
            } else {
                JPA.em().persist(formUser.get());
                return redirect(routes.Application.authentication());
            }
        }
    }

    private static User findUserByUsername(String userName) {
        EntityManager em = play.db.jpa.JPA.em();
        String queryString = "SELECT u FROM User u where u.username = :username";

        TypedQuery<User> query = em.createQuery(queryString, User.class).setParameter("username", userName);

        List<User> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }

    private static User findUserByUsernameAndPassword(String userName, String password) {
        EntityManager em = play.db.jpa.JPA.em();
        String queryString = "SELECT u FROM User u where u.username = :username and u.password = :password";

        TypedQuery<User> query = em.createQuery(queryString, User.class).
                setParameter("username", userName).
                setParameter("password", password);

        List<User> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        } else {
            return results.get(0);
        }
    }
}

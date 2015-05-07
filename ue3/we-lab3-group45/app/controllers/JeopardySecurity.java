package controllers;

import play.mvc.Http;
import play.mvc.Result;

/**
 * Created by Elisabeth on 10.05.2014.
 */
public class JeopardySecurity extends play.mvc.Security.Authenticator {

    @Override
    public String getUsername(Http.Context context) {
        return context.session().get("username");
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return redirect(routes.Application.authentication());

    }
}

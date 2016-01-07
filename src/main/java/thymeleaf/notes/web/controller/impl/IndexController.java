package thymeleaf.notes.web.controller.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import thymeleaf.notes.business.entities.User;
import thymeleaf.notes.web.controller.IController;

/**
 * Thymeleaf Notes Index Controller
 */
public class IndexController implements IController {

    public IndexController() {
        super();
    }

    public void process( final HttpServletRequest request, final HttpServletResponse response,
            final ServletContext servletContext, final TemplateEngine templateEngine ) throws Exception {

        WebContext ctx = new WebContext( request, response, servletContext, request.getLocale() );

        // 向WebContext中写入变量
        ctx.setVariable( "today", Calendar.getInstance() );
        ctx.setVariable( "welcomeMsgKey", "hello.welcome" );

        // 写request变量
        Map < String, String > userMap = new HashMap < String, String >();
        userMap.put( "name", "John Apricot" );
        userMap.put( "nationality", "US" );
        userMap.put( "age", "25" );
        request.setAttribute( "userMap", userMap );
        User[] users = new User[] {
                new User( "Tom", "Cruise", "US", 25 ),
                new User( "Tom", "Hanks", "EN", 24 ),
                new User( "Dwayne", "Johnson", "JP", 23 ),
                new User( "Jason", "Statham", "JP", 26 )
            };
        request.setAttribute( "users", users );
        request.setAttribute( "orderId", 3 );
        request.setAttribute( "isAdmin", true );
        request.setAttribute( "nullValue", null );
        request.setAttribute( "trueValue", true );
        request.setAttribute( "falseValue", false );
        request.setAttribute( "cssStyle", "warning" );
        request.setAttribute( "checkActive", true );
        request.setAttribute( "checkInactive", false );

        templateEngine.process( "index", ctx, response.getWriter() );

    }
}

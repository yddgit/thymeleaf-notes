package thymeleaf.notes.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;

import thymeleaf.notes.business.entities.User;
import thymeleaf.notes.web.application.ThymeleafApplication;
import thymeleaf.notes.web.controller.IController;

/**
 * Thymeleaf过滤器
 */
public class ThymeleafFilter implements Filter {

    private ServletContext servletContext;

    public ThymeleafFilter() {
        super();
    }

    private static void addUserToSession( final HttpServletRequest request ) {
        // Simulate a real user session by adding a user object
        request.getSession( true ).setAttribute( "user", new User( "John", "Apricot", "Antarctica", 35 ) );
    }

    public void init( final FilterConfig filterConfig ) throws ServletException {
        this.servletContext = filterConfig.getServletContext();
    }

    public void doFilter( final ServletRequest request, final ServletResponse response, final FilterChain chain )
            throws IOException, ServletException {
        addUserToSession( (HttpServletRequest) request );
        if ( !process( (HttpServletRequest) request, (HttpServletResponse) response ) ) {
            chain.doFilter( request, response );
        }
    }

    public void destroy() {
        // nothing to do
    }

    private boolean process( HttpServletRequest request, HttpServletResponse response ) throws ServletException {

        try {

            /*
             * Query controller/URL mapping and obtain the controller that will process the request. If no controller is
             * available, return false and let other filters/servlets process the request.
             */
            IController controller = ThymeleafApplication.resolveControllerForRequest( request );
            if ( controller == null ) {
                return false;
            }

            /*
             * Obtain the TemplateEngine instance.
             */
            TemplateEngine templateEngine = ThymeleafApplication.getTemplateEngine();

            /*
             * Write the response headers
             */
            response.setContentType( "text/html;charset=UTF-8" );
            response.setHeader( "Pragma", "no-cache" );
            response.setHeader( "Cache-Control", "no-cache" );
            response.setDateHeader( "Expires", 0 );

            /*
             * Execute the controller and process view template, writing the results to the response writer.
             */
            controller.process( request, response, this.servletContext, templateEngine );

            return true;

        } catch ( Exception e ) {
            throw new ServletException( e );
        }

    }

}

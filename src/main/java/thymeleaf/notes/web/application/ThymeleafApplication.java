package thymeleaf.notes.web.application;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import thymeleaf.notes.web.controller.IController;
import thymeleaf.notes.web.controller.impl.IndexController;

/**
 * 初始化模板解析器和URL->Controller的映射
 */
public class ThymeleafApplication {

    private static Map < String, IController > controllersByURL;
    private static TemplateEngine              templateEngine;

    static {
        initializeControllersByURL();
        initializeTemplateEngine();
    }

    private static void initializeTemplateEngine() {

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();

        // XHTML is the default mode, but we will set it anyway for better understanding of code
        templateResolver.setTemplateMode( "XHTML" );
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix( "/WEB-INF/templates/" );
        templateResolver.setSuffix( ".html" );
        templateResolver.setCharacterEncoding( "UTF-8" );
        // Set template cache TTL to 1 hour. If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs( Long.valueOf( 3600000L ) );

        // Cache is set to true by default. Set to false if you want templates to
        // be automatically updated when modified.
        templateResolver.setCacheable( false );

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver( templateResolver );

        // Cache的行为和大小可以通过实现ICacheManager接口或使用StandardCacheManager对象来控制
        // StandardCacheManager cacheManager = new StandardCacheManager();
        // cacheManager.setTemplateCacheMaxSize( maxSize );
        // templateEngine.setCacheManager( cacheManager );

    }

    private static Map < String, IController > initializeControllersByURL() {

        controllersByURL = new HashMap < String, IController >();
        controllersByURL.put( "/", new IndexController() );

        return controllersByURL;

    }

    public static IController resolveControllerForRequest( final HttpServletRequest request ) {
        final String path = getRequestPath( request );
        return controllersByURL.get( path );
    }

    public static TemplateEngine getTemplateEngine() {
        return templateEngine;
    }

    private static String getRequestPath( final HttpServletRequest request ) {

        String requestURI = request.getRequestURI();
        final String contextPath = request.getContextPath();

        final int fragmentIndex = requestURI.indexOf( ';' );
        if ( fragmentIndex != -1 ) {
            requestURI = requestURI.substring( 0, fragmentIndex );
        }

        if ( requestURI.startsWith( contextPath ) ) {
            return requestURI.substring( contextPath.length() );
        }
        return requestURI;
    }

    private ThymeleafApplication() {
        super();
    }

}

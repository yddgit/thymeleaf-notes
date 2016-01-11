package thymeleaf.notes.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import thymeleaf.notes.entities.User;

/**
 * Thymeleaf示例
 */
@WebServlet( urlPatterns = { "/index" } )
public class ThymeleafServlet extends HttpServlet {

    private static final long serialVersionUID = -112809348820479484L;

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
            IOException {

        // 1. 构造模板解析器

        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();

        // 设置模板类型，默认是XHTML
        templateResolver.setTemplateMode( "XHTML" );
        // 以下设置，可以自动将模板"home"转换为"/WEB-INF/templates/home.html"
        templateResolver.setPrefix( "/WEB-INF/templates/" );
        templateResolver.setSuffix( ".html" );
        // 设置模板编码
        templateResolver.setCharacterEncoding( "UTF-8" );
        // 设置模板缓存生命周期（一小时），如果不设置，缓存将一直有效，直到缓存空间用完之后被新的缓存替换。
        // If not set, entries would live in cache until expelled by LRU
        templateResolver.setCacheTTLMs( Long.valueOf( 3600000L ) );

        // 是否缓存模板，默认为true. 若要模板修改后立即生效，则可改为false
        templateResolver.setCacheable( false );

        // 2. 构造模板引擎

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver( templateResolver );

        // Cache的行为和大小可以通过实现ICacheManager接口或使用StandardCacheManager对象来控制
        // StandardCacheManager cacheManager = new StandardCacheManager();
        // cacheManager.setTemplateCacheMaxSize( maxSize );
        // templateEngine.setCacheManager( cacheManager );

        // 3. 初始化模板数据

        response.setContentType( "text/html;charset=UTF-8" );
        response.setHeader( "Pragma", "no-cache" );
        response.setHeader( "Cache-Control", "no-cache" );
        response.setDateHeader( "Expires", 0 );

        // 将当前Servlet上下文中的数据初始化到Thymeleaf定义的WebContext中
        WebContext ctx = new WebContext( request, response, this.getServletConfig().getServletContext(),
                request.getLocale() );

        // 向WebContext中写入变量，相当于request.setAttribute
        ctx.setVariable( "today", Calendar.getInstance() );
        ctx.setVariable( "welcomeMsgKey", "hello.welcome" );

        // 写session变量
        request.getSession( true ).setAttribute( "user", new User( "John", "Apricot", "Antarctica", 35 ) );
        // 写request变量
        Map < String, String > userMap = new HashMap < String, String >();
        userMap.put( "name", "John Apricot" );
        userMap.put( "nationality", "US" );
        userMap.put( "age", "25" );
        request.setAttribute( "userMap", userMap );
        User[] users = new User[] { new User( "Tom", "Cruise", "US", 25 ), new User( "Tom", "Hanks", "EN", 24 ),
                new User( "Dwayne", "Johnson", "JP", 23 ), new User( "Jason", "Statham", "JP", 26 ) };
        request.setAttribute( "users", users );
        request.setAttribute( "orderId", 3 );
        request.setAttribute( "isAdmin", true );
        request.setAttribute( "nullValue", null );
        request.setAttribute( "trueValue", true );
        request.setAttribute( "falseValue", false );
        request.setAttribute( "cssStyle", "warning" );
        request.setAttribute( "checkActive", true );
        request.setAttribute( "checkInactive", false );

        // 4. 使用Thymeleaf引擎解析模板，并返回解析结果
        templateEngine.process( "index", ctx, response.getWriter() );
    }

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException,
            IOException {
        doGet( request, response );
    }
}

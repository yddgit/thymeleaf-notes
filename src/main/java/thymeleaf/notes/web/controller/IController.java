package thymeleaf.notes.web.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;

/**
 * Controller接口
 */
public interface IController {

    public void process( HttpServletRequest request, HttpServletResponse response, ServletContext servletContext,
            TemplateEngine templateEngine ) throws Exception;

}

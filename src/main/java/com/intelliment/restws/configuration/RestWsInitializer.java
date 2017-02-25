/**
 * web App Initializer to initiate the DispatcherServlet and Application Context
 * It is the replacement of web.xml
 */
package com.intelliment.restws.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.intelliment.restws.security.RestSecurityConfiguration;

public class RestWsInitializer implements WebApplicationInitializer {

	private static final Logger logger = Logger.getLogger(RestWsInitializer.class);

	@Override
	public void onStartup(ServletContext container) throws ServletException {

		logger.info("Initializing context..");

		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(RestWsConfiguration.class, RestSecurityConfiguration.class);
		ctx.setServletContext(container);
		ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));

		servlet.setLoadOnStartup(1);
		servlet.addMapping("/");
	}

}
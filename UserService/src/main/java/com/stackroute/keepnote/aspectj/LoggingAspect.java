package com.stackroute.keepnote.aspectj;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Before;

/* Annotate this class with @Aspect and @Component */

public class LoggingAspect {
	/*
	 * Write loggers for each of the methods of User controller, any particular
	 * method will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */

	private static final Log log = LogFactory.getLog(LoggingAspect.class);

	@Before("execution (* com.stackroute.keepnote.controller.NoteController.*(..))")
	public void logBefore(JoinPoint joinPoint) {
		log.info("log before called.... " + joinPoint.getSignature().getName());
	}

	@After("execution (* com.stackroute.keepnote.controller.NoteController.*(..))")
	public void logAfter(JoinPoint joinPoint) {
		log.info("log after called.... " + joinPoint.getSignature().getName());
	}

	@AfterReturning("execution (* com.stackroute.keepnote.controller.NoteController.*(..))")
	public void logAfterReturning(JoinPoint joinPoint) {
		log.info("log after returning called.... " + joinPoint.getSignature().getName());
	}

	@AfterThrowing("execution (* com.stackroute.keepnote.controller.NoteController.*(..))")
	public void logAfterThrowing(JoinPoint joinPoint) {
		log.info("log after throwing called.... " + joinPoint.getSignature().getName());
	}
}

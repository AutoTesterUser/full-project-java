package main.java.config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Context {

	String description() default "";

	/**
	 * Breve descripción de lo que realiza el método.
	 */

	String page() default "";

	/**
	 * Página donde se ejecuta el método.
	 */

	String depends() default "";
	/**
	 * Método del cual depende el actual.
	 */
	
	String step() default "";
	/*
	  Descripción o nombre del paso que se ejecutará.
	 */

}

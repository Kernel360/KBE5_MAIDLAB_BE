package kernel.maidlab.core.aop.annotation.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kernel.maidlab.common.enums.LogLevel;
import kernel.maidlab.common.enums.ResponseType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionHandler {

	Class<? extends Throwable>[] value() default {};

	ResponseType responseType() default ResponseType.DATABASE_ERROR;

	LogLevel logLevel() default LogLevel.ERROR;

	String message() default "";

	boolean enableNotification() default false;
}
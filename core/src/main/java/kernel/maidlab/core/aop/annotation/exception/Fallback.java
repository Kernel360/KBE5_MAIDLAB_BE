package kernel.maidlab.core.aop.annotation.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kernel.maidlab.core.aop.enums.FallbackType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Fallback {

	String method() default "";

	Class<? extends Throwable>[] exceptions() default {Exception.class};

	FallbackType type() default FallbackType.METHOD;

	String value() default "";
}
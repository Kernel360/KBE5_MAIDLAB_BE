package kernel.maidlab.core.aop.annotation.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kernel.maidlab.core.aop.enums.RetryStrategy;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Retry {

	int maxAttempts() default 3;

	long delay() default 1000;

	double multiplier() default 2.0;

	long maxDelay() default 10000;

	Class<? extends Throwable>[] retryFor() default {};

	Class<? extends Throwable>[] noRetryFor() default {};

	RetryStrategy strategy() default RetryStrategy.EXPONENTIAL;
}
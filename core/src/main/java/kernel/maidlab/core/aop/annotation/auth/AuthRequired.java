package kernel.maidlab.core.aop.annotation.auth;

import kernel.maidlab.common.enums.UserType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @AuthRequired(roles = {UserType.CONSUMER}) - 소비자만 접근 가능
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthRequired {

	UserType[] roles() default {};

	boolean requireToken() default true;

	String message() default "접근 권한이 없습니다.";
}
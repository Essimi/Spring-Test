package kr.or.ddit.validate.constraints;

import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.*;

@Target({FIELD, METHOD})
@Retention(RUNTIME)
@Constraint(validatedBy= {FileMimeCheckValidator.class})
public @interface FileMimeChecker {
	String mime();
	
	String message() default "{kr.or.ddit.validate.constraints.FIleMimeChecker.message}";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };
}

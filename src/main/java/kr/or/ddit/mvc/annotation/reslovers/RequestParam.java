package kr.or.ddit.mvc.annotation.reslovers;

import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.*;

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface RequestParam {
	
	String value(); // 요청 파라메터의 이름 저장
	
	boolean required() default true; // 필수인지 옵션인지 (파라메터가) 설정
	
	String defaultValue() default ""; // 

}

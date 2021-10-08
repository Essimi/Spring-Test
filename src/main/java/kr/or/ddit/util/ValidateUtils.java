package kr.or.ddit.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validation;
import javax.validation.Validator;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.resourceloading.PlatformResourceBundleLocator;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class ValidateUtils {
	private static Validator validator;
	static {
//		ValidatorFactory factory = Validation.buildDefaultValidatorFactory(); 
//		validator = factory.getValidator();
		validator = Validation.byDefaultProvider()
		        .configure()
		        .messageInterpolator(
		                new ResourceBundleMessageInterpolator(
		                        new PlatformResourceBundleLocator( "kr.or.ddit.msgs.errorMessage" )
		                )
		        )
		        .buildValidatorFactory()
		        .getValidator();
		
	}
	public static <T> boolean validate(T target, Map<String, List<String>> errors, Class...groups) {
		boolean valid = true; // 트루값 설정
		Set<ConstraintViolation<T>> violations = validator.validate(target, groups); 
		// COn~~~~~~ = key, value 값 들어있음, 테스트 validator
		valid = violations.size() == 0; // 검증 다 통과할 경우 0
		if(!valid) { // 에러 나왔을경우
			for(ConstraintViolation<T> violation : violations) {
				log.debug("violation = {}", violation);
				log.debug("violations = {}", violations);
				Path path = violation.getPropertyPath(); // key 값 추출
				log.debug("path 값 = {}", path);
				String message = violation.getMessage(); // message(value) 값 추출
				log.debug("message 값 = {}", message);
				log.debug("path.toStirng = {}",path.toString());
				List<String> already = errors.get(path.toString()); // list 에 message 담음
				
				log.debug("already 1 값 = {}", already);
				if(already==null) { // for 문 다 돌고 더이상 담을 message 가 없을 경우
					already = new ArrayList<>(); // arrayList 형식으로 바꿈
					
					errors.put(path.toString(), already);
					log.debug("errors 값 = {}", errors);
				}
				
				already.add(message); // 에러가 있을경우, (if문 안 들어갔을 경우) already 에 message add
				log.debug("already 2 값 = {}", already);
				// for문 재실행
			}
		}
		log.debug("valid = {}", valid);
		return valid;
		
	}
}

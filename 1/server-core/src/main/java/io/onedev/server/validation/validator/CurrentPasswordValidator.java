package io.onedev.server.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;

import io.onedev.server.security.SecurityUtils;
import io.onedev.server.annotation.CurrentPassword;

public class CurrentPasswordValidator implements ConstraintValidator<CurrentPassword, String> {

	private String message;
	
	@Override
	public void initialize(CurrentPassword constaintAnnotation) {
		message = constaintAnnotation.message();
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext constraintContext) {
		if (value != null) {
			AuthenticationToken token = new UsernamePasswordToken(SecurityUtils.getAuthUser().getName(), value);
			try {
				if (SecurityUtils.getSecurityManager().authenticate(token) != null)
					return true;
			} catch (Exception e) {
			}
			constraintContext.disableDefaultConstraintViolation();
			constraintContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
			return false;
		} else {
			return true;
		}
	}
}

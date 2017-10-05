package pl.touk.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidString.Validator.class)

public @interface ValidString {

    String message() default "{Invalid input type. The types has to be string with length min - 2, max - 50";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    public class Validator implements ConstraintValidator<ValidString, String> {

        @Override
        public void initialize(final ValidString hasId) {
        }

        @Override
        public boolean isValid(final String input, final
        ConstraintValidatorContext constraintValidatorContext) {

            if(input.length() < 2)
                return false;
            if(input.length() > 50)
                return false;

            return true;
        }
    }

}

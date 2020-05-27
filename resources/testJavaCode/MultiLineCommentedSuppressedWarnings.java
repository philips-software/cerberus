import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;
/*
@SuppressWarnings("Things which I have no clue")
 */
@Documented
@Constraint(validatedBy = {PermittedManufacturerConstraintValidator.class})
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, ANNOTATION_TYPE })
public @interface MultiLineCommentedSuppressedWarnings {
	
	String message() default "{invalid.manufacturer}";
	
	Class<?>[] groups() default {};//NOPMD
	
	Class<? extends Payload>[] payload() default {};

}

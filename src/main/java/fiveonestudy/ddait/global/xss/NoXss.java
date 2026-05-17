package fiveonestudy.ddait.global.xss;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = NoXssValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoXss {
    String message() default "허용되지 않는 문자가 포함되어 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
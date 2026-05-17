package fiveonestudy.ddait.global.xss;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoXssValidator implements ConstraintValidator<NoXss, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return !value.contains("<script>")
                && !value.contains("</script>")
                && !value.contains("javascript:");
    }
}
package fiveonestudy.ddait.global.xss;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;

public class NoXssValidator implements ConstraintValidator<NoXss, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) return true;

        if (obj instanceof String) {
            return isClean((String) obj);
        }

        return Arrays.stream(obj.getClass().getDeclaredFields())
                .filter(f -> f.getType() == String.class)
                .allMatch(f -> {
                    f.setAccessible(true);
                    try {
                        String value = (String) f.get(obj);
                        return isClean(value);
                    } catch (IllegalAccessException e) {
                        return true;
                    }
                });
    }

    private boolean isClean(String value) {
        if (value == null) return true;
        String lower = value.toLowerCase();

        return !lower.contains("<script")
                && !lower.contains("</script>")
                && !lower.contains("javascript:")
                && !lower.contains("<iframe")
                && !lower.contains("</iframe>")
                && !lower.contains("<img")
                && !lower.contains("onerror=")
                && !lower.contains("onload=")
                && !lower.contains("onclick=")
                && !lower.contains("onmouseover=")
                && !lower.contains("<svg")
                && !lower.contains("<object")
                && !lower.contains("<embed")
                && !lower.contains("vbscript:");
    }
}
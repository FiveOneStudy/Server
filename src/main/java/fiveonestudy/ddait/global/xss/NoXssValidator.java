package fiveonestudy.ddait.global.xss;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class NoXssValidator implements ConstraintValidator<NoXss, Object> {

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) return true;

        if (obj instanceof String) {
            return isClean((String) obj);
        }

        return Arrays.stream(obj.getClass().getDeclaredFields())
                .allMatch(f -> {
                    f.setAccessible(true);
                    try {
                        Object value = f.get(obj);

                        // String 타입 검사
                        if (value instanceof String) {
                            return isClean((String) value);
                        }

                        // List<String> 타입 검사 추가
                        if (value instanceof List<?> list) {
                            return list.stream()
                                    .filter(item -> item instanceof String)
                                    .map(item -> (String) item)
                                    .allMatch(this::isClean);
                        }

                        return true;
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
package fiveonestudy.ddait.global.moderation.policy;

import java.util.regex.Pattern;

public class KoreanPatterns {

    public static final Pattern INTENT =
        Pattern.compile("(방법|하는법|어떻게|알려줘)");
}
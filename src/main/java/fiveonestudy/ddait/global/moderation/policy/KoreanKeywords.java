package fiveonestudy.ddait.global.moderation.policy;

import java.util.List;

public class KoreanKeywords {

    public static final List<String> VIOLENCE = List.of(
            "죽", "죽여", "죽이", "살해",
            "قتل", "قتل해", "قتل자", "قتل고",
            "قتل고싶", "قتل고 싶", "قتل어", "قتل겠",
            "قتل해야", "قتل하는 방법"
    );

    public static final List<String> SELF_HARM = List.of(
            "자살", "죽고싶", "죽고 싶", "죽고싶다",
            "극단적 선택", "목매", "목매달", "투신",
            "자해", "손목", "피보고싶"
    );

    public static final List<String> SEXUAL = List.of(
            "야스", "섹스", "성관계", "자위",
            "음란", "야동", "19금", "야한",
            "가슴", "엉덩이", "노출"
    );

    public static final List<String> HARASSMENT = List.of(
            "씨발", "ㅅㅂ", "ㅂㅅ", "병신",
            "개새끼", "뒤져", "꺼져",
            "죽어라", "미친놈", "존나"
    );
}
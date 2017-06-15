package de.uni.potsdam.doctool.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ngxanh88 on 13.06.17.
 */
public class Setting {

    public static final String PROGRAMMING_LANGUAGE = "PROGRAMMING_LANGUAGE";
    public static final String DOCUMENTATION_LANGUAGE = "DOCUMENTATION_LANGUAGE";
    public static final String ALL_TESTS_ON = "ALL_TESTS_ON";
    public static final String ALIBI = "ALIBI";
    public static final String COMPLETENESS = "COMPLETENESS";
    public static final String ALL_ALIBI_SCORE = "ALL_ALIBI_SCORE";
    public static final String PARSE_ONLY = "PARSE_ONLY";
    public static final String PRIVATE_VISIBLE = "PRIVATE_VISIBLE";
    public static final String PROTECTED_VISIBLE = "PROTECTED_VISIBLE";


    public static final String JAVA_LANGUAGE = "JAVA_LANGUAGE";
    public static final String ANTLR_LANGUAGE = "ANTLR_LANGUAGE";
    public static final String DE_LANGUAGE = "DE_LANGUAGE";
    public static final String EN_LANGUAGE = "EN_LANGUAGE";

    public static final String ON = "ON";
    public static final String OFF = "OFF";

    public static Map<String, String> getDefaultSetting() {
        final Map<String, String> settingMap = new HashMap<>();

        settingMap.put(PROGRAMMING_LANGUAGE,    JAVA_LANGUAGE);
        settingMap.put(DOCUMENTATION_LANGUAGE,  EN_LANGUAGE);
        settingMap.put(ALL_TESTS_ON,            ON);
        settingMap.put(ALIBI,                   ON);
        settingMap.put(COMPLETENESS,            ON);
        settingMap.put(ALL_ALIBI_SCORE,         ON);
        settingMap.put(PARSE_ONLY,              ON);
        settingMap.put(PRIVATE_VISIBLE,         OFF);
        settingMap.put(PROTECTED_VISIBLE,       ON);

        return settingMap;
    }

    public static boolean hasDocToolSetting(@NotNull final Map<String, String> configMap) {
        return configMap.get(PROGRAMMING_LANGUAGE) != null
                && configMap.get(DOCUMENTATION_LANGUAGE) != null
                && configMap.get(ALL_TESTS_ON) != null
                && configMap.get(ALIBI) != null
                && configMap.get(COMPLETENESS) != null
                && configMap.get(ALL_ALIBI_SCORE) != null
                && configMap.get(PARSE_ONLY) != null
                && configMap.get(PRIVATE_VISIBLE) != null
                && configMap.get(PROTECTED_VISIBLE) != null;
    }

    public static String convertBoolean(boolean b) {
        return b ? ON : OFF;
    }
}

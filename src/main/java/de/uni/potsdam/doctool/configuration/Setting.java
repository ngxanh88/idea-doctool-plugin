package de.uni.potsdam.doctool.configuration;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * the Setting helper of DocTool
 */
public class Setting {

    /** the default setting key label : programming language of DocTool */
    public static final String PARSER_LANGUAGE = "PARSER_LANGUAGE";
    /** the default setting key label : documentation language of DocTool */
    public static final String DOCUMENTATION_LANGUAGE = "DOCUMENTATION_LANGUAGE";
    /** the default setting key label : enable or disable all test (alibi and completeness) */
    public static final String ALL_TESTS_ON = "ALL_TESTS_ON";
    /** the default setting key label : enable or disable the alibi test */
    public static final String ALIBI = "ALIBI";
    /** the default setting key label : enable or disable the completeness test */
    public static final String COMPLETENESS = "COMPLETENESS";
    /** the default setting key label : enable or disable return results with all alibi score (info, warning and error result) */
    public static final String ALL_ALIBI_SCORE = "ALL_ALIBI_SCORE";
    /** the default setting key label : enable or disable parse only (when on the DocTool does not print results to log file and console) */
    public static final String PARSE_ONLY = "PARSE_ONLY";
    /** the default setting key label : enable or disable the private member check */
    public static final String PRIVATE_VISIBLE = "PRIVATE_VISIBLE";
    /** the default setting key label : enable or disable the protected member check */
    public static final String PROTECTED_VISIBLE = "PROTECTED_VISIBLE";

    /** the java programming language setting value */
    public static final String JAVACC = "JAVACC";
    /** the antlr programming language setting value */
    public static final String ANTLR = "ANTLR";
    /** the germany documentation language setting value */
    public static final String DE_LANGUAGE = "DE_LANGUAGE";
    /** the english documentation language setting value */
    public static final String EN_LANGUAGE = "EN_LANGUAGE";

    /** the 'on' setting value */
    public static final String ON = "ON";
    /** the 'off' setting value */
    public static final String OFF = "OFF";

    /**
     * get default settings of DocTool
     * @return the default settings map.
     */
    public static Map<String, String> getDefaultSetting() {
        final Map<String, String> settingMap = new HashMap<>();

        settingMap.put(PARSER_LANGUAGE, JAVACC);
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

    /**
     * check configuration map has all default settings of DocTool.
     * @param configMap the configuration map to check.
     * @return true when the configuration map has all default settings.
     */
    public static boolean hasDocToolSetting(@NotNull final Map<String, String> configMap) {
        return configMap.get(PARSER_LANGUAGE) != null
                && configMap.get(DOCUMENTATION_LANGUAGE) != null
                && configMap.get(ALL_TESTS_ON) != null
                && configMap.get(ALIBI) != null
                && configMap.get(COMPLETENESS) != null
                && configMap.get(ALL_ALIBI_SCORE) != null
                && configMap.get(PARSE_ONLY) != null
                && configMap.get(PRIVATE_VISIBLE) != null
                && configMap.get(PROTECTED_VISIBLE) != null;
    }

    /**
     * convert boolean value to the 'on'/'off' setting value
     * @param b the boolean value to convert.
     * @return the 'on'/'off' setting value as {@code String}.
     */
    public static String convertBoolean(boolean b) {
        return b ? ON : OFF;
    }
}

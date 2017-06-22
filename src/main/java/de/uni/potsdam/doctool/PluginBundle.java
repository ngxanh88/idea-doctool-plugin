package de.uni.potsdam.doctool;

import com.intellij.CommonBundle;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import javax.swing.*;
import java.util.ResourceBundle;

/**
 * Resource bundle manager to get {@code String} value from properties files
 * with different language and icon from resource path.
 */
public class PluginBundle {

    /** tool window id is defined in {@code plugin.xml} */
    public static final String DOC_TOOL_WINDOW_ID = "DocTool";

    /** action group id of DocTool is defined in {@code plugin.xml} */
    public static final String DOC_TOOL_ACTION_GROUP = "DocToolActions";

    /** action group id of tree from tool window is defined in {@code plugin.xml} */
    public static final String PROBLEM_TREE_ACTION_GROUP = "ProblemTreeActions";

    private static ResourceBundle resourceBundle;

    @NonNls
    private static final String BUNDLE = "bundle.PluginBundle";

    private PluginBundle() {
    }

    /**
     * get java swing icon from resource folder.
     *
     * @param iconPath relative resource path to icon (icon muss in resource folder on classpath)
     * @return instance of {@link javax.swing.Icon} or null when icon path is not exist.
     */
    public static Icon icon(@NotNull final String iconPath) {
        return IconLoader.getIcon(iconPath);
    }

    /**
     * get String value from properties files as String template and build message with params.
     * String Template use index of param as place holder and change it with params.
     *
     * <p>
     *     For example: String Template: my {1} is {0} . And param array are ["max", "name"] .
     *     Then <b>Result is "my name is max"</b>
     * </p>
     * @param key bundle key, that is registered in {@code PluginBundle.properties}
     * @param params param array to resolve String template, that is from resource bundle.
     * @return resolved String message or empty String when key is not registered.
     */
    public static String message(@PropertyKey(resourceBundle = BUNDLE) final String key, final Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(BUNDLE);
        }

        return resourceBundle;
    }
}

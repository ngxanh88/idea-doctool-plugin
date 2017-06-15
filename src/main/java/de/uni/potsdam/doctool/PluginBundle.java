package de.uni.potsdam.doctool;

import com.intellij.CommonBundle;
import com.intellij.openapi.util.IconLoader;
import com.intellij.reference.SoftReference;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

import javax.swing.*;
import java.lang.ref.Reference;
import java.util.ResourceBundle;

/**
 * Created by ngxanh88 on 06.06.17.
 */
public class PluginBundle {

    public static final String DOC_TOOL_WINDOW_ID = "DocTool";
    public static final String DOC_TOOL_ACTION_GROUP = "DocToolPluginActions";

    private static Reference<ResourceBundle> resourceBundleReference;

    @NonNls
    private static final String BUNDLE = "bundle.PluginBundle";

    private PluginBundle() {
    }

    public static Icon icon(@NotNull final String iconPath) {
        return IconLoader.getIcon(iconPath);
    }

    public static String message(@PropertyKey(resourceBundle = BUNDLE) final String key, final Object... params) {
        return CommonBundle.message(getBundle(), key, params);
    }

    private static ResourceBundle getBundle() {
        ResourceBundle resourceBundle = null;

        if (resourceBundleReference != null) {
            resourceBundle = resourceBundleReference.get();
        }

        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(BUNDLE);
            resourceBundleReference = new SoftReference<>(resourceBundle);
        }

        return resourceBundle;
    }
}

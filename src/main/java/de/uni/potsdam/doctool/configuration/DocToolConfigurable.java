package de.uni.potsdam.doctool.configuration;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.PluginContext;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * The configurable component of Plugin for inclusion into the Settings.
 * This provide the Swing form as GUI component.
 * Registered in {@code plugin.xml} as a {@code projectConfigurable}
 */
public class DocToolConfigurable implements Configurable {

    /** the current project */
    private final Project project;

    /** the DocTool Settings Storage instance. */
    private final DocToolConfigState configState;
    /** The configuration UI in Swing form */
    private final DocToolConfigGUI configGUI;

    /**
     * Create a new configurable component bean.
     * @param project the current project
     */
    public DocToolConfigurable(@NotNull final Project project) {
        this.project = project;
        this.configState = PluginContext.getPluginConfigState(project);
        this.configGUI = new DocToolConfigGUI();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return PluginBundle.message("doctool.configurable.name");
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        this.configGUI.setConfigSettings(this.configState);
        return configGUI.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return !configGUI.hasNoChangedSettings(this.configState);
    }

    @Override
    public void apply() throws ConfigurationException {
        this.configState.addConfig(this.configGUI.getNewSettings());
        PluginContext.getDocTool(this.project).resetConfig();
    }

    @Override
    public void reset() {
    }

    @Override
    public void disposeUIResources() {
    }
}

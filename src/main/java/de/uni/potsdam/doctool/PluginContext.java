package de.uni.potsdam.doctool;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import de.uni.potsdam.doctool.configuration.DocToolConfigState;
import de.uni.potsdam.doctool.configuration.Setting;
import de.uni.potsdam.doctool.service.DocToolService;
import de.uni.potsdam.doctool.toolwindow.DocToolWindowPanel;
import org.jetbrains.annotations.NotNull;

/**
 * A context manager for get all Plugin service and instance,
 * that is registered in {@code plugin.xml}.
 */
public class PluginContext {

    private PluginContext() {
    }

    /**
     * {@link DocToolService} is registered as {@code ProjectService} of Plugin.
     * <p>This Method get DocTool Service Instance from current project.</p>
     *
     * @param project this current project muss be {@link NotNull}.
     * @return DocTool Service Instance.
     */
    @NotNull
    public static DocToolService getDocTool(@NotNull final Project project) {
        return ServiceManager.getService(project, DocToolService.class);
    }

    /**
     * {@link DocToolConfigState} is registered as {@code ProjectService} of Plugin
     * and used as config storage.
     * <p>This Method get DocTool config storage Instance from current project.</p>
     *
     * @param project this current project muss be {@link NotNull}.
     * @return DocTool Config Storage instance {@link DocToolConfigState}
     */
    @NotNull
    public static DocToolConfigState getPluginConfigState(@NotNull final Project project) {
        final DocToolConfigState configState = ServiceManager.getService(project, DocToolConfigState.class);

        if(!configState.hasConfig()) {
            configState.addConfig(Setting.getDefaultSetting());
        }
        return configState;
    }

    /**
     * get DocTool tool window instance.
     *
     * @param project this current project muss be {@link NotNull}.
     * @return instance of {@link ToolWindow}
     */
    @NotNull
    public static ToolWindow getToolWindow(@NotNull final Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(PluginBundle.DOC_TOOL_WINDOW_ID);
    }

    /**
     * get panel from DocTool tool window instance.
     *
     * @param project this current project muss be {@link NotNull}.
     * @return instance of {@link DocToolWindowPanel}
     */
    @NotNull
    public static DocToolWindowPanel getToolWindowPanel(@NotNull final Project project) {
        final ToolWindow toolWindow = getToolWindow(project);

        for (Content currentContent : toolWindow.getContentManager().getContents()) {
            if (currentContent.getComponent() instanceof DocToolWindowPanel) {
                return (DocToolWindowPanel) currentContent.getComponent();
            }
        }

        System.out.println("Could not find tool window panel on tool window with ID " + PluginBundle.DOC_TOOL_WINDOW_ID);
        throw new RuntimeException("Could not find tool window panel for DocTool Plugin");
    }

}

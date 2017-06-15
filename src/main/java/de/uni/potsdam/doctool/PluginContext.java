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
 * Created by ngxanh88 on 09.05.17.
 */
public class PluginContext {

    private PluginContext() {
    }

    @NotNull
    public static DocToolService getDocTool(@NotNull final Project project) {
        return ServiceManager.getService(project, DocToolService.class);
    }

    @NotNull
    public static DocToolConfigState getPluginConfigState(@NotNull final Project project) {
        final DocToolConfigState configState = ServiceManager.getService(project, DocToolConfigState.class);

        if(!configState.hasConfig()) {
            configState.addConfig(Setting.getDefaultSetting());
        }
        return configState;
    }

    @NotNull
    public static ToolWindow getToolWindow(@NotNull final Project project) {
        return ToolWindowManager.getInstance(project).getToolWindow(PluginBundle.DOC_TOOL_WINDOW_ID);
    }

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

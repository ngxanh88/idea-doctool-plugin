package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import de.uni.potsdam.doctool.PluginContext;
import de.uni.potsdam.doctool.toolwindow.DocToolWindowPanel;

/**
 * Action to toggle (hide or show) error item in DocTool tool window.
 */
public class DisplayError extends ToggleAction {

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        final Project project = DataKeys.PROJECT.getData(anActionEvent.getDataContext());
        return project != null && PluginContext.getToolWindowPanel(project).isDisplayingError();
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean b) {
        final Project project = DataKeys.PROJECT.getData(anActionEvent.getDataContext());
        if (project == null) {
            return;
        }

        final DocToolWindowPanel toolWindowPanel = PluginContext.getToolWindowPanel(project);
        toolWindowPanel.setDisplayingError(b);
    }
}

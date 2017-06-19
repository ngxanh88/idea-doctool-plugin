package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import de.uni.potsdam.doctool.PluginContext;

/**
 * Action to close DocTool tool window
 */
public class DocToolWindowClose extends DocToolBaseAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = DataKeys.PROJECT.getData(event.getDataContext());
        if (project == null) {
            return;
        }

        PluginContext.getToolWindow(project).hide(null);
    }
}

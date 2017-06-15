package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import de.uni.potsdam.doctool.PluginContext;

/**
 * Created by ngxanh88 on 01.06.17.
 */
public abstract class DocToolBaseAction extends AnAction {

    @Override
    public void update(final AnActionEvent event) {
        Project project;
        try {
            project = DataKeys.PROJECT.getData(event.getDataContext());
            final Presentation presentation = event.getPresentation();

            if (project != null) {
                final ToolWindow toolWindow = PluginContext.getToolWindow(project);

                // enable
                presentation.setEnabled(toolWindow.isAvailable());
                presentation.setVisible(true);

                return;
            }

            presentation.setEnabled(false);
            presentation.setVisible(false);

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}

package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import de.uni.potsdam.doctool.PluginContext;

/**
 * Created by ngxanh88 on 12.06.17.
 */
public class TreeExpandAll extends DocToolBaseAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        final Project project = DataKeys.PROJECT.getData(event.getDataContext());
        if (project == null) {
            return;
        }

        PluginContext.getToolWindowPanel(project).expandTree();
    }

    @Override
    public void update(final AnActionEvent event) {
        super.update(event);

        final Project project = DataKeys.PROJECT.getData(event.getDataContext());
        if (project == null) {
            return;
        }

        if (!PluginContext.getToolWindowPanel(project).hasDocProblem()) {
            // disable if no problem
            final Presentation presentation = event.getPresentation();
            presentation.setEnabled(false);
        }
    }
}

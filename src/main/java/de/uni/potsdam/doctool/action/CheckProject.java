package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.PluginContext;
import de.uni.potsdam.doctool.service.DocProblem;

import java.util.List;
import java.util.Map;

/**
 * Action to execute DocTool check on the project root path
 */
public class CheckProject extends DocToolBaseAction {

    @Override
    public void actionPerformed(final AnActionEvent event) {
        final Project project = DataKeys.PROJECT.getData(event.getDataContext());
        if (project == null) {
            return;
        }
        PluginContext.getToolWindowPanel(project).displayInProgress();

        final ToolWindow toolWindow = PluginContext.getToolWindow(project);
        toolWindow.activate(() -> {
            try {

                final Map<PsiFile, List<DocProblem>> resultMap = PluginContext.getDocTool(project).checkProject();

                if (resultMap.size() == 0) {
                    PluginContext.getToolWindowPanel(project).displayMessage(PluginBundle.message("doctool.toolwindow.no-file"));
                } else {
                    PluginContext.getToolWindowPanel(project).displayResults(resultMap);
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        });

    }

}

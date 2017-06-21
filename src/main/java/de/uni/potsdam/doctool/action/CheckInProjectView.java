package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.PluginContext;
import de.uni.potsdam.doctool.service.DocProblem;

import java.util.List;
import java.util.Map;

/**
 * Action to execute DocTool check by right click on the Project tool window
 */
public class CheckInProjectView extends DocToolBaseAction {

    @Override
    public void actionPerformed(final AnActionEvent event) {
        final Project project = DataKeys.PROJECT.getData(event.getDataContext());
        if (project == null) {
            return;
        }
        PluginContext.getToolWindowPanel(project).displayInProgress();

        final ToolWindow toolWindow = PluginContext.getToolWindow(project);
        final VirtualFile[] data = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);

        toolWindow.activate(() -> {
            try {

                final Map<PsiFile, List<DocProblem>> resultMap = PluginContext.getDocTool(project).checkVirtualFiles(data);

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

    @Override
    public void update(final AnActionEvent event) {
        super.update(event);

        boolean show = false;
        final VirtualFile[] data = event.getData(PlatformDataKeys.VIRTUAL_FILE_ARRAY);
        if (data != null) {
            show = true;
        }
        event.getPresentation().setVisible(show);
    }
}

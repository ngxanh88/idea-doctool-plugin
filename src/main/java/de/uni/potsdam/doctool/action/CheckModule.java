package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
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
 * Action to execute DocTool check on the Module root path of selected file
 */
public class CheckModule extends DocToolBaseAction {

    @Override
    public void actionPerformed(final AnActionEvent event) {
        final Project project = DataKeys.PROJECT.getData(event.getDataContext());
        if (project == null) {
            return;
        }
        PluginContext.getToolWindowPanel(project).displayInProgress();

        final ToolWindow toolWindow = PluginContext.getToolWindow(project);
        final VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
        final Module module = ModuleUtil.findModuleForFile(selectedFiles[0], project);

        if (module == null) {
            PluginContext.getToolWindowPanel(project).displayMessage(PluginBundle.message("doctool.toolwindow.no-module"));
            return;
        }

        toolWindow.activate(() -> {
            try {

                final VirtualFile[] moduleSourceRoots = ModuleRootManager.getInstance(module).getSourceRoots();
                final Map<PsiFile, List<DocProblem>> resultMap = PluginContext.getDocTool(project).checkVirtualFiles(moduleSourceRoots);

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

        try {
            final Project project = DataKeys.PROJECT.getData(event.getDataContext());
            if (project == null) {
                return;
            }

            final VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
            if (selectedFiles.length == 0) {
                // disable if no file is selected
                final Presentation presentation = event.getPresentation();
                presentation.setEnabled(false);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}

package de.uni.potsdam.doctool.action;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginContext;
import de.uni.potsdam.doctool.service.DocProblem;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

/**
 * Created by ngxanh88 on 01.06.17.
 */
public class CheckCurrentFile extends DocToolBaseAction {

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
                final Map<PsiFile, List<DocProblem>> resultMap = PluginContext.getDocTool(project).checkVirtualFile(getSelectedFile(project));

                PluginContext.getToolWindowPanel(project).displayResults(resultMap);

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

            final VirtualFile selectedFile = getSelectedFile(project);

            // disable if no file is selected
            final Presentation presentation = event.getPresentation();
            if (selectedFile != null) {
                presentation.setEnabled(true);
            } else {
                presentation.setEnabled(false);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private VirtualFile getSelectedFile(@NotNull final Project project) {

        VirtualFile selectedFile = null;

        final Editor selectedTextEditor = FileEditorManager.getInstance(project).getSelectedTextEditor();
        if (selectedTextEditor != null) {
            selectedFile = FileDocumentManager.getInstance().getFile(selectedTextEditor.getDocument());
        }

        if (selectedFile == null) {
            final VirtualFile[] selectedFiles = FileEditorManager.getInstance(project).getSelectedFiles();
            if (selectedFiles.length > 0) {
                selectedFile = selectedFiles[0];
            }
        }

        return selectedFile;
    }
}

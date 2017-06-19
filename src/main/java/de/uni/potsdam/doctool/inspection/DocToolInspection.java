package de.uni.potsdam.doctool.inspection;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginContext;
import de.uni.potsdam.doctool.service.DocProblemConverter;
import de.uni.potsdam.doctool.service.DocProblem;
import de.uni.potsdam.doctool.service.DocToolService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static de.uni.potsdam.doctool.util.AsyncProcess.startAsync;

/**
 * Inspection to static analyse current file when that is opened or changed
 */
public class DocToolInspection extends LocalInspectionTool {

    @Override
    public ProblemDescriptor[] checkFile(@NotNull final PsiFile psiFile, @NotNull final InspectionManager manager, final boolean isOnTheFly) {
        System.out.println("Inspection check file.");
        return DocProblemConverter.convertToListProblemDescriptor(startAsync(() -> inspectFile(psiFile, manager), Collections.emptyList()), manager);
    }

    private List<DocProblem> inspectFile(@NotNull final PsiFile psiFile, @NotNull final InspectionManager manager) {
        final DocToolService docToolService = PluginContext.getDocTool(psiFile.getProject());
        final Map<PsiFile, List<DocProblem>> resultMap = docToolService.checkPsiFile(psiFile);

        return resultMap.get(psiFile);
    }
}

package de.uni.potsdam.doctool.service;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.util.CodeCharacterUtil;
import doctool.core.Results;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * the converter helper for documentation problem.
 */
public class DocProblemConverter {

    /**
     * convert list documentation problem wrapper to array of {@link ProblemDescriptor}.
     * <p>the {@link ProblemDescriptor} is used in {@link de.uni.potsdam.doctool.inspection.DocToolInspection} to inspect problem in source code.</p>
     *
     * @param docProblemList the list documentation problem wrapper
     * @param manager the inspection manager of IDEA Plugin
     * @return the array of {@link ProblemDescriptor}
     */
    @NotNull
    public static ProblemDescriptor[] convertToListProblemDescriptor(final List<DocProblem> docProblemList, @NotNull final InspectionManager manager) {
        return ofNullable(docProblemList)
                .map(docProblems -> docProblemList.stream()
                        .map(docProblem -> convertToProblemDescriptor(docProblem, manager))
                        .toArray(ProblemDescriptor[]::new))
                .orElseGet(() -> ProblemDescriptor.EMPTY_ARRAY);
    }

    /**
     * convert documentation problem wrapper to {@link ProblemDescriptor}.
     * <p>the {@link ProblemDescriptor} is used in {@link de.uni.potsdam.doctool.inspection.DocToolInspection} to inspect problem in source code.</p>
     *
     * @param docProblem the documentation problem wrapper
     * @param inspectionManager the inspection manager of IDEA Plugin
     * @return the new instance of {@link ProblemDescriptor}
     */
    @NotNull
    public static ProblemDescriptor convertToProblemDescriptor(@NotNull final DocProblem docProblem, @NotNull final InspectionManager inspectionManager) {
        return inspectionManager.createProblemDescriptor(docProblem.getTargetElement(), PluginBundle.message("inspection.message", docProblem.getMessage()),
                null, getProblemHighlightType(docProblem), false, docProblem.isAfterEndOfLine());
    }

    @NotNull
    private static ProblemHighlightType getProblemHighlightType(@NotNull final DocProblem docProblem) {
        if (StringUtils.equals(docProblem.getProblemType(), DocProblem.WARNING_TYPE)) {
            return ProblemHighlightType.WEAK_WARNING;
        } else if (StringUtils.equals(docProblem.getProblemType(), DocProblem.ERROR_TYPE)) {
            return ProblemHighlightType.ERROR;
        }

        return ProblemHighlightType.INFORMATION;
    }

    /**
     * convert list results from DocTool checker Service to list documentation problem wrapper.
     *
     * @param psiFile the psi file is checked from DocTool Service
     * @param resultList the list results from DocTool checker Service
     * @return the list documentation problem wrapper
     */
    @NotNull
    public static List<DocProblem> convertToListDocProblem(@NotNull final PsiFile psiFile, final List<Results.Result> resultList) {
        final List<Integer> lineLengthCache = new ArrayList<>();
        lineLengthCache.add(0); // line 1 is offset 0

        return ofNullable(resultList)
                .map(results -> resultList.stream()
                        .map(result -> convertToDocProblem(lineLengthCache, psiFile, result))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    /**
     * convert result from DocTool checker Service to documentation problem wrapper instance.
     *
     * @param lineLengthCache the list of line numbers from source code in psi file, that are scanned and has problem.
     * @param psiFile the psi file is checked from DocTool Service.
     * @param result the results from DocTool checker Service.
     * @return the new instance of documentation problem wrapper.
     */
    @Nullable
    private static DocProblem convertToDocProblem(@NotNull final List<Integer> lineLengthCache, @NotNull final PsiFile psiFile, @NotNull final Results.Result result) {
        final CodeCharacterUtil.Position codePosition = CodeCharacterUtil.findPosition(lineLengthCache, result.getLine(), psiFile.textToCharArray());

        final PsiElement element = codePosition.getElement(psiFile);
        if(element != null) {
            return new DocProblem(codePosition.getElement(psiFile), result, codePosition.isAfterEndOfLine());
        }
        return null;
    }
}

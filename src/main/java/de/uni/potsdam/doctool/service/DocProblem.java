package de.uni.potsdam.doctool.service;

import com.intellij.psi.PsiElement;
import doctool.core.Results;
import org.jetbrains.annotations.NotNull;

/**
 * Created by ngxanh88 on 07.06.17.
 */
public class DocProblem {

    public static final String ERROR_TYPE = "ERROR_TYPE";
    public static final String WARNING_TYPE = "WARNING_TYPE";
    public static final String INFO_TYPE = "INFO_TYPE";

    private final PsiElement targetElement;
    private final int line;
    private final int column;
    private final String elementName;
    private final String message;
    private final Results.Id problemId;
    private final boolean afterEndOfLine;

    private final String problemType;

    public DocProblem(@NotNull final PsiElement targetElement, Results.Result checkedResult, boolean afterEndOfLine) {
        this(targetElement, 0, checkedResult, afterEndOfLine);
    }

    public DocProblem(@NotNull final PsiElement targetElement, int column, Results.Result checkedResult, boolean afterEndOfLine) {
        this.targetElement = targetElement;
        this.column = column;
        this.problemId = checkedResult.getId();
        this.line = checkedResult.getLine();
        this.message = checkedResult.toString();
        this.afterEndOfLine = afterEndOfLine;
        this.elementName = checkedResult.getElementName();
        this.problemType = getProblemTypeWithResult(checkedResult);
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public String getMessage() {
        return message;
    }

    public PsiElement getTargetElement() {
        return targetElement;
    }

    public boolean isAfterEndOfLine() {
        return afterEndOfLine;
    }

    public String getElementName() {
        return elementName;
    }

    public Results.Id getProblemId() {
        return problemId;
    }

    public String getProblemType() {
        return problemType;
    }

    private String getProblemTypeWithResult(Results.Result result) {
        if (result.getId() == Results.Id.ALIBI_CHARACTER && result instanceof Results.AlibiResult) {
            if (((Results.AlibiResult) result).getAlibiCharacter() < 1.0f) {
                return INFO_TYPE;
            } else {
                return WARNING_TYPE;
            }
        }
        return ERROR_TYPE;
    }

}

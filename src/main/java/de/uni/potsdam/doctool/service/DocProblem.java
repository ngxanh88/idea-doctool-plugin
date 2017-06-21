package de.uni.potsdam.doctool.service;

import com.intellij.psi.PsiElement;
import doctool.core.Results;
import org.jetbrains.annotations.NotNull;

/**
 * the Wrapper of Documentation problem for Plugin
 */
public class DocProblem {

    /** the error type label */
    public static final String ERROR_TYPE = "ERROR_TYPE";
    /** the warning type label */
    public static final String WARNING_TYPE = "WARNING_TYPE";
    /** the info type label */
    public static final String INFO_TYPE = "INFO_TYPE";

    /** the psi element has documentation problem */
    private final PsiElement targetElement;
    /** the line number of source code for the documentation problem */
    private final int line;
    /** the column number of line for the documentation problem */
    private final int column;
    /** the name (method/class/variable name) element, that has documentation problem */
    private final String elementName;
    /** the message text is returned from DocTool */
    private final String message;
    /** the problem id is returned from DocTool */
    private final Results.Id problemId;
    /** the problem is after end of line */
    private final boolean afterEndOfLine;

    /** the documentation problem type ({@code ERROR_TYPE}/{@code WARNING_TYPE}/{@code INFO_TYPE}) */
    private final String problemType;

    /**
     * create new instance of documentation problem wrapper
     * with default column number (0 : the first character at the line)
     *
     * @param targetElement the psi element has documentation problem
     * @param checkedResult the results of DocTool checker.
     * @param afterEndOfLine is the problem after end of line.
     */
    public DocProblem(@NotNull final PsiElement targetElement, Results.Result checkedResult, boolean afterEndOfLine) {
        this(targetElement, 0, checkedResult, afterEndOfLine);
    }

    /**
     * create new instance of documentation problem wrapper.
     *
     * @param targetElement the psi element has documentation problem
     * @param column the column number of the line
     * @param checkedResult the results of DocTool checker.
     * @param afterEndOfLine is the problem after end of line.
     */
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
            if (((Results.AlibiResult) result).getAlibiCharacter() >= 1.0f) {
                return INFO_TYPE;
            } if (((Results.AlibiResult) result).getAlibiCharacter() == 0.0f) {
                return ERROR_TYPE;
            } else {
                return WARNING_TYPE;
            }
        }
        return ERROR_TYPE;
    }

}

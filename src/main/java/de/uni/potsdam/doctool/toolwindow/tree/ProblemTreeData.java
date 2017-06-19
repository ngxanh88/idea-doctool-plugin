package de.uni.potsdam.doctool.toolwindow.tree;

import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.service.DocProblem;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;

/**
 * The meta-data (userObject) of problem tree nodes {@link ProblemTreeNode} in the tool window.
 */
public class ProblemTreeData {

    /** the psiFile has problems. */
    private PsiFile file;
    /** the problem in psi file. */
    private DocProblem docProblem;

    /** the icon of {@link ProblemTreeNode} */
    private Icon icon;
    /** the message text of {@link ProblemTreeNode} */
    private String text;

    /**
     * create instance of tree data with only message text.
     * @param text message text of {@link ProblemTreeNode}
     */
    public ProblemTreeData(final String text) {
        this.text = text;
    }

    /**
     * create instance of tree data with psi file instance and information for this file.
     * @param file psi file instance.
     * @param text the information text for this file.
     * @param icon the icon of psi file type or information type
     */
    public ProblemTreeData(final PsiFile file, final String text, Icon icon) {
        this(file, null, text, icon);
    }

    /**
     * create instance of tree data with documentation problem and psi file instance.
     * @param file the psi file has documentation problem.
     * @param docProblem the problem of this psi file
     */
    public ProblemTreeData(final PsiFile file, final DocProblem docProblem) {
        this(file, docProblem, docProblem.getMessage(), null);

        this.icon = getIconWithProblemType(docProblem);
    }

    /**
     * default constructor to init all attribute
     * @param file the psiFile has problems.
     * @param docProblem the problem in psi file.
     * @param text the text of tree node
     * @param icon the icon of tree node
     */
    public ProblemTreeData(final PsiFile file, final DocProblem docProblem, final String text, final Icon icon) {
        this.file = file;
        this.docProblem = docProblem;
        this.icon = icon;
        this.text = text;
    }

    public PsiFile getFile() {
        return file;
    }

    public void setFile(PsiFile file) {
        this.file = file;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DocProblem getDocProblem() {
        return docProblem;
    }

    public void setDocProblem(DocProblem docProblem) {
        this.docProblem = docProblem;
    }

    @Override
    public String toString() {
        if (text != null) {
            return text;
        }

        return "";
    }

    private Icon getIconWithProblemType(final DocProblem docProblem) {
        if (StringUtils.equals(docProblem.getProblemType(), DocProblem.ERROR_TYPE)) {
            return PluginBundle.icon("/general/balloonError.png");

        } else if (StringUtils.equals(docProblem.getProblemType(), DocProblem.WARNING_TYPE)) {
            return PluginBundle.icon("/general/balloonWarning.png");

        } else if(StringUtils.equals(docProblem.getProblemType(), DocProblem.INFO_TYPE)) {
            return PluginBundle.icon("/general/balloonInformation.png");
        }

        return null;
    }
}

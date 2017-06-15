package de.uni.potsdam.doctool.toolwindow.tree;

import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.service.DocProblem;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;

/**
 * Created by ngxanh88 on 01.06.17.
 */
public class ProblemTreeData {

    private PsiFile file;
    private DocProblem docProblem;

    private Icon icon;
    private String text;

    public ProblemTreeData(String text) {
        this.text = text;
    }

    public ProblemTreeData(PsiFile file, String text, Icon icon) {
        this(file, null, text, icon);
    }

    public ProblemTreeData(PsiFile file, DocProblem docProblem) {
        this(file, docProblem, docProblem.getMessage(), null);

        this.icon = getIconWithProblemType(docProblem);
    }

    public ProblemTreeData(PsiFile file, DocProblem docProblem, String text, Icon icon) {
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

    private Icon getIconWithProblemType(DocProblem docProblem) {
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

package de.uni.potsdam.doctool.toolwindow.tree;

import com.intellij.psi.PsiFile;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.service.DocProblem;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.*;

/**
 * Created by ngxanh88 on 01.06.17.
 */
public class DocProblemTreeModel extends DefaultTreeModel {

    private final DefaultMutableTreeNode resultRootNode;

    public DocProblemTreeModel() {
        super(new DefaultMutableTreeNode());

        resultRootNode = new DefaultMutableTreeNode();
        ((DefaultMutableTreeNode) getRoot()).add(resultRootNode);

        setRootMessage(null);
    }

    public void setRootMessage(String messageText) {
        if (messageText == null) {
            resultRootNode.setUserObject(new ProblemTreeData(PluginBundle.message("doctool.toolwindow.no-check")));
        } else {
            resultRootNode.setUserObject(new ProblemTreeData(messageText));
        }

        nodeChanged(resultRootNode);
    }

    public void clear() {
        resultRootNode.removeAllChildren();
        nodeStructureChanged(resultRootNode);
    }

    public TreeNode getResultRootNode() {
        return resultRootNode;
    }

    public void setResultTree(@NotNull final Map<PsiFile, List<DocProblem>> results, boolean displayWarning, boolean displayInfo) {
        resultRootNode.removeAllChildren();

        int itemCount = 0;
        int fileCount = 0;
        for (Map.Entry<PsiFile, List<DocProblem>> e : results.entrySet()) {

            final ProblemTreeNode fileNode = new ProblemTreeNode();
            final List<DocProblem> problemWrappers = e.getValue();

            for (final DocProblem p : problemWrappers) {

                final ProblemTreeNode resultNode = new ProblemTreeNode(new ProblemTreeData(e.getKey(), p));
                fileNode.add(resultNode);

                itemCount++;
            }

            if (problemWrappers.size() > 0) {
                final Icon javaIcon = PluginBundle.icon("/fileTypes/java.png");
                final ProblemTreeData nodeObject = new ProblemTreeData(
                        e.getKey(),
                        PluginBundle.message("doctool.toolwindow.file-result", e.getKey().getName(), problemWrappers.size()),
                        javaIcon);
                fileNode.setUserObject(nodeObject);

                resultRootNode.add(fileNode);
                fileCount++;
            }
        }

        if (itemCount == 0) {
            setRootMessage(PluginBundle.message("doctool.toolwindow.no-result"));
        } else {
            setRootMessage(PluginBundle.message("doctool.toolwindow.sum-result", itemCount, fileCount));
        }

        filter(displayWarning, displayInfo);
        nodeStructureChanged(resultRootNode);
    }

    public void filter(boolean displayWarning, boolean displayInfo) {

        final List<ProblemTreeNode> changedNodes = new ArrayList<>();

        for (int i = 0; i < resultRootNode.getChildCount(); i++) {
            final ProblemTreeNode childNode = (ProblemTreeNode) resultRootNode.getChildAt(i);

            for (ProblemTreeNode problemNode : childNode.getAllChildren()) {
                final ProblemTreeData nodeData = (ProblemTreeData) problemNode.getUserObject();

                if (StringUtils.equals(nodeData.getDocProblem().getProblemType(), DocProblem.WARNING_TYPE)) {
                    problemNode.setVisible(displayWarning);

                    changedNodes.add(childNode);
                }

                if (StringUtils.equals(nodeData.getDocProblem().getProblemType(), DocProblem.INFO_TYPE)) {
                    problemNode.setVisible(displayInfo);

                    changedNodes.add(childNode);
                }
            }
        }

        for (ProblemTreeNode node : changedNodes) {
            nodeStructureChanged(node);
        }
    }
}

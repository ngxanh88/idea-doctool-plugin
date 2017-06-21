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
 * The problem tree model of the DocTool tool window.
 */
public class DocProblemTreeModel extends DefaultTreeModel {

    /** the root node of the problems tree */
    private final DefaultMutableTreeNode resultRootNode;

    /**
     * create new instance of tree model.
     */
    public DocProblemTreeModel() {
        super(new DefaultMutableTreeNode());

        resultRootNode = new DefaultMutableTreeNode();
        ((DefaultMutableTreeNode) getRoot()).add(resultRootNode);

        setRootMessage(null);
    }

    /**
     * set message text for root node of result tree.
     * <p>This will trigger a reload on the model with a node changed event
     * {@link javax.swing.tree.DefaultTreeModel#nodeChanged(TreeNode)}</p>
     *
     * @param messageText the message text of root node.
     */
    public void setRootMessage(final String messageText) {
        if (messageText == null) {
            resultRootNode.setUserObject(new ProblemTreeData(PluginBundle.message("doctool.toolwindow.no-check")));
        } else {
            resultRootNode.setUserObject(new ProblemTreeData(messageText));
        }

        nodeChanged(resultRootNode);
    }

    /**
     * remove all child node from the root node of result tree.
     * <p>This will trigger a reload on the model with a node structure changed event
     * {@link javax.swing.tree.DefaultTreeModel#nodeStructureChanged(TreeNode)}</p>
     */
    public void clear() {
        resultRootNode.removeAllChildren();
        nodeStructureChanged(resultRootNode);
    }

    /**
     * get root node of result tree
     * @return the instance of root node
     */
    public TreeNode getResultRootNode() {
        return resultRootNode;
    }

    /**
     * create problems tree node of the DocTool tool window with DocTool Checker result
     * <p>This will trigger a reload on the model with a node structure changed event
     * {@link javax.swing.tree.DefaultTreeModel#nodeStructureChanged(TreeNode)}</p>
     *
     * @param results the documentation problem results of DocTool checker.
     * @param displayError the option to show or hide error item.
     * @param displayWarning the option to show or hide warning item.
     * @param displayInfo the option to show or hide info item.
     */
    public void createResultTree(@NotNull final Map<PsiFile, List<DocProblem>> results, boolean displayError, boolean displayWarning, boolean displayInfo) {
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

        filter(displayError, displayWarning, displayInfo);
        nodeStructureChanged(resultRootNode);
    }

    /**
     * show or hide the warning and info item of the problems tree in the tool window.
     * <p>This will trigger a reload on the model with a node structure changed event
     * {@link javax.swing.tree.DefaultTreeModel#nodeStructureChanged(TreeNode)}</p>
     *
     * @param displayError the option to show or hide error item.
     * @param displayWarning the option to show or hide warning item.
     * @param displayInfo the option to show or hide info item.
     */
    public void filter(boolean displayError, boolean displayWarning, boolean displayInfo) {

        final List<ProblemTreeNode> changedNodes = new ArrayList<>();

        for (int i = 0; i < resultRootNode.getChildCount(); i++) {
            final ProblemTreeNode childNode = (ProblemTreeNode) resultRootNode.getChildAt(i);

            for (ProblemTreeNode problemNode : childNode.getAllChildren()) {
                final ProblemTreeData nodeData = (ProblemTreeData) problemNode.getUserObject();

                if (StringUtils.equals(nodeData.getDocProblem().getProblemType(), DocProblem.ERROR_TYPE)) {
                    problemNode.setVisible(displayError);

                    changedNodes.add(childNode);
                }

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

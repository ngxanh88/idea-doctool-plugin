package de.uni.potsdam.doctool.toolwindow;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import de.uni.potsdam.doctool.PluginBundle;
import de.uni.potsdam.doctool.service.DocProblem;
import de.uni.potsdam.doctool.toolwindow.tree.DocProblemTreeModel;
import de.uni.potsdam.doctool.toolwindow.tree.ProblemTreeCellRenderer;
import de.uni.potsdam.doctool.toolwindow.tree.ProblemTreeData;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

/**
 * DocTool {@link JPanel} of tool window {@code ToolWindow}
 */
public class DocToolWindowPanel extends JPanel {

    private final Project project;

    private JTree resultsTree;
    private DocProblemTreeModel treeModel;

    private boolean displayingError = true;
    private boolean displayingWarning = true;
    private boolean displayingInfo = false;

    /**
     * create instance of DocToll tool window panel to manage and show all documentation problems as tree.
     * @param project current project that is opened.
     */
    DocToolWindowPanel(@NotNull final Project project) {
        super(new BorderLayout());
        this.project = project;

        setBorder(new EmptyBorder(1, 1, 1, 1));
        add(createToolBarBox(), BorderLayout.WEST);
        add(createToolPanel(), BorderLayout.CENTER);
        expandTree();
    }

    private Box createToolBarBox() {
        final ActionGroup docToolActionGroup = (ActionGroup) ActionManager.getInstance().getAction(PluginBundle.DOC_TOOL_ACTION_GROUP);
        final ActionToolbar docToolActionToolbar = ActionManager.getInstance().createActionToolbar(PluginBundle.DOC_TOOL_WINDOW_ID, docToolActionGroup, false);

        final ActionGroup problemtreeActionGroup = (ActionGroup) ActionManager.getInstance().getAction(PluginBundle.PROBLEM_TREE_ACTION_GROUP);
        final ActionToolbar problemtreeToolbar = ActionManager.getInstance().createActionToolbar(PluginBundle.DOC_TOOL_WINDOW_ID, problemtreeActionGroup, false);

        docToolActionToolbar.getComponent().setVisible(true);

        final Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(docToolActionToolbar.getComponent());
        toolBarBox.add(problemtreeToolbar.getComponent());

        return toolBarBox;
    }

    private JPanel createToolPanel() {

        treeModel = new DocProblemTreeModel();
        resultsTree = new Tree(treeModel);
        resultsTree.setRootVisible(false);

        resultsTree.addTreeSelectionListener(e -> {
            if (e.getPath() != null) {
                scrollToDocProblem(e.getPath());
            }
        });

        resultsTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    final TreePath treePath = resultsTree.getPathForLocation(e.getX(), e.getY());

                    if (treePath != null) {
                        scrollToDocProblem(treePath);
                    }
                }
            }
        });

        resultsTree.setCellRenderer(new ProblemTreeCellRenderer());

        final JPanel toolPanel = new JPanel(new BorderLayout());
        toolPanel.add(new JBScrollPane(resultsTree), BorderLayout.CENTER);

        ToolTipManager.sharedInstance().registerComponent(resultsTree);

        return toolPanel;
    }

    /**
     * check results tree from panel
     * @return true when problem tree model has results.
     */
    public boolean hasDocProblem() {
        return treeModel.getResultRootNode().getChildCount() > 0;
    }

    /**
     * * Show problems tree in tool window when DocTool return results
     * @param results Documentation problem Map
     */
    public void displayResults(final Map<PsiFile, List<DocProblem>> results) {
        treeModel.createResultTree(results, displayingError, displayingWarning, displayingInfo);

        invalidate();
        repaint();
        expandTree();
    }

    /**
     * Show message in tool window when DocTool is starting and checking
     */
    public void displayInProgress() {
        treeModel.clear();
        treeModel.setRootMessage(PluginBundle.message("doctool.toolwindow.in-process"));
    }

    /**
     * * Show message in tool window
     * @param msg message to show
     */
    public void displayMessage(String msg) {
        treeModel.clear();
        treeModel.setRootMessage(msg);
    }

    /**
     * collapse problems tree in tool window
     */
    public void collapseTree() {
        for (int i = 1; i < resultsTree.getRowCount(); ++i) {
            resultsTree.collapseRow(i);
        }
    }

    /**
     * expand problems tree in tool window
     */
    public void expandTree() {
        expandNode(resultsTree, treeModel.getResultRootNode(), new TreePath(treeModel.getPathToRoot(treeModel.getResultRootNode())), 3);
    }

    /**
     * get option to display error item
     * @return true when error item is displayed in tool window
     */
    public boolean isDisplayingError() {
        return displayingError;
    }

    /**
     * set option to display error item
     * @param displayingError option to show or hide error item
     */
    public void setDisplayingError(boolean displayingError) {
        this.displayingError = displayingError;

        treeModel.filter(displayingError, displayingWarning, displayingInfo);
    }

    /**
     * get option to display warning item
     * @return true when warning item is displayed in tool window
     */
    public boolean isDisplayingWarning() {
        return displayingWarning;
    }

    /**
     * set option to display warning item
     * @param displayingWarning option to show or hide warning item
     */
    public void setDisplayingWarning(boolean displayingWarning) {
        this.displayingWarning = displayingWarning;

        treeModel.filter(displayingError, displayingWarning, displayingInfo);
    }

    /**
     * get option to display info item
     * @return true when info item is displayed in tool window
     */
    public boolean isDisplayingInfo() {
        return displayingInfo;
    }

    /**
     * set option to display info item
     * @param displayingInfo option to show or hide info item
     */
    public void setDisplayingInfo(boolean displayingInfo) {
        this.displayingInfo = displayingInfo;

        treeModel.filter(displayingError, displayingWarning, displayingInfo);
    }

    private static void expandNode(final JTree tree, final TreeNode node, final TreePath path, final int level) {
        if (level <= 0) {
            return;
        }

        tree.expandPath(path);

        for (int i = 0; i < node.getChildCount(); ++i) {
            final TreeNode childNode = node.getChildAt(i);
            expandNode(tree, childNode, path.pathByAddingChild(childNode), level - 1);
        }
    }

    private void scrollToDocProblem(final TreePath treePath) {
        final DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) treePath.getLastPathComponent();

        if (treeNode != null && (treeNode.getUserObject() instanceof ProblemTreeData)) {
            final ProblemTreeData treeData = (ProblemTreeData) treeNode.getUserObject();

            final VirtualFile virtualFile = treeData.getFile() == null ? null : treeData.getFile().getVirtualFile();

            if (virtualFile != null && virtualFile.exists()) {
                final FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
                final FileEditor[] fileEditors = fileEditorManager.openFile(virtualFile, true);

                if (fileEditors.length > 0 && fileEditors[0] instanceof TextEditor) {

                    final int line = treeData.getDocProblem() == null ? 0 : treeData.getDocProblem().getLine();
                    final int column = treeData.getDocProblem() == null ? 0 : treeData.getDocProblem().getColumn();

                    final LogicalPosition problemPos = new LogicalPosition(Math.max(line - 1, 0), Math.max(column, 0));

                    final Editor editor = ((TextEditor) fileEditors[0]).getEditor();
                    editor.getCaretModel().moveToLogicalPosition(problemPos);
                    editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                }
            }
        }

    }

}

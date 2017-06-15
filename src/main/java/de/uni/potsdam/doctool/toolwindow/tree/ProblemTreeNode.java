package de.uni.potsdam.doctool.toolwindow.tree;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Vector;

/**
 * Created by ngxanh88 on 01.06.17.
 */
public class ProblemTreeNode extends DefaultMutableTreeNode {

    private boolean visible = true;

    public ProblemTreeNode() {
    }

    public ProblemTreeNode(Object userObject) {
        super(userObject);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @SuppressWarnings("unchecked")
    public Vector<ProblemTreeNode> getAllChildren() {
        return super.children;
    }

    @Override
    public TreeNode getChildAt(int index) {
        int realIndex = -1;
        int visibleIndex = -1;

        for (Object child : super.children) {
            if (((ProblemTreeNode) child).isVisible()) {
                ++visibleIndex;
            }

            ++realIndex;
            if (visibleIndex == index) {
                break;
            }
        }

        return (TreeNode) super.children.get(realIndex);
    }

    @Override
    public int getChildCount() {
        if (super.children == null) {
            return 0;
        }

        int result = 0;
        for (Object child : super.children) {
            final ProblemTreeNode node = (ProblemTreeNode) child;
            if (node.isVisible()) {
                ++result;
            }
        }

        return result;
    }
}

package de.uni.potsdam.doctool.toolwindow.tree;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * The tree cell renderer for problem tree nodes in the tool window.
 */
public class ProblemTreeCellRenderer extends JLabel implements TreeCellRenderer {

    private boolean isSelected;

    /**
     * create instance of tree cell renderer.
     */
    public ProblemTreeCellRenderer() {
        super();
        setOpaque(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(getBackground());

        int offset = 0;
        if (getIcon() != null) {
            offset = getIcon().getIconWidth() + getIconTextGap();
        }

        g.fillRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);

        if (isSelected) {
            g.setColor(UIManager.getColor("Tree.selectionBorderColor"));
            g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        }

        super.paintComponent(g);
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        this.isSelected = selected;

        if (value != null && value instanceof DefaultMutableTreeNode) {
            final Object data = ((DefaultMutableTreeNode) value).getUserObject();

            if (data != null && data instanceof ProblemTreeData) {
                final ProblemTreeData treeNode = (ProblemTreeData) data;

                setIcon(treeNode.getIcon());
                setText(treeNode.toString());
                setFont(tree.getFont());
                setForeground(UIManager.getColor(selected ? "Tree.selectionForeground" : "Tree.textForeground"));
                setBackground(UIManager.getColor(selected ? "Tree.selectionBackground" : "Tree.textBackground"));
                validate();
            }
        }

        return this;
    }

}

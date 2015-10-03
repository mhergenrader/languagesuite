package notecardstation.general;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;

import guicomponents.*;

// Copyright (c) 2010-2011 Michael Hergenrader
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.

/**
 * Main panel in the notecard editor with lists of stacks as well as possible actions to start (view part of Notecard Station)
 * @author Michael Hergenrader
 */
public class StacksTreePanel extends JPanel {

    private NotecardStation owner;

    private JScrollPane treeScrollPane;
    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel treeModel;
    private JTree tree;

    private JToolBar toolbar;
    private ToolbarButton newFolderButton;
    private ToolbarButton newStackButton;
    private ToolbarButton editStackButton;
    private ToolbarButton reviewStackButton;
    private ToolbarButton deleteStackButton;
    private ToolbarButton returnToMainButton;

    public StacksTreePanel(NotecardStation owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(470,300));
        setMaximumSize(new Dimension(470,300));
        setPreferredSize(new Dimension(470,300));

        setupTree();
        setupToolbar();

        setLayout(new BorderLayout());
        add(treeScrollPane,BorderLayout.CENTER);
        add(toolbar,BorderLayout.SOUTH);
    }

    private void setupTree() {
        rootNode = new DefaultMutableTreeNode("Notecard Stacks");
        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.addTreeSelectionListener(owner);
        tree.setToggleClickCount(1); // single click
        tree.setScrollsOnExpand(true);

        treeScrollPane = new JScrollPane(tree);
        treeScrollPane.setMinimumSize(new Dimension(470,250));
        treeScrollPane.setMaximumSize(new Dimension(470,250));
        treeScrollPane.setPreferredSize(new Dimension(470,250));
    }

    private void setupToolbar() {
        toolbar = new JToolBar("panel toolbar");
        toolbar.setFloatable(false);
        toolbar.setOpaque(false);
        toolbar.setBorderPainted(false);
        toolbar.setMinimumSize(new Dimension(470,50));
        toolbar.setMaximumSize(new Dimension(470,50));
        toolbar.setPreferredSize(new Dimension(470,50));

        newFolderButton = new ToolbarButton("Create New Notecard Folder",owner.getImageLocation()+"newfolder.png");
        newFolderButton.addActionListener(owner);
        newStackButton = new ToolbarButton("Create New Notecard Stack",owner.getImageLocation()+"newset_edited-1.png");
        newStackButton.addActionListener(owner);
        editStackButton = new ToolbarButton("Edit Notecard Stack",owner.getImageLocation()+"editset_edited-1.png");
        editStackButton.addActionListener(owner);
        reviewStackButton = new ToolbarButton("Review Notecard Stack(s)",owner.getImageLocation()+"lightbulbicon.png");
        reviewStackButton.addActionListener(owner);
        deleteStackButton = new ToolbarButton("Delete Notecard Stack(s)",owner.getImageLocation()+"deletecomponent_edited-1.png");
        deleteStackButton.addActionListener(owner);
        returnToMainButton = new ToolbarButton("Exit Language Suite",owner.getImageLocation()+"returntomain_edited-1.png");
        returnToMainButton.addActionListener(owner);

        toolbar.addSeparator(new Dimension(60,0));
        toolbar.add(newFolderButton);
        toolbar.addSeparator(new Dimension(10,0));
        toolbar.add(newStackButton);
        toolbar.addSeparator(new Dimension(10,0));
        toolbar.add(editStackButton);
        toolbar.addSeparator(new Dimension(10,0));
        toolbar.add(reviewStackButton);
        toolbar.addSeparator(new Dimension(10,0));
        toolbar.add(deleteStackButton);
        toolbar.addSeparator(new Dimension(10,0));
        toolbar.add(returnToMainButton);

        editStackButton.setEnabled(false);
        reviewStackButton.setEnabled(false);
        deleteStackButton.setEnabled(false);
    }

    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public JTree getTree() {
        return tree;
    }

    public ToolbarButton getNewStackButton() {
        return newStackButton;
    }

    public ToolbarButton getEditStackButton() {
        return editStackButton;
    }

    public ToolbarButton getReviewStackButton() {
        return reviewStackButton;
    }

    public ToolbarButton getDeleteStackButton() {
        return deleteStackButton;
    }

    public ToolbarButton getReturnToMainButton() {
        return returnToMainButton;
    }
}

package conjugationstation;

import javax.swing.*;
import javax.swing.tree.*;
import guicomponents.*;
import conjugationstation.conjugationcomponents.*;
import java.util.*;
import java.awt.*;
import javax.swing.event.*;
import languagesuite.*;

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
 * Main panel for handling all conjugation sets
 * @author Michael Hergenrader
 */
public class SetsViewPanel extends JPanel implements TreeSelectionListener {
    private ConjugationStation owner;

    private DefaultMutableTreeNode rootNode;
    private DefaultTreeModel loadModel;
    private JTree loadTree;
    private JScrollPane scrollPane;

    private JToolBar toolbar;
    private ToolbarButton newFolderButton;
    private ToolbarButton newSetButton;
    private ToolbarButton newLanguageButton;
    private ToolbarButton editSetButton;
    private ToolbarButton loadButton;
    private ToolbarButton deleteButton;
    private ToolbarButton returnToMainButton;

    private ImageIcon foldericon;
    private ImageIcon pageicon;

    private LanguageSet languageSet;
    private ArrayList<Language> selectedLanguages;
    private ArrayList<ConjugationTreeObject> selectedVerbSets;

    private static String defaultImageLocation;

    public SetsViewPanel(ConjugationStation owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(470,300));
        setMaximumSize(new Dimension(470,300));
        setPreferredSize(new Dimension(470,300));

        defaultImageLocation = LanguageSuite.defaultImageLocation;

        selectedLanguages = new ArrayList<Language>();
        selectedVerbSets = new ArrayList<ConjugationTreeObject>();
		
        foldericon = new ImageIcon(defaultImageLocation+"foldericon.png");
        pageicon = new ImageIcon(defaultImageLocation+"pageicon.png");

        initializeTree();
        initializeToolbar();

        setLayout(new BorderLayout());
        add(scrollPane,BorderLayout.CENTER);
        add(toolbar,BorderLayout.SOUTH);
    }

    public class MyTree extends JTree {

        public MyTree(DefaultTreeModel loadModel) {
            super(loadModel);
        }
        @Override
        public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if(value != null) {
                String sValue;

                if(((DefaultMutableTreeNode)value).getUserObject() instanceof Language) {
                    Language current = ((Language)((DefaultMutableTreeNode)value).getUserObject());
                    sValue = current.getTitle() + " (author: " + current.getAuthor() + ")";
                }
                else {
                    sValue = value.toString();
                }


                if (sValue != null) {
                    return sValue;
                }
            }
            return "";
        }
    }


    private void initializeTree() {
        rootNode = new DefaultMutableTreeNode("Languages");
        loadModel = new DefaultTreeModel(rootNode);
        loadTree = new MyTree(loadModel);
        loadTree.setRootVisible(false);
        loadTree.addTreeSelectionListener(this);
        loadTree.setToggleClickCount(1); // single click
        loadTree.setScrollsOnExpand(true);

        if(foldericon != null && pageicon != null) {
            loadTree.setCellRenderer(new LanguageTreeCellRenderer());
        }
        loadTree.setRowHeight(20);

        scrollPane = new JScrollPane(loadTree);
        scrollPane.setMinimumSize(new Dimension(470,250));
        scrollPane.setMaximumSize(new Dimension(470,250));
        scrollPane.setPreferredSize(new Dimension(470,250));
    }

    private void initializeToolbar() {
        toolbar = new JToolBar("Conjugation Station Toolbar");
        toolbar.setOpaque(false);
        toolbar.setBorderPainted(false);
        toolbar.setFloatable(false);
        toolbar.setMinimumSize(new Dimension(470,50));
        toolbar.setMaximumSize(new Dimension(470,50));
        toolbar.setPreferredSize(new Dimension(470,50));

        newFolderButton = new ToolbarButton("Create a new folder inside a language",defaultImageLocation+"newfolder.png");
        newSetButton = new ToolbarButton("Create a new conjugation set",defaultImageLocation+"newset_edited-1.png");
        newLanguageButton = new ToolbarButton("Create a new language",defaultImageLocation+"newlanguage_edited-1.png");
        editSetButton = new ToolbarButton("Edit the current conjugation set",defaultImageLocation+"editset_edited-1.png");
        loadButton = new ToolbarButton("Review the current conjugation set",defaultImageLocation+"lightbulbicon.png");
        deleteButton = new ToolbarButton("Delete the current selected components",defaultImageLocation+"deletecomponent_edited-1.png");
        returnToMainButton = new ToolbarButton("Exit Language Suite",defaultImageLocation+"returntomain_edited-1.png");

        newFolderButton.addActionListener(owner);
        newFolderButton.setEnabled(false);
        newSetButton.addActionListener(owner);
        newSetButton.setEnabled(false);
        newLanguageButton.addActionListener(owner);
        editSetButton.addActionListener(owner);
        editSetButton.setEnabled(false);
        loadButton.addActionListener(owner);
        loadButton.setEnabled(false);
        deleteButton.addActionListener(owner);
        deleteButton.setEnabled(false);
        returnToMainButton.addActionListener(owner);

        toolbar.addSeparator(new Dimension(30,10));
        toolbar.add(newLanguageButton);
        toolbar.addSeparator(new Dimension(10,10));
        toolbar.add(newFolderButton);
        toolbar.addSeparator(new Dimension(10,50));
        toolbar.add(newSetButton);
        toolbar.addSeparator(new Dimension(10,10));
        toolbar.add(editSetButton);
        toolbar.addSeparator(new Dimension(10,10));
        toolbar.add(loadButton);
        toolbar.addSeparator(new Dimension(10,10));
        toolbar.add(deleteButton);
        toolbar.addSeparator(new Dimension(10,10));
        toolbar.add(returnToMainButton);
    }
	
    // helper method to insert a node correctly into a tree to automatically maintain alphabetical order
    public void insertNewLanguageIntoTree(Language language) {
        int index = 0;
        boolean inserting = false;
        for(int i = 0; i < rootNode.getChildCount(); i++) {
            if(language.toString().compareTo(((Language)(((DefaultMutableTreeNode)rootNode.getChildAt(i)).getUserObject())).toString()) < 0) {
                index = i;
                inserting = true; // declare that this has found a place to insert
                break;
            }
        }

        if(inserting) {
            rootNode.insert(new DefaultMutableTreeNode(language),index);
        }
        else { // no place to insert, so just add to the end (not inserting, but appending)
            rootNode.add(new DefaultMutableTreeNode(language));
        }

        loadModel.reload();
    }
	
    public ConjugationTreeObject findTreeObject(ConjugatedVerbSet cvs) {
        for(int i = 0; i < rootNode.getChildCount(); i++) {
            DefaultMutableTreeNode languageChild = ((DefaultMutableTreeNode)rootNode.getChildAt(i));
            if(languageChild.getUserObject() instanceof Language) {
                for(int j = 0; j < languageChild.getChildCount(); j++) {
                    ConjugationTreeObject cto = (ConjugationTreeObject)((DefaultMutableTreeNode)languageChild.getChildAt(j)).getUserObject();
                    if(cto.getVerbSet().equals(cvs)) {
                        return cto;
                    }
                }
            }
        }
        return null;
    }

    public ArrayList<Language> getSelectedLanguages() {
        return selectedLanguages;
    }

    public ArrayList<ConjugationTreeObject> getSelectedConjugationObjects() {
        return selectedVerbSets;
    }

    public void valueChanged(TreeSelectionEvent e) {
        selectedLanguages.clear();
        selectedVerbSets.clear();

        TreePath[] paths = loadTree.getSelectionPaths();

        // problem getting the tree paths or there was nothing at all selected
        if(paths == null || paths.length == 0) {
            newFolderButton.setEnabled(false);
            newSetButton.setEnabled(false);
            editSetButton.setEnabled(false);
            loadButton.setEnabled(false);
            deleteButton.setEnabled(false);
            return;
        }

        // create the lists to analyze
        for(int i = 0; i < paths.length; i++) {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
            if(current.getUserObject() instanceof Language) {
                selectedLanguages.add((Language)current.getUserObject());
            }
            else if(current.getUserObject() instanceof ConjugationTreeObject) {
                selectedVerbSets.add((ConjugationTreeObject)current.getUserObject());
            }
        }

        // no languages selected, but since there is something selected, there must be verb sets - can open all of them
        if(selectedLanguages.isEmpty()) {
            Language firstSelectedLanguage = null;
            boolean enableNewSetButton = true;
            for(int i = 0; i < selectedVerbSets.size(); i++) {
                if(i == 0) {
                    firstSelectedLanguage = selectedVerbSets.get(0).getVerbSet().getLanguage();
                    continue;
                }
                if(!selectedVerbSets.get(0).getVerbSet().getLanguage().toString().equals(selectedVerbSets.get(i).getVerbSet().getLanguage().toString())) {
                    enableNewSetButton = false; // here, there are multiple languages selected, so disable
                    break;
                }
            }

            newFolderButton.setEnabled(false); // no languages selected
            newSetButton.setEnabled(enableNewSetButton);
            editSetButton.setEnabled(true);
            loadButton.setEnabled(true);
            deleteButton.setEnabled(true);
        }
        else { // there are languages selected
            if(selectedVerbSets.isEmpty()) { // only languages are selected
                newSetButton.setEnabled(selectedLanguages.size() == 1);

                boolean thereAreEmptyLanguages = false;
                for(Language l : selectedLanguages) {
                    if(l.getConjugatedVerbSets().isEmpty()) {
                        thereAreEmptyLanguages = true;
                        break;
                    }
                }

                editSetButton.setEnabled(!thereAreEmptyLanguages);
                loadButton.setEnabled(!thereAreEmptyLanguages);

                deleteButton.setEnabled(true);
            }
            else { // there is a mix of languages and verb sets selected
                // need to test if the verb sets and their respective languages are selected
                boolean theLanguagesAndSetsAreSeparated = true;
                boolean thereAreEmptyLanguages = false;
                for(ConjugationTreeObject cvs : selectedVerbSets) {
                    if(selectedLanguages.contains(cvs.getVerbSet().getLanguage())) {
                        theLanguagesAndSetsAreSeparated = false;
                        break;
                    }
                }

                for(Language l : selectedLanguages) {
                    if(l.getConjugatedVerbSets().isEmpty()) {
                        thereAreEmptyLanguages = true;
                        break;
                    }
                }

				// only allow creating a new set if the language and selected verb sets are concentrated (if separate, that means technically, multiple languages selected)
                newFolderButton.setEnabled(true);
                newSetButton.setEnabled(!theLanguagesAndSetsAreSeparated);
                editSetButton.setEnabled(theLanguagesAndSetsAreSeparated && !thereAreEmptyLanguages);
                loadButton.setEnabled(theLanguagesAndSetsAreSeparated && !thereAreEmptyLanguages);
                deleteButton.setEnabled(theLanguagesAndSetsAreSeparated);
            }
        }
    }

    public ToolbarButton getNewSetButton() {
        return newSetButton;
    }

    public ToolbarButton getNewLanguageButton() {
        return newLanguageButton;
    }

    public ToolbarButton getEditSetButton() {
        return editSetButton;
    }

    public ToolbarButton getLoadButton() {
        return loadButton;
    }

    public ToolbarButton getDeleteButton() {
        return deleteButton;
    }

    public ToolbarButton getReturnToMainButton() {
        return returnToMainButton;
    }

    public DefaultMutableTreeNode getRootNode() {
        return rootNode;
    }

    public DefaultTreeModel getTreeModel() {
        return loadModel;
    }

    public JTree getTree() {
        return loadTree;
    }

    class LanguageTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree,value,sel,expanded,leaf,row,hasFocus);

            setText(tree.convertValueToText(value,selected,expanded,leaf,row,hasFocus));
            if(leaf) {
                if(((DefaultMutableTreeNode)value).getUserObject() instanceof Language) {
                    setIcon(foldericon);
                }
                else {
                    setIcon(pageicon);
                }
            }
            else { // not a leaf, just normal folder icon
                setIcon(foldericon);
            }

            return this;
        }
    }
}

package conjugationstation;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

import conjugationstation.conjugationcomponents.*;
import conjugationstation.conjugationreviewer.*;
import conjugationstation.conjugationeditor.*;
import conjugationstation.conjugationreviewer.reports.*;
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
 * Top-level panel for practicing or creating conjugation sets
 * @author Michael Hergenrader
 */
public class ConjugationStation extends JPanel implements ActionListener {

    private LanguageSuite owner;

    private SetsViewPanel setsViewPanel;
    private NewLanguagePanel newLanguagePanel;
    private NewConjugationSetPanel newConjugationSetPanel;

    private CardLayout cardLayout;
    private JPanel cardPanel;

    private static final String defaultLocation = LanguageSuite.defaultDataLocation;
    private static String defaultDataFile = defaultLocation+"languageset.ls";
    private static String defaultReportFile = defaultLocation+"csreports.rep";

    private LanguageSet languageSet;

    private ArrayList<ConjugationSessionReport> reports;

    public ConjugationStation(LanguageSuite owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(476,300));
        setMaximumSize(new Dimension(476,300));
        setPreferredSize(new Dimension(476,300));

        reports = new ArrayList<ConjugationSessionReport>();
        loadReports();

        initializePanels();
        loadLanguageSet();

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        cardPanel.add(setsViewPanel,"Sets View Panel");
        cardPanel.add(newLanguagePanel,"New Language Panel");
        cardPanel.add(newConjugationSetPanel,"New Conjugation Panel");

        add(cardPanel);
    }
	
    public void loadReports() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(defaultReportFile));

            ArrayList<ConjugationSessionReport> fileReports = (ArrayList<ConjugationSessionReport>)in.readObject();
            for(ConjugationSessionReport csr : fileReports) {
                reports.add(new ConjugationSessionReport(csr));
            }
            in.close();
        }
        catch(FileNotFoundException e) { // first time creating the file
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(defaultReportFile));
                out.writeObject(new ArrayList<ConjugationSessionReport>());
                out.close();
            }
            catch(IOException ioe) {
                JOptionPane.showMessageDialog(null,"CANNOT CREATE REPORT FILE (Conjugation Station)!!");
                ioe.printStackTrace();
                System.exit(0);
            }
        }
        catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"CANNOT CONVERT TO A LANGUAGE SET!!");
            e.printStackTrace();
            System.exit(0);
        }
        catch(IOException e) {
        }
    }
    
    public void saveReports() {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(defaultReportFile));
            out.writeObject(reports);
            out.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void initializePanels() {
        setsViewPanel = new SetsViewPanel(this);
        newLanguagePanel = new NewLanguagePanel(this);
        newConjugationSetPanel = new NewConjugationSetPanel(this);
    }

    public SetsViewPanel getSetsViewPanel() {
        return setsViewPanel;
    }

    public NewLanguagePanel getNewLanguagePanel() {
        return newLanguagePanel;
    }

    public NewConjugationSetPanel getNewConjugationSetPanel() {
        return newConjugationSetPanel;
    }

    public ArrayList<ConjugationSessionReport> getReports() {
        return reports;
    }

    public void actionPerformed(ActionEvent ae) {
        if(ae.getSource() == setsViewPanel.getNewSetButton()) {
            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)setsViewPanel.getTree().getLastSelectedPathComponent();
            if(currentNode == null) {
                return;
            }

            newConjugationSetPanel.clearFields();

            if(currentNode.getUserObject() instanceof Language) {
                newConjugationSetPanel.setLanguage((Language)currentNode.getUserObject());
            }
            else if(currentNode.getUserObject() instanceof ConjugationTreeObject) {
                newConjugationSetPanel.setLanguage((Language)((DefaultMutableTreeNode)currentNode.getParent()).getUserObject());
            }

            cardLayout.show(cardPanel,"New Conjugation Panel");
            newConjugationSetPanel.getSetNameField().requestFocusInWindow();
        }
        else if(ae.getSource() == setsViewPanel.getNewLanguageButton()) {
            newLanguagePanel.clearFields();
            newLanguagePanel.setFocusCycleRoot(true); // to make sure its focus traversal policy is carried out, this must occur
            cardLayout.show(cardPanel,"New Language Panel");

            newLanguagePanel.getLanguageNameField().requestFocusInWindow();
        }
        else if(ae.getSource() == setsViewPanel.getEditSetButton()) {            
            loadSelectedSetsForEditing();
        }
        else if(ae.getSource() == setsViewPanel.getLoadButton()) {
            loadSelectedSetsForReview();
        }
        else if(ae.getSource() == setsViewPanel.getDeleteButton()) {
            deleteComponents();
        }
        else if(ae.getSource() == setsViewPanel.getReturnToMainButton()) {
            owner.handleClosingFrame();
        }
        else if(ae.getSource() == newLanguagePanel.getConfirmButton()) {
            addNewLanguage();
        }
        else if(ae.getSource() == newLanguagePanel.getCancelButton()) {
            cardLayout.show(cardPanel,"Sets View Panel");
            //setTitle(stationPanelTitle);
        }
        else if(ae.getSource() == newConjugationSetPanel.getConfirmButton()) {
            addNewConjugationSet();
        }
        else if(ae.getSource() == newConjugationSetPanel.getCancelButton()) {
            cardLayout.show(cardPanel,"Sets View Panel");
        }
    }
	
    private void loadSelectedSetsForReview() {
        for(Language l : setsViewPanel.getSelectedLanguages()) {
            ArrayList<ConjugationTreeObject> validSets = new ArrayList<ConjugationTreeObject>();
            for(ConjugatedVerbSet cvs : l.getConjugatedVerbSets()) {
                ConjugationTreeObject cto = setsViewPanel.findTreeObject(cvs);
                if(cto != null) {
                    if(cto.editorFrame == null) {
                        validSets.add(cto);
                    }
                }
            }
			
            // open up a review frame for everything, provided they belong to the same language
            if(!validSets.isEmpty()) {
                loadValidSetsIntoFrame(validSets,l);
            }
        }

        // load the inner nodes not selected under a tree
        ArrayList<ConjugationTreeObject> validTreeObjects = new ArrayList<ConjugationTreeObject>();
        Language l = null;
        for(ConjugationTreeObject cto : setsViewPanel.getSelectedConjugationObjects()) {
            if(l == null) {
                if(cto.editorFrame == null) { // this is a valid stack, so set initial language
                    l = cto.getVerbSet().getLanguage();
                }
            }

            if(cto.getVerbSet().getLanguage().equals(l)) {
                if(cto.editorFrame == null) {
                    validTreeObjects.add(cto);
                }
            }
            else { // found one that doesn't belong to same language, so start a new frame for the rest
                if(!validTreeObjects.isEmpty()) {
                    loadValidSetsIntoFrame(validTreeObjects,l);
                    validTreeObjects.clear();                    
                }
                if(cto.editorFrame == null) {
                    l = cto.getVerbSet().getLanguage();
                    validTreeObjects.add(cto); // start up a new list if can use this list
                }                
            }            
        }

        // hit the end of the list, so see if any leftovers
        if(!validTreeObjects.isEmpty()) {
            loadValidSetsIntoFrame(validTreeObjects,l);
        }            
    }

    // helper method to create the review frames
    private void loadValidSetsIntoFrame(ArrayList<ConjugationTreeObject> validTreeObjects, Language language) {
        ConjugationReviewFrame reviewFrame = new ConjugationReviewFrame(this,validTreeObjects,language);
        reviewFrame.setSize(600,300);
        reviewFrame.setResizable(false);
        reviewFrame.setLocationRelativeTo(this);
        reviewFrame.setVisible(true);

        for(ConjugationTreeObject current : validTreeObjects) {
            current.addReviewFrame(reviewFrame); // make them have references to this frame - can do this since in same class (inner class)
        }
    }

    private void loadSelectedSetsForEditing() {
        // for every language selected, open up a frame for each of their inner nodes if that said inner node is not being reviewed
        for(Language l : setsViewPanel.getSelectedLanguages()) {
            for(ConjugatedVerbSet cvs : l.getConjugatedVerbSets()) {
                ConjugationTreeObject cto = setsViewPanel.findTreeObject(cvs);
                if(cto != null) {
                    tryToOpenEditorFrameFor(cto);
                }
            }
        }
		
        for(ConjugationTreeObject cto : setsViewPanel.getSelectedConjugationObjects()) { // for the leftovers, do the same
            tryToOpenEditorFrameFor(cto);
        }
    }

    // helper method for loading stacks for editing
    private void tryToOpenEditorFrameFor(ConjugationTreeObject cto) {
        if(cto.editorFrame == null) {
            if(cto.canEditStack()) { // create a new editor frame
                int frameHeight = ConjugationEditorFrame.EDITOR_FRAME_INITIAL_HEIGHT; // height is based on how many subject pronouns are in this language
                int numPronouns = cto.getVerbSet().getLanguage().getNumberOfSubjectPronouns();
                if(numPronouns > 2) {
                    frameHeight += 30*(numPronouns/2 - ((numPronouns%2==0)?1:0));
                }

                cto.editorFrame = new ConjugationEditorFrame(this,cto.getVerbSet().getLanguage(),cto);
                cto.editorFrame.setSize(800,frameHeight);
                cto.editorFrame.setResizable(false);
                cto.editorFrame.setLocationRelativeTo(null);
                cto.editorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                cto.editorFrame.setVisible(true);
            }
        }
        else { // load the frame that is already there
            cto.editorFrame.setLocationRelativeTo(null);
            cto.editorFrame.setVisible(true);
			
            // if minimized, restore it
            cto.editorFrame.setState(Frame.NORMAL);
            cto.editorFrame.toFront();
        }
    }

	// Add a new ConjugatedVerbSet node here to this particular language
    public void addNewConjugationSet() {
        DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)setsViewPanel.getTree().getLastSelectedPathComponent();

        Language currentLanguage = null;
        if(currentNode.getUserObject() instanceof Language) {
            currentLanguage = (Language)currentNode.getUserObject();
        }
        else if(currentNode.getUserObject() instanceof ConjugationTreeObject) {
            currentLanguage = (Language)((DefaultMutableTreeNode)currentNode.getParent()).getUserObject();
        }

        ConjugatedVerbSet set = new ConjugatedVerbSet(newConjugationSetPanel.getTitle(),newConjugationSetPanel.getAuthor(),currentLanguage);
        currentLanguage.addConjugatedVerbSet(set); // must add physically to the language as well

        if(currentNode.getUserObject() instanceof Language) {
            newConjugationSetPanel.insertNewConjugationSetIntoLanguage(currentNode,set);
        }
        else if(currentNode.getUserObject() instanceof ConjugationTreeObject) {
            newConjugationSetPanel.insertNewConjugationSetIntoLanguage(((DefaultMutableTreeNode)currentNode.getParent()),set);
        }
        
        setsViewPanel.getTreeModel().reload();
        
        cardLayout.show(cardPanel,"Sets View Panel");
    }

    private void addNewLanguage() {
        ArrayList<String> subjectPronouns = new ArrayList<String>();
        DefaultListModel pronouns = newLanguagePanel.getPronounsModel();
		
        for(int i = 0; i < pronouns.getSize(); i++) {
            subjectPronouns.add(((SubjectPronoun)pronouns.getElementAt(i)).toString());
        }

        Language l = new Language(newLanguagePanel.getTitle(),newLanguagePanel.getAuthor(),newLanguagePanel.getCreateDate(),subjectPronouns);
        
        setsViewPanel.insertNewLanguageIntoTree(l);
        setsViewPanel.getTreeModel().reload();
        cardLayout.first(cardPanel);
    }

    public void loadLanguageSet() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(defaultDataFile));
            languageSet = (LanguageSet)in.readObject();

            for(Language l : languageSet.getLanguages()) { // populate the JTree
                DefaultMutableTreeNode newLanguageNode = new DefaultMutableTreeNode(l);
                setsViewPanel.getRootNode().add(newLanguageNode);

                for(ConjugatedVerbSet cvs : l.getConjugatedVerbSets()) {
                    newLanguageNode.add(new DefaultMutableTreeNode(new ConjugationTreeObject(cvs)));
                }
            }

            setsViewPanel.getTreeModel().reload();
        }
        catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"CANNOT CONVERT TO A LANGUAGE SET!!");
            e.printStackTrace();
            System.exit(0);
        }
        catch(FileNotFoundException e) { // first time creating the file
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(defaultDataFile));
                out.writeObject(new LanguageSet()); // create default file
            }
            catch(IOException ioe) {
                JOptionPane.showMessageDialog(null,"CANNOT CREATE LANGUAGE SET!!");
                ioe.printStackTrace();
                System.exit(0);
            }
            finally {
                try {
                    out.close();
                }
                catch(IOException o) {
                    JOptionPane.showMessageDialog(null,"CANNOT RETURN SYSTEM RESOURCES!! (Conju Station)");
                    o.printStackTrace();
                    System.exit(0);
                }
            }

        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null,"CANNOT OPEN LANGUAGE SET!!");
            e.printStackTrace();
            System.exit(0);
        }
        finally {
            try {
                if(in != null) {
                    in.close();
                }
            }
            catch(IOException e) {
                JOptionPane.showMessageDialog(null,"CANNOT RETURN SYSTEM RESOURCES!! (2) Conju Station");
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void saveLanguageSet() {
        ObjectOutputStream out;

        try {
            out = new ObjectOutputStream(new FileOutputStream(defaultDataFile));

            // create the language set and save it
            LanguageSet a = new LanguageSet(setsViewPanel.getTreeModel(),setsViewPanel.getRootNode());
            out.writeObject(a);

            out.close();
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null,"CANNOT RETURN SYSTEM RESOURCES!! (3) (conju station)");
        }
    }

    private void deleteComponents() {
        TreePath[] paths = setsViewPanel.getTree().getSelectionPaths();

        if(paths == null || paths.length == 0) {
            return;
        }

        int response = JOptionPane.showConfirmDialog(this,"Are you sure you want to delete these components?\nAll open frames with this component will be closed.","Confirm Deletion",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
        if(response != JOptionPane.YES_OPTION) {
            return;
        }

        // delete all nodes, and close all frames that currently contain/use these nodes
        for(int i = 0; i < paths.length; i++) {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode)paths[i].getLastPathComponent();

            if(current.getUserObject() instanceof ConjugationTreeObject) {
                ConjugationTreeObject currentTreeObject = (ConjugationTreeObject)current.getUserObject();
                safeFreeFrames(currentTreeObject);
                currentTreeObject.getVerbSet().getLanguage().removeConjugatedVerbSet(currentTreeObject.getVerbSet());
                setsViewPanel.getTreeModel().removeNodeFromParent(current);
            }
        }

        for(int i = 0; i < paths.length; i++) {
            DefaultMutableTreeNode current = (DefaultMutableTreeNode)paths[i].getLastPathComponent();

            if(current.getUserObject() instanceof Language) {
                for(int j = 0; j < current.getChildCount(); j++) {
                    DefaultMutableTreeNode currentSetNode = (DefaultMutableTreeNode)current.getChildAt(j);

                    if(currentSetNode.getUserObject() instanceof ConjugationTreeObject) {
                        ConjugationTreeObject currentTreeObject = (ConjugationTreeObject)currentSetNode.getUserObject();
                        safeFreeFrames(currentTreeObject);
                    }                    
                }

                for(int j = 0; j < current.getChildCount(); j++) {
                    DefaultMutableTreeNode currentSetNode = (DefaultMutableTreeNode)current.getChildAt(j);

                    if(currentSetNode.getUserObject() instanceof ConjugationTreeObject) {
                        ConjugationTreeObject currentTreeObject = (ConjugationTreeObject)currentSetNode.getUserObject();                        
                        currentTreeObject.getVerbSet().getLanguage().removeConjugatedVerbSet(currentTreeObject.getVerbSet());
                        setsViewPanel.getTreeModel().removeNodeFromParent(currentSetNode);
                    }
                }

                // after clearing its children nodes, delete language itself
                setsViewPanel.getTreeModel().removeNodeFromParent(current);
            }
        }
    }

    // helper method that will close all frames that have this param that is about to be deleted
    private void safeFreeFrames(ConjugationTreeObject currentTreeObject) {
        for(ConjugationReviewFrame crf : currentTreeObject.getReviewFrames()) {
            crf.cleanUp();
            for(ConjugationTreeObject cto: crf.getTreeObjects()) {
                try {
                    if(!cto.equals(currentTreeObject)) {
                        cto.releaseReviewFrame(crf); // safe clean up - cannot just use handle window closing, because cannot include this window
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            crf.dispose();
        }
        currentTreeObject.clearReviewFrames();

        if(currentTreeObject.getEditorFrame() != null) { // get rid of the editor frame if it has one
            currentTreeObject.getEditorFrame().dispose();
        }
    }
}

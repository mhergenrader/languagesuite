package notecardstation.general;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.io.*;
import java.util.*;

import notecardstation.notecard.*;
import notecardstation.notecardeditor.*;
import notecardstation.notecardreviewer.general.*;
import notecardstation.notecardreviewer.reports.*;

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
 * Main notecard module panel - controls entry into create/editor and reviewer for notecards
 * @author Michael Hergenrader
 */
public class NotecardStation extends JPanel implements ActionListener, TreeSelectionListener {    
    private LanguageSuite owner;

    private StacksTreePanel stacksTreePanel;
    private NewStackPanel newStackPanel;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private NotecardStackSet notecardStackSet;

    private static final String defaultStacksFile = "notecardstacks.ns";
    private static final String defaultFileString = LanguageSuite.defaultDataLocation + defaultStacksFile;
    private static final String defaultReportFile = LanguageSuite.defaultDataLocation + "nsreports.rep";
    private String imageLocation;

    private ArrayList<ReviewSessionReport> reports;

    public class NotecardStackTreeObject {

        private NotecardStack notecardStack;
        public NotecardStackEditorFrame editorFrame;
        private java.util.List<NotecardStackReviewFrame> reviewFrames;

        public NotecardStackTreeObject(NotecardStack notecardStack) {
            this.notecardStack = notecardStack;
            reviewFrames = new ArrayList<NotecardStackReviewFrame>();
        }

        public void overwriteStackWith(NotecardStack newNotecardStack) { // safest way, instead of just resetting reference
            notecardStack.setTitle(newNotecardStack.getTitle());
            notecardStack.setAuthor(newNotecardStack.getAuthor());
            notecardStack.setVersion(newNotecardStack.getVersion());
            notecardStack.setLastModified(newNotecardStack.getLastModified());
            notecardStack.setLanguage(newNotecardStack.getLanguage());

            notecardStack.setSide1Title(newNotecardStack.getSide1Title());
            notecardStack.setSide2Title(newNotecardStack.getSide2Title());
            notecardStack.setSide3Title(newNotecardStack.getSide3Title());

            notecardStack.getNotecards().clear();
            for(Notecard n : newNotecardStack.getNotecards()) {
                if(n instanceof Notecard3D) {
                    notecardStack.getNotecards().add(new Notecard3D((Notecard3D)n));
                }
                else {
                    notecardStack.getNotecards().add(new Notecard(n));
                }
            }
        }

        @Override
        public String toString() {
            return notecardStack.toString();
        }

        public String fullInfo() {
            return this.toString() + "; editor frame open? " + (editorFrame != null) + "; number of review frames = " + reviewFrames.size();
        }

        public NotecardStack getNotecardStack() {
            return notecardStack;
        }

        public int getNumberOfReviewFrames() {
            return reviewFrames.size();
        }

        public boolean canEditStack() {
            return reviewFrames.isEmpty();
        }

        public void addReviewFrame(NotecardStackReviewFrame rf) {
            reviewFrames.add(rf);
        }

        public void releaseReviewFrame(NotecardStackReviewFrame rf) throws Exception {
            if(reviewFrames.size() - 1 < 0) { // could also just test if empty...
                throw new Exception("Error!! Number of frames reviewing this stack can't be negative!");
            }
            reviewFrames.remove(rf);
        }

        public NotecardStackEditorFrame getEditorFrame() {
            return editorFrame;
        }

        public java.util.List<NotecardStackReviewFrame> getReviewFrames() {
            return reviewFrames;
        }

        public void clearReviewFrames() {
            reviewFrames.clear();
        }
    }

    public NotecardStation(LanguageSuite owner) {
        this.owner = owner;
        this.imageLocation = LanguageSuite.defaultImageLocation;

        setMinimumSize(new Dimension(476,300));
        setMaximumSize(new Dimension(476,300));
        setPreferredSize(new Dimension(476,300));

        initializePanels();
        loadStacks();
        reports = new ArrayList<ReviewSessionReport>();
        loadReports();
    }

    public void loadReports() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(defaultReportFile));

            ArrayList<ReviewSessionReport> fileReports = (ArrayList<ReviewSessionReport>)in.readObject();
            for(ReviewSessionReport rsr : fileReports) {
                reports.add(new ReviewSessionReport(rsr));
            }
            in.close();
        }
        catch(FileNotFoundException e) { // first time creating the file
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(defaultReportFile));
                out.writeObject(new ArrayList<ReviewSessionReport>());
                out.close();
            }
            catch(IOException ioe) {
                JOptionPane.showMessageDialog(null,"CANNOT CREATE REPORT FILE!! (notecard station)");
                ioe.printStackTrace();
                System.exit(0);
            }
        }
        catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"CANNOT CONVERT TO A NOTECARD REPORT SET!! (nc station)");
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
        cardPanel = new JPanel();
        stacksTreePanel = new StacksTreePanel(this);
        newStackPanel = new NewStackPanel(this);

        cardLayout = new CardLayout();

        cardPanel.setLayout(cardLayout);

        cardPanel.add(stacksTreePanel,"Stacks Tree Panel");
        cardPanel.add(newStackPanel,"New Stack Panel");
        cardLayout.show(cardPanel,"Stacks Tree Panel");

        add(cardPanel);
    }

    public void loadStacks() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(defaultFileString));
            notecardStackSet = (NotecardStackSet)in.readObject();

            for(NotecardStack ns : notecardStackSet.getStacks()) {
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new NotecardStackTreeObject(ns));
                stacksTreePanel.getRootNode().add(newNode);
            } // allow user to sort them with folders and such?

            stacksTreePanel.getTreeModel().reload();
        }
        catch(ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,"CANNOT CONVERT TO A NOTECARD STACK SET!!");
            e.printStackTrace();
            System.exit(0);
        }
        catch(FileNotFoundException e) { // first time creating the file, since doesn't exist
            ObjectOutputStream out = null;
            try {
                out = new ObjectOutputStream(new FileOutputStream(defaultFileString));
                notecardStackSet = new NotecardStackSet();
                out.writeObject(notecardStackSet); // create default file
            }
            catch(IOException ioe) {
                JOptionPane.showMessageDialog(null,"CANNOT CREATE STACK SET!!");
                ioe.printStackTrace();
                System.exit(0);
            }
            finally {
                try {
                    out.close();
                }
                catch(IOException o) {
                    JOptionPane.showMessageDialog(null,"CANNOT RETURN SYSTEM RESOURCES!! (nc station)");
                    o.printStackTrace();
                    System.exit(0);
                }
            }
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null,"CANNOT OPEN STACKS!!");
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
                JOptionPane.showMessageDialog(null,"CANNOT RETURN SYSTEM RESOURCES!! (2) nc station");
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
	
    public void saveStacks() {
        ObjectOutputStream out;

        try {
            out = new ObjectOutputStream(new FileOutputStream(defaultFileString));

            // create the notecard stack set and save it
            NotecardStackSet a = new NotecardStackSet(stacksTreePanel.getTreeModel(),stacksTreePanel.getRootNode());
            out.writeObject(a);
            out.close();
        }
        catch(IOException e) {
            JOptionPane.showMessageDialog(null,"CANNOT RETURN SYSTEM RESOURCES!! (3) nc station");
        }
    }

    public NotecardStackSet getNotecardStackSet() {
        return notecardStackSet;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public ArrayList<ReviewSessionReport> getReports() {
        return reports;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == stacksTreePanel.getNewStackButton()) {
            // Create a new notecard stack - open up a new editor window with NO stacks in the list
            loadCreateNewStackPanel();
        }
        else if(e.getSource() == stacksTreePanel.getEditStackButton()) {
            // edit notecard stacks - open as many editor windows as are in the list (of selected nodes) when hit - pass the stack to be edited
            loadSelectedStacksForEditing();
        }
        else if(e.getSource() == stacksTreePanel.getReviewStackButton()) {
            loadSelectedStacksForReview();
        }
        else if(e.getSource() == stacksTreePanel.getDeleteStackButton()) {
            deleteSelectedStacks(); // remove any and all nodes currently in the tree
        }
        else if(e.getSource() == stacksTreePanel.getReturnToMainButton()) {
            owner.handleClosingFrame();
        }
        else if(e.getSource() == newStackPanel.getEnterButton()) {
            createNewStack();
        }
        else if(e.getSource() == newStackPanel.getCancelButton()) {
            cardLayout.show(cardPanel,"Stacks Tree Panel");
        }
    }

    // helper method to insert a node correctly into a tree to automatically maintain alphabetical order
    private void insertNewStackObjectIntoTree(NotecardStack notecardStack) {
        NotecardStackTreeObject nsto = new NotecardStackTreeObject(notecardStack); // made a tree node object out of the stack

        int index = 0;
        boolean inserting = false;
        for(int i = 0; i < stacksTreePanel.getRootNode().getChildCount(); i++) {
            if(notecardStack.toString().compareTo(((NotecardStackTreeObject)(((DefaultMutableTreeNode)stacksTreePanel.getRootNode().getChildAt(i)).getUserObject())).notecardStack.toString()) < 0) {
                index = i;
                inserting = true; // declare that this has found a place to insert
                break;
            }
        }

        if(inserting) {
            stacksTreePanel.getRootNode().insert(new DefaultMutableTreeNode(nsto),index);
        }
        else { // no place to insert, so just add to the end (not inserting, but adding)
            stacksTreePanel.getRootNode().add(new DefaultMutableTreeNode(nsto));
        }

        stacksTreePanel.getTreeModel().reload();
    }

    private void loadCreateNewStackPanel() {
        newStackPanel.resetPanel();
        cardLayout.show(cardPanel,"New Stack Panel");
        newStackPanel.requestTitleFocus();
    }

    public void createNewStack() {
        NotecardStack ns = new NotecardStack();
        ns.setTitle(newStackPanel.getTitle());
        ns.setAuthor(newStackPanel.getAuthor());
        ns.setVersion("1.0");
        ns.setLastModified(newStackPanel.getDateCreated());
        insertNewStackObjectIntoTree(ns);

        cardLayout.show(cardPanel,"Stacks Tree Panel");
    }

    private void loadSelectedStacksForEditing() {
        TreePath[] selectedNodes = stacksTreePanel.getTree().getSelectionPaths();

        if(selectedNodes == null) {
            return;
        }

        // load one frame for each one of them, with their titles included (all will be dispose on close)
        for(int i = 0; i < selectedNodes.length; i++) {
            NotecardStackTreeObject current = ((NotecardStackTreeObject)(((DefaultMutableTreeNode)(selectedNodes[i].getLastPathComponent())).getUserObject()));
            if(current.editorFrame == null) {
                if(current.canEditStack()) {
                    current.editorFrame = new NotecardStackEditorFrame(this,current);
                    current.editorFrame.setSize(856,756);
                    current.editorFrame.setResizable(false);
                    current.editorFrame.setLocationRelativeTo(null);
                    current.editorFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    current.editorFrame.setVisible(true);
                }
            }
            else { // already open, so bring to front (and obviously able to open stack already since frame has been opened)
                current.editorFrame.setLocationRelativeTo(null);
                current.editorFrame.setVisible(true);
				
                // if minimized, restore it
                current.editorFrame.setState(Frame.NORMAL);
                current.editorFrame.toFront();
            }
        }
    }

    private void loadSelectedStacksForReview() {
        TreePath[] selectedNodes = stacksTreePanel.getTree().getSelectionPaths();

        if(selectedNodes == null) {
            return;
        }
		
        TreeSet<NotecardStackTreeObject> validSelectedNodes = new TreeSet<NotecardStackTreeObject>(new NotecardStackTreeObjectComparator());

        // will open up one frame that holds all NotecardStacks
        for(int i = 0; i < selectedNodes.length; i++) {
            NotecardStackTreeObject current = ((NotecardStackTreeObject)(((DefaultMutableTreeNode)(selectedNodes[i].getLastPathComponent())).getUserObject()));
            if(current.editorFrame == null) { // if not currently being edited, add this particular set

                if(current.getNotecardStack().getSide1Title().isEmpty() && current.getNotecardStack().getSide2Title().isEmpty()) {
                    if(JOptionPane.showConfirmDialog(this,current.getNotecardStack().toString() + " does not have all appropriate stack titles set.\nWould you still like to open this for review?","Warning: Blank Fields Found!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                        continue; // if user decides not to open this after all (edit it first)
                    }
                }

                validSelectedNodes.add(current);
            }
            // if a notecardstack is being edited - it cannot be reviewable - cannot read and write at the same time
        }
		
        if(!validSelectedNodes.isEmpty()) {
            // open up a new reviewer frame for the selected stack objects
            NotecardStackReviewFrame reviewFrame = new NotecardStackReviewFrame(this,validSelectedNodes); // tell the new frame which stacks it will refer to
            reviewFrame.setSize(700,440);
            reviewFrame.setLocationRelativeTo(null);
            reviewFrame.setResizable(false);
            reviewFrame.setVisible(true);

            for(NotecardStackTreeObject current : validSelectedNodes) {
                current.reviewFrames.add(reviewFrame); // make them have references to this frame
            }
        }
    }

    private void deleteSelectedStacks() {
        if(JOptionPane.showConfirmDialog(this,"Are you sure you want to delete these stacks?\nAll open frames containing them will be closed.","Confirm Deletion",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        TreePath[] paths = stacksTreePanel.getTree().getSelectionPaths();
        if(paths == null) {
            return;
        }
        
        for(int i = 0; i < paths.length; i++) {
            // now closes the frames that were open that contained deletable nodes            
            DefaultMutableTreeNode current = (DefaultMutableTreeNode)paths[i].getLastPathComponent();
            NotecardStackTreeObject currentTreeObject = (NotecardStackTreeObject)current.getUserObject();
            safeFreeFrames(currentTreeObject);
            stacksTreePanel.getTreeModel().removeNodeFromParent(current);
        }        
    }

    private void safeFreeFrames(NotecardStackTreeObject currentTreeObject) {
        for(NotecardStackReviewFrame nsrf : currentTreeObject.getReviewFrames()) {
            nsrf.cleanUp();
            for(NotecardStackTreeObject nsto : nsrf.getTreeObjects()) {
                try {
                    if(!nsto.equals(currentTreeObject)) {
                        nsto.releaseReviewFrame(nsrf); // safe clean up - cannot just use handle window closing, because cannot include this window
                    }
                }
                catch(Exception e) {
                	e.printStackTrace();
                }
            }

            nsrf.dispose();
        }
        currentTreeObject.clearReviewFrames();

        if(currentTreeObject.getEditorFrame() != null) { // get rid of the editor frame if it has one
            currentTreeObject.getEditorFrame().dispose();
        }
    }

    public void valueChanged(TreeSelectionEvent e) {
        TreePath[] paths = stacksTreePanel.getTree().getSelectionPaths();

        stacksTreePanel.getEditStackButton().setEnabled((paths != null && paths.length > 0));
        stacksTreePanel.getReviewStackButton().setEnabled((paths != null && paths.length > 0));
        stacksTreePanel.getDeleteStackButton().setEnabled((paths != null && paths.length > 0));
    }

    public StacksTreePanel getStacksTreePanel() {
        return stacksTreePanel;
    }

    public NewStackPanel getNewStackPanel() {
        return newStackPanel;
    }
}


package conjugationstation.conjugationeditor;

import guicomponents.*;
import conjugationstation.conjugationcomponents.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import conjugationstation.*;
import javax.swing.text.*;
import javax.swing.undo.*;
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
 * Main conjugation editor module with frame, logic included
 * @author Michael Hergenrader
 */
public class ConjugationEditorFrame extends JFrame implements ActionListener, TreeSelectionListener, DocumentListener, FocusListener, UndoableEditListener {

    private ConjugationStation owner;

    private Vector<Component> componentOrder;
    private Component focusedComponent;

    private JMenuBar menuBar;

    private JMenu fileMenu;
    private JMenuItem saveSetMenuItem;
    private JMenuItem printSetMenuItem;
    private JMenuItem exitMenuItem;

    private JMenu editMenu;
    private JMenuItem undoMenuItem;
    private JMenuItem redoMenuItem;
    private JMenuItem cutMenuItem;
    private JMenuItem copyMenuItem;
    private JMenuItem pasteMenuItem;
    private JMenuItem addAccentMenuItem;

    private JMenu verbsMenu;
    private JMenuItem addVerbMenuItem;
    private JMenuItem removeVerbMenuItem;

    private JMenu helpMenu;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;

    // Visual Components
    private JToolBar toolbar;

    private ToolbarButton saveSetButton;
    private ToolbarButton printSetButton;
    private ToolbarButton undoButton;
    private ToolbarButton redoButton;
    private ToolbarButton addInfinitiveButton;
    private ToolbarButton removeInfinitiveButton;
    private ToolbarButton addAccentButton;
    private ToolbarButton helpButton;
    private String defaultLocation = LanguageSuite.defaultImageLocation;

    private Language language;
    private ConjugatedVerbSet verbSet;
    private ConjugationTreeObject verbSetObject;

    private JLabel verbsLabel;
    private JScrollPane scrollPane;
    private DefaultTreeModel infinitiveModel;
    private JTree infinitiveTree;
    private DefaultMutableTreeNode currentNode;

    private DefaultMutableTreeNode rootNode;    

    private JLabel setTitleLabel;
    private JTextField setTitleEntry;

    private JLabel setAuthorLabel;
    private JTextField setAuthorEntry;

    private ArrayList<JLabel> pronounLabels;
    private ArrayList<JTextField> inflectionEntries;

    private JLabel infinitiveLabel;
    private JTextField infinitiveEntry;

    private JLabel infinitiveWarningLabel;

    private UndoManager undoManager;

    private boolean setChanged;    
    private boolean changingNodes = false;
	
    private int numUndosInQueue = 0;
    private int numRedosInQueue = 0;
	
	private String currentInfinitiveText;
    
    public static final int EDITOR_FRAME_INITIAL_HEIGHT = 232;
    public static final int SCROLL_PANE_INITIAL_HEIGHT = 102;

    class MyTreeCellRenderer extends DefaultTreeCellRenderer {
        MyTreeCellRenderer() {
            textSelectionColor = Color.BLACK;
            textNonSelectionColor = Color.WHITE;
            backgroundSelectionColor = Color.DARK_GRAY.brighter();
            backgroundNonSelectionColor = Color.DARK_GRAY.brighter();
        }
    }

    public ConjugationEditorFrame(ConjugationStation owner, Language language, ConjugationTreeObject verbSetObject) {
        super();
        setIconImage(LanguageSuite.frameIcon);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleWindowClosing();
            }
        });

        this.owner = owner;
        this.language = language;
        this.verbSetObject = verbSetObject;
        this.verbSet = new ConjugatedVerbSet(this.verbSetObject.getVerbSet());
        this.verbSet.sortVerbsAlphabetically();

        setTitle("Conjugation Set Editor - " + this.language.toString() + "\\" + this.verbSet.toString());
        
        setupToolbar();
        setupMenu();
        setupLabelsAndEntries();
        setupTree();
        setupComponentOrder();
        setupLayout();
        setupUndoManager();
        getContentPane().setBackground(Color.DARK_GRAY.brighter());
        
        setChanged = false;
    }

    private void setupToolbar() {
        toolbar = new JToolBar("conjugation list editor toolbar");
        toolbar.setMinimumSize(new Dimension(800,50));
        toolbar.setMaximumSize(new Dimension(800,50));
        toolbar.setPreferredSize(new Dimension(800,50));
        toolbar.setFloatable(false);
        toolbar.setOpaque(false);
        toolbar.setBorderPainted(false);        

        saveSetButton = new ToolbarButton("Save this conjugation list",defaultLocation + "saveicon.png");
        saveSetButton.addActionListener(this);
        printSetButton = new ToolbarButton("Print this conjugation list",defaultLocation + "printstack_edited-1.png");
        printSetButton.addActionListener(this);

        undoButton = new ToolbarButton("Undo previous action",defaultLocation + "undoicon2_edited-1.png");
        undoButton.addActionListener(this);
        undoButton.setEnabled(false);
        redoButton = new ToolbarButton("Redo previous action",defaultLocation + "redoicon_edited-1.png");
        redoButton.addActionListener(this);
        redoButton.setEnabled(false);

        addInfinitiveButton = new ToolbarButton("Add new infinitive to conjugation list",defaultLocation + "newcardicon.png");
        addInfinitiveButton.addActionListener(this);
        removeInfinitiveButton = new ToolbarButton("Remove current infinitive from conjugation list",defaultLocation + "deletecardicon.png");
        removeInfinitiveButton.addActionListener(this);

        addAccentButton = new ToolbarButton("Add accent to last character",defaultLocation + "addaccent_edited-1.png");
        addAccentButton.addActionListener(this);
        helpButton = new ToolbarButton("Conjugation Set Creation Help",defaultLocation + "helpicon_edited-1.png");
        helpButton.addActionListener(this);

        toolbar.add(saveSetButton);
        toolbar.add(printSetButton);
        toolbar.addSeparator(new Dimension(50,0));
        toolbar.add(undoButton);
        toolbar.add(redoButton);
        toolbar.addSeparator(new Dimension(50,0));
        toolbar.add(addInfinitiveButton);
        toolbar.add(removeInfinitiveButton);
        toolbar.addSeparator(new Dimension(50,0));
        toolbar.add(addAccentButton);
        toolbar.addSeparator(new Dimension(50,0));
        toolbar.add(helpButton);
    }
	
    private void setupMenu() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        saveSetMenuItem = new JMenuItem("Save Conjugation Set");
        saveSetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        saveSetMenuItem.addActionListener(this);
        printSetMenuItem = new JMenuItem("Print Conjugation Set");
        printSetMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
        printSetMenuItem.addActionListener(this);
        exitMenuItem = new JMenuItem("Close Editor");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK));
        exitMenuItem.addActionListener(this);

        fileMenu.add(saveSetMenuItem);
        fileMenu.add(printSetMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);

        undoMenuItem = new JMenuItem("Undo");
        undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
        undoMenuItem.addActionListener(this);
        undoMenuItem.setEnabled(false);
        redoMenuItem = new JMenuItem("Redo");
        redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        redoMenuItem.addActionListener(this);
        redoMenuItem.setEnabled(false);

        cutMenuItem = new JMenuItem(new DefaultEditorKit.CutAction()); // add icons?
        cutMenuItem.setText("Cut");
        cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK));
        copyMenuItem = new JMenuItem(new DefaultEditorKit.CopyAction());
        copyMenuItem.setText("Copy");
        copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK));
        pasteMenuItem = new JMenuItem(new DefaultEditorKit.PasteAction());
        pasteMenuItem.setText("Paste");
        pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK));

        addAccentMenuItem = new JMenuItem("Add Accent");
        addAccentMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,KeyEvent.CTRL_MASK));
        addAccentMenuItem.addActionListener(this);

        editMenu.add(undoMenuItem);
        editMenu.add(redoMenuItem);
        editMenu.addSeparator();
        editMenu.add(cutMenuItem);
        editMenu.add(copyMenuItem);
        editMenu.add(pasteMenuItem);
        editMenu.addSeparator();
        editMenu.add(addAccentMenuItem);

        verbsMenu = new JMenu("Verbs");
        verbsMenu.setMnemonic(KeyEvent.VK_V);

        addVerbMenuItem = new JMenuItem("Add Verb");
        addVerbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));
        addVerbMenuItem.addActionListener(this);
        removeVerbMenuItem = new JMenuItem("Remove Verb");
        removeVerbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));
        removeVerbMenuItem.addActionListener(this);

        verbsMenu.add(addVerbMenuItem);
        verbsMenu.add(removeVerbMenuItem);

        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        helpMenuItem = new JMenuItem("Editor Help");
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
        helpMenuItem.addActionListener(this);

        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this);

        helpMenu.add(helpMenuItem);
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(verbsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
	
    private void setupLabelsAndEntries() {
        verbsLabel = new JLabel();
        verbsLabel.setMinimumSize(new Dimension(200,20));
        verbsLabel.setMaximumSize(new Dimension(200,20));
        verbsLabel.setPreferredSize(new Dimension(200,20));
        verbsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        verbsLabel.setFont(new Font("serif",Font.ITALIC,14));
        verbsLabel.setForeground(Color.WHITE);

        setTitleLabel = new JLabel("Title");
        setTitleLabel.setMinimumSize(new Dimension(120,20));
        setTitleLabel.setMaximumSize(new Dimension(120,20));
        setTitleLabel.setPreferredSize(new Dimension(120,20));
        setTitleLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        setTitleLabel.setForeground(Color.WHITE);
        
        setTitleEntry = new JTextField(14);
        setTitleEntry.setMinimumSize(new Dimension(100,20));
        setTitleEntry.setMaximumSize(new Dimension(100,20));
        setTitleEntry.setPreferredSize(new Dimension(100,20));
        setTitleEntry.getDocument().addDocumentListener(this);
        setTitleEntry.addFocusListener(this);

        setAuthorLabel = new JLabel("Author");
        setAuthorLabel.setMinimumSize(new Dimension(120,20));
        setAuthorLabel.setMaximumSize(new Dimension(120,20));
        setAuthorLabel.setPreferredSize(new Dimension(120,20));
        setAuthorLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        setAuthorLabel.setForeground(Color.WHITE);

        setAuthorEntry = new JTextField(14);
        setAuthorEntry.setMinimumSize(new Dimension(100,20));
        setAuthorEntry.setMaximumSize(new Dimension(100,20));
        setAuthorEntry.setPreferredSize(new Dimension(100,20));
        setAuthorEntry.getDocument().addDocumentListener(this);
        setAuthorEntry.addFocusListener(this);

        pronounLabels = new ArrayList<JLabel>();
        for(int i = 0; i < language.getSubjectPronouns().size(); i++) {
            JLabel label = new JLabel(language.getSubjectPronouns().get(i).toString());
            label.setMinimumSize(new Dimension(120,20));
            label.setMaximumSize(new Dimension(120,20));
            label.setPreferredSize(new Dimension(120,20));
            label.setHorizontalAlignment(SwingConstants.TRAILING);
            label.addFocusListener(this);
            label.setForeground(Color.WHITE);
            pronounLabels.add(label);
        }
        inflectionEntries = new ArrayList<JTextField>();
        for(int i = 0; i < language.getSubjectPronouns().size(); i++) {
            JTextField field = new JTextField(14);
            field.setMinimumSize(new Dimension(100,20));
            field.setMaximumSize(new Dimension(100,20));
            field.setPreferredSize(new Dimension(100,20));
            field.addFocusListener(this);
            field.getDocument().addDocumentListener(this);
            inflectionEntries.add(field);
        }

        infinitiveLabel = new JLabel("Infinitive");
        infinitiveLabel.setMinimumSize(new Dimension(120,20));
        infinitiveLabel.setMaximumSize(new Dimension(120,20));
        infinitiveLabel.setPreferredSize(new Dimension(120,20));
        infinitiveLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        infinitiveLabel.setForeground(Color.WHITE);

        infinitiveEntry = new JTextField(14);
        infinitiveEntry.getDocument().addDocumentListener(this);
        infinitiveEntry.addFocusListener(this);

        infinitiveWarningLabel = new JLabel("Warning: More than one verb with this infinitive");
        infinitiveWarningLabel.setMinimumSize(new Dimension(300,20));
        infinitiveWarningLabel.setMaximumSize(new Dimension(300,20));
        infinitiveWarningLabel.setPreferredSize(new Dimension(300,20));
        infinitiveWarningLabel.setForeground(Color.YELLOW);
        infinitiveWarningLabel.setIcon(new ImageIcon(defaultLocation+"warningicon.png"));
        infinitiveWarningLabel.setVisible(false);


        setTitleEntry.setText(verbSet.toString());
        setAuthorEntry.setText(verbSet.getAuthor());
    }
	
    private void setupTree() {        
        rootNode = new DefaultMutableTreeNode("Infinitives");
        infinitiveModel = new DefaultTreeModel(rootNode);
		
        for(ConjugatedVerb verb : verbSet.getVerbs()) {
            rootNode.add(new DefaultMutableTreeNode(verb));
            infinitiveModel.reload();
        }       

        infinitiveTree = new JTree(infinitiveModel);
        infinitiveTree.setBackground(Color.DARK_GRAY.brighter());
        infinitiveTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        infinitiveTree.setCellRenderer(new MyTreeCellRenderer());
        infinitiveTree.setRootVisible(false);
        infinitiveTree.addTreeSelectionListener(this);
        infinitiveTree.addFocusListener(this);

        if(rootNode.getChildCount() == 0) { // add and select a default node if this is a new set (or erased set)
            ConjugatedVerb verb = new ConjugatedVerb("New Infinitive",language.getSubjectPronouns().size(),verbSet.toString());

            rootNode.add(new DefaultMutableTreeNode(verb));
            currentNode = rootNode.getFirstLeaf();
            
            infinitiveEntry.setText(((ConjugatedVerb)currentNode.getUserObject()).getInfinitive());
            infinitiveModel.reload();
            infinitiveTree.setSelectionRow(rootNode.getChildCount()-1);
        }
        else {
            currentNode = rootNode.getFirstLeaf();
            infinitiveEntry.setText(((ConjugatedVerb)currentNode.getUserObject()).getInfinitive());

            ConjugatedVerb c = (ConjugatedVerb)currentNode.getUserObject();

            for(int i = 0; i < inflectionEntries.size(); i++) {
                inflectionEntries.get(i).setText(c.getInflection(i).toString());
            }
            
            infinitiveTree.setSelectionRow(0);
        }

        verbsLabel.setText("Verb Infinitives - 1 of " + rootNode.getChildCount());
    }
	
    private void setupComponentOrder() {
        scrollPane = new JScrollPane(infinitiveTree);
        scrollPane.setVerticalScrollBar(CustomScrollBar.createCustomScrollBar());
        
        int scrollPaneHeight = SCROLL_PANE_INITIAL_HEIGHT;

        int numPronouns = language.getNumberOfSubjectPronouns();
        if(numPronouns > 2) {
            scrollPaneHeight += 30*(numPronouns/2 - ((numPronouns%2==0)?1:0));
        }        

        scrollPane.setMinimumSize(new Dimension(200,scrollPaneHeight));
        scrollPane.setMaximumSize(new Dimension(200,scrollPaneHeight));
        scrollPane.setPreferredSize(new Dimension(200,scrollPaneHeight)); // this height will auto adjust based on number of subject pronouns + some threshold
        scrollPane.setBorder(BorderFactory.createEmptyBorder());      

        componentOrder = new Vector<Component>();

        componentOrder.add(setTitleEntry);
        componentOrder.add(setAuthorEntry);

        componentOrder.add(infinitiveEntry);
        for(JTextField t : inflectionEntries) {
            componentOrder.add(t);
        }

        setFocusTraversalPolicy(new MyFocusTraversalPolicy(componentOrder));
    }
	
    private void setupLayout() {

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(toolbar);
        add(verbsLabel);
        add(scrollPane);

        add(setTitleLabel);
        add(setTitleEntry);

        add(setAuthorLabel);
        add(setAuthorEntry);

        add(infinitiveLabel);
        add(infinitiveEntry);
        add(infinitiveWarningLabel);

        for(JLabel l : pronounLabels) {
            add(l);
        }

        for(JTextField t : inflectionEntries) {
            add(t);
        }

        sl.putConstraint(SpringLayout.NORTH,toolbar,0,SpringLayout.NORTH,getContentPane());
        sl.putConstraint(SpringLayout.WEST,toolbar,0,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,verbsLabel,10,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,verbsLabel,0,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,scrollPane,0,SpringLayout.SOUTH,verbsLabel);
        sl.putConstraint(SpringLayout.WEST,scrollPane,0,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,setTitleLabel,16,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,setTitleLabel,0,SpringLayout.EAST,scrollPane);

        sl.putConstraint(SpringLayout.NORTH,setTitleEntry,16,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,setTitleEntry,6,SpringLayout.EAST,setTitleLabel);

        sl.putConstraint(SpringLayout.NORTH,setAuthorLabel,16,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,setAuthorLabel,6,SpringLayout.EAST,setTitleEntry);

        sl.putConstraint(SpringLayout.NORTH,setAuthorEntry,16,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,setAuthorEntry,6,SpringLayout.EAST,setAuthorLabel);

        sl.putConstraint(SpringLayout.NORTH,infinitiveLabel,26,SpringLayout.SOUTH,setTitleLabel);
        sl.putConstraint(SpringLayout.WEST,infinitiveLabel,0,SpringLayout.EAST,scrollPane);

        sl.putConstraint(SpringLayout.NORTH,infinitiveEntry,26,SpringLayout.SOUTH,setTitleLabel);
        sl.putConstraint(SpringLayout.WEST,infinitiveEntry,6,SpringLayout.EAST,infinitiveLabel);

        sl.putConstraint(SpringLayout.NORTH,infinitiveWarningLabel,26,SpringLayout.SOUTH,setTitleLabel);
        sl.putConstraint(SpringLayout.WEST,infinitiveWarningLabel,10,SpringLayout.EAST,infinitiveEntry);

        int x = 0;
        int y = 30;

        for(int i = 0; i < pronounLabels.size(); i++) {
            if(pronounLabels.size()%2 == 0) {
                if(i == pronounLabels.size()/2) {
                    x = 290;
                    y = 30;
                }
                sl.putConstraint(SpringLayout.WEST,pronounLabels.get(i),x,SpringLayout.EAST,scrollPane);
                sl.putConstraint(SpringLayout.NORTH,pronounLabels.get(i),y*((i>=pronounLabels.size()/2)?(i-pronounLabels.size()/2):i)+20,SpringLayout.SOUTH,infinitiveEntry);

                sl.putConstraint(SpringLayout.NORTH,inflectionEntries.get(i),y*((i>=pronounLabels.size()/2)?(i-pronounLabels.size()/2):i)+20,SpringLayout.SOUTH,infinitiveEntry);
                sl.putConstraint(SpringLayout.WEST,inflectionEntries.get(i),6,SpringLayout.EAST,pronounLabels.get(i));
            }
            else {
                if(i == pronounLabels.size()/2+1) {
                    x = 290;
                    y = 30;
                }
                sl.putConstraint(SpringLayout.WEST,pronounLabels.get(i),x,SpringLayout.EAST,scrollPane);
                sl.putConstraint(SpringLayout.NORTH,pronounLabels.get(i),y*((i>pronounLabels.size()/2)?(i-pronounLabels.size()/2-1):i)+20,SpringLayout.SOUTH,infinitiveEntry);

                sl.putConstraint(SpringLayout.NORTH,inflectionEntries.get(i),y*((i>pronounLabels.size()/2)?(i-pronounLabels.size()/2-1):i)+20,SpringLayout.SOUTH,infinitiveEntry);
                sl.putConstraint(SpringLayout.WEST,inflectionEntries.get(i),6,SpringLayout.EAST,pronounLabels.get(i));
            }
        }
    }
	
    private void setupUndoManager() {
        undoManager = new UndoManager();

        setTitleEntry.getDocument().addUndoableEditListener(this);
        setAuthorEntry.getDocument().addUndoableEditListener(this);
        infinitiveEntry.getDocument().addUndoableEditListener(this);
        for(JTextField t : inflectionEntries) {
            t.getDocument().addUndoableEditListener(this);
        }
    }

    public void undoableEditHappened(UndoableEditEvent e) { // when an undoable event fired
        if(e.getSource() != infinitiveEntry) {
            System.out.println("gioengioengioengeingioe");
            undoManager.addEdit(e.getEdit());
            numUndosInQueue++;

            updateUndoState();
            updateRedoState();
            System.out.println(numUndosInQueue + " " + numRedosInQueue);
        }
        
    }

    private boolean saveSet() {
        if(setTitleEntry.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please enter a title for this verb set.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(setAuthorEntry.getText().trim().isEmpty()) {            
            JOptionPane.showMessageDialog(this,"Please enter the author of this verb set.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        verbSet.setTitle(setTitleEntry.getText());
        verbSet.setAuthor(setAuthorEntry.getText());

        setTitle("Conjugation Set Editor - " + this.language.toString() + "\\" + this.verbSet.toString());

        updateCurrentVerb(); // store the current verb so user doesn't have to change the index each time just to update
        refreshVerbSet();       

        verbSetObject.overwriteVerbSetWith(verbSet); // apply changes from current set - copy into true copy (title, author, verbs)
        owner.getSetsViewPanel().getTreeModel().reload();
        
        setChanged = false;
        return true;
    }

    private void updateCurrentVerb() {
        if(currentNode != null) {
            storeCurrentVerb();

            DefaultMutableTreeNode selectedNow = (DefaultMutableTreeNode)infinitiveTree.getLastSelectedPathComponent();
            infinitiveModel.reload();
            infinitiveTree.setSelectionRow(rootNode.getIndex(selectedNow));           
        }
    }

    private void storeCurrentVerb() { // put all of the information in the currently selected conjugated verb
        String infinitiveText = infinitiveEntry.getText().trim();
        if(infinitiveText.isEmpty()) {
            infinitiveText = "New Infinitive";
        }

        ((ConjugatedVerb)currentNode.getUserObject()).setInfinitive(infinitiveText); // store back in node

        for(int i = 0; i < inflectionEntries.size(); i++) { // set the inflections for this verb
            ((ConjugatedVerb)currentNode.getUserObject()).setInflection(i,inflectionEntries.get(i).getText());
        }
    }

    private void refreshVerbSet() { // load all of the child nodes to the stored verbs
        verbSet.clear();
        for(int i = 0; i < rootNode.getChildCount(); i++) {
            ConjugatedVerb v = (ConjugatedVerb)((DefaultMutableTreeNode)rootNode.getChildAt(i)).getUserObject();
            verbSet.addConjugatedVerb(v);//new ConjugatedVerb(v));
        }
    }

    private void sortTreeAlphabetically(Component nextComp) {
        System.out.println("sorting tree");

        updateCurrentVerb();
        refreshVerbSet(); // store the verbs in the set so they can be sorted (including current one)

        // store what was selected
        ConjugatedVerb selected = null;
        if(infinitiveTree.getSelectionPath() != null) {
            selected = (ConjugatedVerb)((DefaultMutableTreeNode)infinitiveTree.getLastSelectedPathComponent()).getUserObject();
        }

        int countOfSame = 0;
        if(infinitiveTree.getSelectionRows() != null) {
            for(int row = 0; row < infinitiveTree.getSelectionRows()[0]; row++) { // go up until the selected one (only one can be selected at a time)
                if(((ConjugatedVerb)(((DefaultMutableTreeNode)infinitiveModel.getChild(rootNode,row)).getUserObject())).getInfinitive().equals(selected.getInfinitive())) {
                    countOfSame++; // count how many of that object are before the chosen one
                }
            }
        }
        
        verbSet.sortVerbsAlphabetically();
        rootNode.removeAllChildren();
        for(ConjugatedVerb verb : verbSet.getVerbs()) { // store all the sorted verbs back in the model to be reflected in the tree
            rootNode.add(new DefaultMutableTreeNode(verb));
            infinitiveModel.reload();
        }

        if(selected != null) { // only select again if something picked
            int row = 0; // find the row of the correct verb (just in case there are multiple
            for(int i = 0; i < verbSet.getVerbs().size(); i++) {
                if(selected.equals(verbSet.getVerbs().get(i))) {
                    row = i;
                    break;
                }
            }
            row += countOfSame; // above loop isn't enough - will always pick the first infinitive if there are some that are the same - this offset fixes it

            infinitiveTree.setSelectionRow(row);
            infinitiveTree.scrollRowToVisible(row);
        }
        
        if(nextComp != null) {
            nextComp.requestFocusInWindow();
        }
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == saveSetButton || e.getSource() == saveSetMenuItem) {
            saveSet();
            focusedComponent.requestFocusInWindow();
        }
        else if(e.getSource() == printSetButton || e.getSource() == printSetMenuItem) {
            System.out.println("set printing");
        }
        else if(e.getSource() == undoButton || e.getSource() == undoMenuItem) {
            System.out.println("undo");
            try {
                undoManager.undo();
                numUndosInQueue--;
                if(numRedosInQueue > 0) {
                    numRedosInQueue++;
                }
                System.out.println(numUndosInQueue + " " + numRedosInQueue);
            }
            catch (CannotUndoException ex) {
                System.out.println("Unable to undo: " + ex);
                ex.printStackTrace();
            }
            updateUndoState();
            updateRedoState();
        }
        else if(e.getSource() == redoButton || e.getSource() == redoMenuItem) {
            System.out.println("redo");
            try {
                undoManager.redo();
                if(numUndosInQueue > 0) {
                    numUndosInQueue++;
                }
                numRedosInQueue--;
                System.out.println(numUndosInQueue + " " + numRedosInQueue);
            } catch (CannotRedoException ex) {
                System.out.println("Unable to redo: " + ex);
                ex.printStackTrace();
            }
            updateRedoState();
            updateUndoState();
            
        }
        else if(e.getSource() == addInfinitiveButton || e.getSource() == addVerbMenuItem) {
            addNewConjugatedVerb();
        }
        else if(e.getSource() == removeInfinitiveButton || e.getSource() == removeVerbMenuItem) {
            deleteConjugatedVerb();
        }
        else if(e.getSource() == addAccentButton || e.getSource() == addAccentMenuItem) {
            try {
                if(focusedComponent instanceof JTextField) {
                    ((JTextField)focusedComponent).setText(Accent.accentFinalCharacter(((JTextField)focusedComponent).getText()));
                }
            }
            catch(IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(this,iae.getMessage(),"Accenting character error!",JOptionPane.ERROR_MESSAGE);
            }
            focusedComponent.requestFocusInWindow();
        }
        else if(e.getSource() == helpButton || e.getSource() == helpMenuItem) {
            System.out.println("help");
        }
        else if(e.getSource() == aboutMenuItem) {
            System.out.println("about");
        }
        else if(e.getSource() == exitMenuItem) {
            handleWindowClosing();
        }
    }
	
    private void addNewConjugatedVerb() {
        ConjugatedVerb verb = new ConjugatedVerb("New Infinitive",language.getSubjectPronouns().size(),verbSet.toString());

        // new automatic alphabetical insertion
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(verb);

        int insertIndex = 0;
        for(int i = 0; i < rootNode.getChildCount(); i++) {
            if(verb.getInfinitive().compareTo(((ConjugatedVerb)((DefaultMutableTreeNode)rootNode.getChildAt(i)).getUserObject()).getInfinitive()) < 0) {
                insertIndex = i;
                break;
            }
        }

        infinitiveModel.insertNodeInto(newNode,rootNode,insertIndex);
        infinitiveTree.setSelectionRow(insertIndex);
        infinitiveTree.scrollRowToVisible(insertIndex);

        infinitiveEntry.requestFocusInWindow();       
        setChanged = true;
    }
    
    private void deleteConjugatedVerb() {
        if(rootNode.getChildCount() == 0) { // there is nothing to remove, so don't do anything
            return;
        }
		
        if(rootNode.getChildCount() == 1) { // deleting the last node, so just clear it - don't let it go to zero size
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode)infinitiveTree.getLastSelectedPathComponent();
            if(selected == null) {
                selected = (DefaultMutableTreeNode)rootNode.getChildAt(0); // if not selected, then just assign to first one
            }

            ConjugatedVerb cv = (ConjugatedVerb)selected.getUserObject();

            cv.setInfinitive("New Infinitive");
            for(int i = 0; i < cv.getNumberOfSubjectPronouns(); i++) {
                cv.setInflection(i,"");
            }            
            
            clearFields();
            infinitiveEntry.setText("New Infinitive");
            infinitiveModel.nodeChanged(selected);
            infinitiveTree.setSelectionRow(rootNode.getIndex(selected));
            infinitiveEntry.requestFocusInWindow();
            setChanged = true;
            return;
        }

        DefaultMutableTreeNode selected = (DefaultMutableTreeNode)infinitiveTree.getLastSelectedPathComponent();

        if(selected == null) { // remove the last node, since nothing selected
            rootNode.remove(rootNode.getChildCount()-1);

            infinitiveModel.reload();
            infinitiveTree.setSelectionRow(rootNode.getIndex(rootNode.getLastLeaf()));
        }
        else { // remove the selected node; if this happens to be last node, select next to be before it; else, the one after
            DefaultMutableTreeNode next;
            if(rootNode.getIndex(selected) == rootNode.getChildCount()-1) {
                next = (DefaultMutableTreeNode)rootNode.getChildBefore(selected);
            }
            else {
                next = (DefaultMutableTreeNode)rootNode.getChildAfter(selected);
            }
            rootNode.remove(selected);
            
            infinitiveModel.reload();
            infinitiveTree.setSelectionRow(rootNode.getIndex(next));
        }
		
        infinitiveEntry.requestFocusInWindow();
        setChanged = true;
    }

    public void valueChanged(TreeSelectionEvent e) {
        changingNodes = true;
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode)infinitiveTree.getLastSelectedPathComponent();

        if(selected != null) {
            if(currentNode != null) { // if the current node isn't null, then update the underlying verbs - only store back in to set if so
                storeCurrentVerb();
                infinitiveModel.nodeChanged(currentNode); // update the title
            }

            // reenable all fields again, since newly selected isn't null
            infinitiveEntry.setEnabled(true);
            for(JTextField jtf : inflectionEntries) {
                jtf.setEnabled(true);
            }

            // load the newly selected verb set into visual components
            infinitiveEntry.setText(((ConjugatedVerb)selected.getUserObject()).getInfinitive());

            for(int i = 0; i < inflectionEntries.size(); i++) {
                inflectionEntries.get(i).setEnabled(true);
                inflectionEntries.get(i).setText(((VerbInflection)((ConjugatedVerb)(selected.getUserObject())).getInflection(i)).toString());
            }

            currentNode = selected;
            verbsLabel.setText("Verb Infinitives - " + (rootNode.getIndex(selected)+1) + " of " + rootNode.getChildCount());

            checkForDuplicateInfinitive(); // fixed!
        }
        else { // user has switched away from selecting anything this time, but still store what was placed
            if(currentNode != null) { // if the current node isn't null, then update the underlying verbs - only store back in to set if so
                storeCurrentVerb();
                infinitiveModel.nodeChanged(currentNode);
            }

            clearFields();
            infinitiveEntry.setEnabled(false);
            for(JTextField jtf : inflectionEntries) {
                jtf.setEnabled(false);
            }

            currentNode = selected;
            verbsLabel.setText("Verb Infinitives - nothing selected");
            infinitiveWarningLabel.setVisible(false);
        }

        changingNodes = false;
    }

    private void checkForDuplicateInfinitive() {
        String s = infinitiveEntry.getText().trim();
        infinitiveWarningLabel.setVisible((!s.isEmpty() && !s.equals("New Infinitive") && (numberOfSameInfinitive(s) > 0)));
    }
	
    private int numberOfSameInfinitive(String text) {
        int count = 0;
		
        for(int i = 0; i < rootNode.getChildCount(); i++) {
            if(rootNode.getChildAt(i) != currentNode && ((ConjugatedVerb)((DefaultMutableTreeNode)rootNode.getChildAt(i)).getUserObject()).getInfinitive().equals(text)) {
                count++;
            }
        }
		
        return count;
    }
	
    public void insertUpdate(DocumentEvent e) {
        if(!changingNodes) {
            setChanged = true;
            if(e.getDocument() == infinitiveEntry.getDocument()) {
                checkForDuplicateInfinitive();                
            }
        }
    }
	
    public void changedUpdate(DocumentEvent e) {
        if(!changingNodes) {
            setChanged = true;
            if(e.getDocument() == infinitiveEntry.getDocument()) {
                checkForDuplicateInfinitive();                
            }
        }
    }
	
    public void removeUpdate(DocumentEvent e) {
        if(!changingNodes) {
            setChanged = true;
            if(e.getDocument() == infinitiveEntry.getDocument()) {
                checkForDuplicateInfinitive();                
            }
        }
    }

    private void clearFields() {
        infinitiveEntry.setText("");
        for(JTextField t : inflectionEntries) {
            t.setText("");
        }
    }

    public static class MyFocusTraversalPolicy extends FocusTraversalPolicy {
        Vector<Component> order;

        public MyFocusTraversalPolicy(Vector<Component> order) {
            this.order = new Vector<Component>(order.size());
            this.order.addAll(order);
        }
		
        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            int idx = (order.indexOf(aComponent) + 1) % order.size();
            return order.get(idx);
        }
		
        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            int idx = order.indexOf(aComponent)-1;
            if(idx < 0) {
                idx = order.size()-1;
            }
            return order.get(idx);
        }
		
        public Component getDefaultComponent(Container focusCycleRoot) { // the one that is set as the default selected component that will be edited
            return order.get(0);
        }
		
        public Component getLastComponent(Container focusCycleRoot) {
            return order.lastElement();
        }
		
        public Component getFirstComponent(Container focusCycleRoot) {
            return order.get(0);
        }        
    }

    public void focusLost(FocusEvent e) {
        if(e.getSource() == infinitiveEntry) {
            if(e.getOppositeComponent() != addVerbMenuItem && e.getOppositeComponent() != addInfinitiveButton) {
                sortTreeAlphabetically(e.getOppositeComponent());
            }
        }
    }
	
    public void focusGained(FocusEvent e) {
        focusedComponent = (Component)e.getSource(); // assign the newly focused component
		
		// if the infinitive entry has not been changed yet, allow user to overwrite entirely, else, put in edit mode - here, user has already changed the infinitive (this is ignored)
        if(focusedComponent == infinitiveEntry && ((JTextField)infinitiveEntry).getText().equals("New Infinitive")) {
            ((JTextField)focusedComponent).selectAll();
        }
        if(focusedComponent == infinitiveEntry) {
            currentInfinitiveText = infinitiveEntry.getText();
        }
        else if(focusedComponent == infinitiveTree) { // if user selects something in the tree, automatically select the infinitive (tree added to focus listener)
            infinitiveEntry.requestFocusInWindow();
        }
    }

    private void handleWindowClosing() {
        updateCurrentVerb();
        refreshVerbSet(); // must perform these operations first since user might enter something and not change verb or save yet, so must take this into account

        String emptyEntry = findEmptyEntry();
        if(emptyEntry != null) {
            if(JOptionPane.showConfirmDialog(this,emptyEntry + "\nContinue closing frame?","Blank field found!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }
        }

        if(setChanged) {
            int selection = JOptionPane.showConfirmDialog(this,"This conjugation list has changed.\nWould you like to save these changes?","Unsaved Changes Exist!",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(selection == JOptionPane.YES_OPTION) {
                if(saveSet()) {
                    this.verbSetObject.editorFrame = null;
                    dispose();
                }
            }
            else if(selection == JOptionPane.NO_OPTION) {
                this.verbSetObject.editorFrame = null;
                dispose();
            }
        }
        else { // already saved, so just close the window
            this.verbSetObject.editorFrame = null;
            dispose();
        }
    }

    private String findEmptyEntry() {
        for(ConjugatedVerb cv : verbSet.getVerbs()) {
            if(cv.getInfinitive().isEmpty()) {
                return "A verb with a blank infinitive has been found in this set.";
            }
			
            for(VerbInflection vi : cv.getConjugations()) {
                if(vi.toString().isEmpty()) {
                    return cv.toString() + " has been found to have an empty inflection.";
                }
            }
        }
        return null;
    }

    private void updateUndoState() {
        if (undoManager.canUndo()) {
            undoMenuItem.setEnabled(true);
            undoMenuItem.setText(undoManager.getUndoPresentationName());
            undoButton.setEnabled(true);
            undoButton.setToolTipText(undoManager.getUndoPresentationName());
        }
        else {
            undoMenuItem.setEnabled(false);
            undoMenuItem.setText("Undo");
            undoButton.setEnabled(false);
            undoButton.setToolTipText("Undo previous item");
        }
    }

    private void updateRedoState() {
        if (undoManager.canRedo()) {
            redoMenuItem.setEnabled(true);
            redoMenuItem.setText(undoManager.getRedoPresentationName());
            redoButton.setEnabled(true);
            redoButton.setToolTipText(undoManager.getRedoPresentationName());
        } else {
            redoMenuItem.setEnabled(false);
            redoMenuItem.setText("Redo");
            redoButton.setEnabled(false);
            redoButton.setToolTipText("Redo previous item");
        }
    }
}
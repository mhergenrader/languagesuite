package notecardstation.notecardeditor;

import guicomponents.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import languagesuite.LanguageSuite;
import notecardstation.general.NotecardStation;
import notecardstation.notecard.*;

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
 * Main frame containing the notecard editor module
 * @author Michael Hergenrader
 */
public class NotecardStackEditorFrame extends JFrame implements ActionListener, FocusListener, DocumentListener {
    private static String imageLocation = LanguageSuite.defaultImageLocation;

    private NotecardStation owner;
    private NotecardStack stack;
    private NotecardStation.NotecardStackTreeObject treeObject;

    private Vector<Component> componentOrder;
    private MyFocusTraversalPolicy myPolicy;

    // Menu bar
    private JMenuBar frameMenuBar;
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu stackMenu;
    private JMenu helpMenu;

    private JMenuItem saveStack;
    private JMenuItem printStack;
    private JMenuItem exit;

    private JMenuItem undo;
    private JMenuItem redo;
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem addAccent;
    
    private JMenuItem addCard2D;
    private JMenuItem addCard3D;
    private JMenuItem deleteCard;
    
    private JMenu currentCardMenu;
    private JMenuItem twoSides;
    private JMenuItem threeSides;
    private JMenuItem addImage;
    private JMenuItem removeImage;

    private JMenuItem help;
    private JMenuItem about;

    // Toolbar
    private JToolBar frameToolBar;
    private ToolbarButton saveStackButton;
    private ToolbarButton printStackButton;

    private ToolbarButton addCard2DButton;
    private ToolbarButton addCard3DButton;
    private ToolbarButton deleteCardButton;

    private ToolbarButton undoButton;
    private ToolbarButton redoButton;
    
    private ToolbarButton addImageButton;
    private ToolbarButton removeImageButton;
    private ToolbarButton addAccentButton;

    private ToolbarButton helpButton;

    // Panels
    private StackInfoPanel stackInfoPanel;
    private CardListPanel cardListPanel;
    private CardAttributePanel cardAttributePanel;
    private NotecardDataPanel notecardDataPanel;

    private DefaultListModel cardListModel;
    private int stackSize;

    private Component focusedComponent;

    private boolean shouldFire;
    private boolean stackModified; // if ANY field is modified, then this will be set to true
    // if this is true, then should prompt user if they would like to save
    // if user just changes cards, then this doesn't change
	
	public boolean changingCards = false;
    
    public NotecardStackEditorFrame(NotecardStation owner, NotecardStation.NotecardStackTreeObject stackTreeObject) {
        super(stackTreeObject.getNotecardStack().toString() + " - Notecard Stack Editor");
        setIconImage(LanguageSuite.frameIcon);

        this.treeObject = stackTreeObject;
        this.stack = new NotecardStack(stackTreeObject.getNotecardStack()); // hold a copy of the reference - if user saves, overwrite changes into it
        this.stack.sortStackAlphabetically();
        this.owner = owner;
		
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleClosingFrame();
            }
        });

        getContentPane().setBackground(Color.DARK_GRAY.brighter());

        componentOrder = new Vector<Component>();
        cardListModel = new DefaultListModel();

        if(stack.getNotecards().isEmpty()) { // only the first time - stack will be empty only this time
            createDefaultCard();
        }
        else {
            loadStackIntoListModel();
        } // upon creation, card list panel automatically picks the first one

        stackSize = cardListModel.getSize();

        createPanels();
        setupMenuBar();
        setupToolBar();
        setupLayout();
        loadStackInformation();
        setupComponentFocus();
        showInitialCardInfo();

        shouldFire = true;
        stackModified = false;
    }

    public DefaultListModel getCardListModel() {
        return cardListModel;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void actionPerformed(ActionEvent ae) {
        deleteCardButton.setEnabled(!cardListModel.isEmpty());
        deleteCard.setEnabled(!cardListModel.isEmpty());

        if(ae.getSource() == saveStack || ae.getSource() == saveStackButton) {
            saveStack();
            focusedComponent.requestFocusInWindow();
        }
        else if(ae.getSource() == undo || ae.getSource() == undoButton) {
            System.out.println("undo");
        }
        else if(ae.getSource() == redo || ae.getSource() == redoButton) {
            System.out.println("redo");
        }
        else if(ae.getSource() == addCard2D || ae.getSource() == addCard2DButton) {
            stackModified = true;
            addCard(2);
        }
        else if(ae.getSource() == addCard3D || ae.getSource() == addCard3DButton) {
            stackModified = true;
            addCard(3);
        }
        else if(ae.getSource() == deleteCard || ae.getSource() == deleteCardButton) {
            stackModified = true;
            deleteCard();
        }
        else if(ae.getSource() == twoSides || ae.getSource() == cardAttributePanel.get2DButton()) {
            cardAttributePanel.makeNotecard2D();
            stackModified = true;
            componentOrder.remove(notecardDataPanel.getSide3TextBox());
            myPolicy = new MyFocusTraversalPolicy(componentOrder);
            setFocusTraversalPolicy(myPolicy);
        }
        else if(ae.getSource() == threeSides || ae.getSource() == cardAttributePanel.get3DButton()) {
            cardAttributePanel.makeNotecard3D();
            stackModified = true;
            componentOrder.add(notecardDataPanel.getSide3TextBox());
            myPolicy = new MyFocusTraversalPolicy(componentOrder);
            setFocusTraversalPolicy(myPolicy);
        }
        else if(ae.getSource() == addAccent || ae.getSource() == addAccentButton) {
            try {
                if(focusedComponent instanceof JTextField) {
                    ((JTextField)focusedComponent).setText(Accent.accentFinalCharacter(((JTextField)focusedComponent).getText()));
                }
            }
            catch(IllegalArgumentException iae) { // if timed, and setting is such that it should stop when issues arise, then stop the timer
                JOptionPane.showMessageDialog(this,iae.getMessage(),"Accenting character error!",JOptionPane.ERROR_MESSAGE);                
            }
            focusedComponent.requestFocusInWindow();
        }
        else if(ae.getSource() == cardAttributePanel.getSide1EqualToTitleBox()) {
            notecardDataPanel.getSide1TextBox().setText(cardAttributePanel.isSide1EqualToTitle()?cardAttributePanel.getCardTitleText():"");
			
            if(cardAttributePanel.isSide1EqualToTitle()) {
                notecardDataPanel.getSide2TextBox().requestFocusInWindow();
            }
            else { // if focus in second text box, put it in the first one; else, just return the cursor to where it came from
                ((focusedComponent == notecardDataPanel.getSide2TextBox())?notecardDataPanel.getSide1TextBox():focusedComponent).requestFocusInWindow();
            }
        }
        else if(ae.getSource() == exit) {
            handleClosingFrame();
        }
        else if(ae.getSource() == addImage || ae.getSource() == addImageButton) {
            System.out.println("add image pressed");
        }
        else if(ae.getSource() == removeImage || ae.getSource() == removeImageButton) {
            System.out.println("remove image pressed");
        }
        else if(ae.getSource() == help || ae.getSource() == helpButton) {
            System.out.println("help selected");
        }
        else if(ae.getSource() == about) {
            System.out.println("about selected");
        }
        else if(ae.getSource() == printStack || ae.getSource() == printStackButton) {
            System.out.println("print stack selected");
        }
    }
    
    private boolean saveStack() {
        if(stackInfoPanel.getStackDataPanel().getStackTitle().trim().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter a title for this notecard stack.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(stackInfoPanel.getStackDataPanel().getAuthor().trim().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter an author for this notecard stack.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(stackInfoPanel.getStackDataPanel().getVersionNumber().trim().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter a version number for this notecard stack.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(stackInfoPanel.getStackSideTitlesPanel().getLanguage().trim().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter the languages for this notecard stack.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(stackInfoPanel.getStackSideTitlesPanel().getSide1Title().trim().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter a title for Side 1 of this notecard stack.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(stackInfoPanel.getStackSideTitlesPanel().getSide2Title().trim().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter a title for Side 2 of this notecard stack.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }
        boolean has3DCards = false;
        for(int i = 0; i < cardListModel.getSize(); i++) {
            if(cardListModel.getElementAt(i) instanceof Notecard3D) {
                has3DCards = true;
                break;
            }
        }
        if(has3DCards && stackInfoPanel.getStackSideTitlesPanel().getSide3Title().length() == 0) {
            JOptionPane.showMessageDialog(this,"Please enter a title for Side 3 of this notecard stack.","Error! Invalid data!",JOptionPane.ERROR_MESSAGE);
            return false;
        }

        cardListPanel.getCardList().updateUI(); // causes the visual list to update to the titles (refreshes the list)

        setTitle(stackInfoPanel.getStackDataPanel().getStackTitle() + " - Notecard Stack Editor");
		
        cardListPanel.tryToStoreCurrentCard();

        stackInfoPanel.getStackDataPanel().updateLastModified();

        refreshNotecardStack();

        // since user has chosen to save, update the *real stack* with the information
        treeObject.overwriteStackWith(stack);

        owner.getStacksTreePanel().getTreeModel().reload();

        stackModified = false;
        return true;
    }

    private void refreshNotecardStack() {
        // set all the current cards for this stack to what is there now (from the list model)
        // in other words, update this copy of the stack
        stack.getNotecards().clear();
        for(int i = 0; i < cardListModel.getSize(); i++) {
            if(((Notecard)cardListModel.getElementAt(i)).getNotecardStack() == null) {
                ((Notecard)cardListModel.getElementAt(i)).setStackReference(this.stack);
            }

            if(cardListModel.getElementAt(i) instanceof Notecard3D) {
                stack.addNotecard(new Notecard3D((Notecard3D)cardListModel.getElementAt(i)));
            }
            else {
                stack.addNotecard(new Notecard((Notecard)cardListModel.getElementAt(i)));
            }
        }

        stack.setTitle(stackInfoPanel.getStackDataPanel().getStackTitle());
        stack.setAuthor(stackInfoPanel.getStackDataPanel().getAuthor());
        stack.setVersion(stackInfoPanel.getStackDataPanel().getVersionNumber());
        stack.setLastModified(stackInfoPanel.getStackDataPanel().getLastModified());

        stack.setLanguage(stackInfoPanel.getStackSideTitlesPanel().getLanguage());
        stack.setSide1Title(stackInfoPanel.getStackSideTitlesPanel().getSide1Title());
        stack.setSide2Title(stackInfoPanel.getStackSideTitlesPanel().getSide2Title());
        stack.setSide3Title(stackInfoPanel.getStackSideTitlesPanel().getSide3Title());

        for(Notecard n : stack.getNotecards()) { // must do this, as the stack name might be changed, and cards need reference to it
            n.setStackReference(stack);
        }
    }  

    private void sortStackAlphabetically() {
        shouldFire = false;
        
        cardListPanel.tryToStoreCurrentCard();
        refreshNotecardStack();

        Notecard selected = null;
        if(cardListPanel.getCardList().getSelectedIndex() > -1) {
            selected = (Notecard)cardListModel.get(cardListPanel.getCardList().getSelectedIndex());
        }

        int countOfSame = 0, maxIndex = cardListPanel.getCardList().getSelectedIndex();
        for(int row = 0; row < maxIndex; row++) {
            if(((Notecard)cardListModel.get(row)).getTitle().equals(selected.getTitle())) {
                countOfSame++;
            }
        }       

        // modify the stack behind the scenes (away from the list model), and add the stuff back to the list model, which will now be in alphabetical order
        stack.sortStackAlphabetically();
        
        cardListModel.clear();
        for(Notecard card : stack.getNotecards()) { // store all the sorted verbs back in the model to be reflected in the tree
            if(card instanceof Notecard3D) {
                cardListModel.addElement(new Notecard3D(card));
            }
            else {
                cardListModel.addElement(new Notecard(card));
            }
        }
        
        if(selected != null) { // only select again if something picked
            int row = 0;
            for(int i = 0; i < cardListModel.getSize(); i++) {
                if(selected.getTitle().equals(((Notecard)cardListModel.get(i)).getTitle())) {
                    row = i;
                    break;
                }
            }
            row += countOfSame;
            
            cardListPanel.getCardList().setSelectedIndex(row);

            cardListPanel.showNewlySelectedCardData();
            cardListPanel.updateCardLabelAndComponents();
            cardListPanel.updateCurrentSelectedIndex();
        }

        checkForDuplicateCardTitles();        
        shouldFire = true;
    }
    
    public boolean shouldFire() { // allows overriding when valueChanged operations can be called
        return shouldFire;
    }
	
    private void addCard(int numberOfDimensions) {
        shouldFire = false;
		
        Notecard n = (numberOfDimensions == 2)?new Notecard("Blank 2-D Card"):new Notecard3D("Blank 3-D Card");
        n.setStackReference(this.stack);

        // find index to place the new card at
        int insertIndex = -1;
        for(int i = 0; i < cardListModel.getSize(); i++) {
            if(n.getTitle().compareTo(((Notecard)cardListModel.get(i)).getTitle()) < 0) { // if add =, then will insert in front (if not at back)
                insertIndex = i;
                break;
            }
        }

        cardListPanel.tryToStoreCurrentCard(); // if something was selected before the add operation, save it back in its notecard object

        if(insertIndex < 0) { // couldn't find a place to insert it, so add to the end
            cardListModel.addElement(n);
        }
        else {
            cardListModel.add(insertIndex,n);
        }
        stackSize++;

        // select the new card - if there wasn't a place, select the last card
        cardListPanel.getCardList().setSelectedIndex((insertIndex < 0)?cardListModel.getSize()-1:insertIndex);
        cardListPanel.showNewlySelectedCardData();
		
        cardListPanel.updateCardLabelAndComponents(); // display what card is chosen (if applicable) and enable/disable components
        cardListPanel.updateCurrentSelectedIndex();
        
        cardAttributePanel.getCardTitleBox().requestFocusInWindow();

        shouldFire = true;
    }

    private void deleteCard() {
        shouldFire = false; // override - don't need to store anything, and something will always be selected
        if(cardListModel.isEmpty()) {
            shouldFire = true;
            return;
        }
        if(cardListModel.getSize() == 1) { // user is attempting to delete the last card - just replace it with a cleared card
            cardListModel.remove(0);
            // delete old one and add in new blank 2-D one
            Notecard n = new Notecard("Blank 2-D Card");
            n.setStackReference(this.stack);
            cardListModel.addElement(n);
            cardListPanel.getCardList().setSelectedIndex(0); // stack size doesn't change here
        }
        else { // multiple cards in the stack - test if something is selected or not
            int cardIndexToDelete = cardListPanel.getCardList().getSelectedIndex();
            cardListModel.remove((cardIndexToDelete > -1)?cardIndexToDelete:cardListModel.getSize()-1);
            cardListPanel.getCardList().setSelectedIndex((cardIndexToDelete > -1)?((cardIndexToDelete==cardListModel.getSize())?cardIndexToDelete-1:cardIndexToDelete):cardListModel.getSize()-1);

            stackSize--;
        }
        cardListPanel.showNewlySelectedCardData();
        cardListPanel.updateCardLabelAndComponents();
        cardListPanel.updateCurrentSelectedIndex();

        cardAttributePanel.getCardTitleBox().requestFocusInWindow();

        shouldFire = true;
    }

    private void handleClosingFrame() {
        cardListPanel.tryToStoreCurrentCard();
        refreshNotecardStack();

        // check if blank fields found no matter whether this stack was saved or not - inform the user if so -
        // this will occur before any sort of exiting checks - if user doesn't want to keep going, it won't matter
        // whether or not (s)he has saved - this will return back to the frame - this should be, as it is now, a
        // lower level notification - saving checks will occur later
        Notecard emptyFieldsCard = findFirstCardWithBlankFields();
        if(emptyFieldsCard != null) {
            if(JOptionPane.showConfirmDialog(this,"Warning: A blank field was found in\n"+emptyFieldsCard.toString()+". Continue closing frame?","Blank field found!",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }
        }

        if(stackModified) {
            int userResponse = JOptionPane.showConfirmDialog(this,"This stack has been modified.\nWould you like to save your changes?","Warning: Unsaved changes exist!",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
            if(userResponse == JOptionPane.YES_OPTION) {
                if(saveStack()) { // if able to save stack, close the frame
                    this.treeObject.editorFrame = null;
                    dispose();
                }
            }
            else if(userResponse == JOptionPane.NO_OPTION) {
                this.treeObject.editorFrame = null;
                dispose();
            }
            // cancel option not needed to be tested here: just ignore all logic
        }
        else { // nothing changed, so just dispose of the frame
            this.treeObject.editorFrame = null;
            dispose();
        }
    }

    // call this after storing everything to current stack
    // this will be checked before overwriting the real stack
    private Notecard findFirstCardWithBlankFields() {
        for(Notecard n : stack.getNotecards()) {
            if(n.getFirstSide().toString().isEmpty() || n.getSecondSide().toString().isEmpty()) {
                return n;
            }
            if(n instanceof Notecard3D && ((Notecard3D)n).getThirdSide().toString().isEmpty()) {
                return n;
            }
        }
        return null;
    }

    public CardListPanel getCardPanel() {
        return cardListPanel;
    }
	
    public NotecardDataPanel getNotecardDataPanel() {
        return notecardDataPanel;
    }
	
    public CardAttributePanel getCardAttributePanel() {
        return cardAttributePanel;
    }

    public NotecardStack getNotecardStack() {
        return stack;
    }

    public void setStackModified(boolean value) {
        stackModified = value;
    }
	
    public Vector<Component> getComponentVector() {
        return componentOrder;
    }

    public void checkForDuplicateCardTitles() {
        String s = cardAttributePanel.getCardTitleText().trim();
        cardAttributePanel.activateWarningLabel(cardListPanel.getCurrentSelectedIndex() > -1 && !s.isEmpty() && !s.equals("Blank 3-D Card") && !s.equals("Blank 2-D Card") && (numberOfSameCardTitle(s) > 0));
    }

    private int numberOfSameCardTitle(String text) {
        int count = 0;
        int index = cardListPanel.getCurrentSelectedIndex();
        
        for(int i = 0; i < cardListModel.getSize(); i++) {
            if(i != index && ((Notecard)cardListModel.get(i)).getTitle().equals(text)) {
                count++;
            }
        }
        return count;
    }

    public class MyFocusTraversalPolicy extends FocusTraversalPolicy {
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

    public void updateNotecardPanelFocus() {
        int current = cardListPanel.getCurrentSelectedIndex();
        if(cardListModel.get(current) instanceof Notecard3D) {
            componentOrder.add(notecardDataPanel.getSide3TextBox());
        }
        else {
            componentOrder.remove(notecardDataPanel.getSide3TextBox());
        }
        myPolicy = new MyFocusTraversalPolicy(componentOrder);
        setFocusTraversalPolicy(myPolicy);
    }

    private boolean shouldSkipToSecondSide = false;

    private boolean userClickedComponentThatAlphabetizes(FocusEvent e) { // if a component not in this list was clicked, sort the list
        return e.getOppositeComponent() != addCard2D && e.getOppositeComponent() != addCard2DButton && e.getOppositeComponent() != addCard3D && e.getOppositeComponent() != addCard3DButton && e.getOppositeComponent() != deleteCard && e.getOppositeComponent() != deleteCardButton && e.getOppositeComponent() != cardAttributePanel.getSide1EqualToTitleBox();
    }

    // try to automatically go to the second side if the box is checked
    public void focusLost(FocusEvent e) {
        // if current box is the title box and user wants to make the first side equal to the title and the
		// user isn't trying to turn that option off, then copy it to first side
        if(focusedComponent == cardAttributePanel.getCardTitleBox()) {
            if(cardAttributePanel.isSide1EqualToTitle()) { // if user has checked that (s)he wants the title to be the same as the side 1 text (for convenience)
                Notecard selected = (cardListPanel.getCardList().getSelectedIndex() > -1)?(Notecard)cardListModel.get(cardListPanel.getCardList().getSelectedIndex()):null;
                if(e.getOppositeComponent() != cardAttributePanel.getSide1EqualToTitleBox() && selected != null && !selected.getFirstSide().toString().equals(selected.getTitle())) {
                    notecardDataPanel.getSide1TextBox().setText(cardAttributePanel.getCardTitleText());
                }
                if(e.getOppositeComponent() == notecardDataPanel.getSide1TextBox()) { // if headed for the side1 box, shorten the trip to side 2
                    shouldSkipToSecondSide = true;
                }
            }
            
            if(userClickedComponentThatAlphabetizes(e)) { // prevents unnecessary sorting
                changingCards = true;
                sortStackAlphabetically();
                changingCards = false;
            }
        }
    }
    
    public void focusGained(FocusEvent e) {
        focusedComponent = (Component)e.getSource();
        if(focusedComponent == cardAttributePanel.getCardTitleBox() && (cardAttributePanel.getCardTitleText().equals("Blank 2-D Card") || cardAttributePanel.getCardTitleText().equals("Blank 3-D Card"))) {
            ((JTextField)focusedComponent).selectAll();
        }
        else if(focusedComponent == notecardDataPanel.getSide1TextBox()) {
			// if the option is set to have the title be the same as the first side, go to the second side automatically
            if(shouldSkipToSecondSide) {
                notecardDataPanel.getSide2TextBox().requestFocusInWindow();
                shouldSkipToSecondSide = false;
            }
        }
        else if(focusedComponent == cardListPanel.getCardList()) {
            cardAttributePanel.getCardTitleBox().requestFocusInWindow();
        }
        else if(focusedComponent == cardAttributePanel.get3DButton()) {
            e.getOppositeComponent().requestFocusInWindow(); // return focus back so not stuck on the button
        }
        else if(focusedComponent == cardAttributePanel.get2DButton()) {
            if(e.getOppositeComponent() == notecardDataPanel.getSide3TextBox()) {
                notecardDataPanel.getSide2TextBox().requestFocusInWindow();
            }
            else {
                e.getOppositeComponent().requestFocusInWindow();
            }
        }
    }
	
    public void changedUpdate(DocumentEvent e) {
        if(!changingCards) {
            stackModified = true;
        }
        if(e.getDocument() == cardAttributePanel.getCardTitleBox().getDocument()) {
            checkForDuplicateCardTitles();
        }
    }
	
    public void removeUpdate(DocumentEvent e) {
        if(!changingCards) {
            stackModified = true;
        }
        if(e.getDocument() == cardAttributePanel.getCardTitleBox().getDocument()) {
            checkForDuplicateCardTitles();
        }
    }
	
    public void insertUpdate(DocumentEvent e) {
        if(!changingCards) {
            stackModified = true;
        }
        if(e.getDocument() == cardAttributePanel.getCardTitleBox().getDocument()) {
            checkForDuplicateCardTitles();
        }
    }
	
    private void setupMenuBar() {
        frameMenuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        editMenu = new JMenu("Edit");
        stackMenu = new JMenu("Stack");
        helpMenu = new JMenu("Help");

        currentCardMenu = new JMenu("Current Card");

        saveStack = new JMenuItem("Save Stack");
        printStack = new JMenuItem("Print Stack");
        exit = new JMenuItem("Close Editor");

        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");

        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        addAccent = new JMenuItem("Add Accent");

        addCard2D = new JMenuItem("Add 2-D Card");
        addCard3D = new JMenuItem("Add 3-D Card");
        deleteCard = new JMenuItem("Remove Card");
        twoSides = new JMenuItem("Two Dimensional");
        threeSides = new JMenuItem("Three Dimensional");
        addImage = new JMenuItem("Add Image");
        removeImage = new JMenuItem("Remove Image");

        help = new JMenuItem("Editor Help");
        about = new JMenuItem("About");

        saveStack.addActionListener(this);
        printStack.addActionListener(this);
        exit.addActionListener(this);

        undo.addActionListener(this);
        redo.addActionListener(this);
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        addAccent.addActionListener(this);

        addCard2D.addActionListener(this);
        addCard3D.addActionListener(this);
        deleteCard.addActionListener(this);
        twoSides.addActionListener(this);
        threeSides.addActionListener(this);
        addImage.addActionListener(this);
        removeImage.addActionListener(this);

        help.addActionListener(this);
        about.addActionListener(this);

        fileMenu.setMnemonic(KeyEvent.VK_F);
        editMenu.setMnemonic(KeyEvent.VK_E);
        stackMenu.setMnemonic(KeyEvent.VK_S);
        helpMenu.setMnemonic(KeyEvent.VK_H);

        saveStack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,KeyEvent.CTRL_MASK));
        printStack.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,KeyEvent.CTRL_MASK));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK));

        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,KeyEvent.CTRL_MASK));
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,KeyEvent.CTRL_MASK));
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,KeyEvent.CTRL_MASK));
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,KeyEvent.CTRL_MASK));
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK));

        addCard2D.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,KeyEvent.CTRL_MASK));
        addCard3D.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,KeyEvent.CTRL_MASK));
        deleteCard.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE,0));

        addImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,KeyEvent.CTRL_MASK));
        removeImage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,KeyEvent.CTRL_MASK));

        twoSides.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,KeyEvent.CTRL_MASK));
        threeSides.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3,KeyEvent.CTRL_MASK));

        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));

        currentCardMenu.add(twoSides);
        currentCardMenu.add(threeSides);
        currentCardMenu.addSeparator();
        currentCardMenu.add(addImage);
        currentCardMenu.add(removeImage);

        fileMenu.add(saveStack);
        fileMenu.add(printStack);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.addSeparator();
        editMenu.add(cut);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.addSeparator();
        editMenu.add(addAccent);

        stackMenu.add(addCard2D);
        stackMenu.add(addCard3D);
        stackMenu.add(deleteCard);
        stackMenu.addSeparator();
        stackMenu.add(currentCardMenu);

        helpMenu.add(help);
        helpMenu.add(about);

        frameMenuBar.add(fileMenu);
        frameMenuBar.add(editMenu);
        frameMenuBar.add(stackMenu);
        frameMenuBar.add(helpMenu);

        setJMenuBar(frameMenuBar);
    }
	
    private void setupToolBar() {
        frameToolBar = new JToolBar("frame toolbar");
        frameToolBar.setFloatable(false);
        frameToolBar.setOpaque(false);
        frameToolBar.setBorderPainted(false);

        frameToolBar.setMinimumSize(new Dimension(1050,54));
        frameToolBar.setMaximumSize(new Dimension(1050,54));
        frameToolBar.setPreferredSize(new Dimension(1050,54));

        saveStackButton = new ToolbarButton("Save this stack",imageLocation+"saveicon.png");
        saveStackButton.addActionListener(this);
        printStackButton = new ToolbarButton("Print this stack",imageLocation+"printstack_edited-1.png");
        printStackButton.addActionListener(this);

        undoButton = new ToolbarButton("Undo previous action",imageLocation+"undoicon2_edited-1.png");
        undoButton.addActionListener(this);
        redoButton = new ToolbarButton("Redo previous action",imageLocation+"redoicon_edited-1.png");
        redoButton.addActionListener(this);
        addAccentButton = new ToolbarButton("Loop through accents to add to the final letter",imageLocation+"addaccent_edited-1.png");
        addAccentButton.addActionListener(this);

        addCard2DButton = new ToolbarButton("Add a new 2-D card to this stack",imageLocation+"newcardicon.png");
        addCard2DButton.addActionListener(this);
        addCard3DButton = new ToolbarButton("Add a new 3-D card to this stack",imageLocation+"3daddicon.png");
        addCard3DButton.addActionListener(this);
        deleteCardButton = new ToolbarButton("Removed selected card(s) from this stack. If no card is selected, deletes the last card",imageLocation+"deletecardicon.png"); // could move this
        deleteCardButton.addActionListener(this);

        addImageButton = new ToolbarButton("Add image to the current notecard side",imageLocation+"addimageicon_edited-1.png");
        addImageButton.addActionListener(this);
        removeImageButton = new ToolbarButton("Remove image from the current notecard side",imageLocation+"removeimageicon_edited-1.png");
        removeImageButton.addActionListener(this);

        helpButton = new ToolbarButton("Open Language Suite Help",imageLocation+"helpicon_edited-1.png");
        helpButton.addActionListener(this);

        frameToolBar.add(saveStackButton);
        frameToolBar.add(printStackButton);
        frameToolBar.addSeparator(new Dimension(20,0));
        frameToolBar.add(undoButton);
        frameToolBar.add(redoButton);
        frameToolBar.addSeparator(new Dimension(50,0));
        frameToolBar.add(addCard2DButton);
        frameToolBar.add(addCard3DButton);
        frameToolBar.add(deleteCardButton);
        frameToolBar.addSeparator(new Dimension(20,0));
        frameToolBar.add(addImageButton);
        frameToolBar.add(removeImageButton);
        frameToolBar.add(addAccentButton);
        frameToolBar.addSeparator(new Dimension(50,0));
        frameToolBar.addSeparator(new Dimension(50,0));
        frameToolBar.add(helpButton);

        add(frameToolBar,BorderLayout.NORTH);
    }
	
    private void createPanels() {
        cardListPanel = new CardListPanel(this);
        cardAttributePanel = new CardAttributePanel(this);
        notecardDataPanel = new NotecardDataPanel(this);        
        stackInfoPanel = new StackInfoPanel(this);
    }
	
    private void setupLayout() {
        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(stackInfoPanel);
        add(cardListPanel);
        add(cardAttributePanel);
        add(notecardDataPanel);

        sl.putConstraint(SpringLayout.WEST,stackInfoPanel,0,SpringLayout.WEST,getContentPane());
        sl.putConstraint(SpringLayout.NORTH,stackInfoPanel,21,SpringLayout.SOUTH,frameToolBar);

        sl.putConstraint(SpringLayout.WEST,cardListPanel,10,SpringLayout.EAST,stackInfoPanel);
        sl.putConstraint(SpringLayout.NORTH,cardListPanel,10,SpringLayout.SOUTH,frameToolBar);

        sl.putConstraint(SpringLayout.WEST,cardAttributePanel,0,SpringLayout.WEST,getContentPane());
        sl.putConstraint(SpringLayout.NORTH,cardAttributePanel,4,SpringLayout.SOUTH,stackInfoPanel);

        sl.putConstraint(SpringLayout.WEST,notecardDataPanel,0,SpringLayout.WEST,getContentPane());
        sl.putConstraint(SpringLayout.NORTH,notecardDataPanel,0,SpringLayout.SOUTH,cardAttributePanel);
    }
	
    private void createDefaultCard() {
        Notecard n = new Notecard("Blank 2-D Card","","");
        n.setStackReference(this.stack);
        cardListModel.addElement(n); // default card created at startup
    }
	
    private void loadStackIntoListModel() {
        for(Notecard n : stack.getNotecards()) {
            cardListModel.addElement(n);
        }
    }
	
    private void setupComponentFocus() {
        stackInfoPanel.addComponentsToOrder();
        cardAttributePanel.addComponentsToOrder();
        notecardDataPanel.addComponentsToOrder();
        if(cardListModel.get(cardListPanel.getCurrentSelectedIndex()) instanceof Notecard3D) {
            componentOrder.add(notecardDataPanel.getSide3TextBox());
        }

        myPolicy = new MyFocusTraversalPolicy(componentOrder);
        setFocusTraversalPolicy(myPolicy);
    }
	
    private void showInitialCardInfo() {
        notecardDataPanel.getSide1TextBox().setText(((Notecard)cardListModel.get(0)).getFirstSide().toString());
        notecardDataPanel.getSide2TextBox().setText(((Notecard)cardListModel.get(0)).getSecondSide().toString());
		
        // adjust for 3-D cards that are first - show third side and set the text and radio button
        if(cardListModel.get(0) instanceof Notecard3D) {
            notecardDataPanel.showThirdSide(true);
            notecardDataPanel.getSide3TextBox().setText(((Notecard3D)cardListModel.get(0)).getThirdSide().toString());
        }
        cardAttributePanel.setCardTitleText(((Notecard)cardListModel.get(0)).getTitle());
        cardAttributePanel.set3DButtonSelected((cardListModel.get(0) instanceof Notecard3D));
    }
	
    private void loadStackInformation() {
        stackInfoPanel.getStackDataPanel().setStackTitle(stack.getTitle());
        stackInfoPanel.getStackDataPanel().setAuthor(stack.getAuthor());
        stackInfoPanel.getStackDataPanel().setVersionNumber(stack.getVersion());
        stackInfoPanel.getStackDataPanel().setLastModified(stack.getLastModified());

        stackInfoPanel.getStackSideTitlesPanel().setLanguage(stack.getLanguage());
        stackInfoPanel.getStackSideTitlesPanel().setSide1Title(stack.getSide1Title());
        stackInfoPanel.getStackSideTitlesPanel().setSide2Title(stack.getSide2Title());
        stackInfoPanel.getStackSideTitlesPanel().setSide3Title(stack.getSide3Title());

        notecardDataPanel.setSide1LabelText(stack.getSide1Title());
        notecardDataPanel.setSide2LabelText(stack.getSide2Title());
        notecardDataPanel.setSide3LabelText(stack.getSide3Title());
    }
}

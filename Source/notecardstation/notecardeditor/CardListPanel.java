package notecardstation.notecardeditor;

import guicomponents.CustomScrollBar;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
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
 * Panel that displays a list of all notecards in the current stack, used for the editor
 * @author Michael Hergenrader
 */
public class CardListPanel extends JPanel {

    private NotecardStackEditorFrame owner;

    private JLabel currentCard;
    private JScrollPane scrollPane;
    private JList cardList;

    private int currentSelectedIndex = 0;
    private NotecardListCellRenderer renderer;

    public CardListPanel(NotecardStackEditorFrame owner) {
        this.owner = owner;

        setBackground(Color.DARK_GRAY.brighter());

        setMinimumSize(new Dimension(280,116));
        setMaximumSize(new Dimension(280,116));
        setPreferredSize(new Dimension(280,116));

        setLayout(new BorderLayout());

        currentCard = new JLabel();
        currentCard.setHorizontalAlignment(SwingConstants.CENTER);
        currentCard.setForeground(Color.WHITE);

        cardList = new JList(owner.getCardListModel());
        cardList.setSelectionForeground(Color.WHITE);
        cardList.setSelectionBackground(Color.BLACK);
        cardList.setBackground(Color.DARK_GRAY.brighter());
        cardList.setForeground(Color.WHITE);
        cardList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        renderer = new NotecardListCellRenderer();
        cardList.setCellRenderer(renderer);
        cardList.addFocusListener(owner);

        currentSelectedIndex = 0;
        cardList.setSelectedIndex(currentSelectedIndex);

        currentCard.setText("Card 1 of " + (owner.getCardListModel().getSize()));

        CardListSelectionListener clsl = new CardListSelectionListener();
        cardList.addListSelectionListener(clsl);

        scrollPane = new JScrollPane(cardList);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // speed up the scroll bar
        scrollPane.setMinimumSize(new Dimension(280,116));
        scrollPane.setMaximumSize(new Dimension(280,116));
        scrollPane.setPreferredSize(new Dimension(280,116));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBar(CustomScrollBar.createCustomScrollBar());
        
        add(currentCard,BorderLayout.NORTH);
        add(scrollPane,BorderLayout.CENTER);
    }

    public void setCurrentCardLabel(String text) {
        currentCard.setText(text);
    }

    public JList getCardList() {
        return cardList;
    }

    public void setCurrentSelectedIndex(int value) {
        currentSelectedIndex = value;
    }

    public int getCurrentSelectedIndex() {
        return currentSelectedIndex;
    }
	
    private void setComponentsEnabled(boolean value) {
        owner.getCardAttributePanel().getCardTitleBox().setEnabled(value);

        owner.getCardAttributePanel().set2DButtonEnabled(value);
        owner.getCardAttributePanel().set3DButtonEnabled(value);
        owner.getCardAttributePanel().setSide1EqualToTitleBoxEnabled(value);

        owner.getNotecardDataPanel().getSide1TextBox().setEnabled(value);
        owner.getNotecardDataPanel().getSide2TextBox().setEnabled(value);
        owner.getNotecardDataPanel().getSide3TextBox().setEnabled(value);
    }
	
    class CardListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            if(!owner.shouldFire() || cardList.getSelectedIndex() == currentSelectedIndex) {
                return;
            }

            owner.changingCards = true;

            tryToStoreCurrentCard(); // if something was selected before, then make sure to save it (doesn't matter if something selected afterward or not)
            
            if(cardList.getSelectedIndex() > -1) { // some card is selected
                showNewlySelectedCardData();
                owner.getCardAttributePanel().getCardTitleBox().requestFocusInWindow();
            }
            else { // nothing selected, so clear and disable all the fields to edit a particular notecard
                clearDataBoxes();
            }

            updateCardLabelAndComponents(); // display what card is chosen (if applicable) and enable/disable components
            updateCurrentSelectedIndex();
            owner.checkForDuplicateCardTitles();
            owner.changingCards = false;
        }        
    }
	
    private void clearDataBoxes() {
        owner.getCardAttributePanel().getCardTitleBox().setText("");
        owner.getNotecardDataPanel().getSide1TextBox().setText("");
        owner.getNotecardDataPanel().getSide2TextBox().setText("");
        owner.getNotecardDataPanel().getSide3TextBox().setText("");
    }

    // UTILITY METHODS
	
    public void tryToStoreCurrentCard() { // save the current data from the notecard sides input from the user into this card that is being left behind
        if(currentSelectedIndex < 0) {
            return; // nothing to store
        }
        Notecard selected = (Notecard)owner.getCardListModel().get(currentSelectedIndex);
        String titleText = owner.getCardAttributePanel().getCardTitleText();
        if(titleText.isEmpty()) {
            titleText = (selected instanceof Notecard3D)?"Blank 3-D Card":"Blank 2-D Card";
        }
        selected.setTitle(titleText);

        // if option is selected to store first side to be the same as the title, then store it
		// else, just store with what's there
        // don't store it yet until put there by user
        String firstSideText = owner.getNotecardDataPanel().getSide1TextBox().getText();

        selected.getFirstSide().setText(firstSideText); // save sides
        selected.getSecondSide().setText(owner.getNotecardDataPanel().getSide2TextBox().getText());
        if(selected instanceof Notecard3D) {
            ((Notecard3D)selected).getThirdSide().setText(owner.getNotecardDataPanel().getSide3TextBox().getText());
        }
    }
	
    public void showNewlySelectedCardData() {
        int newIndex = cardList.getSelectedIndex();

        owner.getCardAttributePanel().setCardTitleText(((Notecard)owner.getCardListModel().get(newIndex)).getTitle());

        // load the visual notecard sides (on screen) with the underlying notecard side data of the newly selected card
        owner.getNotecardDataPanel().getSide1TextBox().setText(((Notecard)owner.getCardListModel().get(newIndex)).getFirstSide().toString());
        owner.getNotecardDataPanel().getSide2TextBox().setText(((Notecard)owner.getCardListModel().get(newIndex)).getSecondSide().toString());

        boolean cardIs3D = owner.getCardListModel().get(newIndex) instanceof Notecard3D;
        owner.getNotecardDataPanel().showThirdSide(cardIs3D);
        owner.getNotecardDataPanel().getSide3TextBox().setText(cardIs3D?((Notecard3D)owner.getCardListModel().get(newIndex)).getThirdSide().toString():"");
        owner.getCardAttributePanel().set3DButtonSelected(cardIs3D);
        owner.getCardAttributePanel().set2DButtonSelected(!cardIs3D);
    }

    public void updateCardLabelAndComponents() {
        setComponentsEnabled(cardList.getSelectedIndex() > -1);
		// if a card is selected and the title is equal to side 1 already, true; else false
        owner.getCardAttributePanel().setSide1EqualToTitleBoxSelected(((cardList.getSelectedIndex() > -1) && ((Notecard)owner.getCardListModel().get(cardList.getSelectedIndex())).getTitle().equals(((Notecard)owner.getCardListModel().get(cardList.getSelectedIndex())).getFirstSide().toString()))); 
        currentCard.setText((cardList.getSelectedIndex() > -1)?"Card " + (cardList.getSelectedIndex()+1) + " of " + owner.getStackSize():"Nothing selected");
    }

    public void updateCurrentSelectedIndex() {
        currentSelectedIndex = cardList.getSelectedIndex(); // set the currently selected index to this one
        cardList.ensureIndexIsVisible(currentSelectedIndex);
		
        owner.updateNotecardPanelFocus(); // update the focus for the current card
    }
}

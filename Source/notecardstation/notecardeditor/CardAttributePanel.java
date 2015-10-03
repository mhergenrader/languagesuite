package notecardstation.notecardeditor;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import languagesuite.LanguageSuite;
import notecardstation.notecard.*;
import notecardstation.notecardeditor.*;

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
 * First creation panel for new notecards: title, number of dimensions
 * @author Michael Hergenrader
 */
public class CardAttributePanel extends JPanel {
    private NotecardStackEditorFrame owner;

    private JLabel cardTitle;
    private JTextField cardTitleEntry;

    private JLabel cardDimension;
    private ButtonGroup dimensionButtonGroup;
    private JRadioButton twoDimensionButton;
    private JRadioButton threeDimensionButton;

    private JLabel titleWarningLabel;

    private JCheckBox side1EqualToTitleBox;

    public CardAttributePanel(NotecardStackEditorFrame owner) {
        this.owner = owner;
        
        setBackground(Color.DARK_GRAY.brighter());
        setMinimumSize(new Dimension(780,56));
        setMaximumSize(new Dimension(780,56));
        setPreferredSize(new Dimension(780,56));

        cardTitle = new JLabel("Card Title:");
        cardTitle.setForeground(Color.WHITE);
        cardTitle.setMinimumSize(new Dimension(57,24));
        cardTitle.setMaximumSize(new Dimension(57,24));
        cardTitle.setPreferredSize(new Dimension(57,24));

        cardTitleEntry = new JTextField(20);
        cardTitleEntry.addFocusListener(owner);
        cardTitleEntry.getDocument().addDocumentListener(owner);

        cardDimension = new JLabel("Dimensions:");
        cardDimension.setForeground(Color.WHITE);
        cardDimension.setMinimumSize(new Dimension(70,24));
        cardDimension.setMaximumSize(new Dimension(70,24));
        cardDimension.setPreferredSize(new Dimension(70,24));
        dimensionButtonGroup = new ButtonGroup();

        twoDimensionButton = new JRadioButton("Two",true);
        twoDimensionButton.setOpaque(false);
        twoDimensionButton.setForeground(Color.WHITE);
        twoDimensionButton.addActionListener(owner);
        twoDimensionButton.addFocusListener(owner);
        
        threeDimensionButton = new JRadioButton("Three");
        threeDimensionButton.setOpaque(false);
        threeDimensionButton.setForeground(Color.WHITE);
        threeDimensionButton.addActionListener(owner);
        threeDimensionButton.addFocusListener(owner);      

        titleWarningLabel = new JLabel("Warning: Notecard with this title already exists");
        titleWarningLabel.setMinimumSize(new Dimension(350,30));
        titleWarningLabel.setMaximumSize(new Dimension(350,30));
        titleWarningLabel.setPreferredSize(new Dimension(350,30));
        titleWarningLabel.setForeground(Color.YELLOW);
        titleWarningLabel.setIcon(new ImageIcon(LanguageSuite.defaultImageLocation+"warningicon.png"));
        titleWarningLabel.setVisible(false);

        side1EqualToTitleBox = new JCheckBox("Side 1 = Title?");
        side1EqualToTitleBox.setOpaque(false);
        side1EqualToTitleBox.setSelected(true);
        side1EqualToTitleBox.setForeground(Color.WHITE);
        side1EqualToTitleBox.addActionListener(owner);

        dimensionButtonGroup.add(twoDimensionButton);
        dimensionButtonGroup.add(threeDimensionButton);

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(cardTitle);
        add(cardTitleEntry);
        add(cardDimension);
        add(twoDimensionButton);
        add(threeDimensionButton);
        add(side1EqualToTitleBox);
        add(titleWarningLabel);

        sl.putConstraint(SpringLayout.NORTH,cardTitle,8,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,cardTitle,10,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,cardTitleEntry,12,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,cardTitleEntry,6,SpringLayout.EAST,cardTitle);

        sl.putConstraint(SpringLayout.NORTH,cardDimension,8,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,cardDimension,20,SpringLayout.EAST,cardTitleEntry);

        sl.putConstraint(SpringLayout.NORTH,twoDimensionButton,8,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,twoDimensionButton,4,SpringLayout.EAST,cardDimension);

        sl.putConstraint(SpringLayout.NORTH,threeDimensionButton,8,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,threeDimensionButton,4,SpringLayout.EAST,twoDimensionButton);

        sl.putConstraint(SpringLayout.NORTH,side1EqualToTitleBox,8,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,side1EqualToTitleBox,30,SpringLayout.EAST,threeDimensionButton);

        sl.putConstraint(SpringLayout.NORTH,titleWarningLabel,0,SpringLayout.SOUTH,cardTitleEntry);
        sl.putConstraint(SpringLayout.WEST,titleWarningLabel,10,SpringLayout.WEST,this);
    }

    public JRadioButton get2DButton() {
        return twoDimensionButton;
    }

    public JRadioButton get3DButton() {
        return threeDimensionButton;
    }

    public void set2DButtonEnabled(boolean value) {
        twoDimensionButton.setEnabled(value);
    }

    public void set2DButtonSelected(boolean value) {
        twoDimensionButton.setSelected(value);
    }

    public void set3DButtonEnabled(boolean value) {
        threeDimensionButton.setEnabled(value);
    }

    public void set3DButtonSelected(boolean value) {
        threeDimensionButton.setSelected(value);
    }

    public void setSide1EqualToTitleBoxEnabled(boolean value) {
        side1EqualToTitleBox.setEnabled(value);
    }

    public void setSide1EqualToTitleBoxSelected(boolean value) {
        side1EqualToTitleBox.setSelected(value);
    }

    public void activateWarningLabel(boolean value) {
        titleWarningLabel.setVisible(value);
    }

    public void makeNotecard2D() {
        Notecard temp = (Notecard)owner.getCardPanel().getCardList().getSelectedValue();
        if(temp instanceof Notecard3D) {
            Notecard n = new Notecard(temp);
            owner.getCardListModel().set(owner.getCardPanel().getCardList().getSelectedIndex(),n); // copy 3-D notecard into a 2-D one

            owner.getNotecardDataPanel().showThirdSide(false);
            twoDimensionButton.setSelected(true);
            owner.setStackModified(true);
        }
    }

    public void makeNotecard3D() {
        Notecard temp = (Notecard)owner.getCardPanel().getCardList().getSelectedValue();
        if(!(temp instanceof Notecard3D)) {
            Notecard3D n3 = new Notecard3D(temp);
            owner.getCardListModel().set(owner.getCardPanel().getCardList().getSelectedIndex(),n3);
            
            owner.getNotecardDataPanel().showThirdSide(true);
            threeDimensionButton.setSelected(true);
            owner.setStackModified(true);
        }
    }

    public void addComponentsToOrder() {
        owner.getComponentVector().add(cardTitleEntry);
    }

    public int getNumberOfDimensions() {
        return twoDimensionButton.isSelected()?2:3;
    }

    public void setNumberOfDimensions(int numDimensions) throws IllegalArgumentException {
        if(numDimensions == 2) {
            twoDimensionButton.setSelected(true);
        }
        else if(numDimensions == 3) {
            threeDimensionButton.setSelected(true);
        }
        else {
            throw new IllegalArgumentException("Error! Valid dimensions are 2 and 3.");
        }
    }

    public JTextField getCardTitleBox() {
        return cardTitleEntry;
    }

    public String getCardTitleText() {
        return cardTitleEntry.getText();
    }

    public void setCardTitleText(String text) {
        cardTitleEntry.setText(text);
    }

    public boolean isSide1EqualToTitle() {
        return side1EqualToTitleBox.isSelected();
    }

    public JCheckBox getSide1EqualToTitleBox() {
        return side1EqualToTitleBox;
    }
}

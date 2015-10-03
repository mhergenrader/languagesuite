package notecardstation.notecardreviewer.userinteraction;

import javax.swing.*;
import notecardstation.notecardreviewer.general.*;
import java.awt.*;

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
 * Visual panel for 3-D notecard entries
 * @author Michael Hergenrader
 */
public class EnterTextThreeSidesPanel extends UserInputPanel implements HasThreeSides,EntersText {

    private JLabel side2Title;
    private JLabel side3Title;
    private JTextField side2Entry;
    private JTextField side3Entry;

    public EnterTextThreeSidesPanel(UserInteractionPanel uiPanel) {
        super(uiPanel);

        setMinimumSize(UserInteractionPanel.USER_INTERACTION_PANEL_SIZE);
        setMaximumSize(UserInteractionPanel.USER_INTERACTION_PANEL_SIZE);
        setPreferredSize(UserInteractionPanel.USER_INTERACTION_PANEL_SIZE);
        setBackground(Color.DARK_GRAY.brighter());

        side2Title = new JLabel("Side 2 Title");
        side2Title.setForeground(Color.WHITE);
        side2Title.setMinimumSize(UserInputPanel.TITLE_SIZE);
        side2Title.setMaximumSize(UserInputPanel.TITLE_SIZE);
        side2Title.setPreferredSize(UserInputPanel.TITLE_SIZE);
        side2Title.setHorizontalAlignment(SwingConstants.CENTER);

        side2Entry = new JTextField(20);
        side2Entry.addFocusListener(owner.getOwner());

        side3Title = new JLabel("Side 3 Title");
        side3Title.setForeground(Color.WHITE);
        side3Title.setMinimumSize(UserInputPanel.TITLE_SIZE);
        side3Title.setMaximumSize(UserInputPanel.TITLE_SIZE);
        side3Title.setPreferredSize(UserInputPanel.TITLE_SIZE);
        side3Title.setHorizontalAlignment(SwingConstants.CENTER);

        side3Entry = new JTextField(20);
        side3Entry.addFocusListener(owner.getOwner());

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(side2Title);
        add(side3Title);
        add(side2Entry);
        add(side3Entry);

        sl.putConstraint(SpringLayout.NORTH,side2Title,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,side2Title,26,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,side3Title,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.EAST,side3Title,-26,SpringLayout.EAST,this);

        sl.putConstraint(SpringLayout.NORTH,side2Entry,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.WEST,side2Entry,16,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,side3Entry,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.EAST,side3Entry,-16,SpringLayout.EAST,this);
    }

    public JTextField getSide2Entry() {
        return side2Entry;
    }

    public JTextField getSide3Entry() {
        return side3Entry;
    }

    public void setSide2Title(String text) {
        side2Title.setText(text);
    }

    public String getSide2Title() {
        return side2Title.getText();
    }

    public void setSide3Title(String text) {
        side3Title.setText(text);
    }

    public String getSide3Title() {
        return side3Title.getText();
    }

    public void requestDefaultComponentFocus() {
        side2Entry.requestFocusInWindow();
    }

    public void requestSide3Focus() {
        side3Entry.requestFocusInWindow();
    }

    public void clearUserInput() {
        side2Entry.setText("");
        side3Entry.setText("");
    }

    public void clearSide2Entry() {
        side2Entry.setText("");
    }

    public void clearSide3Entry() {
        side3Entry.setText("");
    }

    public void enableSides() {
        side2Entry.setEnabled(true);
        side3Entry.setEnabled(true);
    }
    
}

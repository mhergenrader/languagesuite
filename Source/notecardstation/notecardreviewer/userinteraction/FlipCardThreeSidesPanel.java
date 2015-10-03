package notecardstation.notecardreviewer.userinteraction;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.*;
import notecardstation.notecardreviewer.general.*;

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
 * Visual panel for displaying 3-D notecards
 * @author Michael Hergenrader
 */
public class FlipCardThreeSidesPanel extends UserInputPanel implements HasThreeSides, FlipsCard {

    private JLabel side2Title;
    private JLabel side3Title;
    private JButton flipLeftButton;
    private JButton flipRightButton;
    private JButton previousCardButton;
    private JButton nextCardButton;

    public FlipCardThreeSidesPanel(UserInteractionPanel uiPanel) {
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

        side3Title = new JLabel("Side 3 Title");
        side3Title.setForeground(Color.WHITE);
        side3Title.setMinimumSize(UserInputPanel.TITLE_SIZE);
        side3Title.setMaximumSize(UserInputPanel.TITLE_SIZE);
        side3Title.setPreferredSize(UserInputPanel.TITLE_SIZE);
        side3Title.setHorizontalAlignment(SwingConstants.CENTER);

        flipLeftButton = new JButton("Flip Left");
        flipLeftButton.addActionListener(uiPanel.getOwner());
        flipRightButton = new JButton("Flip Right");
        flipRightButton.addActionListener(uiPanel.getOwner());

        previousCardButton = new JButton("Previous");
        previousCardButton.addActionListener(uiPanel.getOwner());
        nextCardButton = new JButton("Next");
        nextCardButton.addActionListener(uiPanel.getOwner());
        nextCardButton.setMinimumSize(new Dimension(83,26));
        nextCardButton.setMaximumSize(new Dimension(83,26));
        nextCardButton.setPreferredSize(new Dimension(83,26));

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(side2Title);
        add(side3Title);
        add(previousCardButton);
        add(flipLeftButton);
        add(flipRightButton);
        add(nextCardButton);

        sl.putConstraint(SpringLayout.NORTH,side2Title,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,side2Title,26,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,side3Title,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,side3Title,0,SpringLayout.EAST,side2Title);

        sl.putConstraint(SpringLayout.NORTH,previousCardButton,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.WEST,previousCardButton,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,flipLeftButton,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.WEST,flipLeftButton,14,SpringLayout.EAST,previousCardButton);

        sl.putConstraint(SpringLayout.NORTH,flipRightButton,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.WEST,flipRightButton,140,SpringLayout.EAST,flipLeftButton);

        sl.putConstraint(SpringLayout.NORTH,nextCardButton,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.WEST,nextCardButton,14,SpringLayout.EAST,flipRightButton);
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

    public JButton getFlipLeftButton() {
        return flipLeftButton;
    }

    public JButton getFlipRightButton() {
        return flipRightButton;
    }

    public JButton getPreviousCardButton() {
        return previousCardButton;
    }

    public JButton getNextCardButton() {
        return nextCardButton;
    }

    public void requestDefaultComponentFocus() {
        flipRightButton.requestFocusInWindow();
    }
}

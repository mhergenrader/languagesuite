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
 * Visual display for 2-D notecards for review
 * @author Michael Hergenrader
 */
public class FlipCardTwoSidesPanel extends UserInputPanel implements FlipsCard {

    private JLabel side2Title;
    private JButton flipCardButton;
    private JButton previousCardButton;
    private JButton nextCardButton;

    public FlipCardTwoSidesPanel(UserInteractionPanel uiPanel) {
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

        flipCardButton = new JButton("Flip Card");
        flipCardButton.addActionListener(uiPanel.getOwner());
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
        add(previousCardButton);
        add(flipCardButton);
        add(nextCardButton);

        sl.putConstraint(SpringLayout.NORTH,side2Title,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,side2Title,138,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,previousCardButton,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.WEST,previousCardButton,0,SpringLayout.WEST,this);//125,0,0

        sl.putConstraint(SpringLayout.NORTH,flipCardButton,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.WEST,flipCardButton,(((int)UserInteractionPanel.USER_INTERACTION_PANEL_SIZE.getWidth())-84)/2,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,nextCardButton,0,SpringLayout.SOUTH,side2Title);
        sl.putConstraint(SpringLayout.EAST,nextCardButton,0,SpringLayout.EAST,this);
    }

    public void setSide2Title(String text) {
        side2Title.setText(text);
    }

    public JButton getFlipCardButton() {
        return flipCardButton;
    }

    public JButton getPreviousCardButton() {
        return previousCardButton;
    }

    public JButton getNextCardButton() {
        return nextCardButton;
    }

    public void requestDefaultComponentFocus() {
        flipCardButton.requestFocusInWindow();
    }
}

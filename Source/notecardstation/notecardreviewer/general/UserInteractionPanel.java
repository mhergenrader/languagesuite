package notecardstation.notecardreviewer.general;

import javax.swing.*;
import java.awt.*;
import notecardstation.notecardreviewer.general.NotecardStackReviewFrame.ReviewMode;
import notecardstation.notecardreviewer.userinteraction.*;

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
 * Handles various user interactions when reviewing notecards: allows them to flip cards or enter text answers to them
 * @author Michael Hergenrader
 */
public class UserInteractionPanel extends JPanel {

    private NotecardStackReviewFrame owner;

    private EnterTextTwoSidesPanel twoSideEntryPanel;
    private EnterTextThreeSidesPanel threeSideEntryPanel;
	
    private FlipCardTwoSidesPanel twoSideFlipPanel;
    private FlipCardThreeSidesPanel threeSideFlipPanel;

    private UserInputPanel userInputPanel;

    private JPanel renderer;
    private CardLayout cardLayout;

    public static enum CardDimension { TWO_SIDES, THREE_SIDES };
    public static enum PanelMode { ENTRY, FLIP };

    public static final Dimension USER_INTERACTION_PANEL_SIZE = new Dimension(500,50);

    public UserInteractionPanel(NotecardStackReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(USER_INTERACTION_PANEL_SIZE);
        setMaximumSize(USER_INTERACTION_PANEL_SIZE);
        setPreferredSize(USER_INTERACTION_PANEL_SIZE);
        setBackground(Color.DARK_GRAY.brighter());
        
        setupPanels();
        setupLayout();
    }

    private void setupPanels() {
        twoSideEntryPanel = new EnterTextTwoSidesPanel(this);
        threeSideEntryPanel = new EnterTextThreeSidesPanel(this);
        twoSideFlipPanel = new FlipCardTwoSidesPanel(this);
        threeSideFlipPanel = new FlipCardThreeSidesPanel(this);
    }
    private void setupLayout() {
        renderer = new JPanel();
        cardLayout = new CardLayout();

        renderer.setLayout(cardLayout);
        renderer.add(twoSideEntryPanel,"Two Side Entry");
        renderer.add(threeSideEntryPanel,"Three Side Entry");
        renderer.add(twoSideFlipPanel,"Two Side Flip");
        renderer.add(threeSideFlipPanel,"Three Side Flip");
		
        cardLayout.show(renderer,"Two Side Entry");
        userInputPanel = twoSideEntryPanel;
        add(renderer);
    }
	
	// helper method that adjusts the view pane to view the proper text for the buttons of user interactions based on modes and # notecard sides
    public void refreshTo(CardDimension numberOfSides, ReviewMode reviewMode) throws IllegalArgumentException {
        if(numberOfSides == CardDimension.TWO_SIDES) {
            if(reviewMode == ReviewMode.PRACTICE_MODE || reviewMode == ReviewMode.TEST_MODE) {
                cardLayout.show(renderer,"Two Side Entry");
                userInputPanel = twoSideEntryPanel;
            }
            else if(reviewMode == ReviewMode.FLIP_MODE) {
                cardLayout.show(renderer,"Two Side Flip");
                userInputPanel = twoSideFlipPanel;
            }
            else {
                throw new IllegalArgumentException("can only do flip or entry. (2 sides)");
            }
        }
        else if(numberOfSides == CardDimension.THREE_SIDES) {
            if(reviewMode == ReviewMode.PRACTICE_MODE || reviewMode == ReviewMode.TEST_MODE) {
                cardLayout.show(renderer,"Three Side Entry");
                userInputPanel = threeSideEntryPanel;
            }
            else if(reviewMode == ReviewMode.FLIP_MODE) {
                cardLayout.show(renderer,"Three Side Flip");
                userInputPanel = threeSideFlipPanel;
            }
            else {
                throw new IllegalArgumentException("can only do flip or entry. (3 sides)");
            }
        }
        else {
            throw new IllegalArgumentException("can only do 2 or 3 notecard sides!!!");
        }
    }

    public UserInputPanel getUserInputPanel() {
        return userInputPanel;
    }

    public NotecardStackReviewFrame getOwner() {
        return owner;
    }
}

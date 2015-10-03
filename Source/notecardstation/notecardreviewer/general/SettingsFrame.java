package notecardstation.notecardreviewer.general;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import notecardstation.notecard.*;
import notecardstation.notecardreviewer.general.NotecardStackReviewFrame.NotecardSides;
import notecardstation.notecardreviewer.general.NotecardStackReviewFrame.NotecardStackReviewInfo;
import java.util.*;
import languagesuite.LanguageSuite;

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
 * Main settings frame for adjusting how a user wants to run through notecard stack(s)
 * @author Michael Hergenrader
 */
public class SettingsFrame extends JFrame {

    private NotecardStackReviewFrame owner;

    private JTabbedPane tabbedPane;
    private JLabel settingsLabel;

    private JScrollPane practiceSettingsScrollPane;
    private JPanel practiceSettingsPanel;

    private JCheckBox wantToStopClockBox;
    private JLabel stopClockTestDefaultText;
    private JCheckBox wantToShuffleAtEndBox;
    private JLabel shuffleAtEndTestDefaultText;
    private JLabel shuffleAtEndNote;
    private JCheckBox wantToSkipWrongAnswersBox;
    private JLabel skipWrongAnswersTestDefaultText;
    private JCheckBox wantToClearAnswerBox;
    private JLabel clearWrongAnswerTestDefaultText;
    private JCheckBox caseSensitiveBox;
    private JLabel caseSensitivityTestDefaultText;
    private JButton resetDefaults; // for the general settings

    private JScrollPane shufflingSettingsScrollPane;
    private JPanel shufflingSettingsPanel;

    private JCheckBox wantToMixMultipleStacksBox;
    private JLabel mixMultipleStacksTestDefaultText;

    private JScrollPane stackSettingsScrollPane; // settings for particular stacks
    private JPanel stackSettingsPanel;

    private java.util.List<StackSettingsPanel> stackSettingsPanels;

    private JScrollPane listsScrollPane;
    private ListsPanel listsPanel;

    // represents all the settings for one particular notecard stack
    public class StackSettingsPanel extends JPanel implements ActionListener {
        private NotecardStackReviewInfo stackInfo;

        // various labels and fields
        private JLabel stackTitleLabel;
        private JLabel defaultSide2D;
        private JPanel buttonsPanel2D;
        private JRadioButton sideAButton2D;
        private JRadioButton sideBButton2D;
        private ButtonGroup defaultSide2DButtonGroup;        

        private JLabel which3DSidesToTestLabel;
        private JPanel whichSidesPanel;
        private JRadioButton sideAandBButton;
        private JRadioButton sideAandCButton;
        private JRadioButton sideBandCButton;
        private JRadioButton sideABCButton;
        private ButtonGroup which3DSidesToTestButtonGroup;

        private JLabel defaultSide3D;
        private JPanel buttonsPanel3D;
        private JRadioButton sideAButton3D;
        private JRadioButton sideBButton3D;
        private JRadioButton sideCButton3D;
        private ButtonGroup defaultSide3DButtonGroup;

        private JPanel resetPanel;
        private JButton reset;

        public StackSettingsPanel(NotecardStackReviewInfo nsi) {
            this.stackInfo = nsi;
            initializeComponents();
            addComponents();
        }

        public NotecardSides get2DDefaultSide() {
            return sideAButton2D.isSelected()?NotecardSides.SIDE_1:NotecardSides.SIDE_2;
        }
        public void set2DDefaultSide(NotecardSides side) throws IllegalArgumentException {
            if(side == NotecardSides.SIDE_1) {
                sideAButton2D.setSelected(true);
            }
            else if(side == NotecardSides.SIDE_2) {
                sideBButton2D.setSelected(true);
            }
            else {
                throw new IllegalArgumentException("Cannot assign default side of 3 to a 2-D card");
            }
        }

        public ArrayList<NotecardSides> get3DSidesToTest() {
            ArrayList<NotecardSides> sides = new ArrayList<NotecardSides>();
            if(sideABCButton.isSelected()) { // if all are to be tested, then send all three (allow 2-side subsets for review of 3-D cards)
                sides.add(NotecardSides.SIDE_1);
                sides.add(NotecardSides.SIDE_2);
                sides.add(NotecardSides.SIDE_3);
                return sides;
            }
            if(sideAandBButton.isSelected()) {
                sides.add(NotecardSides.SIDE_1);
                sides.add(NotecardSides.SIDE_2);
                return sides;
            }
            else if(sideBandCButton.isSelected()) {
                sides.add(NotecardSides.SIDE_2);
                sides.add(NotecardSides.SIDE_3);
                return sides;
            }
            else {
                sides.add(NotecardSides.SIDE_1);
                sides.add(NotecardSides.SIDE_3);
                return sides;
            }
        }
        public void set3DSidesToTest(java.util.List<NotecardSides> sides) throws IllegalArgumentException {
            if(sides.size() == 3) {
                sideABCButton.setSelected(true);
                if(!sideAButton3D.isEnabled()) { 
                    sideAButton3D.setEnabled(true);
                }
                if(!sideBButton3D.isEnabled()) {
                    sideBButton3D.setEnabled(true);
                }
                if(!sideCButton3D.isEnabled()) {
                    sideCButton3D.setEnabled(true);
                }
            }
            else if(sides.size() == 2) {
                if(!sides.contains(NotecardSides.SIDE_1)) {
                    sideBandCButton.setSelected(true);
                    sideAButton3D.setEnabled(false);
                    if(sideAButton3D.isSelected()) {
                        sideBButton3D.setSelected(true);
                    }
                    if(!sideBButton3D.isEnabled()) {
                        sideBButton3D.setEnabled(true);
                    }
                    if(!sideCButton3D.isEnabled()) {
                        sideCButton3D.setEnabled(true);
                    }
                }
                else {
                    if(sides.contains(NotecardSides.SIDE_2)) {
                        sideAandBButton.setSelected(true);
                        sideCButton3D.setEnabled(false);
                        if(sideCButton3D.isSelected()) {
                            sideAButton3D.setSelected(true);
                        }
                        if(!sideAButton3D.isEnabled()) {
                            sideAButton3D.setEnabled(true);
                        }
                        if(!sideBButton3D.isEnabled()) {
                            sideBButton3D.setEnabled(true);
                        }
                    }
                    else {
                        sideAandCButton.setSelected(true);
                        sideBButton3D.setEnabled(false);
                        if(sideBButton3D.isSelected()) {
                            sideAButton3D.setSelected(true);
                        }
                        if(!sideAButton3D.isEnabled()) {
                            sideAButton3D.setEnabled(true);
                        }
                        if(!sideCButton3D.isEnabled()) {
                            sideCButton3D.setEnabled(true);
                        }
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Number of sides to test must be 2 or 3; the passed list has " + sides.size() + " elements");
            }
        }

        public NotecardSides get3DDefaultSide() {
            return sideAButton3D.isSelected()?NotecardSides.SIDE_1:(sideBButton3D.isSelected()?NotecardSides.SIDE_2:NotecardSides.SIDE_3);
        }

        private NotecardStackReviewInfo getNotecardStackInfo() {
            return stackInfo;
        }

        private void initializeComponents() {
            buttonsPanel2D = new JPanel();
            whichSidesPanel = new JPanel();
            buttonsPanel3D = new JPanel();

            stackTitleLabel = new JLabel("~~~ "+ stackInfo.stack.toString() + " ~~~");
            stackTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            stackTitleLabel.setFont(new Font("serif",Font.ITALIC | Font.BOLD,16));
            
            defaultSide2D = new JLabel("Default (Front) Side for 2-D cards:");
            defaultSide2D.setFont(new Font("serif",Font.ITALIC,14));
            sideAButton2D = new JRadioButton(stackInfo.stack.getSide1Title().isEmpty()?"no title 1":stackInfo.stack.getSide1Title());
            sideAButton2D.setSelected(true);
            sideBButton2D = new JRadioButton(stackInfo.stack.getSide2Title().isEmpty()?"no title 2":stackInfo.stack.getSide2Title());
            defaultSide2DButtonGroup = new ButtonGroup();
            defaultSide2DButtonGroup.add(sideAButton2D);
            defaultSide2DButtonGroup.add(sideBButton2D);
            buttonsPanel2D.setLayout(new BoxLayout(buttonsPanel2D,BoxLayout.LINE_AXIS));
            buttonsPanel2D.add(sideAButton2D);
            buttonsPanel2D.add(sideBButton2D);

            which3DSidesToTestLabel = new JLabel("Which sides of the 3-D cards would you like to review?");
            which3DSidesToTestLabel.setFont(new Font("serif",Font.ITALIC,14));
            sideAandBButton = new JRadioButton(stackInfo.stack.getSide1Title().isEmpty()?"no title 1":stackInfo.stack.getSide1Title() + " and " + (stackInfo.stack.getSide2Title().isEmpty()?"no title 2":stackInfo.stack.getSide2Title()));
            sideAandBButton.addActionListener(this);
            sideAandCButton = new JRadioButton(stackInfo.stack.getSide1Title().isEmpty()?"no title 1":stackInfo.stack.getSide1Title() + " and " + (stackInfo.stack.getSide3Title().isEmpty()?"no title 3":stackInfo.stack.getSide3Title()));
            sideAandCButton.addActionListener(this);
            sideBandCButton = new JRadioButton(stackInfo.stack.getSide2Title().isEmpty()?"no title 2":stackInfo.stack.getSide2Title() + " and " + (stackInfo.stack.getSide3Title().isEmpty()?"no title 3":stackInfo.stack.getSide3Title()));
            sideBandCButton.addActionListener(this);
            sideABCButton = new JRadioButton("All Sides");
            sideABCButton.addActionListener(this);
            sideABCButton.setSelected(true);
            which3DSidesToTestButtonGroup = new ButtonGroup();
            which3DSidesToTestButtonGroup.add(sideAandBButton);
            which3DSidesToTestButtonGroup.add(sideAandCButton);
            which3DSidesToTestButtonGroup.add(sideBandCButton);
            which3DSidesToTestButtonGroup.add(sideABCButton);
            whichSidesPanel.setLayout(new BoxLayout(whichSidesPanel,BoxLayout.LINE_AXIS));
            whichSidesPanel.add(sideAandBButton);
            whichSidesPanel.add(sideAandCButton);
            whichSidesPanel.add(sideBandCButton);
            whichSidesPanel.add(sideABCButton);

            defaultSide3D = new JLabel("Default(Front) Side for 3-D cards:");
            defaultSide3D.setFont(new Font("serif",Font.ITALIC,14));
            sideAButton3D = new JRadioButton(stackInfo.stack.getSide1Title());
            sideAButton3D.setSelected(true);
            sideBButton3D = new JRadioButton(stackInfo.stack.getSide2Title());
            sideCButton3D = new JRadioButton(stackInfo.stack.getSide3Title());
            defaultSide3DButtonGroup = new ButtonGroup();
            defaultSide3DButtonGroup.add(sideAButton3D);
            defaultSide3DButtonGroup.add(sideBButton3D);
            defaultSide3DButtonGroup.add(sideCButton3D);
            buttonsPanel3D.setLayout(new BoxLayout(buttonsPanel3D,BoxLayout.LINE_AXIS));
            buttonsPanel3D.add(sideAButton3D);
            buttonsPanel3D.add(sideBButton3D);
            buttonsPanel3D.add(sideCButton3D);

            resetPanel = new JPanel();
            
            reset = new JButton("Reset " + stackInfo.stack.toString() + " Settings");
            reset.addActionListener(this);
            resetPanel.add(reset);
        }
		
        private void addComponents() {
            boolean has3DCards = stackHas3DCards(stackInfo.stack);

            setLayout(new GridLayout(has3DCards?8:4,1));
            add(stackTitleLabel);
            add(defaultSide2D);
            add(buttonsPanel2D);
            if(has3DCards) {
                add(which3DSidesToTestLabel);
                add(whichSidesPanel);
                add(defaultSide3D);
                add(buttonsPanel3D);
            }
            add(resetPanel);
        }

        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == sideAandBButton) {
                sideCButton3D.setEnabled(false);
                if(sideCButton3D.isSelected()) {
                    sideAButton3D.setSelected(true);
                }
                if(!sideAButton3D.isEnabled()) {
                    sideAButton3D.setEnabled(true);
                }
                if(!sideBButton3D.isEnabled()) {
                    sideBButton3D.setEnabled(true);
                }
            }
            else if(ae.getSource() == sideAandCButton) {
                sideBButton3D.setEnabled(false);
                if(sideBButton3D.isSelected()) {
                    sideAButton3D.setSelected(true);
                }
                if(!sideAButton3D.isEnabled()) {
                    sideAButton3D.setEnabled(true);
                }
                if(!sideCButton3D.isEnabled()) {
                    sideCButton3D.setEnabled(true);
                }
            }
            else if(ae.getSource() == sideBandCButton) {
                sideAButton3D.setEnabled(false);
                if(sideAButton3D.isSelected()) {
                    sideBButton3D.setSelected(true);
                }
                if(!sideBButton3D.isEnabled()) {
                    sideBButton3D.setEnabled(true);
                }
                if(!sideCButton3D.isEnabled()) {
                    sideCButton3D.setEnabled(true);
                }
            }
            else if(ae.getSource() == sideABCButton) {
                if(!sideAButton3D.isEnabled()) {
                    sideAButton3D.setEnabled(true);
                }
                if(!sideBButton3D.isEnabled()) {
                    sideBButton3D.setEnabled(true);
                }
                if(!sideCButton3D.isEnabled()) {
                    sideCButton3D.setEnabled(true);
                }
            }
            else if(ae.getSource() == reset) {
                sideAButton2D.setSelected(true);
                if(stackHas3DCards(stackInfo.stack)) {
                    sideAButton3D.setEnabled(true);
                    sideAButton3D.setSelected(true);
                    sideBButton3D.setEnabled(true);
                    sideCButton3D.setEnabled(true);
                    sideABCButton.setSelected(true);
                }
            }
        }
    }

    public SettingsFrame(NotecardStackReviewFrame owner) {
        super("Review Settings - Notecard Review Station");
        setIconImage(LanguageSuite.frameIcon);
        
        this.owner = owner;

        stackSettingsPanels = new ArrayList<StackSettingsPanel>();

        initializePracticeComponents();
        initializeShufflingComponents();
        initializeStackPanelComponents();
		
        listsPanel = new ListsPanel(owner);
        listsScrollPane = new JScrollPane(listsPanel);
        
        tabbedPane = new JTabbedPane();

        tabbedPane.add(listsScrollPane,"Select Notecards");
        tabbedPane.add(stackSettingsScrollPane,"Stack Default Settings");
        tabbedPane.add(practiceSettingsScrollPane,"Practice Settings");
        tabbedPane.add(shufflingSettingsScrollPane,"Multiple Stack Settings");        
        
        add(tabbedPane);
    }

    private void initializePracticeComponents() {        
        settingsLabel = new JLabel("Practice Mode Settings");
        settingsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        settingsLabel.setFont(new Font("Serif",Font.ITALIC,18));

        // want to stop the clock setting (general)
        wantToStopClockBox = new JCheckBox("Stop clock after each entry?");
        wantToStopClockBox.setSelected(false);
        stopClockTestDefaultText = new JLabel("       NOTE: In test mode, the clock does not stop.");

        // shuffling settings (general)
        wantToShuffleAtEndBox = new JCheckBox("Shuffle the notecards again after one pass completed?");
        wantToShuffleAtEndBox.setSelected(true);
        shuffleAtEndTestDefaultText = new JLabel("       NOTE: In test mode, the notecards are shuffled at the end.");
        shuffleAtEndNote = new JLabel("       The shuffling is determined by this next setting."); // make a tool tip?


        // skipping on to next card (general)
        wantToSkipWrongAnswersBox = new JCheckBox("If any entry is wrong, move on to next card?");
        wantToSkipWrongAnswersBox.setSelected(true);
        wantToSkipWrongAnswersBox.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               if(wantToSkipWrongAnswersBox.isSelected()) {
                   wantToClearAnswerBox.setSelected(true);
                   wantToClearAnswerBox.setEnabled(false);
               }
               else {
                   wantToClearAnswerBox.setEnabled(true);
               }
           }
        });
        skipWrongAnswersTestDefaultText = new JLabel("       NOTE: In test mode, if an answer is wrong, the session moves on and clears answers.");

        // clearing wrong answers if staying at this card (general)
        wantToClearAnswerBox = new JCheckBox("If not moving on to the next card, clear wrong entries?");
        wantToClearAnswerBox.setSelected(true);
        wantToClearAnswerBox.setEnabled(false);
        clearWrongAnswerTestDefaultText = new JLabel("       NOTE: In test mode, the answers are cleared after a wrong answer.");
        // if moves on to next card, will automatically clear - make sure this is clear to user

        caseSensitiveBox = new JCheckBox("Should checking inputted answers be case-sensitive?");
        caseSensitiveBox.setSelected(true);
        caseSensitivityTestDefaultText = new JLabel("       NOTE: In test mode, the answers are case-sensitive (A is not the same as a).");

        resetDefaults = new JButton("Reset Defaults");
        resetDefaults.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                resetDefaults();
            }
        });

        practiceSettingsPanel = new JPanel();
        practiceSettingsPanel.setLayout(new GridLayout(13,1));

        practiceSettingsPanel.add(settingsLabel);
        practiceSettingsPanel.add(wantToStopClockBox);
        practiceSettingsPanel.add(stopClockTestDefaultText);
        practiceSettingsPanel.add(wantToShuffleAtEndBox);
        practiceSettingsPanel.add(shuffleAtEndTestDefaultText);
        practiceSettingsPanel.add(shuffleAtEndNote);
        practiceSettingsPanel.add(wantToSkipWrongAnswersBox);
        practiceSettingsPanel.add(skipWrongAnswersTestDefaultText);
        practiceSettingsPanel.add(wantToClearAnswerBox);
        practiceSettingsPanel.add(clearWrongAnswerTestDefaultText);
        practiceSettingsPanel.add(caseSensitiveBox);
        practiceSettingsPanel.add(caseSensitivityTestDefaultText);
        practiceSettingsPanel.add(resetDefaults);

        practiceSettingsScrollPane = new JScrollPane(practiceSettingsPanel);
    }

    private void initializeShufflingComponents() {
        wantToMixMultipleStacksBox = new JCheckBox("When shuffling with multiple notecard stacks, interlace their cards?");
        mixMultipleStacksTestDefaultText = new JLabel("       NOTE: There is no default for test mode.");
        
        shufflingSettingsPanel = new JPanel();
        shufflingSettingsPanel.setLayout(new GridLayout(10,1));
        shufflingSettingsPanel.add(wantToMixMultipleStacksBox);
        shufflingSettingsPanel.add(mixMultipleStacksTestDefaultText);

        shufflingSettingsScrollPane = new JScrollPane(shufflingSettingsPanel);
    }

    private void initializeStackPanelComponents() {
        stackSettingsPanel = new JPanel();

        stackSettingsPanel.setLayout(new BoxLayout(stackSettingsPanel,BoxLayout.PAGE_AXIS));

        int numStacks = owner.getStacks().size();
        for(int i = 0; i < numStacks; i++) {
            StackSettingsPanel ssp = new StackSettingsPanel(owner.getStacksAndSettings().get(i));            
            stackSettingsPanels.add(ssp);
            stackSettingsPanel.add(ssp);
        }

        // special case for visual: if only one stack and doesn't have 3-D cards
        stackSettingsScrollPane = new JScrollPane(stackSettingsPanel);

        if(numStacks == 1) {
            boolean has3DCards = stackHas3DCards(owner.getStacksAndSettings().get(0).stack);
            if(!has3DCards) {
                stackSettingsPanels.get(0).setMinimumSize(new Dimension(570,144));
                stackSettingsPanels.get(0).setMaximumSize(new Dimension(570,144));
                stackSettingsPanels.get(0).setPreferredSize(new Dimension(570,144));
            }
        } 
    }

    private boolean stackHas3DCards(NotecardStack stack) {
        for(Notecard n : stack.getNotecards()) {
            if(n instanceof Notecard3D) {
                return true;
            }
        }
        return false;
    }

    // General Practice Mode Settings setters and getters:
    public boolean shouldStopClock() {
        return wantToStopClockBox.isSelected();
    }
	
    public void setShouldStopClock(boolean value) {
        wantToStopClockBox.setSelected(value);
    }

    public boolean shouldShuffleAtEnd() {
        return wantToShuffleAtEndBox.isSelected();
    }
	
    public void setShouldShuffleAtEnd(boolean value) {
        wantToShuffleAtEndBox.setSelected(value);
    }

    public boolean shouldMixMultipleDecks() {
        return wantToMixMultipleStacksBox.isSelected();
    }
	
    public void setShouldMixMultipleDecks(boolean value) {
        wantToMixMultipleStacksBox.setSelected(value);
    }

    public boolean shouldSkipWrongAnswers() {
        return wantToSkipWrongAnswersBox.isSelected();
    }
	
    public void setShouldSkipWrongAnswers(boolean value) {
        wantToSkipWrongAnswersBox.setSelected(value);
    }

    public boolean shouldClearAnswer() {
        return wantToClearAnswerBox.isSelected();
    }
	
    public void setShouldClearAnswer(boolean value) {
        wantToClearAnswerBox.setSelected(value);
    }

    public boolean shouldUseCaseSensitivity() {
        return caseSensitiveBox.isSelected();
    }

    public void setCaseSensitivity(boolean value) {
        caseSensitiveBox.setSelected(true);
    }

    public ListsPanel getListsPanel() {
        return listsPanel;
    }
    
    private void resetDefaults() {
        setShouldStopClock(false);
        setShouldShuffleAtEnd(true);
        setShouldSkipWrongAnswers(true);
        setShouldClearAnswer(true);
        setCaseSensitivity(true);
        wantToClearAnswerBox.setEnabled(false);
    }

    public void updateAllStackSettings() {
        for(StackSettingsPanel panel : stackSettingsPanels) {
            panel.stackInfo.set2DDefaultSide(panel.get2DDefaultSide());

            if(stackHas3DCards(panel.stackInfo.stack)) {
                panel.stackInfo.setSidesToTest(panel.get3DSidesToTest());
                panel.stackInfo.set3DDefaultSide(panel.get3DDefaultSide());
            }
        }
    }    
}

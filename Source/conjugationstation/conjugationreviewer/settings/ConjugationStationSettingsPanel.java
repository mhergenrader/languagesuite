package conjugationstation.conjugationreviewer.settings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

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
 * Swing panel for all settings for conjugation practice
 * @author Michael Hergenrader
 */
public class ConjugationStationSettingsPanel extends JPanel {

    private JCheckBox wantToSkipWrongAnswersBox;
    private JCheckBox wantToClearAnswerBox;
    private JCheckBox wantToStopClockBox;
    private JCheckBox wantToShuffleAtEndBox;
    private JCheckBox wantToCaseSensitiveBox;

    private JLabel practiceTitleLabel;

    private JButton resetDefault;

    public ConjugationStationSettingsPanel() {
        practiceTitleLabel = new JLabel("Practice Settings");
        practiceTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        practiceTitleLabel.setFont(new Font("Serif",Font.ITALIC,18));

        wantToSkipWrongAnswersBox = new JCheckBox("If entry is wrong, move on to next verb.");
        wantToSkipWrongAnswersBox.setSelected(true);
        wantToSkipWrongAnswersBox.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) { // this avoids the problem of having skip checked and clear not checked
               if(wantToSkipWrongAnswersBox.isSelected()) {
                   wantToClearAnswerBox.setSelected(true);
                   wantToClearAnswerBox.setEnabled(false);
               }
               else {
                   wantToClearAnswerBox.setEnabled(true);
               }
           }
        });
		
        wantToClearAnswerBox = new JCheckBox("Clear entry after a wrong answer");
        wantToClearAnswerBox.setSelected(true);
        wantToClearAnswerBox.setEnabled(false); // this is because it must be selected if the skip button is selected
        wantToStopClockBox = new JCheckBox("Stop clock at each entry");
        wantToStopClockBox.setSelected(false);
        wantToShuffleAtEndBox = new JCheckBox("After all verbs presented, reshuffle");
        wantToShuffleAtEndBox.setSelected(true);
        wantToCaseSensitiveBox = new JCheckBox("Should answers be checked on a case-sensitive basis?");
        wantToCaseSensitiveBox.setSelected(true);

        resetDefault = new JButton("Reset Defaults");
        resetDefault.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                wantToSkipWrongAnswersBox.setSelected(true);
                wantToClearAnswerBox.setSelected(true);
                wantToStopClockBox.setSelected(false);
                wantToShuffleAtEndBox.setSelected(true);
                wantToCaseSensitiveBox.setSelected(true);
            }
        });

        setLayout(new GridLayout(14,1));

        add(practiceTitleLabel);
        add(new JLabel(""));
        add(wantToSkipWrongAnswersBox);
        add(new JLabel("       NOTE: In test mode, if an answer is wrong, the next verb will appear."));
        add(wantToClearAnswerBox);
        add(new JLabel("       NOTE: In test mode, an incorrect answer is cleared (set moves on)."));
        add(wantToStopClockBox);
        add(new JLabel("       NOTE: In test mode, the clock does not stop."));
        add(wantToShuffleAtEndBox);
        add(new JLabel("       NOTE: In test mode, the verbs are reshuffled after each pass."));
        add(wantToCaseSensitiveBox);
        add(new JLabel("       NOTE: In test mode, case sensitivity is used. (A is not the same as a)"));
        add(new JLabel(""));
        add(resetDefault);
    }

    public JCheckBox getSkipWrongAnswersBox() {
        return wantToSkipWrongAnswersBox;
    }
    public JCheckBox getClearAnswerBox() {
        return wantToClearAnswerBox;
    }
    public JCheckBox getStopClockBox() {
        return wantToStopClockBox;
    }
    public JCheckBox getShuffleAtEndBox() {
        return wantToShuffleAtEndBox;
    }
    public JCheckBox getCaseSensitivityBox() {
        return wantToCaseSensitiveBox;
    }
}
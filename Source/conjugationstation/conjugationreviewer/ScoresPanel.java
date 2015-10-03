package conjugationstation.conjugationreviewer;

import javax.swing.*;
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
 * Panel that keeps running tally of the current correct/incorrect answers
 * @author Michael Hergenrader
 */
public class ScoresPanel extends JPanel {

    private ConjugationReviewFrame owner;

    private JLabel modeLabel;

    private JLabel correctLabel;
    private JLabel incorrectLabel;
    private JLabel percentageLabel;

    private int numCorrect;
    private int numIncorrect;
    private int percentage;
    
    public ScoresPanel(ConjugationReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(120,100));
        setMaximumSize(new Dimension(120,100));
        setPreferredSize(new Dimension(120,100));
        setBackground(Color.DARK_GRAY.brighter());

        numCorrect = 0;
        numIncorrect = 0;
        percentage = 0;

        modeLabel = new JLabel();
        modeLabel.setForeground(Color.WHITE);
        modeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        modeLabel.setFont(new Font("serif",Font.BOLD,16));
        modeLabel.setVisible(false);

        correctLabel = new JLabel("Correct: 0");
        correctLabel.setForeground(Color.WHITE);
        incorrectLabel = new JLabel("Incorrect: 0");
        incorrectLabel.setForeground(Color.WHITE);
        percentageLabel = new JLabel("Percentage: 0%");
        percentageLabel.setForeground(Color.WHITE);

        setLayout(new GridLayout(6,1));
        add(modeLabel);
        add(new JLabel(""));
        add(correctLabel);
        add(incorrectLabel);
        add(percentageLabel);
        add(new JLabel(""));
    }

    public void setModeLabel(String text) {
        modeLabel.setText(text);
    }

    public void showModeLabel(boolean value) {
        modeLabel.setVisible(value);
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public int getNumIncorrect() {
        return numIncorrect;
    }

    public int getPercentage() {
        return percentage;
    }

    public void addCorrectAnswer() {
        numCorrect++;
    }

    public void addIncorrectAnswer() {
        numIncorrect++;
    }

    public void resetScores() {
        numCorrect = 0;
        numIncorrect = 0;
        percentage = 0;
        updateScores();
    }

    public void updateScores() {
        if(numCorrect+numIncorrect != 0) {
            percentage = ((int)(((double)numCorrect/((double)numCorrect+(double)numIncorrect))*100));
        }
        else {
            percentage = 0;
        }
        correctLabel.setText("Correct: " + numCorrect);
        incorrectLabel.setText("Incorrect: " + numIncorrect);
        percentageLabel.setText("Percentage: " + percentage + "%");
    }
}

package notecardstation.notecardreviewer.general;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;
import notecardstation.notecardreviewer.general.NotecardStackReviewFrame.ReviewMode;

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
 * Panel that displays current statistics for correct and incorrect notecard answers
 * @author Michael Hergenrader
 */
public class StatsPanel extends JPanel {

    private NotecardStackReviewFrame owner;

    private JLabel reviewModeLabel;
    private JLabel stackNameLabel;
    private JLabel currentCardIndexLabel;

    private JLabel correctLabel;
    private JLabel incorrectLabel;
    private JLabel percentageLabel;

    private int numCorrect;
    private int numIncorrect;
    private int percentage;

    private static final Dimension statsPanelDimension = new Dimension(120,160);

    public StatsPanel(NotecardStackReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(statsPanelDimension);
        setMaximumSize(statsPanelDimension);
        setPreferredSize(statsPanelDimension);
        setBackground(Color.DARK_GRAY.brighter());

        numCorrect = 0;
        numIncorrect = 0;
        percentage = 0;

        reviewModeLabel = new JLabel();
        reviewModeLabel.setForeground(Color.WHITE);
        reviewModeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        reviewModeLabel.setFont(new Font("serif",Font.BOLD,16));
        stackNameLabel = new JLabel();
        stackNameLabel.setForeground(Color.WHITE);
        stackNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        stackNameLabel.setFont(new Font("serif",Font.ITALIC | Font.BOLD,14));
        currentCardIndexLabel = new JLabel();
        currentCardIndexLabel.setForeground(Color.WHITE);
        currentCardIndexLabel.setHorizontalAlignment(SwingConstants.CENTER);
        currentCardIndexLabel.setFont(new Font("serif",Font.ITALIC | Font.BOLD,14));
        correctLabel = new JLabel("Correct: 0");
        correctLabel.setForeground(Color.WHITE);
        incorrectLabel = new JLabel("Incorrect: 0");
        incorrectLabel.setForeground(Color.WHITE);
        percentageLabel = new JLabel("Percentage: 0%");
        percentageLabel.setForeground(Color.WHITE);

        setLayout(new GridLayout(10,1));
        add(reviewModeLabel);
        add(new JLabel(""));
        add(stackNameLabel);
        add(currentCardIndexLabel);
        add(new JLabel(""));
        add(correctLabel);
        add(incorrectLabel);
        add(percentageLabel);
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

    public void addCorrectAnswer(int numPoints) {
        numCorrect += numPoints;
    }
	
    public void addIncorrectAnswer() {
        numIncorrect++;
    }

    public void addIncorrectAnswer(int numPoints) {
        numIncorrect += numPoints;
    }

    public void showStats(boolean value) {
        correctLabel.setVisible(value);
        incorrectLabel.setVisible(value);
        percentageLabel.setVisible(value);
    }

    public void setReviewModeLabel(ReviewMode reviewMode) {
        if(reviewMode == null) {
            reviewModeLabel.setText("");
            return;
        }
        switch(reviewMode) {
            case FLIP_MODE:
                reviewModeLabel.setText("Flip cards");
                break;
            case PRACTICE_MODE:
                reviewModeLabel.setText("Practice");
                break;
            case TEST_MODE:
                reviewModeLabel.setText("Test");
                break;
        }
    }

    public void setStackTitleLabel(String text) {
        stackNameLabel.setText(text);
    }

    public void setCurrentCardIndexLabel(String text) {
        currentCardIndexLabel.setText(text);
    }

    public void resetScores() {
        numCorrect = 0;
        numIncorrect = 0;
        percentage = 0;
        updateVisualScores();
    }

    public void updateVisualScores() {
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

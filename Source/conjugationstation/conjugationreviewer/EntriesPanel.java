package conjugationstation.conjugationreviewer;

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
 * Main panel in conjugation practice in which user supplies his/her answer
 * @author Michael Hergenrader
 */
public class EntriesPanel extends JPanel {

    private ConjugationReviewFrame owner;

    private JButton enterButton;
    private JButton addAccentButton;

    private JLabel lSetLabel;
    private JLabel rSetLabel;
    private JLabel lPronounLabel;
    private JLabel rPronounLabel;
    private JLabel lVerbLabel;
    private JLabel rVerbLabel;
    private JLabel answerLabel;
    private JTextField answerEntry;

    public EntriesPanel(ConjugationReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(300,150));
        setMaximumSize(new Dimension(300,150));
        setPreferredSize(new Dimension(300,150));
        setBackground(Color.DARK_GRAY.brighter());

        enterButton = new JButton("Enter");
        enterButton.addActionListener(owner);
        enterButton.setEnabled(false);
        addAccentButton = new JButton("Add Accent");
        addAccentButton.addActionListener(owner);
        addAccentButton.setEnabled(false);

        setupLabels();
        answerEntry = new JTextField(20);
        answerEntry.setEnabled(false);

        setBorder(BorderFactory.createTitledBorder(""));

        setupLayout();
    }

    private void setupLabels() {
        lSetLabel = new JLabel("Verb Set  ");
        lSetLabel.setFont(new Font("Serif",Font.ITALIC,14));
        lSetLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        lSetLabel.setForeground(Color.WHITE);
        
        rSetLabel = new JLabel("");
        rSetLabel.setForeground(Color.WHITE);

        lPronounLabel = new JLabel("Pronoun  ");
        lPronounLabel.setFont(new Font("Serif",Font.ITALIC,14));
        lPronounLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        lPronounLabel.setForeground(Color.WHITE);

        rPronounLabel = new JLabel("");
        rPronounLabel.setForeground(Color.WHITE);

        lVerbLabel = new JLabel("Verb  ");
        lVerbLabel.setFont(new Font("Serif",Font.ITALIC,14));
        lVerbLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        lVerbLabel.setForeground(Color.WHITE);
        
        rVerbLabel = new JLabel("");
        rVerbLabel.setForeground(Color.WHITE);
        
        answerLabel = new JLabel("Answer  ");
        answerLabel.setFont(new Font("Serif",Font.ITALIC,14));
        answerLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        answerLabel.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        setLayout(new GridLayout(5,2));

        add(lSetLabel);
        add(rSetLabel);

        add(lPronounLabel);
        add(rPronounLabel);

        add(lVerbLabel);
        add(rVerbLabel);

        add(answerLabel);
        add(answerEntry);

        add(enterButton);
        add(addAccentButton);
    }

    public void showRLabels() {
        rSetLabel.setVisible(true);
        rPronounLabel.setVisible(true);
        rVerbLabel.setVisible(true);
    }

    public void hideRLabels() {
        rSetLabel.setVisible(false);
        rPronounLabel.setVisible(false);
        rVerbLabel.setVisible(false);
    }

    public JLabel getRightSetLabel() {
        return rSetLabel;
    }

    public JLabel getRightPronounLabel() {
        return rPronounLabel;
    }

    public JLabel getRightVerbLabel() {
        return rVerbLabel;
    }

    public JTextField getUserAnswerField() {
        return answerEntry;
    }

    public String getUserAnswer() {
        return answerEntry.getText();
    }

    public JButton getEnterButton() {
        return enterButton;
    }

    public JButton getAddAccentButton() {
        return addAccentButton;
    }

    class ConfirmAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if(owner.isRunning()) {
                owner.confirmAnswer();
            }
        }
    }
}

package notecardstation.notecardreviewer.general;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.BevelBorder;

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
 * Notecard view panel that shows one side of text
 * @author Michael Hergenrader
 */
public class NotecardPanel extends JPanel {
    private JLabel sideTitle;
    private JTextField sideText;
    private NotecardStackReviewFrame owner;

    private JPanel titlePanel;
    private JPanel cardPanel;

    public NotecardPanel(NotecardStackReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(400,220));
        setMaximumSize(new Dimension(400,220));
        setPreferredSize(new Dimension(400,220));
        setBackground(Color.DARK_GRAY.brighter());
        
        setupTitlePanel();
        setupCardPanel();
        setupLayout();
    }

    public NotecardPanel(NotecardStackReviewFrame owner, String initialText) {
        this(owner);
        sideText.setText(initialText);
    }

    private void setupTitlePanel() {
        titlePanel = new JPanel();
        titlePanel.setMinimumSize(new Dimension(400,20));
        titlePanel.setMaximumSize(new Dimension(400,20));
        titlePanel.setPreferredSize(new Dimension(400,20));
        titlePanel.setBackground(Color.DARK_GRAY.brighter());

        sideTitle = new JLabel();
        sideTitle.setOpaque(false);
        sideTitle.setBackground(Color.DARK_GRAY.brighter());
        sideTitle.setForeground(Color.WHITE);
        sideTitle.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(sideTitle);
    }

    private void setupCardPanel() {
        cardPanel = new JPanel();
        cardPanel.setMinimumSize(new Dimension(400,200));
        cardPanel.setMaximumSize(new Dimension(400,200));
        cardPanel.setPreferredSize(new Dimension(400,200));

        cardPanel.setLayout(new BorderLayout());
        sideText = new JTextField(20);
        sideText.setEditable(false);
        sideText.setBackground(Color.WHITE);
        sideText.setHorizontalAlignment(SwingConstants.CENTER);
        sideText.setBorder(BorderFactory.createCompoundBorder(new BevelBorder(BevelBorder.RAISED),new BevelBorder(BevelBorder.LOWERED)));

        setBackground(Color.DARK_GRAY.brighter());

        cardPanel.add(sideText,BorderLayout.CENTER);
    }

    private void setupLayout() {
        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(titlePanel);
        add(cardPanel);

        sl.putConstraint(SpringLayout.NORTH,titlePanel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,titlePanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,cardPanel,0,SpringLayout.SOUTH,titlePanel);
        sl.putConstraint(SpringLayout.WEST,cardPanel,0,SpringLayout.WEST,this);
    }    

    public String getNotecardText() {
        return sideText.getText();
    }

    public void setNotecardText(String text) {
        sideText.setText(text);
    }

    public String getSideTitleText() {
        return sideTitle.getText();
    }

    public void setTitleText(String text) {
        sideTitle.setText(text);
    }

    public void showTitleText(boolean value) {
        sideTitle.setVisible(value);
    }

    public void showNotecardText(boolean value) {
        sideText.setVisible(value);
    }
}

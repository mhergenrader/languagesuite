package notecardstation.notecardeditor;

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
 * Panel for editing the side titles and language for a new notecard stack
 * @author Michael Hergenrader
 */
public class StackSideTitlesPanel extends JPanel implements KeyListener {

    private NotecardStackEditorFrame owner;

    private JLabel language;
    private JLabel side1Title;
    private JLabel side2Title;
    private JLabel side3Title;

    private JTextField languageEntry;
    private JTextField side1TitleEntry;
    private JTextField side2TitleEntry;
    private JTextField side3TitleEntry;

    private GridLayout gridLayout;

    public StackSideTitlesPanel(NotecardStackEditorFrame owner) {

        this.owner = owner;

        setBackground(Color.DARK_GRAY.brighter());

        setMinimumSize(new Dimension(250,100));
        setPreferredSize(new Dimension(250,100));
        setMaximumSize(new Dimension(250,100));

        initializeLabels();
        initializeTextFields();
        setupLayout();
    }

    private void setupLayout() {
        gridLayout = new GridLayout(4,2);
        gridLayout.setVgap(4);
        setLayout(gridLayout);
        
        add(language);
        add(languageEntry);

        add(side1Title);
        add(side1TitleEntry);

        add(side2Title);
        add(side2TitleEntry);

        add(side3Title);
        add(side3TitleEntry);
    }

    public void addComponentsToOrder() {
        owner.getComponentVector().add(languageEntry);
        owner.getComponentVector().add(side1TitleEntry);
        owner.getComponentVector().add(side2TitleEntry);
        owner.getComponentVector().add(side3TitleEntry);
    }

    private void initializeLabels() {
        language = new JLabel("Language(s)");
        language.setForeground(Color.WHITE);
        side1Title = new JLabel("Side 1 Title");
        side1Title.setForeground(Color.WHITE);
        side2Title = new JLabel("Side 2 Title");
        side2Title.setForeground(Color.WHITE);
        side3Title = new JLabel("Side 3 Title");
        side3Title.setForeground(Color.WHITE);
    }

    private void initializeTextFields() {
        languageEntry = new JTextField(20);
        languageEntry.addFocusListener(owner);
        languageEntry.getDocument().addDocumentListener(owner);
        side1TitleEntry = new JTextField("Side 1",20);
        side1TitleEntry.addKeyListener(this);
        side1TitleEntry.addFocusListener(owner);
        side1TitleEntry.getDocument().addDocumentListener(owner);
        side2TitleEntry = new JTextField("Side 2",20);
        side2TitleEntry.addKeyListener(this);
        side2TitleEntry.addFocusListener(owner);
        side2TitleEntry.getDocument().addDocumentListener(owner);
        side3TitleEntry = new JTextField("Side 3",20);
        side3TitleEntry.addKeyListener(this);
        side3TitleEntry.addFocusListener(owner);
        side3TitleEntry.getDocument().addDocumentListener(owner); 
    }

    public JTextField getLanguageField() {
        return languageEntry;
    }
	
    public JTextField getSide1TitleField() {
        return side1TitleEntry;
    }
	
    public JTextField getSide2TitleField() {
        return side2TitleEntry;
    }
	
    public JTextField getSide3TitleField() {
        return side3TitleEntry;
    }

    public String getLanguage() {
        return languageEntry.getText();
    }

    public String getSide1Title() {
        return side1TitleEntry.getText();
    }

    public String getSide2Title() {
        return side2TitleEntry.getText();
    }

    public String getSide3Title() {
        return side3TitleEntry.getText();
    }
    
    public void setLanguage(String text) {
        languageEntry.setText(text);
    }

    public void setSide1Title(String text) {
        side1TitleEntry.setText(text);
    }

    public void setSide2Title(String text) {
        side2TitleEntry.setText(text);
    }

    public void setSide3Title(String text) {
        side3TitleEntry.setText(text);
    }

    public void keyPressed(KeyEvent e) {
    }
	
    public void keyReleased(KeyEvent e) {
        //owner.setStackModified(true);
        if(e.getSource() == side1TitleEntry) {
            owner.getNotecardDataPanel().setSide1LabelText(side1TitleEntry.getText());
        }
        else if(e.getSource() == side2TitleEntry) {
            owner.getNotecardDataPanel().setSide2LabelText(side2TitleEntry.getText());
        }
        if(e.getSource() == side3TitleEntry) {
            owner.getNotecardDataPanel().setSide3LabelText(side3TitleEntry.getText());
        }
    }
	
    public void keyTyped(KeyEvent e) {
        if(e.getSource() == side1TitleEntry) {
            owner.getNotecardDataPanel().setSide1LabelText(side1TitleEntry.getText());
        }
        else if(e.getSource() == side2TitleEntry) {
            owner.getNotecardDataPanel().setSide2LabelText(side2TitleEntry.getText());
        }
        if(e.getSource() == side3TitleEntry) {
            owner.getNotecardDataPanel().setSide3LabelText(side3TitleEntry.getText());
        }
    }
}

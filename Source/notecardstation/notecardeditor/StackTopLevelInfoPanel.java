package notecardstation.notecardeditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
 * Panel for inputting metadata about a new notecard stack
 * @author Michael Hergenrader
 */
public class StackTopLevelInfoPanel extends JPanel {

    private JLabel stackTitle;
    private JLabel author;

    private JLabel versionNumber;
    private JLabel lastModifiedLLabel;

    private JTextField stackTitleEntry;
    private JTextField authorEntry;

    private JTextField versionNumberEntry;
    private JLabel lastModifiedRLabel;

    private GridLayout gridLayout;

    private NotecardStackEditorFrame owner;

    public StackTopLevelInfoPanel(NotecardStackEditorFrame owner) {
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

        add(stackTitle);
        add(stackTitleEntry);

        add(author);
        add(authorEntry);

        add(versionNumber);
        add(versionNumberEntry);

        add(lastModifiedLLabel);
        add(lastModifiedRLabel);
    }

    public void addComponentsToOrder() {
        owner.getComponentVector().add(stackTitleEntry);
        owner.getComponentVector().add(authorEntry);
        owner.getComponentVector().add(versionNumberEntry);
    }

    private void initializeLabels() {
        stackTitle = new JLabel("Stack Title");
        stackTitle.setForeground(Color.WHITE);
        author = new JLabel("Author");
        author.setForeground(Color.WHITE);
        versionNumber = new JLabel("Version Number");
        versionNumber.setForeground(Color.WHITE);
        lastModifiedLLabel = new JLabel("Last Modified");
        lastModifiedLLabel.setForeground(Color.WHITE);
        lastModifiedRLabel = new JLabel(owner.getNotecardStack().getLastModified());
        lastModifiedRLabel.setForeground(Color.WHITE);
    }

    private void initializeTextFields() {
        stackTitleEntry = new JTextField(20);
        stackTitleEntry.addFocusListener(owner);
        stackTitleEntry.getDocument().addDocumentListener(owner);
        authorEntry = new JTextField(20);
        authorEntry.addFocusListener(owner);
        authorEntry.getDocument().addDocumentListener(owner);
        versionNumberEntry = new JTextField(20);
        versionNumberEntry.addFocusListener(owner);
        versionNumberEntry.getDocument().addDocumentListener(owner);
    }

    public JTextField getStackTitleEntry() {
        return stackTitleEntry;
    }

    public String getStackTitle() {
        return stackTitleEntry.getText();
    }
	
    public void setStackTitle(String text) {
        stackTitleEntry.setText(text);
    }
    
    public String getAuthor() {
        return authorEntry.getText();
    }
	
    public void setAuthor(String text) {
        authorEntry.setText(text);
    }

    public String getVersionNumber() {
        return versionNumberEntry.getText();
    }
	
    public void setVersionNumber(String text) {
        versionNumberEntry.setText(text);
    }

    public String getLastModified() {
        return lastModifiedRLabel.getText();
    }
	
    public void setLastModified(String text) {
        lastModifiedRLabel.setText(text);
    }
	
    public void updateLastModified() {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy    HH:mm:ss");
        Date date = new Date();
        String lastModified = dateFormat.format(date);
        lastModifiedRLabel.setText(lastModified);
    }
}

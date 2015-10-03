package notecardstation.general;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.text.*;
import javax.swing.event.*;
import javax.swing.tree.DefaultMutableTreeNode;

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
 * Panel that provides user with means to create new notecard stacks
 * @author Michael Hergenrader
 */
public class NewStackPanel extends JPanel implements DocumentListener {

    private NotecardStation owner;

    private JLabel newStackLabel;

    private JLabel titleLabel;
    private JTextField titleEntry;
    private JLabel authorLabel;
    private JTextField authorEntry;

    private JLabel versionL;
    private JLabel createDate;

    private JButton enterButton;
    private JButton cancelButton;

    private String dateCreated;

    private JLabel titleErrorMessage;
    private JLabel authorErrorMessage;

    private JPanel entriesPanel;

    public NewStackPanel(NotecardStation owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(356,300));
        setMaximumSize(new Dimension(356,300));
        setPreferredSize(new Dimension(356,300));
        
        setupComponents();
    }

    private void setupComponents() {
        newStackLabel = new JLabel("Create New Notecard Stack");
        newStackLabel.setFont(new Font("serif",Font.ITALIC | Font.BOLD,18));
        newStackLabel.setHorizontalAlignment(SwingConstants.CENTER);

        entriesPanel = new JPanel();
        entriesPanel.setMinimumSize(new Dimension(350,190));
        entriesPanel.setMaximumSize(new Dimension(350,190));
        entriesPanel.setPreferredSize(new Dimension(350,190));

        AddStackAction pressedAction = new AddStackAction(); // allows for a keyboard shortcut for enter key when user has completed entering the name (alternative to the button)
        
        titleLabel = new JLabel("Enter Stack Title");
        titleLabel.setFont(new Font("serif",Font.ITALIC,14));
        
        titleEntry = new JTextField(20);
        titleEntry.getDocument().addDocumentListener(this);
        titleEntry.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        titleEntry.getActionMap().put("pressed",pressedAction);
        titleEntry.setAction(pressedAction);

        authorLabel = new JLabel("Enter Author");
        authorLabel.setFont(new Font("serif",Font.ITALIC,14));
        
        authorEntry = new JTextField(20);
        authorEntry.getDocument().addDocumentListener(this);
        authorEntry.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        authorEntry.getActionMap().put("pressed",pressedAction);
        authorEntry.setAction(pressedAction); // establish enter shortcut so don't have to use the enter button

        versionL = new JLabel("Version 1.0");
        versionL.setFont(new Font("serif",Font.ITALIC,14));

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy    HH:mm:ss");
        Date date = new Date();
        dateCreated = dateFormat.format(date);
        createDate = new JLabel("Created: " + dateCreated);

        enterButton = new JButton("Create");
        enterButton.addActionListener(owner);
        
        enterButton.setEnabled(false);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(owner);

        titleErrorMessage = new JLabel("Please enter a stack title.");
        titleErrorMessage.setForeground(Color.GREEN);

        authorErrorMessage = new JLabel("Please enter an author name for this stack.");
        authorErrorMessage.setForeground(Color.GREEN);

        entriesPanel.setLayout(new GridLayout(6,2));
        entriesPanel.add(titleLabel);
        entriesPanel.add(titleEntry);
        entriesPanel.add(authorLabel);
        entriesPanel.add(authorEntry);
        entriesPanel.add(versionL);
        entriesPanel.add(new JLabel(""));
        entriesPanel.add(createDate);
        entriesPanel.add(new JLabel(""));
        entriesPanel.add(enterButton);
        entriesPanel.add(cancelButton);

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(newStackLabel);
        add(entriesPanel);
        add(titleErrorMessage);
        add(authorErrorMessage);

        sl.putConstraint(SpringLayout.NORTH,newStackLabel,10,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,newStackLabel,74,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,entriesPanel,10,SpringLayout.SOUTH,newStackLabel);
        sl.putConstraint(SpringLayout.WEST,entriesPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,titleErrorMessage,0,SpringLayout.SOUTH,entriesPanel);
        sl.putConstraint(SpringLayout.WEST,titleErrorMessage,10,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,authorErrorMessage,10,SpringLayout.SOUTH,titleErrorMessage);
        sl.putConstraint(SpringLayout.WEST,authorErrorMessage,10,SpringLayout.WEST,this);
    }

    class AddStackAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if(canContinueTitle && canContinueAuthor) {
                owner.createNewStack();
            }
        }
    }

    public JButton getEnterButton() {
        return enterButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public void clearTitleEntry() {
        titleEntry.setText("");
    }

    public void requestTitleFocus() {
        titleEntry.requestFocusInWindow();
    }

    public void clearAuthorEntry() {
        authorEntry.setText("");
    }

    public String getTitle() {
        return titleEntry.getText();
    }

    public String getAuthor() {
        return authorEntry.getText();
    }

    public String getDateCreated() {
        return dateCreated;
    }

    private boolean canContinueTitle = false;
    private boolean canContinueAuthor = false;

    public void insertUpdate(DocumentEvent e) {
        if(e.getDocument() == titleEntry.getDocument()) {
            if(notecardStackAlreadyExists(titleEntry.getText().trim())) {
                canContinueTitle = false;
                titleErrorMessage.setText("Cannot continue! Notecard stack already exists.");
                titleErrorMessage.setForeground(Color.RED);
            }
            else {
                if(titleEntry.getText().trim().isEmpty()) {
                    titleErrorMessage.setText("Please enter a stack title.");
                    titleErrorMessage.setForeground(Color.GREEN);
                    canContinueTitle = false;
                }
                else {
                    titleErrorMessage.setText("");
                    canContinueTitle = true;
                }
            }
        }
        else if(e.getDocument() == authorEntry.getDocument()) {
            if(authorEntry.getText().trim().isEmpty()) {
                authorErrorMessage.setText("Please enter an author name for this stack.");
                authorErrorMessage.setForeground(Color.GREEN);
                canContinueAuthor = false;
            }
            else {
                authorErrorMessage.setText("");
                canContinueAuthor = true;
            }
        }

        enterButton.setEnabled(canContinueTitle && canContinueAuthor);
    }
    public void changedUpdate(DocumentEvent e) {
        if(e.getDocument() == titleEntry.getDocument()) {
            if(notecardStackAlreadyExists(titleEntry.getText().trim())) {
                canContinueTitle = false;
                titleErrorMessage.setText("Cannot continue! Notecard stack already exists.");
                titleErrorMessage.setForeground(Color.RED);
            }
            else {
                if(titleEntry.getText().trim().isEmpty()) {
                    titleErrorMessage.setText("Please enter a stack title.");
                    titleErrorMessage.setForeground(Color.GREEN);
                    canContinueTitle = false;
                }
                else {
                    titleErrorMessage.setText("");
                    canContinueTitle = true;
                }
            }
        }
        else if(e.getDocument() == authorEntry.getDocument()) {
            if(authorEntry.getText().trim().isEmpty()) {
                authorErrorMessage.setText("Please enter an author name for this stack.");
                authorErrorMessage.setForeground(Color.GREEN);
                canContinueAuthor = false;
            }
            else {
                authorErrorMessage.setText("");
                canContinueAuthor = true;
            }
        }

        enterButton.setEnabled(canContinueTitle && canContinueAuthor);
    }
    public void removeUpdate(DocumentEvent e) {
        if(e.getDocument() == titleEntry.getDocument()) {
            if(notecardStackAlreadyExists(titleEntry.getText().trim())) {
                canContinueTitle = false;
                titleErrorMessage.setText("Cannot continue! Notecard stack already exists.");
                titleErrorMessage.setForeground(Color.RED);
            }
            else {
                if(titleEntry.getText().trim().isEmpty()) {
                    titleErrorMessage.setText("Please enter a stack title.");
                    titleErrorMessage.setForeground(Color.GREEN);
                    canContinueTitle = false;
                }
                else {
                    titleErrorMessage.setText("");
                    canContinueTitle = true;
                }
            }
        }
        else if(e.getDocument() == authorEntry.getDocument()) {
            if(authorEntry.getText().trim().isEmpty()) {
                authorErrorMessage.setText("Please enter an author name for this stack.");
                authorErrorMessage.setForeground(Color.GREEN);
                canContinueAuthor = false;
            }
            else {
                authorErrorMessage.setText("");
                canContinueAuthor = true;
            }
        }

        enterButton.setEnabled(canContinueTitle && canContinueAuthor);
    }

    public void resetPanel() {
        titleEntry.setText("");
        authorEntry.setText("");

        titleErrorMessage.setText("Please enter a stack title.");
        titleErrorMessage.setForeground(Color.GREEN);

        authorErrorMessage.setText("Please enter an author name for this stack.");
        authorErrorMessage.setForeground(Color.GREEN);

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy    HH:mm:ss");
        Date date = new Date();
        dateCreated = dateFormat.format(date);
        createDate.setText("Created: " + dateCreated);

        canContinueTitle = false;
        canContinueAuthor = false;
        enterButton.setEnabled(false);
    }

    private boolean notecardStackAlreadyExists(String text) {

        DefaultMutableTreeNode root = owner.getStacksTreePanel().getRootNode();

        for(int i = 0; i < root.getChildCount(); i++) {
            if(((NotecardStation.NotecardStackTreeObject)(((DefaultMutableTreeNode)owner.getStacksTreePanel().getTreeModel().getChild(root,i)).getUserObject())).getNotecardStack().toString().equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

}

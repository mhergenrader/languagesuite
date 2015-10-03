package conjugationstation;

import conjugationstation.conjugationcomponents.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
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
 * Main panel for creating new conjugation sets for practice: start with metadata and the available subject pronouns
 * @author Michael Hergenrader
 */
public class NewConjugationSetPanel extends JPanel implements DocumentListener {

    private ConjugationStation owner;

    private JLabel newSetLabel;

    private JLabel nameLabel;
    private JTextField setNameField;

    private JLabel authorLabel;
    private JTextField setAuthorField;

    private JLabel languageLabel;
    
    private JButton confirmButton;
    private JButton cancelButton;

    private JPanel entryPanel;
    private JPanel languagePanel;
    private JPanel buttonsPanel;

    private JLabel languageAuthorLabel;
    private JLabel languageCreateDateLabel;
    private JLabel subjectPronounsLabel;

    private JLabel errorMessageLabel;

    private JTextArea subjectPronounsText;
    private JScrollPane scrollPane;

    private Language language;

    private boolean titleApproved = false;
    private boolean authorApproved = false;

    public NewConjugationSetPanel(ConjugationStation owner) {
        this.owner = owner;

        newSetLabel = new JLabel("Create New Conjugation Set");
        newSetLabel.setFont(new Font("serif",Font.ITALIC | Font.BOLD,18));
        newSetLabel.setHorizontalAlignment(SwingConstants.CENTER);

        nameLabel = new JLabel("            Set Title");
        nameLabel.setFont(new Font("sans serif",Font.ITALIC,14));
        setNameField = new JTextField(20);
        setNameField.getDocument().addDocumentListener(this);

        authorLabel = new JLabel("            Author");
        authorLabel.setFont(new Font("sans serif",Font.ITALIC,14));
        setAuthorField = new JTextField(20);
        setAuthorField.getDocument().addDocumentListener(this);
        
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(owner);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(owner);

        AddAction pressedAction = new AddAction(); // allows for a keyboard shortcut for enter key when user has completed entering the name (alternative to the button)
        setNameField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        setNameField.getActionMap().put("pressed",pressedAction);
        setNameField.setAction(pressedAction);
        setAuthorField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        setAuthorField.getActionMap().put("pressed",pressedAction);
        setAuthorField.setAction(pressedAction);

        languageLabel = new JLabel();
        languageLabel.setFont(new Font("serif",Font.ITALIC,14));
        languageAuthorLabel = new JLabel();
        languageAuthorLabel.setFont(new Font("Serif",Font.ITALIC,14));
        languageCreateDateLabel = new JLabel();
        languageCreateDateLabel.setFont(new Font("Serif",Font.ITALIC,14));
        subjectPronounsLabel = new JLabel("Subject Pronouns:");
        subjectPronounsLabel.setFont(new Font("Serif",Font.ITALIC,14));
        subjectPronounsText = new JTextArea(1,20);
        subjectPronounsText.setEditable(false);
        scrollPane = new JScrollPane(subjectPronounsText,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        errorMessageLabel = new JLabel();
        errorMessageLabel.setForeground(Color.RED);
        errorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        scrollPane.setMinimumSize(new Dimension(350,40));
        scrollPane.setMaximumSize(new Dimension(350,40));
        scrollPane.setPreferredSize(new Dimension(350,40));

        setupPanels();
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    public JTextField getSetNameField() {
        return setNameField;
    }

    public void setLanguage(Language l) {
        language = l;

        languageLabel.setText("Language: " + language.toString());
        languageAuthorLabel.setText("Author: " + language.getAuthor());
        languageCreateDateLabel.setText("Language Created: " + language.getCreateDate());
        StringBuilder sp = new StringBuilder("");
        for(int i = 0; i < language.getSubjectPronouns().size()-1; i++) {
            sp.append(language.getSubjectPronouns().get(i).toString() + ", ");
        }
        sp.append(language.getSubjectPronouns().get(language.getSubjectPronouns().size()-1));
        subjectPronounsText.setText(sp.toString());
    }

    private void setupPanels() {
        entryPanel = new JPanel();
        entryPanel.setMinimumSize(new Dimension(350,50));
        entryPanel.setMaximumSize(new Dimension(350,50));
        entryPanel.setPreferredSize(new Dimension(350,50));
        languagePanel = new JPanel();
        languagePanel.setMinimumSize(new Dimension(350,90));
        languagePanel.setMaximumSize(new Dimension(350,90));
        languagePanel.setPreferredSize(new Dimension(350,90));
        buttonsPanel = new JPanel();
        buttonsPanel.setMinimumSize(new Dimension(350,50));
        buttonsPanel.setMaximumSize(new Dimension(350,50));
        buttonsPanel.setPreferredSize(new Dimension(350,50));

        entryPanel.setLayout(new GridLayout(2,2));
        entryPanel.add(nameLabel);
        entryPanel.add(setNameField);
        entryPanel.add(authorLabel);
        entryPanel.add(setAuthorField);

        languagePanel.setLayout(new GridLayout(4,1));
        languagePanel.add(languageLabel);
        languagePanel.add(languageAuthorLabel);
        languagePanel.add(languageCreateDateLabel);
        languagePanel.add(subjectPronounsLabel);        

        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);

        JPanel errorPanel = new JPanel();
        errorPanel.add(errorMessageLabel);
        errorPanel.setMinimumSize(new Dimension(350,20));
        errorPanel.setMaximumSize(new Dimension(350,20));
        errorPanel.setPreferredSize(new Dimension(350,20));

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(newSetLabel);
        add(entryPanel);
        add(languagePanel);
        add(scrollPane);
        add(buttonsPanel);
        add(errorPanel);

        sl.putConstraint(SpringLayout.NORTH,newSetLabel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,newSetLabel,71,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,entryPanel,10,SpringLayout.SOUTH,newSetLabel);
        sl.putConstraint(SpringLayout.WEST,entryPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,languagePanel,6,SpringLayout.SOUTH,entryPanel);
        sl.putConstraint(SpringLayout.WEST,languagePanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,scrollPane,0,SpringLayout.SOUTH,languagePanel);
        sl.putConstraint(SpringLayout.WEST,scrollPane,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,buttonsPanel,4,SpringLayout.SOUTH,scrollPane);
        sl.putConstraint(SpringLayout.WEST,buttonsPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,errorPanel,0,SpringLayout.SOUTH,buttonsPanel);
        sl.putConstraint(SpringLayout.WEST,errorPanel,0,SpringLayout.WEST,this);
    }

    public String getTitle() {
        return setNameField.getText();
    }

    public String getAuthor() {
        return setAuthorField.getText();
    }
    
    class AddAction extends AbstractAction {
        public void actionPerformed(ActionEvent e) {
            if(titleApproved && authorApproved) {
                owner.addNewConjugationSet();
            }
        }
    }

    public void insertNewConjugationSetIntoLanguage(DefaultMutableTreeNode languageNode, ConjugatedVerbSet cvs) {
        int index = 0;
        boolean inserting = false;
        for(int i = 0; i < languageNode.getChildCount(); i++) {
            if(cvs.toString().compareTo(((ConjugationTreeObject)(((DefaultMutableTreeNode)languageNode.getChildAt(i)).getUserObject())).getVerbSet().toString()) < 0) {
                index = i;
                inserting = true; // declare that this has found a place to insert
                break;
            }
        }

        ConjugationTreeObject cto = new ConjugationTreeObject(cvs);

        if(inserting) {
            languageNode.insert(new DefaultMutableTreeNode(cto),index);
        }
        else { // no place to insert, so just add to the end (not inserting, but appending)
            languageNode.add(new DefaultMutableTreeNode(cto));
        }

        owner.getSetsViewPanel().getTreeModel().reload();
    }

    private void manageButtonAndLabel(DocumentEvent e) {
        if(e.getDocument() == setNameField.getDocument()) {
            if(setNameField.getText().trim().isEmpty()) {
                titleApproved = false;
                if(setAuthorField.getText().trim().isEmpty()) {
                    errorMessageLabel.setForeground(Color.GREEN);
                    errorMessageLabel.setText("Please enter a name and author for this set.");
                }
                else {
                    errorMessageLabel.setForeground(Color.GREEN);
                    errorMessageLabel.setText("Please enter a name for this set.");
                }
            }
            else {
                if(conjugationSetAlreadyExists(setNameField.getText().trim())) {
                    titleApproved = false;
                    errorMessageLabel.setForeground(Color.RED);
                    errorMessageLabel.setText("Cannot continue! Set with this name already exists.");
                }
                else {
                    titleApproved = true;
                    if(setAuthorField.getText().trim().isEmpty()) {
                        errorMessageLabel.setForeground(Color.GREEN);
                        errorMessageLabel.setText("Please enter an author for this set.");
                    }
                    else {
                        errorMessageLabel.setText("");
                    }
                }
            }
        }
        else if(e.getDocument() == setAuthorField.getDocument()) {
            if(setAuthorField.getText().trim().isEmpty()) {
                authorApproved = false;
                if(setNameField.getText().trim().isEmpty()) {
                    errorMessageLabel.setForeground(Color.GREEN);
                    errorMessageLabel.setText("Please enter a name and author for this set.");
                }
                else {
                    if(conjugationSetAlreadyExists(setNameField.getText().trim())) {
                        errorMessageLabel.setForeground(Color.RED);
                        errorMessageLabel.setText("Cannot continue! Set with this name already exists.");
                    }
                    else {
                        errorMessageLabel.setForeground(Color.GREEN);
                        errorMessageLabel.setText("Please enter an author for this set.");
                    }
                }
            }
            else {
                authorApproved = true;
                if(setNameField.getText().trim().isEmpty()) {
                    errorMessageLabel.setForeground(Color.GREEN);
                    errorMessageLabel.setText("Please enter a name for this set.");
                }
                else {
                    if(conjugationSetAlreadyExists(setNameField.getText().trim())) {
                        errorMessageLabel.setForeground(Color.RED);
                        errorMessageLabel.setText("Cannot continue! Set with this name already exists.");
                    }
                    else {
                        errorMessageLabel.setText("");
                    }
                }
            }
        }

        confirmButton.setEnabled(titleApproved && authorApproved);
    }

    public void insertUpdate(DocumentEvent e) {
        manageButtonAndLabel(e);
    }
	
    public void changedUpdate(DocumentEvent e) {
        manageButtonAndLabel(e);
    }
	
    public void removeUpdate(DocumentEvent e) {
        manageButtonAndLabel(e);
    }
	
    private boolean conjugationSetAlreadyExists(String text) {
        DefaultMutableTreeNode languageRoot;
        
        if(((DefaultMutableTreeNode)owner.getSetsViewPanel().getTree().getLastSelectedPathComponent()).getUserObject() instanceof Language) {
            languageRoot = (DefaultMutableTreeNode)owner.getSetsViewPanel().getTree().getLastSelectedPathComponent();
        }
        else {
            languageRoot = (DefaultMutableTreeNode)((DefaultMutableTreeNode)owner.getSetsViewPanel().getTree().getLastSelectedPathComponent()).getParent();
        }

        for(int i = 0; i < languageRoot.getChildCount(); i++) {
            if(((ConjugationTreeObject)(((DefaultMutableTreeNode)owner.getSetsViewPanel().getTreeModel().getChild(languageRoot,i)).getUserObject())).toString().equalsIgnoreCase(text)) {
                return true;
            }
        }
        return false;
    }

    public void clearFields() {
        titleApproved = false;
        authorApproved = false;
        setNameField.setText("");
        setAuthorField.setText("");
        errorMessageLabel.setForeground(Color.GREEN);
        errorMessageLabel.setText("Please enter a name and author for this set.");
        confirmButton.setEnabled(false);
    }
}

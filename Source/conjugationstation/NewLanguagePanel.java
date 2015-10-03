package conjugationstation;

import conjugationstation.conjugationcomponents.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
 * Main panel for creating new languages, under which new conjugation sets can be created
 * @author Michael Hergenrader
 */
public class NewLanguagePanel extends JPanel implements ActionListener, DocumentListener, ListSelectionListener {
    private ConjugationStation owner;

    private JLabel newLanguageLabel;

    private JTextField languageNameField;
    private JTextField authorField;
    private JLabel createDateLabel;

    private JTextField subjectPronounField;

    private JList subjectPronounList;
    private DefaultListModel pronounListModel;
    private JScrollPane scrollPane;

    private JButton addSubjectPronounButton;
    private JButton removeSubjectPronounButton;

    private JButton confirmButton;
    private JButton cancelButton;

    private JPanel entriesPanel;
    private JPanel listPanel;
    private JPanel buttonsPanel;

    private JLabel subjectPronounLabel;

    private JLabel errorMessageLabel;
    private JLabel subjectPronounErrorMessageLabel;

    private Vector<Component> componentOrder;
    private MyFocusTraversalPolicy myPolicy;

    private boolean canAddPronoun = true;
    private boolean titleApproved = false;
    private boolean authorApproved = false;

    public NewLanguagePanel(ConjugationStation owner) {
        this.owner = owner;        

        newLanguageLabel = new JLabel("Create New Language");
        newLanguageLabel.setFont(new Font("serif",Font.ITALIC | Font.BOLD,18));
        newLanguageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        pronounListModel = new DefaultListModel();
        subjectPronounList = new JList(pronounListModel);
        subjectPronounList.addListSelectionListener(this);
        subjectPronounList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        subjectPronounList.setDragEnabled(true);
        subjectPronounList.setSelectionBackground(Color.BLACK);
        subjectPronounList.setSelectionForeground(Color.WHITE);
        scrollPane = new JScrollPane(subjectPronounList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setMinimumSize(new Dimension(236,96));
        scrollPane.setMaximumSize(new Dimension(236,96));
        scrollPane.setPreferredSize(new Dimension(236,96));

        setupTextFields();
        setupButtons();

        setupEntriesPanel();
        setupListPanel();
        setupButtonsPanel();

        errorMessageLabel = new JLabel("");
        errorMessageLabel.setForeground(Color.RED);
        errorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorMessageLabel.setMinimumSize(new Dimension(350,20));
        errorMessageLabel.setMaximumSize(new Dimension(350,20));
        errorMessageLabel.setPreferredSize(new Dimension(350,20));

        subjectPronounErrorMessageLabel = new JLabel("");
        subjectPronounErrorMessageLabel.setForeground(Color.RED);
        subjectPronounErrorMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subjectPronounErrorMessageLabel.setMinimumSize(new Dimension(350,20));
        subjectPronounErrorMessageLabel.setMaximumSize(new Dimension(350,20));
        subjectPronounErrorMessageLabel.setPreferredSize(new Dimension(350,20));

        componentOrder = new Vector<Component>();
        componentOrder.add(languageNameField);
        componentOrder.add(authorField);
        componentOrder.add(subjectPronounField);

        myPolicy = new MyFocusTraversalPolicy(componentOrder);
        setFocusTraversalPolicy(myPolicy);

        setupLayout();
    }

    private void setupLayout() {
        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(newLanguageLabel);
        add(entriesPanel);
        add(listPanel);
        add(buttonsPanel);
        add(errorMessageLabel);
        add(subjectPronounErrorMessageLabel);

        sl.putConstraint(SpringLayout.NORTH,newLanguageLabel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,newLanguageLabel,94,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,entriesPanel,0,SpringLayout.SOUTH,newLanguageLabel);
        sl.putConstraint(SpringLayout.WEST,entriesPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,listPanel,0,SpringLayout.SOUTH,entriesPanel);
        sl.putConstraint(SpringLayout.WEST,listPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,buttonsPanel,0,SpringLayout.SOUTH,listPanel);
        sl.putConstraint(SpringLayout.WEST,buttonsPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,errorMessageLabel,0,SpringLayout.SOUTH,buttonsPanel);
        sl.putConstraint(SpringLayout.WEST,errorMessageLabel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,subjectPronounErrorMessageLabel,0,SpringLayout.SOUTH,errorMessageLabel);
        sl.putConstraint(SpringLayout.WEST,subjectPronounErrorMessageLabel,0,SpringLayout.WEST,this);
    }

    static class MyFocusTraversalPolicy extends FocusTraversalPolicy {
        Vector<Component> order;

        public MyFocusTraversalPolicy(Vector<Component> order) {
            this.order = new Vector<Component>(order.size());
            this.order.addAll(order);
        }

        public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
            int idx = (order.indexOf(aComponent) + 1) % order.size();
            return order.get(idx);
        }

        public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
            int idx = order.indexOf(aComponent)-1;
            if(idx < 0) {
                idx = order.size()-1;
            }
            return order.get(idx);
        }

        public Component getDefaultComponent(Container focusCycleRoot) { // the one that is set as the default selected component that will be edited
            return order.get(0);
        }

        public Component getLastComponent(Container focusCycleRoot) {
            return order.lastElement();
        }

        public Component getFirstComponent(Container focusCycleRoot) {
            return order.get(0);
        }
    }

    private void setupButtonsPanel() {
        buttonsPanel = new JPanel();
        buttonsPanel.setMinimumSize(new Dimension(350,40));
        buttonsPanel.setMaximumSize(new Dimension(350,40));
        buttonsPanel.setPreferredSize(new Dimension(350,40));
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);
    }

    private void setupListPanel() {
        listPanel = new JPanel();
        listPanel.setMinimumSize(new Dimension(350,96));
        listPanel.setMaximumSize(new Dimension(350,96));
        listPanel.setPreferredSize(new Dimension(350,96));

        subjectPronounLabel = new JLabel("Subject Pronoun");
        subjectPronounLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subjectPronounLabel.setMinimumSize(new Dimension(114,20));
        subjectPronounLabel.setMaximumSize(new Dimension(114,20));
        subjectPronounLabel.setPreferredSize(new Dimension(114,20));

        addSubjectPronounButton.setMaximumSize(new Dimension(113,34));
        addSubjectPronounButton.setMinimumSize(new Dimension(113,34));
        addSubjectPronounButton.setPreferredSize(new Dimension(113,34));

        removeSubjectPronounButton.setMaximumSize(new Dimension(113,34));
        removeSubjectPronounButton.setMinimumSize(new Dimension(113,34));
        removeSubjectPronounButton.setPreferredSize(new Dimension(113,34));

        SpringLayout sl = new SpringLayout();
        listPanel.setLayout(sl);

        listPanel.add(scrollPane);
        listPanel.add(subjectPronounField);
        listPanel.add(addSubjectPronounButton);
        listPanel.add(removeSubjectPronounButton);

        sl.putConstraint(SpringLayout.NORTH,scrollPane,0,SpringLayout.NORTH,listPanel);
        sl.putConstraint(SpringLayout.WEST,scrollPane,0,SpringLayout.WEST,listPanel);

        sl.putConstraint(SpringLayout.NORTH,subjectPronounField,0,SpringLayout.NORTH,listPanel);
        sl.putConstraint(SpringLayout.WEST,subjectPronounField,0,SpringLayout.EAST,scrollPane);
        
        sl.putConstraint(SpringLayout.NORTH,addSubjectPronounButton,1,SpringLayout.SOUTH,subjectPronounField);
        sl.putConstraint(SpringLayout.WEST,addSubjectPronounButton,0,SpringLayout.EAST,scrollPane);

        sl.putConstraint(SpringLayout.NORTH,removeSubjectPronounButton,0,SpringLayout.SOUTH,addSubjectPronounButton);
        sl.putConstraint(SpringLayout.WEST,removeSubjectPronounButton,0,SpringLayout.EAST,scrollPane);
    }

    private void setupEntriesPanel() {
        entriesPanel = new JPanel();
        entriesPanel.setMinimumSize(new Dimension(350,100));
        entriesPanel.setMaximumSize(new Dimension(350,100));
        entriesPanel.setPreferredSize(new Dimension(350,100));
        entriesPanel.setLayout(new GridLayout(4,2));
        entriesPanel.add(new JLabel("Language Name"));
        entriesPanel.add(languageNameField);
        entriesPanel.add(new JLabel("Author"));
        entriesPanel.add(authorField);
        entriesPanel.add(new JLabel("Create Date"));
        entriesPanel.add(createDateLabel);

        JLabel subjectPronouns = new JLabel("Subject Pronouns");
        subjectPronouns.setFont(new Font("Serif",Font.ITALIC,14));
        entriesPanel.add(subjectPronouns);
        entriesPanel.add(new JLabel(""));
    }

    public String getTitle() {
        return languageNameField.getText();
    }

    public JTextField getLanguageNameField() {
        return languageNameField;
    }

    public String getAuthor() {
        return authorField.getText();
    }

    public String getCreateDate() {
        return createDateLabel.getText();
    }

    public DefaultListModel getPronounsModel() {
        return pronounListModel;
    }

    public JButton getConfirmButton() {
        return confirmButton;
    }

    public JButton getCancelButton() {
        return cancelButton;
    }

    private void setupTextFields() {
        languageNameField = new JTextField(20);
        languageNameField.getDocument().addDocumentListener(this);
        
        authorField = new JTextField(20);
        authorField.getDocument().addDocumentListener(this);

        createDateLabel = new JLabel();
        subjectPronounField = new JTextField(10);
        subjectPronounField.setMinimumSize(new Dimension(114,26));
        subjectPronounField.setMaximumSize(new Dimension(114,26));
        subjectPronounField.setPreferredSize(new Dimension(114,26));
        subjectPronounField.getDocument().addDocumentListener(this);
    }

    private void setupButtons() {
        addSubjectPronounButton = new JButton("Add");
        addSubjectPronounButton.setMinimumSize(new Dimension(114,36));
        addSubjectPronounButton.setMaximumSize(new Dimension(114,36));
        addSubjectPronounButton.setPreferredSize(new Dimension(114,36));
        addSubjectPronounButton.addActionListener(this);

        AddAction pressedAction = new AddAction(); // allows for a keyboard shortcut for enter key when user has completed entering the name (alternative to the button)
        subjectPronounField.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        subjectPronounField.getActionMap().put("pressed",pressedAction);
        subjectPronounField.setAction(pressedAction);

        removeSubjectPronounButton = new JButton("Remove");
        removeSubjectPronounButton.setMinimumSize(new Dimension(114,36));
        removeSubjectPronounButton.setMaximumSize(new Dimension(114,36));
        removeSubjectPronounButton.setPreferredSize(new Dimension(114,36));
        removeSubjectPronounButton.addActionListener(this);
        removeSubjectPronounButton.setEnabled(false);
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(owner);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(owner);
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addSubjectPronounButton && canAddPronoun) {
            tryToAddSubjectPronoun();
        }
        else if(e.getSource() == removeSubjectPronounButton) {
            if(subjectPronounList.getSelectedIndex() > -1 && pronounListModel.getSize() > 0) {
                pronounListModel.remove(subjectPronounList.getSelectedIndex());
                subjectPronounField.setEnabled(true);
                
                if(!subjectPronounAlreadyExists(subjectPronounField.getText())) {
                    addSubjectPronounButton.setEnabled(true);
                    canAddPronoun = true;
                }
                else {
                    subjectPronounErrorMessageLabel.setForeground(Color.RED);
                    subjectPronounErrorMessageLabel.setText("Cannot add pronoun! Subject pronoun already exists.");
                }
                subjectPronounField.requestFocusInWindow();
            }
        }
    }

    private void tryToAddSubjectPronoun() {
        if(subjectPronounField.getText().length() > 0) {
            SubjectPronoun s = new SubjectPronoun(subjectPronounField.getText());
            pronounListModel.addElement(s);
            subjectPronounList.setSelectedValue(s,true);
            subjectPronounField.setText("");            
        }
        if(pronounListModel.getSize() == 16) {
            subjectPronounField.setEnabled(false);
            addSubjectPronounButton.setEnabled(false);
            subjectPronounErrorMessageLabel.setForeground(Color.RED);
            subjectPronounErrorMessageLabel.setText("Pronoun amount limit reached.");
        }
    }

    public void clearFields() {
        languageNameField.setText("");
        authorField.setText("");
        
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy    HH:mm:ss");
        Date date = new Date();
        String dateCreated = dateFormat.format(date);
        createDateLabel.setText(dateCreated);
        
        pronounListModel.clear();
        subjectPronounField.setText("");
        errorMessageLabel.setForeground(Color.GREEN);
        errorMessageLabel.setText("Please enter both a name and author for this language.");
        subjectPronounErrorMessageLabel.setForeground(Color.GREEN);
        subjectPronounErrorMessageLabel.setText("Please enter subject pronouns.");

        subjectPronounField.setEnabled(true);
        addSubjectPronounButton.setEnabled(true);
        removeSubjectPronounButton.setEnabled(false);

        canAddPronoun = true;
        titleApproved = false;
        authorApproved = false;
        
        confirmButton.setEnabled(false);
    }

    public void valueChanged(ListSelectionEvent e) {
        removeSubjectPronounButton.setEnabled(subjectPronounList.getSelectedIndex() > -1);
        if(pronounListModel.getSize() == 0) {
            subjectPronounErrorMessageLabel.setForeground(Color.GREEN);
            subjectPronounErrorMessageLabel.setText("Please enter subject pronouns for this language.");
        }
        else {
            subjectPronounErrorMessageLabel.setText("");
        }

        if(subjectPronounAlreadyExists(subjectPronounField.getText().trim())) {
            subjectPronounErrorMessageLabel.setForeground(Color.RED);
            subjectPronounErrorMessageLabel.setText("Cannot add pronoun! Subject pronoun already exists.");
        }

        confirmButton.setEnabled(titleApproved && authorApproved && !pronounListModel.isEmpty());
    }

    class AddAction extends AbstractAction {
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == subjectPronounField && canAddPronoun) {
                tryToAddSubjectPronoun();
            }
        }
    }

    private void manageLabelsAndApproval(DocumentEvent e) {
        if(e.getDocument() == languageNameField.getDocument()) {
            if(languageNameField.getText().trim().isEmpty()) {
                titleApproved = false;

                errorMessageLabel.setForeground(Color.GREEN);
                if(authorField.getText().trim().isEmpty()) {
                    errorMessageLabel.setText("Please enter both a title and an author.");
                }
                else {
                    errorMessageLabel.setText("Please enter a title for this language.");
                }
            }
            else {
                if(languageAlreadyExists(languageNameField.getText().trim())) {
                    titleApproved = false;
                    errorMessageLabel.setForeground(Color.RED);
                    errorMessageLabel.setText("Language title already exists!");
                }
                else {
                    titleApproved = true; // not already in the language list and not empty
                    if(authorField.getText().trim().isEmpty()) {
                        errorMessageLabel.setForeground(Color.GREEN);
                        errorMessageLabel.setText("Please enter an author for this language.");
                    }
                    else {
                        errorMessageLabel.setText("");
                    }
                }
            }
        }
        else if(e.getDocument() == authorField.getDocument()) {
            if(authorField.getText().trim().isEmpty()) {
                authorApproved = false;
                errorMessageLabel.setForeground(Color.GREEN);
                if(languageNameField.getText().trim().isEmpty()) {
                    errorMessageLabel.setText("Please enter both a title and an author.");
                }
                else {
                    if(!languageAlreadyExists(languageNameField.getText().trim())) {
                        errorMessageLabel.setForeground(Color.GREEN);
                        errorMessageLabel.setText("Please enter an author for this language.");
                    }
                    else {
                        errorMessageLabel.setForeground(Color.RED);
                        errorMessageLabel.setText("Language title already exists!");
                    }
                }
            }
            else {
                authorApproved = true;
                if(languageNameField.getText().trim().isEmpty()) {
                    errorMessageLabel.setForeground(Color.GREEN);
                    errorMessageLabel.setText("Please enter a title for this language.");
                }
                else {
                    if(!languageAlreadyExists(languageNameField.getText().trim())) {
                        errorMessageLabel.setText("");
                    }
                    else {
                        errorMessageLabel.setForeground(Color.RED);
                        errorMessageLabel.setText("Language title already exists!");
                    }
                }
            }
        }
        else if(e.getDocument() == subjectPronounField.getDocument()) {
            if(subjectPronounAlreadyExists(subjectPronounField.getText().trim())) {
                addSubjectPronounButton.setEnabled(false);
                canAddPronoun = false;
                subjectPronounErrorMessageLabel.setForeground(Color.RED);
                subjectPronounErrorMessageLabel.setText("Cannot add pronoun! Subject pronoun already exists.");
            }
            else {
                addSubjectPronounButton.setEnabled(true);
                canAddPronoun = true;
                subjectPronounErrorMessageLabel.setText("");
            }
        }

        confirmButton.setEnabled(titleApproved && authorApproved && !pronounListModel.isEmpty());
    }

    public void changedUpdate(DocumentEvent e) {
        manageLabelsAndApproval(e);
    }   
	 
    public void insertUpdate(DocumentEvent e) {
        manageLabelsAndApproval(e);
    }
	
    public void removeUpdate(DocumentEvent e) {
        manageLabelsAndApproval(e);
    }
    
    private boolean languageAlreadyExists(String title) {
        for(int i = 0; i < owner.getSetsViewPanel().getRootNode().getChildCount(); i++) {
            if(((Language)(((DefaultMutableTreeNode)owner.getSetsViewPanel().getTreeModel().getChild(owner.getSetsViewPanel().getRootNode(),i)).getUserObject())).getTitle().equalsIgnoreCase(title)) {
                return true;
            }
        }
        return false;
    }

    private boolean subjectPronounAlreadyExists(String pronounText) {
        for(int i = 0; i < pronounListModel.getSize(); i++) {
            if(pronounListModel.getElementAt(i).toString().equals(pronounText)) {
                return true;
            }
        }
        return false;
    }
}

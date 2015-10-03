package conjugationstation.conjugationreviewer.settings;

import conjugationstation.conjugationreviewer.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import conjugationstation.conjugationcomponents.*;

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
 * When loading a new conjugation practice section, allow user to select subsets of practice sets to use
 * @author Michael Hergenrader
 */
public class ListsPanel extends JPanel implements ListSelectionListener {
    private ConjugationReviewFrame owner;

    private JLabel verbSetLabel;
    private DefaultListModel verbSetModel;
    private JList verbSetList;
    private JScrollPane verbSetScrollPane;
    private int currentVerbSetIndex;

    private ArrayList<ArrayList<Integer>> verbIndicesSelected;

    private JLabel verbsLabel;
    private DefaultListModel verbsModel;
    private JList verbsList;
    private JScrollPane verbsScrollPane;

    private JLabel subjectPronounsLabel;
    private DefaultListModel subjectPronounsModel;
    private JList subjectPronounsList;
    private JScrollPane subjectPronounsScrollPane;

    // MUST have frame owner reference no matter what (to load the data)
    public ListsPanel(ConjugationReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(550,220));
        setMaximumSize(new Dimension(550,220));
        setPreferredSize(new Dimension(550,220));

        verbIndicesSelected = new ArrayList<ArrayList<Integer>>();
        
        setupLabels();
        setupLists();

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(verbSetLabel);
        add(verbSetScrollPane);

        add(verbsLabel);
        add(verbsScrollPane);

        add(subjectPronounsLabel);
        add(subjectPronounsScrollPane);

        sl.putConstraint(SpringLayout.NORTH,verbSetLabel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,verbSetLabel,25,SpringLayout.WEST,this);
        sl.putConstraint(SpringLayout.NORTH,verbSetScrollPane,0,SpringLayout.SOUTH,verbSetLabel);
        sl.putConstraint(SpringLayout.WEST,verbSetScrollPane,25,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,verbsLabel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,verbsLabel,25,SpringLayout.EAST,verbSetLabel);
        sl.putConstraint(SpringLayout.NORTH,verbsScrollPane,0,SpringLayout.SOUTH,verbsLabel);
        sl.putConstraint(SpringLayout.WEST,verbsScrollPane,25,SpringLayout.EAST,verbSetScrollPane);

        sl.putConstraint(SpringLayout.NORTH,subjectPronounsLabel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,subjectPronounsLabel,25,SpringLayout.EAST,verbsLabel);
        sl.putConstraint(SpringLayout.NORTH,subjectPronounsScrollPane,0,SpringLayout.SOUTH,subjectPronounsLabel);
        sl.putConstraint(SpringLayout.WEST,subjectPronounsScrollPane,25,SpringLayout.EAST,verbsScrollPane);
    }

    private void setupLabels() {
        verbSetLabel = new JLabel("Verb Sets");
        verbSetLabel.setMinimumSize(new Dimension(150,20));
        verbSetLabel.setMaximumSize(new Dimension(150,20));
        verbSetLabel.setPreferredSize(new Dimension(150,20));
        verbSetLabel.setHorizontalAlignment(SwingConstants.CENTER);
        verbsLabel = new JLabel("Verbs");
        verbsLabel.setMinimumSize(new Dimension(150,20));
        verbsLabel.setMaximumSize(new Dimension(150,20));
        verbsLabel.setPreferredSize(new Dimension(150,20));
        verbsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subjectPronounsLabel = new JLabel("Subject Pronouns");
        subjectPronounsLabel.setMinimumSize(new Dimension(150,20));
        subjectPronounsLabel.setMaximumSize(new Dimension(150,20));
        subjectPronounsLabel.setPreferredSize(new Dimension(150,20));
        subjectPronounsLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupLists() {
        verbSetModel = new DefaultListModel();
		
        // fill the verb set model
        for(ConjugatedVerbSet cvs : owner.getVerbSets()) {
            verbSetModel.addElement(new VerbSetCheckboxComponent(cvs));
            verbIndicesSelected.add(new ArrayList<Integer>()); // create an index storage space for each verb set
        }

        for(int i = 0; i < owner.getVerbSets().size(); i++) {
            for(int j = 0; j < owner.getVerbSets().get(i).getVerbs().size(); j++) {
                verbIndicesSelected.get(i).add(new Integer(j));
            }
        } // this loop will cause every verb to be selected initially, which is probably preferrable to the user

        verbSetList = new JList(verbSetModel);
        
        verbSetList.setSelectionForeground(Color.WHITE);
        verbSetList.setSelectionBackground(Color.BLACK);
        verbSetList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        verbSetList.setSelectedIndex(0);

        CheckboxRenderer r = new CheckboxRenderer();
        verbSetList.setCellRenderer(r);

        verbSetList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JList list = (JList)e.getSource();
                if(!list.isEnabled() || e.getClickCount() != 2) { // double click to make the checkbox toggle
                    return;
                }

                int index = list.locationToIndex(e.getPoint());

                VerbSetCheckboxComponent c = (VerbSetCheckboxComponent)list.getModel().getElementAt(index);

                c.setSelected(!c.isSelected());
                list.repaint(list.getCellBounds(index, index)); // just visually update that portion
            }
        });

        currentVerbSetIndex = 0;

        verbSetList.addListSelectionListener(this);
        verbSetScrollPane = new JScrollPane(verbSetList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        verbSetScrollPane.setMinimumSize(new Dimension(150,200));
        verbSetScrollPane.setMaximumSize(new Dimension(150,200));
        verbSetScrollPane.setPreferredSize(new Dimension(150,200));

        verbsModel = new DefaultListModel();
		
        // fill the model - add all to the selected list
        for(int i = 0; i < owner.getVerbSets().get(0).getVerbs().size(); i++) {
            ConjugatedVerb cv = (ConjugatedVerb)owner.getVerbSets().get(0).getVerbs().get(i);
            verbsModel.addElement(cv);
            verbIndicesSelected.get(0).add(new Integer(i));
        }

        verbsList = new JList(verbsModel);
        verbsList.setSelectionForeground(Color.WHITE);
        verbsList.setSelectionBackground(Color.BLACK);
        verbsList.setSelectionInterval(0,verbsModel.getSize()-1);
        verbsList.addListSelectionListener(this);
        verbsScrollPane = new JScrollPane(verbsList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        verbsScrollPane.setMinimumSize(new Dimension(150,200));
        verbsScrollPane.setMaximumSize(new Dimension(150,200));
        verbsScrollPane.setPreferredSize(new Dimension(150,200));

        subjectPronounsModel = new DefaultListModel();
		
        // fill the pronouns model
        for(SubjectPronoun sp : owner.getLanguage().getSubjectPronouns()) {
            subjectPronounsModel.addElement(sp);
        }

        subjectPronounsList = new JList(subjectPronounsModel);
        subjectPronounsList.setSelectionForeground(Color.WHITE);
        subjectPronounsList.setSelectionBackground(Color.BLACK);
        subjectPronounsList.addListSelectionListener(this);
        subjectPronounsList.setSelectionInterval(0,subjectPronounsModel.getSize()-1); // select all by default
        subjectPronounsScrollPane = new JScrollPane(subjectPronounsList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        subjectPronounsScrollPane.setMinimumSize(new Dimension(150,200));
        subjectPronounsScrollPane.setMaximumSize(new Dimension(150,200));
        subjectPronounsScrollPane.setPreferredSize(new Dimension(150,200));
    }

    public void disableLists() {
        verbSetList.setEnabled(false);
        verbsList.setEnabled(false);
        subjectPronounsList.setEnabled(false);
    }

    public void enableLists() {
        verbSetList.setEnabled(true);
        verbsList.setEnabled(true);
        subjectPronounsList.setEnabled(true);
    }

    public void valueChanged(ListSelectionEvent e) {
        if(e.getSource() == verbSetList) {
            if(verbSetList.getSelectedIndex() > -1) { // user has made a selection to display the verbs available in this particular verb set
                refreshSelectedVerbs(currentVerbSetIndex);

                currentVerbSetIndex = verbSetList.getSelectedIndex();
                updateVerbsList(verbSetList.getSelectedIndex());
            }
        }
        else if(e.getSource() == verbsList) { // each time user changes an index, update the list          
        }
        else if(e.getSource() == subjectPronounsList) {
        }
    }

    private void refreshSelectedVerbs(int indexOfVerbSet) {
        verbIndicesSelected.get(indexOfVerbSet).clear();
		
        for(int i = 0; i < verbsList.getSelectedIndices().length; i++) {
            verbIndicesSelected.get(indexOfVerbSet).add(new Integer(verbsList.getSelectedIndices()[i]));
        }
    }

    private void updateVerbsList(int indexOfVerbSet) {
        verbsModel.clear();
        ConjugatedVerbSet verbSet = (ConjugatedVerbSet)((VerbSetCheckboxComponent)verbSetModel.getElementAt(indexOfVerbSet)).getVerbSet();

        for(ConjugatedVerb cv : verbSet.getVerbs()) {
            verbsModel.addElement((ConjugatedVerb)cv);
        }

        // select all of the saved indices        
        int[] selected = new int[verbIndicesSelected.get(indexOfVerbSet).size()];
        for(int i = 0; i < selected.length; i++) {
            selected[i] = verbIndicesSelected.get(indexOfVerbSet).get(i).intValue();
        }
        verbsList.setSelectedIndices(selected);        
    }

    public JList getPronounsList() {
        return subjectPronounsList;
    }

    public DefaultListModel getVerbSetsModel() {
        return verbSetModel;
    }

    public DefaultListModel getVerbsModel() {
        return verbsModel;
    }

    public DefaultListModel getPronounsModel() {
        return subjectPronounsModel;
    }

    public ArrayList<ArrayList<Integer>> getSelectedVerbIndices() {
        return verbIndicesSelected;
    }

    public void updateCurrentList() {
        refreshSelectedVerbs(currentVerbSetIndex);
    }

    class CheckboxRenderer extends JCheckBox implements ListCellRenderer {
        public CheckboxRenderer() {
            setSelected(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            if(isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setText(value.toString());
            setSelected(((VerbSetCheckboxComponent)value).isSelected());
            return this;
        }
    }
}

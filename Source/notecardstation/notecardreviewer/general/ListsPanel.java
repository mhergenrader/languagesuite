package notecardstation.notecardreviewer.general;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import notecardstation.notecard.*;

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
 * Panel that lists all possible notecard stacks for review
 * @author Michael Hergenrader
 */
public class ListsPanel extends JPanel implements ListSelectionListener {

    private NotecardStackReviewFrame owner;

    private JLabel stacksLabel;
    private DefaultListModel stacksModel;
    private JList stacksList;
    private JScrollPane stacksScrollPane;
    private int currentStackIndex;

    private ArrayList<ArrayList<Integer>> cardIndicesSelected;

    private JLabel cardsLabel;
    private DefaultListModel cardsModel;
    private JList cardsList;
    private JScrollPane cardsScrollPane;

    private static final Dimension LIST_DIMENSION = new Dimension(250,222);

    public ListsPanel(NotecardStackReviewFrame owner) {
        this.owner = owner;

        cardIndicesSelected = new ArrayList<ArrayList<Integer>>();

        setupLabels();
        setupLists();

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(stacksLabel);
        add(stacksScrollPane);

        add(cardsLabel);
        add(cardsScrollPane);

        sl.putConstraint(SpringLayout.NORTH,stacksLabel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,stacksLabel,30,SpringLayout.WEST,this);
        sl.putConstraint(SpringLayout.NORTH,stacksScrollPane,0,SpringLayout.SOUTH,stacksLabel);
        sl.putConstraint(SpringLayout.WEST,stacksScrollPane,30,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,cardsLabel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,cardsLabel,25,SpringLayout.EAST,stacksLabel);
        sl.putConstraint(SpringLayout.NORTH,cardsScrollPane,0,SpringLayout.SOUTH,cardsLabel);
        sl.putConstraint(SpringLayout.WEST,cardsScrollPane,25,SpringLayout.EAST,stacksScrollPane); //25
    }

    private void setupLabels() {
        stacksLabel = new JLabel("Notecard Stacks");
        stacksLabel.setMinimumSize(new Dimension(250,20));
        stacksLabel.setMaximumSize(new Dimension(250,20));
        stacksLabel.setPreferredSize(new Dimension(250,20));
        stacksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cardsLabel = new JLabel("Notecards to Review");
        cardsLabel.setMinimumSize(new Dimension(250,20));
        cardsLabel.setMaximumSize(new Dimension(250,20));
        cardsLabel.setPreferredSize(new Dimension(250,20));
        cardsLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupLists() {
        stacksModel = new DefaultListModel();
        ArrayList<NotecardStack> stacks = owner.getStacks();

        for(NotecardStack ns : stacks) {
            stacksModel.addElement(new NotecardStackCheckboxComponent(ns));
            cardIndicesSelected.add(new ArrayList<Integer>());
        }

        for(int i = 0; i < stacks.size(); i++) {
            for(int j = 0; j < stacks.get(i).getNotecards().size(); j++) {
                cardIndicesSelected.get(i).add(new Integer(j));
            }
        } // this loop will cause every notecard to be selected initially, which is probably preferrable to the user

        stacksList = new JList(stacksModel);

        stacksList.setSelectionForeground(Color.WHITE);
        stacksList.setSelectionBackground(Color.BLACK);
        stacksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        stacksList.setSelectedIndex(0);

        CheckboxRenderer r = new CheckboxRenderer();
        stacksList.setCellRenderer(r);

        stacksList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                JList list = (JList)e.getSource();
                if(!list.isEnabled() || e.getClickCount() != 2) { // double click to make the checkbox toggle
                    return;
                }

                int index = list.locationToIndex(e.getPoint());

                NotecardStackCheckboxComponent c = (NotecardStackCheckboxComponent)list.getModel().getElementAt(index);

                c.setSelected(!c.isSelected());
                list.repaint(list.getCellBounds(index, index));
            }
        });

        currentStackIndex = 0;

        stacksList.addListSelectionListener(this);
        stacksScrollPane = new JScrollPane(stacksList);
        stacksScrollPane.setMinimumSize(LIST_DIMENSION);
        stacksScrollPane.setMaximumSize(LIST_DIMENSION);
        stacksScrollPane.setPreferredSize(LIST_DIMENSION);

        cardsModel = new DefaultListModel();
        for(int i = 0; i < stacks.get(0).getNotecards().size(); i++) {
            Notecard n = (Notecard)stacks.get(0).getNotecards().get(i);
            cardsModel.addElement(n);
            cardIndicesSelected.get(0).add(new Integer(i));
        }

        cardsList = new JList(cardsModel);
        cardsList.setSelectionForeground(Color.WHITE);
        cardsList.setSelectionBackground(Color.BLACK);
        cardsList.setSelectionInterval(0,cardsModel.getSize()-1);
        cardsList.addListSelectionListener(this);
        cardsScrollPane = new JScrollPane(cardsList);
        cardsScrollPane.setMinimumSize(LIST_DIMENSION);
        cardsScrollPane.setMaximumSize(LIST_DIMENSION);
        cardsScrollPane.setPreferredSize(LIST_DIMENSION);
    }

    public void disableLists() {
        stacksList.setEnabled(false);
        cardsList.setEnabled(false);
    }

    public void enableLists() {
        stacksList.setEnabled(true);
        cardsList.setEnabled(true);
    }

    public void valueChanged(ListSelectionEvent e) {
        if(e.getSource() == stacksList) {
            if(stacksList.getSelectedIndex() > -1) {
                refreshSelectedCards(currentStackIndex);

                currentStackIndex = stacksList.getSelectedIndex();
                updateCardsList(stacksList.getSelectedIndex());
            }
        }
        else if(e.getSource() == cardsList) { // each time user changes an index, update the list
        }
    }

    private void refreshSelectedCards(int indexOfCardSet) {
        cardIndicesSelected.get(indexOfCardSet).clear();
        for(int i = 0; i < cardsList.getSelectedIndices().length; i++) {
            cardIndicesSelected.get(indexOfCardSet).add(new Integer(cardsList.getSelectedIndices()[i]));
        }
    }

    private void updateCardsList(int indexOfCardSet) {
        cardsModel.clear();
        NotecardStack ns = (NotecardStack)((NotecardStackCheckboxComponent)stacksModel.getElementAt(indexOfCardSet)).getNotecardStack();

        for(Notecard n : ns.getNotecards()) {
            cardsModel.addElement((Notecard)n);
        }

        // select all of the saved indices
        int[] selected = new int[cardIndicesSelected.get(indexOfCardSet).size()];
        for(int i = 0; i < selected.length; i++) {
            selected[i] = cardIndicesSelected.get(indexOfCardSet).get(i).intValue();
        }
        cardsList.setSelectedIndices(selected);
    }

    public DefaultListModel getStacksModel() {
        return stacksModel;
    }

    public DefaultListModel getCardsModel() {
        return cardsModel;
    }

    public ArrayList<ArrayList<Integer>> getSelectedCardIndices() {
        return cardIndicesSelected;
    }

    public void updateCurrentList() {
        refreshSelectedCards(currentStackIndex);
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
            setSelected(((NotecardStackCheckboxComponent)value).isSelected());
            return this;
        }
    }
}

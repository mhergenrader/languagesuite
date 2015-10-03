package notecardstation.notecardreviewer.reports;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

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
 * Show a table of all answers, incorrect and correct, from the previous notecard review session
 * @author Michael Hergenrader
 */
public class ReviewSessionPanel extends JPanel {
    private ReviewSessionReport report;

    private JPanel topPanel;
    private JPanel statsPanel;

    private JTable missedVerbsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    private JPanel buttonsPanel;
    private JButton saveButton;
    private JButton printButton;

    private static final Dimension panelDimension = new Dimension(762,520);

    public ReviewSessionPanel(ReviewSessionReport report) { // just VISUAL components of the report
        this.report = report;

        setMinimumSize(panelDimension);
        setMaximumSize(panelDimension);
        setPreferredSize(panelDimension);

        setupTopPanel();
        setupStatsPanel();
        setupTable();

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(topPanel);
        add(statsPanel);
        add(scrollPane);

        sl.putConstraint(SpringLayout.NORTH,topPanel,0,SpringLayout.NORTH,this);
        sl.putConstraint(SpringLayout.WEST,topPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,statsPanel,20,SpringLayout.SOUTH,topPanel);
        sl.putConstraint(SpringLayout.WEST,statsPanel,0,SpringLayout.WEST,this);

        sl.putConstraint(SpringLayout.NORTH,scrollPane,0,SpringLayout.SOUTH,statsPanel);
        sl.putConstraint(SpringLayout.WEST,scrollPane,0,SpringLayout.WEST,this);
    }


    public void setReport(ReviewSessionReport csr) {
        this.report = csr;
    }

    private void setupTopPanel() {
        topPanel = new JPanel();
        topPanel.setMinimumSize(new Dimension(630,80));
        topPanel.setMaximumSize(new Dimension(630,80));
        topPanel.setPreferredSize(new Dimension(630,80));
        topPanel.setLayout(new GridLayout(4,1));

        JLabel dateLabel = new JLabel(report.dateAndTime.toString());
        dateLabel.setHorizontalAlignment(SwingConstants.TRAILING);

        topPanel.add(dateLabel);
        topPanel.add(new JLabel("Name: " + report.userName.toString()));

        StringBuilder sb = new StringBuilder();
        if(report.stacksUsed.size() == 1) {
            sb.append(report.stacksUsed.get(0).toString());
        }
        else {
            for(int i = 0; i < report.stacksUsed.size()-1; i++) {
                sb.append(report.stacksUsed.get(i).toString() + ", ");
            }
            sb.append(report.stacksUsed.get(report.stacksUsed.size()-1).toString());
        }
        topPanel.add(new JLabel("Stacks Used: " + sb.toString()));
    }

    private void setupStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setMinimumSize(new Dimension(630,100));
        statsPanel.setMaximumSize(new Dimension(630,100));
        statsPanel.setPreferredSize(new Dimension(630,100));
        statsPanel.setLayout(new GridLayout(5,2));

        statsPanel.add(new JLabel("Mode: " + report.reviewMode.toString()));
        statsPanel.add(new JLabel("Number correct: " + report.numberCorrect));
        statsPanel.add(new JLabel("Time Limit: " + report.timeLimit.toString()));
        statsPanel.add(new JLabel("Number incorrect: " + report.numberIncorrect));
        statsPanel.add(new JLabel("One pass: " + (report.timeLimit.toString().equals("N/A")?(report.onePassUsed?"yes":"no"):"N/A")));
        statsPanel.add(new JLabel("Percentage: " + ((int)(((double)report.numberCorrect/((double)report.numberCorrect+(double)report.numberIncorrect))*100)) + "%"));
        statsPanel.add(new JLabel("Number of pauses: " + report.numberOfPauses));
        statsPanel.add(new JLabel(""));
        statsPanel.add(new JLabel("Incorrect Answers:"));
    }

    private void setupTable() {
        String[] columnNames = { "Notecard Stack","Shown Side","Shown Side Text","Tested Side","Correct Answer","Your Answer" };

        tableModel = new MyDefaultTableModel(columnNames,0);
		
        for(int i = 0; i < report.missedEntries.size(); i++) {
            MissedEntry missedEntry = report.missedEntries.get(i);
            tableModel.addRow(new Object[]{missedEntry.getNotecardStack(),missedEntry.getFrontSideTitle(),missedEntry.getFrontSideText(),missedEntry.getTestedSideTitle(),missedEntry.getTestedSideCorrectAnswer(),missedEntry.getTestedSideUserAnswer()});
        }

        missedVerbsTable = new JTable(tableModel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        missedVerbsTable.setRowSorter(sorter);
		
        missedVerbsTable.getColumn("Notecard Stack").setMinWidth(136);
        missedVerbsTable.getColumn("Notecard Stack").setMaxWidth(136);
        missedVerbsTable.getColumn("Notecard Stack").setPreferredWidth(136);
        missedVerbsTable.getColumn("Notecard Stack").setResizable(false);

        missedVerbsTable.getColumn("Shown Side").setMinWidth(130);
        missedVerbsTable.getColumn("Shown Side").setMaxWidth(130);
        missedVerbsTable.getColumn("Shown Side").setPreferredWidth(130);
        missedVerbsTable.getColumn("Shown Side").setResizable(false);

        missedVerbsTable.getColumn("Shown Side Text").setMinWidth(100);
        missedVerbsTable.getColumn("Shown Side Text").setMaxWidth(100);
        missedVerbsTable.getColumn("Shown Side Text").setPreferredWidth(100);
        missedVerbsTable.getColumn("Shown Side Text").setResizable(false);

        missedVerbsTable.getColumn("Tested Side").setMinWidth(132);
        missedVerbsTable.getColumn("Tested Side").setMaxWidth(132);
        missedVerbsTable.getColumn("Tested Side").setPreferredWidth(132);
        missedVerbsTable.getColumn("Tested Side").setResizable(false);

        missedVerbsTable.getColumn("Correct Answer").setMinWidth(132);
        missedVerbsTable.getColumn("Correct Answer").setMaxWidth(132);
        missedVerbsTable.getColumn("Correct Answer").setPreferredWidth(132);
        missedVerbsTable.getColumn("Correct Answer").setResizable(false);

        missedVerbsTable.getColumn("Your Answer").setMinWidth(132);
        missedVerbsTable.getColumn("Your Answer").setMaxWidth(132);
        missedVerbsTable.getColumn("Your Answer").setPreferredWidth(132);
        missedVerbsTable.getColumn("Your Answer").setResizable(false);

        scrollPane = new JScrollPane(missedVerbsTable);
        scrollPane.setMinimumSize(new Dimension(762,320));
        scrollPane.setMaximumSize(new Dimension(762,320));
        scrollPane.setPreferredSize(new Dimension(762,320));
    }

    class MyDefaultTableModel extends DefaultTableModel {
        public MyDefaultTableModel() {
            super();
        }

        public MyDefaultTableModel(Object[][] data, Object[] columnNames) {
            super(data,columnNames);
        }

        public MyDefaultTableModel(Object[] columnNames, int rowCount) {
            super(columnNames,0);
        }

        public boolean isCellEditable(int row, int column) {
            return false; // automatically disable editing this table
        }
    }
}

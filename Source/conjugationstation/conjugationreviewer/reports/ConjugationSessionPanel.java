package conjugationstation.conjugationreviewer.reports;

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
 * This panel displays the final results of a conjugation practice session
 * This shows the mistakes and correct answers for those mistakes, along with scores.
 * @author Michael Hergenrader
 */
public class ConjugationSessionPanel extends JPanel {

    private ConjugationSessionReport report;

    private JPanel topPanel;
    private JPanel statsPanel;

    private JTable missedVerbsTable;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;

    public ConjugationSessionPanel(ConjugationSessionReport report) { // just VISUAL components of the report
        this.report = report;

        setMinimumSize(new Dimension(630,520));
        setMaximumSize(new Dimension(630,520));
        setPreferredSize(new Dimension(630,520));

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


    public void setReport(ConjugationSessionReport csr) {
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

        topPanel.add(new JLabel("Language: " + report.language.toString()));
        StringBuilder sb = new StringBuilder();

        if(report.verbSetsUsed.size() == 1) {
            sb.append(report.verbSetsUsed.get(0).toString());
        }
        else {
            for(int i = 0; i < report.verbSetsUsed.size()-1; i++) {
                sb.append(report.verbSetsUsed.get(i).toString() + ", ");
            }
            sb.append(report.verbSetsUsed.get(report.verbSetsUsed.size()-1).toString());
        }
        topPanel.add(new JLabel("Verb Sets Used: " + sb.toString()));
    }

    private void setupStatsPanel() {
        statsPanel = new JPanel();
        statsPanel.setMinimumSize(new Dimension(630,100));
        statsPanel.setMaximumSize(new Dimension(630,100));
        statsPanel.setPreferredSize(new Dimension(630,100));
        statsPanel.setLayout(new GridLayout(5,2));

        statsPanel.add(new JLabel("Mode: " + report.conjugationMode.toString()));
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
        String[] columnNames = { "Verb Set","Subject Pronoun","Infinitive","Correct Answer","Your Answer" };

        tableModel = new MyDefaultTableModel(columnNames,0);

        // fill the table model
        for(int i = 0; i < report.missedVerbs.size(); i++) {
            MissedVerb mv = report.missedVerbs.get(i);
            tableModel.addRow(new Object[]{mv.getConjugatedVerb().getSetReference(),mv.getSubjectPronoun().toString(),mv.getConjugatedVerb().toString(),mv.getCorrectAnswer(),mv.getUserAnswer()});
        }

        missedVerbsTable = new JTable(tableModel);

        TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tableModel);
        missedVerbsTable.setRowSorter(sorter);

        missedVerbsTable.getColumn("Verb Set").setMinWidth(136);
        missedVerbsTable.getColumn("Verb Set").setMaxWidth(136);
        missedVerbsTable.getColumn("Verb Set").setPreferredWidth(136);
        missedVerbsTable.getColumn("Verb Set").setResizable(false);

        missedVerbsTable.getColumn("Subject Pronoun").setMinWidth(130);
        missedVerbsTable.getColumn("Subject Pronoun").setMaxWidth(130);
        missedVerbsTable.getColumn("Subject Pronoun").setPreferredWidth(130);
        missedVerbsTable.getColumn("Subject Pronoun").setResizable(false);

        missedVerbsTable.getColumn("Infinitive").setMinWidth(100);
        missedVerbsTable.getColumn("Infinitive").setMaxWidth(100);
        missedVerbsTable.getColumn("Infinitive").setPreferredWidth(100);
        missedVerbsTable.getColumn("Infinitive").setResizable(false);

        missedVerbsTable.getColumn("Correct Answer").setMinWidth(132);
        missedVerbsTable.getColumn("Correct Answer").setMaxWidth(132);
        missedVerbsTable.getColumn("Correct Answer").setPreferredWidth(132);
        missedVerbsTable.getColumn("Correct Answer").setResizable(false);

        missedVerbsTable.getColumn("Your Answer").setMinWidth(132);
        missedVerbsTable.getColumn("Your Answer").setMaxWidth(132);
        missedVerbsTable.getColumn("Your Answer").setPreferredWidth(132);
        missedVerbsTable.getColumn("Your Answer").setResizable(false);

        scrollPane = new JScrollPane(missedVerbsTable);
        scrollPane.setMinimumSize(new Dimension(630,320));
        scrollPane.setMaximumSize(new Dimension(630,320));
        scrollPane.setPreferredSize(new Dimension(630,320));
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

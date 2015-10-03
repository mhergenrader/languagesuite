package notecardstation.notecardreviewer.reports;

import javax.swing.*;
import notecardstation.notecardreviewer.general.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import languagesuite.LanguageSuite;

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
 * List all previous reports from notecard practices for this user
 * @author Michael Hergenrader
 */
public class ReportListFrame extends JFrame implements ActionListener, ListSelectionListener {
    private NotecardStackReviewFrame owner;
    private DefaultListModel reportListModel;
    private JList reportList;
    private JScrollPane scrollPane;

    private JPanel buttonsPanel;
    private JButton viewButton;
    private JButton removeButton;
    private JButton cancelButton;

    public ReportListFrame(NotecardStackReviewFrame owner) {
        super("View Report");
        setIconImage(LanguageSuite.frameIcon);
        
        this.owner = owner;
        initialize();
    }

    private void initialize() {
        reportListModel = new DefaultListModel();

        for(int i = 0; i < owner.getReports().size(); i++) {
            reportListModel.addElement(owner.getReports().get(i));
        }
        
        reportList = new JList(reportListModel);
        reportList.setSelectionBackground(Color.BLACK);
        reportList.setSelectionForeground(Color.WHITE);
        reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reportList.addListSelectionListener(this);
        scrollPane = new JScrollPane(reportList); // as needed?
        scrollPane.setMinimumSize(new Dimension(500,240));
        scrollPane.setMaximumSize(new Dimension(500,240));
        scrollPane.setPreferredSize(new Dimension(500,240));

        buttonsPanel = new JPanel();
        buttonsPanel.setMinimumSize(new Dimension(500,60));
        buttonsPanel.setMaximumSize(new Dimension(500,60));
        buttonsPanel.setPreferredSize(new Dimension(500,60));

        viewButton = new JButton("View");
        viewButton.addActionListener(this);
        viewButton.setEnabled(false);
        removeButton = new JButton("Remove");
        removeButton.addActionListener(this);
        removeButton.setEnabled(false);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);

        getRootPane().setDefaultButton(viewButton);

        buttonsPanel.add(viewButton);
        buttonsPanel.add(removeButton);
        buttonsPanel.add(cancelButton);

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        getContentPane().add(scrollPane);
        getContentPane().add(buttonsPanel);

        sl.putConstraint(SpringLayout.NORTH,scrollPane,0,SpringLayout.NORTH,getContentPane());
        sl.putConstraint(SpringLayout.WEST,scrollPane,0,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,buttonsPanel,0,SpringLayout.SOUTH,scrollPane);
        sl.putConstraint(SpringLayout.WEST,buttonsPanel,0,SpringLayout.WEST,getContentPane());
    }

    public void updateFrame() {
        reportListModel.clear();
        for(int i = 0; i < owner.getReports().size(); i++) {
            reportListModel.addElement(owner.getReports().get(i));
        }
        reportList.updateUI();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == cancelButton) {
            dispose();
        }
        else if(e.getSource() == removeButton) {
            if(reportListModel.isEmpty() || reportList.getSelectedIndex() < 0) {
                return;
            }
			
            // if only one card to delete
            int index = reportList.getSelectedIndex();

            if(index < reportListModel.getSize()-1) { // not the last one
                reportListModel.remove(index);
                owner.getReports().remove(index);
                reportList.setSelectedIndex(index);
            }
            else { // if this is the last report
                reportList.setSelectedIndex(index-1);
                reportListModel.remove(index);
                owner.getReports().remove(index);
            }
        }
        else if(e.getSource() == viewButton) {
            if(reportList.getSelectedIndex() > -1) {
                ReviewSessionReport rsr = owner.getReports().get(reportList.getSelectedIndex());
				
                JFrame report = new JFrame("Notecard Review Session Report - " + rsr.toString());
                report.setSize(768,626); //636
                report.setResizable(false);
                SpringLayout sl = new SpringLayout();
                report.setLayout(sl);

                ReviewSessionPanel rsp = new ReviewSessionPanel(rsr);

                report.add(rsp);
                report.add(owner.getPrintSessionPanel());
                report.add(owner.getCopyrightPanel());

                sl.putConstraint(SpringLayout.NORTH,rsp,0,SpringLayout.NORTH,report.getContentPane());
                sl.putConstraint(SpringLayout.WEST,rsp,0,SpringLayout.WEST,report.getContentPane());

                sl.putConstraint(SpringLayout.NORTH,owner.getPrintSessionPanel(),0,SpringLayout.SOUTH,rsp);
                sl.putConstraint(SpringLayout.WEST,owner.getPrintSessionPanel(),0,SpringLayout.WEST,report.getContentPane());

                sl.putConstraint(SpringLayout.NORTH,owner.getCopyrightPanel(),0,SpringLayout.SOUTH,owner.getPrintSessionPanel());
                sl.putConstraint(SpringLayout.WEST,owner.getCopyrightPanel(),0,SpringLayout.WEST,report.getContentPane());

                report.setLocationRelativeTo(null);
                report.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                report.setVisible(true);
            }
        }
    }

    public void valueChanged(ListSelectionEvent e) {
        if(reportList.getSelectedIndex() > -1) {
            viewButton.setEnabled(true);
            removeButton.setEnabled(true);
        }
        else {
            viewButton.setEnabled(false);
            removeButton.setEnabled(false);
        }
    }
}

package conjugationstation.conjugationreviewer.reports;

import javax.swing.*;
import java.awt.*;
import conjugationstation.conjugationreviewer.*;

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
 * Panel for report actions from conjugation practice
 * @author Michael Hergenrader
 */
public class ReportButtonsPanel extends JPanel {

    private ConjugationReviewFrame owner;

    private JButton saveReport;
    private JButton printReport;

    public ReportButtonsPanel(ConjugationReviewFrame owner) {
        this.owner = owner;
        
        setMinimumSize(new Dimension(630,40));
        setMaximumSize(new Dimension(630,40));
        setPreferredSize(new Dimension(630,40));

        saveReport = new JButton("Save Report");
        saveReport.addActionListener(owner);
        printReport = new JButton("Print Report");
        printReport.addActionListener(owner);
        add(saveReport);
        add(printReport);        
    }

    public JButton getSaveReportButton() {
        return saveReport;
    }
    public JButton getPrintReportButton() {
        return printReport;
    }

    public void disableSaveButton() {
        saveReport.setText("Saved!");
        saveReport.setEnabled(false);
    }

    public void restoreSaveButton() {
        saveReport.setText("Save Report");
        saveReport.setEnabled(true);
    }
}

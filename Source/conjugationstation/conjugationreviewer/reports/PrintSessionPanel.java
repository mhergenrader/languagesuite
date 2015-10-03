package conjugationstation.conjugationreviewer.reports;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
 * Panel for allowing the user to print out the report on the results frame for conjugations
 * @author Michael Hergenrader
 */
public class PrintSessionPanel extends JPanel { // just a print button, no save button

    private ConjugationReviewFrame owner;
    private JButton printReport;

    public PrintSessionPanel(ConjugationReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(630,40));
        setMaximumSize(new Dimension(630,40));
        setPreferredSize(new Dimension(630,40));

        printReport = new JButton("Print Report");
        printReport.addActionListener(owner);
        add(printReport);
    }

    public JButton getPrintReportButton() {
        return printReport;
    }
}

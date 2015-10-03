package conjugationstation.conjugationreviewer.reports;

import javax.swing.*;
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
 * Provides legalese copyright information regarding the results of the report (just to make it more official, really)
 * @author Michael Hergenrader
 */
public final class CopyrightPanel extends JPanel {

    private JLabel infoLabel;
    private JLabel copyrightLabel;

    public CopyrightPanel() {
        setMinimumSize(new Dimension(630,40));
        setMaximumSize(new Dimension(630,40));
        setPreferredSize(new Dimension(630,40));

        infoLabel = new JLabel("Session Report generated by Language Suite Conjugation Station");
        copyrightLabel = new JLabel("Copyright © 2010 Michael Hergenrader");
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);

        setLayout(new GridLayout(2,1));
        add(infoLabel);
        add(copyrightLabel);
    }
}
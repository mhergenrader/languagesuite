package languagesuite;

import java.awt.Dimension;
import javax.swing.*;

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
 * Container help panel for multiple modules
 * @author Michael Hergenrader
 */
public class HelpPanel extends JPanel {

    private JTabbedPane tabbedPane;

    private GeneralHelpPanel generalHelp;
    private ConjugationEditorHelpPanel ceHelpPanel;
    private ConjugationReviewerHelpPanel crHelpPanel;
    private NotecardEditorHelpPanel neHelpPanel;
    private NotecardReviewerHelpPanel nrHelpPanel;
    
    public HelpPanel() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setMinimumSize(new Dimension(380,340));
        tabbedPane.setMaximumSize(new Dimension(380,340));
        tabbedPane.setPreferredSize(new Dimension(380,340));

        generalHelp = new GeneralHelpPanel();
        ceHelpPanel = new ConjugationEditorHelpPanel();
        crHelpPanel = new ConjugationReviewerHelpPanel();
        neHelpPanel = new NotecardEditorHelpPanel();
        nrHelpPanel = new NotecardReviewerHelpPanel();

        tabbedPane.add(generalHelp,"General");
        tabbedPane.add(ceHelpPanel,"Conjugation Editor");
        tabbedPane.add(crHelpPanel,"Conjugation Reviewer");
        tabbedPane.add(neHelpPanel,"Notecard Editor");
        tabbedPane.add(nrHelpPanel,"Notecard Reviewer");

        add(tabbedPane);
    }
}

package conjugationstation.conjugationreviewer.settings;

import conjugationstation.conjugationreviewer.*;
import javax.swing.*;
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
 * Overall separate (new) frame for all conjugation practice settings
 * @author Michael Hergenrader
 */
public class SettingsFrame extends JFrame {
    private ConjugationReviewFrame owner;
    
    private JTabbedPane tabbedPane;

    private ListsPanel listsPanel;

    private JScrollPane practiceScrollPane;    
    private ConjugationStationSettingsPanel cssp;

    public SettingsFrame(ConjugationReviewFrame owner) {
        super("Conjugation Station Settings");
        setIconImage(LanguageSuite.frameIcon);
        
        this.owner = owner;       

        listsPanel = new ListsPanel(owner);

        cssp = new ConjugationStationSettingsPanel();
        practiceScrollPane = new JScrollPane(cssp);

        tabbedPane = new JTabbedPane();

        tabbedPane.add(listsPanel,"Select Verbs");
        tabbedPane.add(practiceScrollPane,"Practice Settings");

        add(tabbedPane);
    }

    public ListsPanel getListsPanel() {
        return listsPanel;
    }

    public boolean shouldClearAnswer() {
        return cssp.getClearAnswerBox().isSelected();
    }
    public void setShouldClearAnswer(boolean value) {
        cssp.getClearAnswerBox().setSelected(value);
    }

    public boolean shouldStopClock() {
        return cssp.getStopClockBox().isSelected();
    }
    public void setShouldStopClock(boolean value) {
        cssp.getStopClockBox().setSelected(value);
    }

    public boolean shouldShuffleAtEnd() {
        return cssp.getShuffleAtEndBox().isSelected();
    }
    public void setShouldShuffleAtEnd(boolean value) {
        cssp.getShuffleAtEndBox().setSelected(value);
    }

    public boolean shouldSkipWrongAnswers() {
        return cssp.getSkipWrongAnswersBox().isSelected();
    }
    public void setShouldSkipWrongAnswers(boolean value) {
        cssp.getSkipWrongAnswersBox().setSelected(value);
    }

    public boolean shouldUseCaseSensitivity() {
        return cssp.getCaseSensitivityBox().isSelected();
    }
    public void setShouldUseCaseSensitivity(boolean value) {
        cssp.getCaseSensitivityBox().setSelected(value);
    }
}
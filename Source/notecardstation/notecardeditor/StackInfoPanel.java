package notecardstation.notecardeditor;

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
 * High level metadata for notecard stacks shown on this panel
 * @author Michael Hergenrader
 */
public class StackInfoPanel extends JPanel {

    private NotecardStackEditorFrame owner;

    private StackTopLevelInfoPanel stackDataPanel;
    private StackSideTitlesPanel sideTitlesPanel;
    
    public StackInfoPanel(NotecardStackEditorFrame owner) {
        this.owner = owner;

        setBackground(Color.DARK_GRAY.brighter());

        stackDataPanel = new StackTopLevelInfoPanel(owner);
        sideTitlesPanel = new StackSideTitlesPanel(owner);

        FlowLayout fl = new FlowLayout();
        setLayout(fl);
        fl.setHgap(20);

        add(stackDataPanel);
        add(sideTitlesPanel);
    }

    public void addComponentsToOrder() {
        stackDataPanel.addComponentsToOrder();
        sideTitlesPanel.addComponentsToOrder();
    }

    public StackTopLevelInfoPanel getStackDataPanel() {
        return stackDataPanel;
    }

    public StackSideTitlesPanel getStackSideTitlesPanel() {
        return sideTitlesPanel;
    }
}

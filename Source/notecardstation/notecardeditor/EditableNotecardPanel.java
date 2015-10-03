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
 * Custom JPanel that represents a notecard side in which side data is input
 * @author Michael Hergenrader
 */
public class EditableNotecardPanel extends NotecardPanel {
    private JTextField sideEntry;

    private NotecardDataPanel owner;

    public EditableNotecardPanel(NotecardDataPanel owner) {
        this.owner = owner;

        setLayout(new BorderLayout());
        sideEntry = new JTextField(20);
        sideEntry.setHorizontalAlignment(SwingConstants.CENTER);
        sideEntry.setBorder(BorderFactory.createEmptyBorder()); // removes a border so just looks like a notecard without an inner text field
        sideEntry.setDocument(new LengthLimitDocument(58));

        sideEntry.addMouseListener(owner);

        add(sideEntry,BorderLayout.CENTER);
    }

    public JTextField getSideTextBox() {
        return sideEntry;
    }

    public void setSideTextBox(String text) {
        sideEntry.setText(text);
    }
}

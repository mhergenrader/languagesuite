package notecardstation.notecardreviewer.general;

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
 * Helper panel for allowing users to submit their entry or add an accent to the final character
 * @author Michael Hergenrader
 */
public class UserButtonsPanel extends JPanel {

    private NotecardStackReviewFrame owner;

    private JButton enterButton;
    private JButton addAccentButton;

    public UserButtonsPanel(NotecardStackReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(400,50));
        setMaximumSize(new Dimension(400,50));
        setPreferredSize(new Dimension(400,50));
        setBackground(Color.DARK_GRAY.brighter());

        initializeButtons();
        add(enterButton);
        add(addAccentButton);
    }

    private void initializeButtons() {
        enterButton = new JButton("Enter");
        enterButton.setEnabled(false);
        addAccentButton = new JButton("Add Accent");
        addAccentButton.setEnabled(false);

        enterButton.addActionListener(owner);
        addAccentButton.addActionListener(owner);
    }

    public void setEnterButtonEnabledState(boolean value) {
        enterButton.setEnabled(value);
    }

    public void setAddAccentButtonEnabledState(boolean value) {
        addAccentButton.setEnabled(value);
    }

    public JButton getEnterButton() {
        return enterButton;
    }

    public JButton getAddAccentButton() {
        return addAccentButton;
    }
}

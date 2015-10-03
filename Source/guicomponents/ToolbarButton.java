package guicomponents;

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
 * Custom toolbar button class for housing square images for any of the Language Suite modules
 * @author Michael Hergenrader
 */
public class ToolbarButton extends JButton { // specialized button

    public ToolbarButton(String toolTipText) {
        setBorderPainted(false);
        setMinimumSize(new Dimension(50,50));
        setMaximumSize(new Dimension(50,50));
        setPreferredSize(new Dimension(50,50));
        setOpaque(false);
        super.setToolTipText(toolTipText);
    }

    public ToolbarButton(String toolTipText, String imageLocation) {
        this(toolTipText);
        setIcon(new ImageIcon(imageLocation));
    }

    private ToolbarButton() {
    }

    public void setToolTipText(String text) {
        super.setToolTipText(text);
    }

    public void setIcon(String imageLocation) {
        setIcon(new ImageIcon(imageLocation));
    }
}

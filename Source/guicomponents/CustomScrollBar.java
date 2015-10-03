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
 * Custom styled scrollbar for use in multiple components
 * @author Michael Hergenrader
 */
public class CustomScrollBar {
    public static JScrollBar createCustomScrollBar() {
        JScrollBar scrollBar = new JScrollBar(JScrollBar.VERTICAL);

        scrollBar.setUI(new javax.swing.plaf.metal.MetalScrollBarUI() {
            @Override
            protected void paintThumb(Graphics g,JComponent c,Rectangle thumb) {
                g.setColor(Color.DARK_GRAY.brighter());
                g.fillRect(thumb.x,thumb.y,thumb.width-1,thumb.height-1);
                g.setColor(Color.WHITE);
                g.drawRoundRect(thumb.x,thumb.y,thumb.width-1,thumb.height-1,20,20);
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
                g.setColor(Color.DARK_GRAY.brighter());
                g.fillRect(trackBounds.x,trackBounds.y,trackBounds.width,trackBounds.height);
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                java.awt.Dimension prefSize = super.createIncreaseButton(orientation).getPreferredSize();

                JButton incrButton = new JButton("" + '\u25be');
                incrButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY.brighter()));
                incrButton.setPreferredSize(prefSize);
                incrButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
                incrButton.setRolloverEnabled(false);
                incrButton.setFocusable(false);
                incrButton.setBackground(Color.DARK_GRAY.brighter());
                incrButton.setForeground(Color.WHITE);
                return incrButton;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                java.awt.Dimension prefSize = super.createDecreaseButton(orientation).getPreferredSize();

                JButton decrButton = new JButton("" + '\u25b4');
                decrButton.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY.brighter()));
                decrButton.setPreferredSize(prefSize);
                decrButton.setMargin(new java.awt.Insets(0, 0, 0, 0));
                decrButton.setRolloverEnabled(false);
                decrButton.setFocusable(false);
                decrButton.setBackground(Color.DARK_GRAY.brighter());
                decrButton.setForeground(Color.WHITE);
                return decrButton;
            }
        });

        scrollBar.setOpaque(false);

        for(java.awt.Component c : scrollBar.getComponents()) {
            ((JComponent)c).setBackground(Color.DARK_GRAY.brighter());
        }

        return scrollBar;
    }
}

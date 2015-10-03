package conjugationstation.conjugationreviewer;

import javax.swing.*;
import java.awt.event.*;
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
 * Special purpose dialog used to make the clock stop after an answer entered
 * @author Michael Hergenrader
 */
public class TimerDialog extends JDialog implements WindowListener {

    private JButton confirm;
    private JTextField message;
    private JFrame owner;
    
    public TimerDialog(JFrame owner, String title, boolean modal) {
        super(owner,title,modal);
        this.owner = owner;

        addWindowListener(this);
        
        DisposeAction dispose = new DisposeAction();

        confirm = new JButton("OK");
        confirm.addActionListener(dispose);
        
        message = new JTextField(40);
        message.setBorder(BorderFactory.createEmptyBorder());
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setEditable(false);
        message.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        message.getActionMap().put("pressed",dispose);
        message.setAction(dispose);

        JPanel messagePanel = new JPanel();
        messagePanel.setMinimumSize(new Dimension(300,70));
        messagePanel.setMaximumSize(new Dimension(300,70));
        messagePanel.setPreferredSize(new Dimension(300,70));
        messagePanel.add(message);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setMinimumSize(new Dimension(300,50));
        buttonPanel.setMaximumSize(new Dimension(300,50));
        buttonPanel.setPreferredSize(new Dimension(300,50));
        buttonPanel.add(confirm);

        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(messagePanel);
        add(buttonPanel);

        sl.putConstraint(SpringLayout.NORTH,messagePanel,0,SpringLayout.NORTH,getContentPane());
        sl.putConstraint(SpringLayout.WEST,messagePanel,0,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,buttonPanel,0,SpringLayout.SOUTH,messagePanel);
        sl.putConstraint(SpringLayout.WEST,buttonPanel,0,SpringLayout.WEST,getContentPane());
    }

    public void setTitle(String title) {
        super.setTitle(title);
    }

    public void setMessage(String message) {
        this.message.setText(message);
        this.message.requestFocusInWindow();
    }
	
    class DisposeAction extends AbstractAction {
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == message || ae.getSource() == confirm) {
                if(owner instanceof ConjugationReviewFrame) {
                    if(((ConjugationReviewFrame)owner).isRunning()) {
                        ((ConjugationReviewFrame)owner).setRunning(true); // no matter what set it running again
                    }
                }
                dispose();
            }
        }
    }

    public void setMessageInFocus() {
        message.requestFocusInWindow();
    }

    public void windowClosing(WindowEvent e) { // if user uses the mouse to close the window with the upper right [x]
        ((ConjugationReviewFrame)owner).setRunning(true);
    }
	// Other necessary override methods
    public void windowClosed(WindowEvent e) {
    }
    public void windowOpened(WindowEvent e) {
    }
    public void windowIconified(WindowEvent e) {
    }
    public void windowDeiconified(WindowEvent e) {
    }
    public void windowActivated(WindowEvent e) {
    }
    public void windowDeactivated(WindowEvent e) {
    }
}

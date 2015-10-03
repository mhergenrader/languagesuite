package notecardstation.notecardreviewer.general;

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
 * Special-purpose dialog used to make the clock stop after an answer entered (announcing whether user correct or not)
 * @author Michael Hergenrader
 */
public class TimerDialog extends JDialog implements WindowListener {
    private JPanel dialogPanel;

    private JPanel buttonPanel;
    private JButton confirm;
    private JTextField message;
    private JTextField messageLine2;
    private JFrame owner;
    
    public TimerDialog(JFrame owner, String title, boolean modal) {
        super(owner,title,modal);
        this.owner = owner;

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        DisposeAction dispose = new DisposeAction();

        dialogPanel = new JPanel();

        confirm = new JButton("OK");
        confirm.addActionListener(dispose);
        
        message = new JTextField(10);
        message.setHorizontalAlignment(SwingConstants.CENTER);
        message.setEditable(false);
        message.setBorder(BorderFactory.createEmptyBorder());
        messageLine2 = new JTextField(10);
        messageLine2.setHorizontalAlignment(SwingConstants.CENTER);
        messageLine2.setEditable(false);
        messageLine2.setBorder(BorderFactory.createEmptyBorder());

        message.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        message.getActionMap().put("pressed",dispose);
        message.setAction(dispose);
        messageLine2.getInputMap().put(KeyStroke.getKeyStroke("ENTER"),"pressed");
        messageLine2.getActionMap().put("pressed",dispose);
        messageLine2.setAction(dispose);

        buttonPanel = new JPanel();
        buttonPanel.add(confirm);

        dialogPanel.setLayout(new BoxLayout(dialogPanel,BoxLayout.Y_AXIS));
        dialogPanel.add(message);
        dialogPanel.add(messageLine2);
        dialogPanel.add(buttonPanel);

        addWindowListener(this);

        add(dialogPanel);
    }

    public void setTitle(String title) {
        super.setTitle(title);
    }

    public void setMessage(String message) {
        this.message.setText(message);
        confirm.requestFocusInWindow();
    }

    public void setMessageLine2(String message) {
        this.messageLine2.setText(message);
        confirm.requestFocusInWindow();
    }

    class DisposeAction extends AbstractAction {
        public void actionPerformed(ActionEvent ae) {
            if(ae.getSource() == message || ae.getSource() == confirm) {
                if(owner instanceof NotecardStackReviewFrame) {
                    if(((NotecardStackReviewFrame)owner).isRunning()) {
                        ((NotecardStackReviewFrame)owner).setTimerRunning(true);
                    }
                }
                dispose();
            }
        }
    }

    public void setMessageInFocus() {
        message.requestFocusInWindow();
    }

    public void setMessageLine2InFocus() {
        messageLine2.requestFocusInWindow();
    }

    public void windowClosing(WindowEvent e) {
        ((NotecardStackReviewFrame)owner).setTimerRunning(true);
    }
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

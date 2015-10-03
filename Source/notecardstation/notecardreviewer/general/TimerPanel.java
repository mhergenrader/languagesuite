package notecardstation.notecardreviewer.general;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.text.MaskFormatter;

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
 * Displays the current time during notecard review sessions
 * @author Michael Hergenrader
 */
public class TimerPanel extends JPanel {
    private NotecardStackReviewFrame owner;

    private ButtonGroup optionsButtonGroup;
    private JRadioButton timed;
    private JRadioButton notTimed;
    private JCheckBox onePass;

    private JFormattedTextField timerEntry;
    private java.util.List<TimeToken> timeTokens;

    private static final int MINUTES_INDEX = 0;
    private static final int SECONDS_INDEX = 1;

    private JLabel timerLabel;

    public TimerPanel(NotecardStackReviewFrame owner) {
        this.owner = owner;

        setMinimumSize(new Dimension(100,100));
        setMaximumSize(new Dimension(100,100));
        setPreferredSize(new Dimension(100,100));
        setBackground(Color.DARK_GRAY.brighter());

        timed = new JRadioButton("Timed");
        timed.setBackground(Color.DARK_GRAY.brighter());
        timed.setForeground(Color.WHITE);
        timed.setToolTipText("Timed practice or test based on the clock entry above.");
        timed.setSelected(true);
        timed.addActionListener(owner);
        notTimed = new JRadioButton("Not Timed");
        notTimed.setBackground(Color.DARK_GRAY.brighter());
        notTimed.setForeground(Color.WHITE);
        notTimed.addActionListener(owner);
        notTimed.setToolTipText("Infinite practice until the stop button is pressed.");
        onePass = new JCheckBox("One Pass?");
        onePass.setBackground(Color.DARK_GRAY.brighter());
        onePass.setForeground(Color.WHITE);
        onePass.setToolTipText("Not timed: go through the chosen notecards once.");
        onePass.setEnabled(false);
        onePass.setVisible(false);

        ButtonGroup optionsButtonGroup = new ButtonGroup();
        optionsButtonGroup.add(timed);
        optionsButtonGroup.add(notTimed);

        timeTokens = new ArrayList<TimeToken>();

        timerEntry = new JFormattedTextField(createMask("##:##"));
        timerEntry.setFocusLostBehavior(JFormattedTextField.PERSIST);
        timerEntry.setHorizontalAlignment(SwingConstants.CENTER);

        timerLabel = new JLabel("Enter time");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setFont(new Font("Serif",Font.BOLD,20));
        timerLabel.setBackground(Color.DARK_GRAY.brighter());
        timerLabel.setForeground(Color.WHITE);

        setLayout(new GridLayout(5,1));
        add(timerLabel);
        add(timerEntry);
        add(timed);
        add(notTimed);
        add(onePass);
    }

    public void disableComponents() {
        timed.setEnabled(false);
        notTimed.setEnabled(false);
        timerEntry.setEnabled(false);
        onePass.setEnabled(false);
    }

    public void enableComponents() {
        timed.setEnabled(true);
        notTimed.setEnabled(true);
        timerEntry.setEnabled(true);
        onePass.setEnabled(true);
    }

    public JLabel getTimeLabel() {
        return timerLabel;
    }

    public JFormattedTextField getTimerEntry() {
        return timerEntry;
    }

    public Integer getMinutes() {
        return Integer.parseInt(timeTokens.get(MINUTES_INDEX).stringBuilder.toString());
    }

    public Integer getSeconds() {
        return Integer.parseInt(timeTokens.get(SECONDS_INDEX).stringBuilder.toString());
    }

    public JRadioButton getTimedButton() {
        return timed;
    }

    public JRadioButton getNotTimedButton() {
        return notTimed;
    }

    public JCheckBox getOnePassButton() {
        return onePass;
    }

    private MaskFormatter createMask(String text) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(text);
            formatter.setPlaceholderCharacter('#');
        }
        catch(java.text.ParseException e) {
            e.printStackTrace();
        }
        return formatter;
    }

    class TimeToken {
        public StringBuilder stringBuilder;
        public int numDigits;

        public static final int MAX_LENGTH = 2;

        public TimeToken(String string) {
            this.stringBuilder = new StringBuilder(string);
            numDigits = 0;
        }

        public String toString() {
            return stringBuilder.toString() + ", " + numDigits + " digits.";
        }

        public void setString(String s) {
            stringBuilder.delete(0,stringBuilder.length()); // clear
            stringBuilder.append(s);
        }

        public boolean equalsZero() {
            if(stringBuilder.toString().charAt(0) == '0') {
                if(stringBuilder.toString().charAt(1) == '#' || stringBuilder.toString().charAt(1) == '0') {
                    return true;
                }
            }
            else if(stringBuilder.toString().charAt(0) == '#') {
                if(stringBuilder.toString().charAt(1) == '0') {
                    return true;
                }
            }
            return false;
        }
    }

    public String makeGoodFormat() throws NumberFormatException {
        timeTokens.clear();

        String s = timerEntry.getText();
        StringTokenizer st = new StringTokenizer(s,":");

        while(st.hasMoreTokens()) {
            TimeToken tt = new TimeToken(st.nextToken());

            for(int k = 0; k < TimeToken.MAX_LENGTH; k++) {
                if(Character.isDigit(tt.stringBuilder.toString().charAt(k))) {
                    tt.numDigits++;
                }
            }

            timeTokens.add(tt);
        }

        if(timeTokens.get(MINUTES_INDEX).equalsZero() && timeTokens.get(SECONDS_INDEX).equalsZero()) {
            JOptionPane.showMessageDialog(owner,"Timer has no minutes and no seconds.","Timer cannot start!",JOptionPane.ERROR_MESSAGE);
            throw new NumberFormatException("00:00");
        }
		
        switch(timeTokens.get(MINUTES_INDEX).numDigits) {
            case 0:
                if(timeTokens.get(SECONDS_INDEX).numDigits == 0) {
                    timeTokens.get(MINUTES_INDEX).setString("01");
                    timeTokens.get(SECONDS_INDEX).setString("00");
                }
                else if(timeTokens.get(SECONDS_INDEX).equalsZero()) {
                    JOptionPane.showMessageDialog(owner,"Please enter a minutes value.","Timer cannot start!",JOptionPane.ERROR_MESSAGE);
                    throw new NumberFormatException("##:00");
                }
                break;
            case 1:
                if(timeTokens.get(MINUTES_INDEX).stringBuilder.toString().charAt(0) == '#') {
                    timeTokens.get(MINUTES_INDEX).setString("0"+timeTokens.get(MINUTES_INDEX).stringBuilder.toString().charAt(1));
                }
                else {
                    timeTokens.get(MINUTES_INDEX).setString("0"+timeTokens.get(MINUTES_INDEX).stringBuilder.toString().charAt(0));
                }
                break;
        }
		
        switch(timeTokens.get(SECONDS_INDEX).numDigits) {
            case 0:
                if(timeTokens.get(MINUTES_INDEX).equalsZero()) {
                    JOptionPane.showMessageDialog(owner,"Please enter a seconds value or make the minutes greater than zero.","Timer cannot start!",JOptionPane.ERROR_MESSAGE);
                    throw new NumberFormatException("00:##");
                }
                else {
                    timeTokens.get(SECONDS_INDEX).setString("00");
                }
                break;
            case 1:
                if(timeTokens.get(SECONDS_INDEX).stringBuilder.toString().charAt(0) == '#') {
                    timeTokens.get(SECONDS_INDEX).setString("0"+timeTokens.get(SECONDS_INDEX).stringBuilder.toString().charAt(1));
                }
                else {
                    timeTokens.get(SECONDS_INDEX).setString("0"+timeTokens.get(SECONDS_INDEX).stringBuilder.toString().charAt(0));
                }
                if(timeTokens.get(MINUTES_INDEX).numDigits == 0) {
                    timeTokens.get(MINUTES_INDEX).setString("00");
                }
                break;
            case 2:
                if(timeTokens.get(MINUTES_INDEX).equalsZero() && timeTokens.get(SECONDS_INDEX).equalsZero()) {
                    JOptionPane.showMessageDialog(owner,"Timer has no minutes and no seconds.","Timer cannot start!",JOptionPane.ERROR_MESSAGE);
                    throw new NumberFormatException("00:00");
                }
                else {
                    try {
                        int seconds = Integer.valueOf(timeTokens.get(SECONDS_INDEX).stringBuilder.toString()).intValue();
                        if(timeTokens.get(MINUTES_INDEX).numDigits == 0) {
                            timeTokens.get(MINUTES_INDEX).setString("00");
                        }

                        if(seconds > 59) {
                            int newMinutes = Integer.valueOf(timeTokens.get(MINUTES_INDEX).stringBuilder.toString()).intValue();

                            
                            if(newMinutes == 99) {
                                JOptionPane.showMessageDialog(owner,"Please enter a time under 99:59","Timer cannot start!",JOptionPane.ERROR_MESSAGE);
                                throw new NumberFormatException("99");
                            }

                            newMinutes++;

                            if(newMinutes < 10) {
                                timeTokens.get(MINUTES_INDEX).setString("0"+newMinutes);
                            }
                            else {
                                timeTokens.get(MINUTES_INDEX).setString(String.valueOf(newMinutes));
                            }

                            seconds -= 60;
                            if(seconds < 10) {
                                timeTokens.get(SECONDS_INDEX).setString("0"+seconds);
                            }
                            else {
                                timeTokens.get(SECONDS_INDEX).setString(String.valueOf(seconds));
                            }
                        }
                    }
                    catch(NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }

        return (timeTokens.get(MINUTES_INDEX).stringBuilder.toString() + ":" + timeTokens.get(SECONDS_INDEX).stringBuilder.toString());
    }

}

package notecardstation.notecardeditor;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;

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
 * Higher level view of all sides of a notecard - allows user to swap and rotate side data and see the entire notecard object
 * @author Michael Hergenrader
 */
public class NotecardDataPanel extends JPanel implements MouseListener {

    private NotecardStackEditorFrame owner;

    private EditableNotecardPanel side1;
    private EditableNotecardPanel side2;
    private EditableNotecardPanel side3;

    private JPopupMenu popupMenu;
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem paste;
    private JMenuItem swapContentsLeft;
    private JMenuItem swapContentsRight;

    private JLabel side1Label;
    private JLabel side2Label;
    private JLabel side3Label;

    public NotecardDataPanel(NotecardStackEditorFrame owner) {
        this.owner = owner;

        setBackground(Color.DARK_GRAY.brighter());

        setMinimumSize(new Dimension(850,500));
        setMaximumSize(new Dimension(850,500));
        setPreferredSize(new Dimension(850,500));

        SpringLayout sl = new SpringLayout();
        setLayout(sl);
		
        initializePopupMenu();
		
        side1 = new EditableNotecardPanel(this);
        side2 = new EditableNotecardPanel(this);
        side3 = new EditableNotecardPanel(this);

        side1.addMouseListener(this);
        side2.addMouseListener(this);
        side3.addMouseListener(this);

        side1.getSideTextBox().addFocusListener(owner);
        side1.getSideTextBox().getDocument().addDocumentListener(owner);
		
        side2.getSideTextBox().addFocusListener(owner);
        side2.getSideTextBox().getDocument().addDocumentListener(owner);
		
        side3.getSideTextBox().addFocusListener(owner);
        side3.getSideTextBox().getDocument().addDocumentListener(owner);

        side3.setVisible(false);
		
        side1Label = new JLabel("Side 1");
        side1Label.setForeground(Color.WHITE);
        side1Label.setHorizontalAlignment(SwingConstants.CENTER);
        side1Label.setMinimumSize(new Dimension(400,15));
        side1Label.setMaximumSize(new Dimension(400,15));
        side1Label.setPreferredSize(new Dimension(400,15));
        side2Label = new JLabel("Side 2");
        side2Label.setForeground(Color.WHITE);
        side2Label.setHorizontalAlignment(SwingConstants.CENTER);
        side2Label.setMinimumSize(new Dimension(400,15));
        side2Label.setMaximumSize(new Dimension(400,15));
        side2Label.setPreferredSize(new Dimension(400,15));
        side3Label = new JLabel("Side 3");
        side3Label.setForeground(Color.WHITE);
        side3Label.setHorizontalAlignment(SwingConstants.CENTER);
        side3Label.setMinimumSize(new Dimension(400,15));
        side3Label.setMaximumSize(new Dimension(400,15));
        side3Label.setPreferredSize(new Dimension(400,15));
        side3Label.setVisible(false);

        add(side1Label);
        add(side1);
        add(side2Label);
        add(side2);
        add(side3Label);
        add(side3);

        sl.putConstraint(SpringLayout.WEST,side1Label,10,SpringLayout.WEST,this);
        sl.putConstraint(SpringLayout.NORTH,side1Label,10,SpringLayout.NORTH,this);

        sl.putConstraint(SpringLayout.WEST,side1,10,SpringLayout.WEST,this);
        sl.putConstraint(SpringLayout.NORTH,side1,0,SpringLayout.SOUTH,side1Label);

        sl.putConstraint(SpringLayout.EAST,side2Label,-10,SpringLayout.EAST,this);
        sl.putConstraint(SpringLayout.NORTH,side2Label,10,SpringLayout.NORTH,this);

        sl.putConstraint(SpringLayout.EAST,side2,-10,SpringLayout.EAST,this);
        sl.putConstraint(SpringLayout.NORTH,side2,0,SpringLayout.SOUTH,side2Label);

        sl.putConstraint(SpringLayout.WEST,side3Label,225,SpringLayout.WEST,this);
        sl.putConstraint(SpringLayout.NORTH,side3Label,241,SpringLayout.NORTH,this);

        sl.putConstraint(SpringLayout.WEST,side3,225,SpringLayout.WEST,this);
        sl.putConstraint(SpringLayout.NORTH,side3,0,SpringLayout.SOUTH,side3Label);
    }

    public JTextField getSide1TextBox() {
        return side1.getSideTextBox();
    }

    public JTextField getSide2TextBox() {
        return side2.getSideTextBox();
    }

    public JTextField getSide3TextBox() {
        return side3.getSideTextBox();
    }

    private void initializePopupMenu() {
        popupMenu = new JPopupMenu();
        cut = new JMenuItem(new DefaultEditorKit.CutAction());
        cut.setText("Cut");
        copy = new JMenuItem(new DefaultEditorKit.CopyAction());
        copy.setText("Copy");
        paste = new JMenuItem(new DefaultEditorKit.PasteAction());
        paste.setText("Paste");
        swapContentsLeft = new JMenuItem("Swap Contents Left");
        swapContentsLeft.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               if(!side3.isVisible()) {
                   swapTwoSides();
               }
               else {
                   swapThreeSidesLeft();
               }
           }
        });

        swapContentsRight = new JMenuItem("Swap Contents Right");
        swapContentsRight.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent ae) {
               if(!side3.isVisible()) {
                   swapTwoSides();
               }
               else {
                   swapThreeSidesRight();
               }
           }
        });

        popupMenu.add(cut);
        popupMenu.add(copy);
        popupMenu.add(paste);
        popupMenu.addSeparator();
        popupMenu.add(swapContentsLeft);
        popupMenu.add(swapContentsRight);


    }
	
    private void swapTwoSides() {
        String s = side1.getSideTextBox().getText();
        side1.getSideTextBox().setText(side2.getSideTextBox().getText());
        side2.getSideTextBox().setText(s);
    }
	
	// rotation operations
    private void swapThreeSidesLeft() {
        String s = side1.getSideTextBox().getText();
        side1.getSideTextBox().setText(side2.getSideTextBox().getText());
        side2.getSideTextBox().setText(side3.getSideTextBox().getText());
        side3.getSideTextBox().setText(s);
    }

    private void swapThreeSidesRight() {
        String s = side1.getSideTextBox().getText();
        side1.getSideTextBox().setText(side3.getSideTextBox().getText());
        side3.getSideTextBox().setText(side2.getSideTextBox().getText());
        side2.getSideTextBox().setText(s);
    }

    public void showThirdSide(boolean value) {
        side3.setVisible(value);
        side3Label.setVisible(value);
    }

    public void addComponentsToOrder() {
        owner.getComponentVector().add(side1.getSideTextBox());
        owner.getComponentVector().add(side2.getSideTextBox());
    }

    public void mouseClicked(MouseEvent me) {
        if(me.getSource() == side1) {
        }
        else if(me.getSource() == side2) {
        }
        else if(me.getSource() == side3) {
        }
    }
	
    public void mouseEntered(MouseEvent me) { // methods that probably aren't needed but must be defined
    }
	
    public void mouseExited(MouseEvent me) {
    }
	
    public void mousePressed(MouseEvent me) {
        me.getComponent().requestFocusInWindow();
        if(me.isPopupTrigger()) {
            popupMenu.show(me.getComponent(),me.getX(),me.getY());
        }
    }
    public void mouseReleased(MouseEvent me) {
        if(me.isPopupTrigger()) {
            popupMenu.show(me.getComponent(),me.getX(),me.getY());
        }
    }

    public void setSide1LabelText(String value) {
        side1Label.setText(value);
    }
	
    public void setSide2LabelText(String value) {
        side2Label.setText(value);
    }
	
    public void setSide3LabelText(String value) {
        side3Label.setText(value);
    }



}

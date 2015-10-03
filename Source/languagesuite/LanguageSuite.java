package languagesuite;

import conjugationstation.ConjugationStation;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import notecardstation.general.NotecardStation;

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
 * Main launcher class for the Language Suite program
 * @author Michael Hergenrader
 */
public class LanguageSuite extends JFrame {
    private static final java.awt.Dimension frameSize = new java.awt.Dimension(480,360);

    // names and locations of External Data Files (currently Windows focused)
    private File languageSuiteDataDirectory;
    public static final String defaultDataLocation = System.getProperty("user.home") + "\\Language Suite Data\\";
    public static final String defaultImageLocation = System.getProperty("user.home") + "\\Documents\\NetBeansProjects\\LanguageSuite\\src\\languagesuiteimages\\";
    public static final Image frameIcon = new ImageIcon(defaultImageLocation + "languagesuiteicon.png").getImage();

	// main container modules for notecard and conjugation practice
    private JTabbedPane stationTabbedPane;
    private ConjugationStation conjugationStation;
    private NotecardStation notecardStation;

    private HelpPanel helpDummyPanel;
	
	
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        System.out.println(System.getProperty("user.home"));
        LanguageSuite mainFrame = new LanguageSuite();
        mainFrame.setSize(frameSize);
        mainFrame.setIconImage(frameIcon);
        mainFrame.setResizable(false);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
    }

    public LanguageSuite() {
        super("Language Suite by Michael Hergenrader");

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleClosingFrame();
            }
        });

        // Create the default directory for where the data files will be stored with user's data
        languageSuiteDataDirectory = new File(defaultDataLocation);
        if(!languageSuiteDataDirectory.isDirectory() && !languageSuiteDataDirectory.mkdirs()) { // if the folder doesn't already exist, try to make it - if cannot, then exit
            JOptionPane.showMessageDialog(null,"Error: could not create Language Suite data directory.","Failure to create directory.",JOptionPane.ERROR_MESSAGE);
            return;
        }

        stationTabbedPane = new JTabbedPane();

        conjugationStation = new ConjugationStation(this);
        notecardStation = new NotecardStation(this);
        helpDummyPanel = new HelpPanel();

        stationTabbedPane.add(conjugationStation,"Conjugation Station");
        stationTabbedPane.add(notecardStation,"Notecard Station");
        stationTabbedPane.add(helpDummyPanel,"Help");

        add(stationTabbedPane);
    }

    public void handleClosingFrame() {
        conjugationStation.saveLanguageSet();
        conjugationStation.saveReports();

        notecardStation.saveStacks();
        notecardStation.saveReports();
        
        System.exit(0);
    }
}

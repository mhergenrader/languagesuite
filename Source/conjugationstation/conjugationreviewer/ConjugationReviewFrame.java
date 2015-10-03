package conjugationstation.conjugationreviewer;

import conjugationstation.conjugationreviewer.settings.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import conjugationstation.conjugationcomponents.*;
import conjugationstation.conjugationreviewer.reports.*;
import conjugationstation.ConjugationStation;
import conjugationstation.ConjugationTreeObject;
import guicomponents.*;
import languagesuite.LanguageSuite;

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
 * Main review frame for the conjugation practice module
 * @author Michael Hergenrader
 */
public class ConjugationReviewFrame extends JFrame implements ActionListener {
    private ConjugationStation owner;

    enum ConjugationMode {
		PRACTICE_MODE,
		TEST_MODE
	}

    private Language language;
    private ArrayList<ConjugationTreeObject> treeObjects;
    private ArrayList<ConjugatedVerbSet> verbSets;
    private ArrayList<ConjugatedVerb> verbs;
    private ConjugatedVerb currentVerb;
    private int currentVerbIndex;
    private SubjectPronoun currentPronoun;
    private ArrayList<SubjectPronoun> pronounsToUse;

    private ConjugationMode conjugationMode;

    private Random generator;
    private static String defaultFrameTitle = "Conjugation Station";    
    private static final String defaultImageLocation = LanguageSuite.defaultImageLocation;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu controlsMenu;
    private JMenu settingsMenu;
    private JMenu helpMenu;
    private JMenuItem viewReport;
    private JMenuItem exit;
    private JMenuItem playMenuItem;
    private JMenuItem pauseMenuItem;
    private JMenuItem stopMenuItem;
    private JCheckBox practiceModeMenuItem;
    private JCheckBox testModeMenuItem;
    private ButtonGroup modeButtonGroup;
    private JMenuItem settingsMenuItem;
    private JMenuItem helpMenuItem;
    private JMenuItem aboutMenuItem;

    private JToolBar toolbar; // toolbar items
    private ToolbarButton viewReportButton;
    private ToolbarButton startButton;
    private ToolbarButton pauseButton;
    private ToolbarButton stopButton;
    private ToolbarButton addAccentButton;
    private ToolbarButton settingsButton;
    private ToolbarButton helpButton;

    private EntriesPanel entriesPanel;
    private TimerPanel timerPanel;
    private ScoresPanel scoresPanel;

    private javax.swing.Timer frameTimer;
    private int minutes;
    private int seconds;
    private boolean running; // doesn't just mean the timer - means the actual conjugation loop is running
    private boolean timed;

    private SettingsFrame settingsFrame;

    private ConjugationSessionReport report;
    private ReportListFrame reportListFrame;
    private ReportButtonsPanel reportButtonsPanel;
    private PrintSessionPanel printSessionPanel;
    private CopyrightPanel copyrightPanel;

    private TimerDialog dialog;    

    public ConjugationReviewFrame(ConjugationStation owner, ArrayList<ConjugationTreeObject> treeObjects, Language language) {
        super(defaultFrameTitle + " - " + language.toString() + " Conjugation Practice");
        setIconImage(LanguageSuite.frameIcon);

        this.owner = owner;

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                handleFrameClosing();
            }
        });
        
        this.language = language;
        this.treeObjects = new ArrayList<ConjugationTreeObject>();
        for(ConjugationTreeObject cto : treeObjects) {
            this.treeObjects.add(cto); // copy the references
        }
        this.verbSets = new ArrayList<ConjugatedVerbSet>();
        for(ConjugationTreeObject cto : this.treeObjects) {
            this.verbSets.add(new ConjugatedVerbSet(cto.getVerbSet()));
        }

        pronounsToUse = new ArrayList<SubjectPronoun>();

        currentVerbIndex = 0;
        timed = true;
        verbs = new ArrayList<ConjugatedVerb>();
        generator = new Random();
        
        conjugationMode = ConjugationMode.PRACTICE_MODE; // must initialize!

        getContentPane().setBackground(Color.DARK_GRAY.brighter());

        setupPanels();
        setupTimerDialog();
        setupMenu();
        setupToolbar();        
        setupSettingsFrame();
        setupReportsFrame();
        setupTimer();
        setupLayout();

        getRootPane().setDefaultButton(entriesPanel.getEnterButton());
    }

    public ArrayList<ConjugationSessionReport> getReports() {
        return owner.getReports();
    }

    private void saveCurrentReport() {
        owner.getReports().add(0,new ConjugationSessionReport(report));
    }

    private void setupMenu() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        controlsMenu = new JMenu("Controls");
        controlsMenu.setMnemonic(KeyEvent.VK_O);
        settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        MenuListener ml = new MenuListener();

        viewReport = new JMenuItem("View Report(s)");
        viewReport.addActionListener(ml);
        viewReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK));
        exit = new JMenuItem("Close Station");
        exit.addActionListener(ml);
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,KeyEvent.CTRL_MASK));

        fileMenu.add(viewReport);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        playMenuItem = new JMenuItem("Start");
        playMenuItem.addActionListener(this);
        pauseMenuItem = new JMenuItem("Pause"); // will change to resume when paused
        pauseMenuItem.addActionListener(this);
        pauseMenuItem.setEnabled(false);
        stopMenuItem = new JMenuItem("Stop");
        stopMenuItem.addActionListener(this);
        stopMenuItem.setEnabled(false);

        controlsMenu.add(playMenuItem);
        controlsMenu.add(pauseMenuItem);
        controlsMenu.add(stopMenuItem);
        
        modeButtonGroup = new ButtonGroup();
        practiceModeMenuItem = new JCheckBox("Practice Mode");
        practiceModeMenuItem.setSelected(true);
        practiceModeMenuItem.addActionListener(ml);
        testModeMenuItem = new JCheckBox("Test Mode");
        testModeMenuItem.addActionListener(ml);
        modeButtonGroup.add(practiceModeMenuItem);
        modeButtonGroup.add(testModeMenuItem);

        settingsMenuItem = new JMenuItem("Settings Menu...");
        settingsMenuItem.addActionListener(ml);

        settingsMenu.add(practiceModeMenuItem);
        settingsMenu.add(testModeMenuItem);
        settingsMenu.addSeparator();
        settingsMenu.add(settingsMenuItem);

        helpMenuItem = new JMenuItem("Help");
        helpMenuItem.addActionListener(this);
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
        aboutMenuItem = new JMenuItem("About");
        aboutMenuItem.addActionListener(this);

        helpMenu.add(helpMenuItem);
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(controlsMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
    private void setupToolbar() {
        toolbar = new JToolBar("Conjugation Frame Toolbar");
        toolbar.setMinimumSize(new Dimension(500,50));
        toolbar.setMaximumSize(new Dimension(500,50));
        toolbar.setPreferredSize(new Dimension(500,50));
        toolbar.setOpaque(false);
        toolbar.setFloatable(false);
        toolbar.setBorderPainted(false);

        viewReportButton = new ToolbarButton("View/Refresh reports of practice or test mode results",defaultImageLocation+"findicon_edited-1.png");
        viewReportButton.addActionListener(this);
        startButton = new ToolbarButton("Start conjugation practice",defaultImageLocation+"starticon_edited-1.png");
        startButton.addActionListener(this);
        pauseButton = new ToolbarButton("Pause current conjugation practice",defaultImageLocation+"pauseicon_edited-1.png");
        pauseButton.addActionListener(this);
        pauseButton.setEnabled(false);
        stopButton = new ToolbarButton("End the current conjugation practice and display results",defaultImageLocation+"stopicon_edited-1.png");
        stopButton.setEnabled(false);
        stopButton.addActionListener(this);
        addAccentButton = new ToolbarButton("Add accent to the final character typed",defaultImageLocation+"addaccent_edited-1.png");
        addAccentButton.addActionListener(this);
        addAccentButton.setEnabled(false);
        settingsButton = new ToolbarButton("Access Settings Menu",defaultImageLocation+"settingsicon_edited-1.png");
        settingsButton.addActionListener(this);
        helpButton = new ToolbarButton("Help",defaultImageLocation+"helpicon_edited-1.png");
        helpButton.addActionListener(this);

        toolbar.add(viewReportButton);
        toolbar.addSeparator(new Dimension(50,50));
        toolbar.add(startButton);
        toolbar.add(pauseButton);
        toolbar.add(stopButton);
        toolbar.addSeparator(new Dimension(50,50));
        toolbar.add(addAccentButton);
        toolbar.add(settingsButton);
        toolbar.addSeparator(new Dimension(50,50));
        toolbar.add(helpButton);
    }
	
    private void setupTimer() {
        frameTimer = new javax.swing.Timer(1000,new TimerListener());
        minutes = 0;
        seconds = 0;
        running = false;
    }
	
    private void setupSettingsFrame() {
        settingsFrame = new SettingsFrame(this);
        settingsFrame.setSize(560,290);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(this);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
	
    private void setupReportsFrame() {
        reportListFrame = new ReportListFrame(this); // try to have this one opened just once
        reportListFrame.setSize(510,300);
        reportListFrame.setResizable(false);
        reportListFrame.setLocationRelativeTo(this);
        reportListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
	
    private void setupPanels() {
        entriesPanel = new EntriesPanel(this);
        timerPanel = new TimerPanel(this);
        scoresPanel = new ScoresPanel(this);
        reportButtonsPanel = new ReportButtonsPanel(this);
        printSessionPanel = new PrintSessionPanel(this);
        copyrightPanel = new CopyrightPanel();
    }
	
    private void setupLayout() {
        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(toolbar);
        add(scoresPanel);
        add(entriesPanel);
        add(timerPanel);

        sl.putConstraint(SpringLayout.NORTH,toolbar,0,SpringLayout.NORTH,getContentPane());
        sl.putConstraint(SpringLayout.WEST,toolbar,0,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,scoresPanel,50,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,scoresPanel,20,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,entriesPanel,30,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,entriesPanel,150,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,timerPanel,50,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.EAST,timerPanel,-20,SpringLayout.EAST,getContentPane());//10
    }
	
    private void setupTimerDialog() {
        dialog = new TimerDialog(this,"",true);
        dialog.setMinimumSize(new Dimension(300,150));
        dialog.setMaximumSize(new Dimension(300,150));
        dialog.setPreferredSize(new Dimension(300,150));
        dialog.setLocationRelativeTo(this);
    }
    
    public ArrayList<ConjugatedVerbSet> getVerbSets() {
        return verbSets;
    }

    public Language getLanguage() {
        return language;
    }
	
    public void confirmAnswer() { // also used by entries panel
        if(isRunning()) {
            int subjectIndex = language.getSubjectPronouns().indexOf((SubjectPronoun)currentPronoun);
            String correctAnswer = currentVerb.getInflection(subjectIndex).toString();

            if(conjugationMode == ConjugationMode.PRACTICE_MODE && settingsFrame.shouldStopClock()) {
                running = false;
            }

            boolean correct; // had to pull this out to test for case-sensitivity
            if(conjugationMode == ConjugationMode.PRACTICE_MODE) {
                if(settingsFrame.shouldUseCaseSensitivity()) {
                    correct = entriesPanel.getUserAnswer().equals(correctAnswer);
                }
                else {
                    correct = entriesPanel.getUserAnswer().equalsIgnoreCase(correctAnswer);
                }
            }
            else { // test mode
                correct = entriesPanel.getUserAnswer().equals(correctAnswer);
            }

            if(correct) {
                scoresPanel.addCorrectAnswer();
                scoresPanel.updateScores();

                dialog.setTitle("Correct!");
                dialog.setMessage("Correct!");
                dialog.setMessageInFocus();
                dialog.setVisible(true);

                entriesPanel.getUserAnswerField().setText("");

                if(currentVerbIndex == verbs.size()-1) { // wraparound unless one pass selected
                    currentVerbIndex = 0;
                    if(!timed) {
                        if(timerPanel.getOnePassButton().isSelected()) {
                            endSession();
                            return;
                        }
                        else { // infinite - this can only occur in practice mode
                            if(settingsFrame.shouldShuffleAtEnd()) {
                                Collections.shuffle(verbs);
                            }
                        }
                    }
                    else if(timed) {
                        if(conjugationMode == ConjugationMode.TEST_MODE || (conjugationMode == ConjugationMode.PRACTICE_MODE && settingsFrame.shouldShuffleAtEnd())) {
                            Collections.shuffle(verbs);
                        }
                    }
                }
                else {
                    currentVerbIndex++; // always the case since user got it right
                }

                currentVerb = verbs.get(currentVerbIndex);
                currentPronoun = pronounsToUse.get(generator.nextInt(pronounsToUse.size())); // can be updated no matter what if correct
				
                entriesPanel.getRightSetLabel().setText(currentVerb.getSetReference());
                entriesPanel.getRightPronounLabel().setText(currentPronoun.toString());
                entriesPanel.getRightVerbLabel().setText(currentVerb.toString());
            }
            else {
                scoresPanel.addIncorrectAnswer();
                scoresPanel.updateScores();

                report.missedVerbs.add(new MissedVerb(currentVerb,currentPronoun,correctAnswer,entriesPanel.getUserAnswerField().getText()));

                dialog.setTitle("Incorrect!");
                dialog.setMessage("The correct answer is \"" + correctAnswer + "\".");
                dialog.setMessageInFocus();
                dialog.setVisible(true);

                if(conjugationMode == ConjugationMode.TEST_MODE) {
                    if(currentVerbIndex == verbs.size()-1) {
                        if(!timed) { // one pass through is implied here since test mode
                            endSession();
                            return;
                        }
                        else {
                            Collections.shuffle(verbs); // mandatory shuffling in test mode
                            currentVerbIndex = 0;
                        }
                    }
                    else {
                        currentVerbIndex++; // in test mode, automatically move on
                    }

                    currentVerb = verbs.get(currentVerbIndex);
                    currentPronoun = pronounsToUse.get(generator.nextInt(pronounsToUse.size())); // in a test, moves on no matter what, so update this

                    entriesPanel.getRightSetLabel().setText(currentVerb.getSetReference());
                    entriesPanel.getRightPronounLabel().setText(currentPronoun.toString());
                    entriesPanel.getRightVerbLabel().setText(currentVerb.toString());
                    entriesPanel.getUserAnswerField().setText(""); // moves on to the next verb
                }
                else { // practice mode: based on settings, clear or don't clear user entry
                    // also, if timed, and at the end, then based on settings, shuffle or don't shuffle (right now, by default, doesn't shuffle)
                    if(currentVerbIndex == verbs.size()-1) {
                        if((timed || (!timed && !timerPanel.getOnePassButton().isSelected()))) {
                            if(verbs.size() > 1 && settingsFrame.shouldSkipWrongAnswers()) {
                                entriesPanel.getUserAnswerField().setText("");
                                entriesPanel.getUserAnswerField().requestFocusInWindow(); // skipping, so just clear it so user can start on the next verb

                                if(settingsFrame.shouldShuffleAtEnd()) {
                                    Collections.shuffle(verbs);
                                }
                                currentVerbIndex = 0; // wrap around if skipping
                            }
                        }
                        else if(!timed && timerPanel.getOnePassButton().isSelected()) {
                            endSession();
                            return;
                        }
                    }
                    else {
                        if(settingsFrame.shouldSkipWrongAnswers()) {
                            currentVerbIndex++; // user can elect to move on or not
                        }
                    }

                    currentVerb = verbs.get(currentVerbIndex);

                    if(settingsFrame.shouldSkipWrongAnswers()) { // only update if user wants to skip
                        currentPronoun = pronounsToUse.get(generator.nextInt(pronounsToUse.size()));
                    }

                    entriesPanel.getRightSetLabel().setText(currentVerb.getSetReference());
                    entriesPanel.getRightPronounLabel().setText(currentPronoun.toString());
                    entriesPanel.getRightVerbLabel().setText(currentVerb.toString());
                    if(settingsFrame.shouldClearAnswer()) {
                        entriesPanel.getUserAnswerField().setText("");
                        entriesPanel.getUserAnswerField().requestFocusInWindow();
                    }                    
                }
            }
        }
    }

    private void tryToStartSession() {
        verbs.clear(); // start fresh
        settingsFrame.getListsPanel().updateCurrentList();
		
        for(int i = 0; i < settingsFrame.getListsPanel().getSelectedVerbIndices().size(); i++) {
            if(!((VerbSetCheckboxComponent)settingsFrame.getListsPanel().getVerbSetsModel().getElementAt(i)).isSelected()) {
                continue; // only add the sets that are checked
            }

            for(Integer j : settingsFrame.getListsPanel().getSelectedVerbIndices().get(i)) {
                ConjugatedVerb cv = verbSets.get(i).getVerbs().get(j.intValue());
                verbs.add(cv);
            }
        }
		
        if(verbs.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Error! No verbs selected for practice!","Session Start Error",JOptionPane.ERROR_MESSAGE);
            return;
        }
        if(settingsFrame.getListsPanel().getPronounsList().getSelectedIndices().length == 0) {
            JOptionPane.showMessageDialog(this,"Error! No subject pronouns selected for practice!","Session Start Error",JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            timerPanel.getTimerEntry().setValue(timerPanel.makeGoodFormat());
        }
        catch(NumberFormatException nfe) {
            return;
        }
        
        settingsFrame.dispose();
        reportListFrame.dispose();

        Collections.shuffle(verbs);
        currentVerb = verbs.get(0);

        pronounsToUse.clear();
		
        for(int i = 0; i < settingsFrame.getListsPanel().getPronounsList().getSelectedValues().length; i++) {
            pronounsToUse.add((SubjectPronoun)settingsFrame.getListsPanel().getPronounsList().getSelectedValues()[i]);
        }

        currentPronoun = pronounsToUse.get(generator.nextInt(pronounsToUse.size()));

        entriesPanel.getRightSetLabel().setText(currentVerb.getSetReference());
        entriesPanel.getRightPronounLabel().setText(currentPronoun.toString());
        entriesPanel.getRightVerbLabel().setText(currentVerb.toString());

        toggleComponentsForRunningSession();

        timerPanel.getTimeLabel().setForeground(Color.WHITE);
        entriesPanel.getUserAnswerField().requestFocusInWindow();

        initializeReport();
        running = true;
        frameTimer.start();
        entriesPanel.showRLabels();
    }

    private void toggleComponentsForRunningSession() {
        viewReportButton.setEnabled(false);
        viewReport.setEnabled(false);
        settingsMenu.setEnabled(false);
        settingsButton.setEnabled(false);
        startButton.setEnabled(false);
        playMenuItem.setEnabled(false);
        pauseButton.setEnabled(true);
        pauseMenuItem.setEnabled(true);
        stopButton.setEnabled(true);
        stopMenuItem.setEnabled(true);
        entriesPanel.showRLabels();
        entriesPanel.getEnterButton().setEnabled(true);
        entriesPanel.getAddAccentButton().setEnabled(true);
        addAccentButton.setEnabled(true);
        entriesPanel.getUserAnswerField().setEnabled(true);
        timerPanel.disableComponents();
        timerPanel.getOnePassButton().setEnabled(false);
        minutes = timerPanel.getMinutes().intValue();
        seconds = timerPanel.getSeconds().intValue();

        scoresPanel.setModeLabel(conjugationMode == ConjugationMode.TEST_MODE?"Test Mode":"Practice Mode");
        scoresPanel.showModeLabel(true);
    }

    private void initializeReport() {
        report = new ConjugationSessionReport();
        report.setDateAndTime();
        report.setLanguage(language.toString()); // set the report language to the language of this whole frame
		
        if(conjugationMode == ConjugationMode.TEST_MODE) {
            report.setConjugationMode("Test");
        }
        else {
            report.setConjugationMode("Practice");
        }

        for(ConjugatedVerb v : verbs) {
            if(!report.verbSetsUsed.contains(v.getSetReference().toString())) {
                report.verbSetsUsed.add(v.getSetReference().toString());
            }
        }

        report.setTimeLimit(timed?(String.valueOf(minutes)+":"+((seconds<10)?"0"+seconds:String.valueOf(seconds))):"N/A");
        report.onePassUsed = timerPanel.getOnePassButton().isSelected();
    }

    private void pauseSession() {
        if(running) {
            running = false;
            entriesPanel.hideRLabels();
            entriesPanel.getUserAnswerField().setEnabled(false);
            entriesPanel.getEnterButton().setEnabled(false);
            entriesPanel.getAddAccentButton().setEnabled(false);
            addAccentButton.setEnabled(false);
            pauseMenuItem.setText("Resume");
            pauseButton.setToolTipText("Resume current conjugation practice");
            report.numberOfPauses++;
        }
        else { // resume
            entriesPanel.showRLabels();
            entriesPanel.getUserAnswerField().setEnabled(true);
            entriesPanel.getEnterButton().setEnabled(true);
            entriesPanel.getAddAccentButton().setEnabled(true);
            addAccentButton.setEnabled(true);
            pauseMenuItem.setText("Pause");
            pauseButton.setToolTipText("Pause current conjugation practice");
            running = true;
        }
    }

    private void endSession() {
        frameTimer.stop();

        if(dialog.isActive()) {
            dialog.dispose();            
        }
		
        running = false; // prevent the clock from running
        
        viewReportButton.setEnabled(true);
        viewReport.setEnabled(true);
        startButton.setEnabled(true);
        playMenuItem.setEnabled(true);
        pauseButton.setEnabled(false);
        pauseButton.setToolTipText("Pause current conjugation practice");
        pauseMenuItem.setEnabled(false);
        pauseMenuItem.setText("Pause");
        stopButton.setEnabled(false);
        stopMenuItem.setEnabled(false);
        entriesPanel.getEnterButton().setEnabled(false);
        entriesPanel.getAddAccentButton().setEnabled(false);
        addAccentButton.setEnabled(false);
        entriesPanel.getUserAnswerField().setText("");
        timerPanel.enableComponents();
		
        if(conjugationMode != ConjugationMode.TEST_MODE) {
            timerPanel.getOnePassButton().setEnabled(true);
        }

        settingsMenu.setEnabled(true);
        settingsButton.setEnabled(true);

        scoresPanel.showModeLabel(false);

        entriesPanel.getRightSetLabel().setText("");
        entriesPanel.getRightPronounLabel().setText("");
        entriesPanel.getRightVerbLabel().setText("");
        entriesPanel.hideRLabels();
        
        currentVerb = null;
        currentPronoun = null;
        currentVerbIndex = 0;
        entriesPanel.getUserAnswerField().setEnabled(false);

        minutes = timerPanel.getMinutes().intValue();
        seconds = timerPanel.getSeconds().intValue();        


        if(timed) {
            timerPanel.getTimeLabel().setText(minutes + " : " + ((seconds < 10)?"0"+seconds:""+seconds));
        }
        else {
            timerPanel.getTimeLabel().setText("");
            timerPanel.getTimerEntry().setEnabled(false);
        }

        timerPanel.getTimeLabel().setForeground(Color.WHITE);

        if(scoresPanel.getNumCorrect()+scoresPanel.getNumIncorrect() > 0) {
            report.numberCorrect = scoresPanel.getNumCorrect();
            report.numberIncorrect = scoresPanel.getNumIncorrect();            
         
            if(JOptionPane.showConfirmDialog(null,"You got " + scoresPanel.getNumCorrect() + " out of " + (scoresPanel.getNumCorrect()+scoresPanel.getNumIncorrect()) + " for a score of " + (scoresPanel.getPercentage()) + "%.\n" + "Would you like to view your session report?","Conjugation Session Terminated!",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                reportButtonsPanel.restoreSaveButton();

                JFrame reportFrame = new JFrame("Conjugation Session Report");
                reportFrame.setSize(636,626);
                reportFrame.setResizable(false);
                SpringLayout sl = new SpringLayout();
                reportFrame.setLayout(sl);

                ConjugationSessionPanel csp = new ConjugationSessionPanel(report);

                reportFrame.add(csp);
                reportFrame.add(reportButtonsPanel);
                reportFrame.add(copyrightPanel);

                sl.putConstraint(SpringLayout.NORTH,csp,0,SpringLayout.NORTH,reportFrame.getContentPane());
                sl.putConstraint(SpringLayout.WEST,csp,0,SpringLayout.WEST,reportFrame.getContentPane());

                sl.putConstraint(SpringLayout.NORTH,reportButtonsPanel,0,SpringLayout.SOUTH,csp);
                sl.putConstraint(SpringLayout.WEST,reportButtonsPanel,0,SpringLayout.WEST,reportFrame.getContentPane());

                sl.putConstraint(SpringLayout.NORTH,copyrightPanel,0,SpringLayout.SOUTH,reportButtonsPanel);
                sl.putConstraint(SpringLayout.WEST,copyrightPanel,0,SpringLayout.WEST,reportFrame.getContentPane());

                reportFrame.setLocationRelativeTo(null);
                reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                reportFrame.setVisible(true);
            } // if user wants to see the report, open a new window to show it to them            
        }

        scoresPanel.resetScores();
    }


    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == startButton || e.getSource() == playMenuItem) {
            tryToStartSession();
        }
        else if(e.getSource() == pauseButton || e.getSource() == pauseMenuItem) {
            pauseSession();
        }
        else if(e.getSource() == stopButton || e.getSource() == stopMenuItem) {
            endSession();
        }
        else if(e.getSource() == entriesPanel.getEnterButton()) {
            confirmAnswer();
        }
        else if(e.getSource() == addAccentButton || e.getSource() == entriesPanel.getAddAccentButton()) {
            try {
                entriesPanel.getUserAnswerField().setText(Accent.accentFinalCharacter(entriesPanel.getUserAnswerField().getText()));
            }
            catch(IllegalArgumentException iae) { // if timed, and setting is such that it should stop, then stop the timer
                JOptionPane.showMessageDialog(this,iae.getMessage(),"Accenting character error!",JOptionPane.ERROR_MESSAGE);
            }
            entriesPanel.getUserAnswerField().requestFocusInWindow();
        }
        else if(e.getSource() == viewReportButton) {
            viewReports();
        }
        else if(e.getSource() == settingsButton) {
            settingsFrame.setState(JFrame.NORMAL);
            settingsFrame.setVisible(true);
            settingsFrame.toFront();
        }
        else if(e.getSource() == helpButton || e.getSource() == helpMenuItem) {
            System.out.println("help pressed");
        }
        else if(e.getSource() == aboutMenuItem) {
            System.out.println("about pressed");
        }
        else if(e.getSource() == timerPanel.getTimedButton()) {
            timerPanel.getTimeLabel().setText(minutes + " : " + ((seconds < 10)?"0"+seconds:""+seconds));
            timerPanel.getTimerEntry().setEnabled(true);
            timed = true;

            timerPanel.getOnePassButton().setEnabled(false);
            timerPanel.getOnePassButton().setVisible(false);            
        }
        else if(e.getSource() == timerPanel.getNotTimedButton()) {
            timerPanel.getTimeLabel().setText("");
            timerPanel.getTimerEntry().setEnabled(false);
            timed = false;

            if(conjugationMode != ConjugationMode.TEST_MODE) { // only allow to change if not in test mode
                timerPanel.getOnePassButton().setEnabled(true);
            }
            timerPanel.getOnePassButton().setVisible(true);
        }
        else if(e.getSource() == reportButtonsPanel.getSaveReportButton()) {
            saveCurrentReport();
            reportButtonsPanel.disableSaveButton();
        }
        else if(e.getSource() == reportButtonsPanel.getPrintReportButton() || e.getSource() == printSessionPanel.getPrintReportButton()) {
            System.out.println("Printing report");
        }
    }

    class MenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == practiceModeMenuItem) {
                conjugationMode = ConjugationMode.PRACTICE_MODE;
                timerPanel.getOnePassButton().setEnabled(true);
            }
            else if(e.getSource() == testModeMenuItem) {
                conjugationMode = ConjugationMode.TEST_MODE;
                timerPanel.getOnePassButton().setEnabled(false);
                timerPanel.getOnePassButton().setSelected(true); // no matter what
            }
            else if(e.getSource() == settingsMenuItem) {
                settingsFrame.setState(JFrame.NORMAL);
                settingsFrame.setVisible(true);
                settingsFrame.toFront();
            }
            else if(e.getSource() == viewReport) {
                viewReports();
            }
            else if(e.getSource() == exit) {
                handleFrameClosing();
            }
        }
    }

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(running) {
                if(timed) {
                    seconds--;
                    if(seconds == -1) {
                        seconds = 59;
                        minutes--;
                        if(minutes < 0) {
                            endSession();
                            return;
                        }
                    }

                    timerPanel.getTimeLabel().setText(minutes + " : " + ((seconds<10)?"0":"") + seconds);
                    if(seconds <= 5 && minutes == 0) {
                        timerPanel.getTimeLabel().setForeground(Color.RED);
                    }
                }
                else {
                    timerPanel.getTimeLabel().setText("");
                }
            }
        }
    }
    

    private void viewReports() {
        reportListFrame.updateFrame();
        reportListFrame.setState(JFrame.NORMAL);
        reportListFrame.setLocationRelativeTo(null);
        reportListFrame.setVisible(true);
        reportListFrame.toFront();
    }

    public CopyrightPanel getCopyrightPanel() {
        return copyrightPanel;
    }

    public PrintSessionPanel getPrintSessionPanel() {
        return printSessionPanel;
    }

    public boolean isRunning() {
        return frameTimer.isRunning();
    }

    public void setRunning(boolean value) {
        running = value;
    }

    public void handleFrameClosing() {
        cleanUp();

        for(ConjugationTreeObject cto : treeObjects) {
            try {
                cto.releaseReviewFrame(this);
            }
            catch(Exception e) {
				e.printStackTrace();
            }
        }
        dispose();
    }

    public void cleanUp() {
        frameTimer.stop();
        settingsFrame.dispose();
        reportListFrame.dispose();
    }

    public java.util.List<ConjugationTreeObject> getTreeObjects() {
        return treeObjects;
    }
}

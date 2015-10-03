package notecardstation.notecardreviewer.general;

import guicomponents.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import languagesuite.LanguageSuite;

import notecardstation.notecard.*;
import notecardstation.notecardreviewer.*;
import notecardstation.notecardreviewer.reports.*;
import notecardstation.notecardreviewer.userinteraction.*;
import notecardstation.general.*;
import notecardstation.general.NotecardStation.NotecardStackTreeObject;

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
 * Main JFrame for the Notecard Station for reviewing notecards
 * @author Michael Hergenrader
 */
public class NotecardStackReviewFrame extends JFrame implements ActionListener, WindowListener, FocusListener {

    public enum ReviewMode {
		FLIP_MODE,
		PRACTICE_MODE, // practice mode involves entering text (like test mode), as opposed to flipping notecards
		TEST_MODE
	};
    
    private ReviewMode reviewMode;

    private NotecardStation owner;
    private Set<NotecardStackTreeObject> treeNodes;
    private ArrayList<NotecardStackReviewInfo> stackAndSettingsList;

    private enum StackRepresentation {
		ONE_STACK,
		NON_MIXED_STACKS,
		MIXED_STACKS
	}; // set this from settings and from the lists panel
	
    private StackRepresentation stackArrangement;

    private ArrayList<ArrayList<ReviewNotecard>> cardsToUsePartitioned;
    private ArrayList<ReviewNotecard> cardsToUseMixed;
    private ArrayList<NotecardStack> stacksToUse;

    private ReviewNotecard currentCard;
    private int currentCardIndex;
    private int currentListIndex;

    // Menu
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu controlsMenu;
    private JMenu settingsMenu;
    private JMenu helpMenu;
    private JMenuItem viewReport;
    private JMenuItem exit;
    private JMenuItem start;
    private JMenuItem pause;
    private JMenuItem stop;
    private JCheckBox flipMode;
    private JCheckBox practiceMode;
    private JCheckBox testMode;
    private ButtonGroup modeButtons;
    private JMenuItem settings;
    private JMenuItem help;
    private JMenuItem about;

    // Toolbar
    private JToolBar toolbar;
    private ToolbarButton viewReportButton;
    private ToolbarButton startButton;
    private ToolbarButton pauseButton;
    private ToolbarButton stopButton;
    private ToolbarButton addAccentButton;
    private ToolbarButton settingsButton;
    private ToolbarButton helpButton;

    // Frame components
    private NotecardPanel notecardPanel;
    private UserInteractionPanel interactionPanel;
    private UserButtonsPanel buttonsPanel;
    private TimerPanel timerPanel;
    private StatsPanel statsPanel;

    private SettingsFrame settingsFrame;

    private javax.swing.Timer reviewTimer;
    private int minutes;
    private int seconds;
    private boolean running; // doesn't just mean the timer - means the actual notecard stack review loop is running
    private boolean timed;

    private static final String defaultImageLocation = LanguageSuite.defaultImageLocation;
    
    private JTextField focusedComponent; // reference to the current component with the focus

    private TimerDialog answerDialog; // appears when user presses the enter button

    private boolean secondSideIncorrectFlag = false;
    private boolean thirdSideIncorrectFlag = false;
    private boolean correct2ndSide;
    private boolean correct3rdSide;

    public enum NotecardSides {
		SIDE_1,
		SIDE_2,
		SIDE_3
	};

    private ReviewSessionReport report;
    private ReportListFrame reportListFrame;
    private ReportButtonsPanel reportButtonsPanel;
    private PrintSessionPanel printSessionPanel;

    private CopyrightPanel copyrightPanel;

    public class NotecardStackReviewInfo { // this separates settings for the reviewer from the stack object itself
        public NotecardStack stack;

        private NotecardSides twoDFrontSide; // the default sides
        private NotecardSides threeDFrontSide;
        private ArrayList<NotecardSides> sidesToTest;

        public NotecardStackReviewInfo(NotecardStack stack) {
            this.stack = stack;
            this.twoDFrontSide = NotecardSides.SIDE_1;
            this.threeDFrontSide = NotecardSides.SIDE_1;
            sidesToTest = new ArrayList<NotecardSides>();
        }

        public void set2DDefaultSide(NotecardSides side) {
            this.twoDFrontSide = side;
        }

        public NotecardSides get2DDefaultSide() {
            return twoDFrontSide;
        }

        public void set3DDefaultSide(NotecardSides side) {
            this.threeDFrontSide = side;
        }

        public NotecardSides get3DDefaultSide() {
            return threeDFrontSide;
        }

        public void setSidesToTest(ArrayList<NotecardSides> sides) {
            sidesToTest.clear();
            for(NotecardSides side : sides) {
                sidesToTest.add(side);
            }
        }

        public ArrayList<NotecardSides> getSidesToTest() {
            return sidesToTest;
        }
    }
    
    public class ReviewNotecard { // an abstract notecard that just copies its data
        private boolean hasThreeSides;

        private String side1Title;
        private String side2Title;
        private String side3Title;

        private String side1Text;
        private String side2Text;
        private String side3Text;

        private NotecardStack stackReference;

        // to create a 2-D card
        public ReviewNotecard(NotecardStack stackReference, String side1Title, String side1Text, String side2Title, String side2Text) {
            hasThreeSides = false;

            this.stackReference = stackReference;
            this.side1Title = new String(side1Title);
            this.side1Text = new String(side1Text);
            this.side2Title = new String(side2Title);
            this.side2Text = new String(side2Text);
        }

        // to create a 3-D card
        public ReviewNotecard(NotecardStack stackReference, String side1Title, String side1Text, String side2Title, String side2Text, String side3Title, String side3Text) {
            this(stackReference,side1Title,side1Text,side2Title,side2Text);
            hasThreeSides = true;

            this.side3Title = new String(side3Title);
            this.side3Text = new String(side3Text);
        }

        public String getSide1Title() {
            return side1Title;
        }

        public String getSide2Title() {
            return side2Title;
        }

        public String getSide3Title() {
            return side3Title;
        }

        public String getSide1Text() {
            return side1Text;
        }

        public String getSide2Text() {
            return side2Text;
        }

        public String getSide3Text() {
            return side3Text;
        }

        public boolean hasThreeSides() {
            return hasThreeSides;
        }

        public NotecardStack getNotecardStack() {
            return stackReference;
        }
    }

    public NotecardStackReviewFrame(NotecardStation owner, Set<NotecardStackTreeObject> stacksToReview) {
        super("Notecard Review");
        setIconImage(LanguageSuite.frameIcon);

        addWindowListener(this);

        cardsToUseMixed = new ArrayList<ReviewNotecard>();
        cardsToUsePartitioned = new ArrayList<ArrayList<ReviewNotecard>>();

        this.owner = owner;
        this.treeNodes = new TreeSet<NotecardStackTreeObject>(new NotecardStackTreeObjectComparator());
        for(NotecardStackTreeObject nsto : stacksToReview) {
            this.treeNodes.add(nsto);
        }

        stackAndSettingsList = new ArrayList<NotecardStackReviewInfo>();
        for(NotecardStackTreeObject nsto : this.treeNodes) {
            NotecardStack ns = new NotecardStack(nsto.getNotecardStack());
            stackAndSettingsList.add(new NotecardStackReviewInfo(ns));
        }
		
        reviewMode = ReviewMode.FLIP_MODE;
        stacksToUse = new ArrayList<NotecardStack>();

        setupSettingsFrame();
        setupReportsFrame();
        setupTimerDialog();
        setupMenu();
        setupToolbar();
        setupFrameComponents();
        setupLayout();
        setupStationLogic();

        getContentPane().setBackground(Color.DARK_GRAY.brighter());
        getRootPane().setDefaultButton(buttonsPanel.getEnterButton());

        reviewTimer = new javax.swing.Timer(1000,new TimerListener());
    }

    private void saveCurrentReport() {
		// user chooses to save this report, so copy the current, temporary report into a more permanent one
        owner.getReports().add(0,new ReviewSessionReport(report));
    }
    
    private void setupSettingsFrame() {
        settingsFrame = new SettingsFrame(this);
        settingsFrame.setSize(600,300);
        settingsFrame.setResizable(false);
        settingsFrame.setLocationRelativeTo(this);
        settingsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
	
    private void setupReportsFrame() {
        reportListFrame = new ReportListFrame(this);
        reportListFrame.setSize(500,300);
        reportListFrame.setResizable(false);
        reportListFrame.setLocationRelativeTo(this);
        reportListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
	
    private void setupTimerDialog() {
        answerDialog = new TimerDialog(this,"",true);
        answerDialog.setMinimumSize(new Dimension(350,200));
        answerDialog.setMaximumSize(new Dimension(350,200));
        answerDialog.setPreferredSize(new Dimension(350,200));
        answerDialog.setLocationRelativeTo(this);
    }
	
    private void setupMenu() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        viewReport = new JMenuItem("View Report(s)");
        viewReport.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,KeyEvent.CTRL_MASK));
        viewReport.addActionListener(this);

        exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,KeyEvent.CTRL_MASK));
        exit.addActionListener(this);

        fileMenu.add(viewReport);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        controlsMenu = new JMenu("Controls");
        controlsMenu.setMnemonic(KeyEvent.VK_O);

        start = new JMenuItem("Start");
        start.addActionListener(this);

        pause = new JMenuItem("Pause");
        pause.addActionListener(this);

        stop = new JMenuItem("Stop");
        stop.addActionListener(this);

        controlsMenu.add(start);
        controlsMenu.add(pause);
        controlsMenu.add(stop);

        settingsMenu = new JMenu("Settings");
        settingsMenu.setMnemonic(KeyEvent.VK_S);

        modeButtons = new ButtonGroup();
        flipMode = new JCheckBox("Flip Mode");
        flipMode.setSelected(true);
        flipMode.addActionListener(this);
        practiceMode = new JCheckBox("Practice Mode");
        practiceMode.addActionListener(this);
        testMode = new JCheckBox("Test Mode");
        testMode.addActionListener(this);
        modeButtons.add(flipMode);
        modeButtons.add(practiceMode);
        modeButtons.add(testMode);

        settings = new JMenuItem("Settings Menu...");
        settings.addActionListener(this);

        settingsMenu.add(flipMode);
        settingsMenu.add(practiceMode);
        settingsMenu.add(testMode);
        settingsMenu.addSeparator();
        settingsMenu.add(settings);
        
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        help = new JMenuItem("Help");
        help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,0));
        help.addActionListener(this);

        about = new JMenuItem("About");
        about.addActionListener(this);

        helpMenu.add(help);
        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(controlsMenu);
        menuBar.add(settingsMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
	
    private void setupToolbar() {
        toolbar = new JToolBar("Review Buttons");
        toolbar.setMinimumSize(new Dimension(700,50));
        toolbar.setMaximumSize(new Dimension(700,50));
        toolbar.setPreferredSize(new Dimension(700,50));
        toolbar.setFloatable(false);
        toolbar.setOpaque(false);
        toolbar.setBorderPainted(false);

        viewReportButton = new ToolbarButton("View/Refresh Reports",defaultImageLocation+"findicon_edited-1.png");
        viewReportButton.addActionListener(this);

        startButton = new ToolbarButton("Start notecard review with current settings",defaultImageLocation+"starticon_edited-1.png");
        startButton.addActionListener(this);

        pauseButton = new ToolbarButton("Pause notecard review",defaultImageLocation+"pauseicon_edited-1.png");
        pauseButton.addActionListener(this);
        pauseButton.setEnabled(false);

        stopButton = new ToolbarButton("Stop this notecard review session",defaultImageLocation+"stopicon_edited-1.png");
        stopButton.addActionListener(this);
        stopButton.setEnabled(false);

        addAccentButton = new ToolbarButton("Add accent to current text field",defaultImageLocation+"addaccent_edited-1.png");
        addAccentButton.addActionListener(this);
        addAccentButton.setEnabled(false);

        settingsButton = new ToolbarButton("Settings for notecard review",defaultImageLocation+"settingsicon_edited-1.png");
        settingsButton.addActionListener(this);

        helpButton = new ToolbarButton("Language Suite Help - Notecard Review Station",defaultImageLocation+"helpicon_edited-1.png");
        helpButton.addActionListener(this);

        toolbar.add(viewReportButton);
        toolbar.addSeparator(new Dimension(50,0));
        toolbar.add(startButton);
        toolbar.add(pauseButton);
        toolbar.add(stopButton);
        toolbar.addSeparator(new Dimension(50,0));
        toolbar.add(addAccentButton);
        toolbar.add(settingsButton);
        toolbar.addSeparator(new Dimension(50,0));
        toolbar.add(helpButton);
    }
	
    private void setupFrameComponents() {
        notecardPanel = new NotecardPanel(this);
        interactionPanel = new UserInteractionPanel(this);
        interactionPanel.setVisible(false);
        buttonsPanel = new UserButtonsPanel(this);
        timerPanel = new TimerPanel(this);
        timerPanel.setVisible(false);
        statsPanel = new StatsPanel(this);
        statsPanel.showStats(false);
        reportButtonsPanel = new ReportButtonsPanel(this);
        printSessionPanel = new PrintSessionPanel(this);
        copyrightPanel = new CopyrightPanel();
    }
	
    private void setupLayout() {
        SpringLayout sl = new SpringLayout();
        setLayout(sl);

        add(toolbar);
        add(notecardPanel);
        add(interactionPanel);
        add(buttonsPanel);
        add(timerPanel);
        add(statsPanel);

        sl.putConstraint(SpringLayout.NORTH,toolbar,0,SpringLayout.NORTH,getContentPane());
        sl.putConstraint(SpringLayout.WEST,toolbar,0,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,notecardPanel,10,SpringLayout.SOUTH,toolbar);
        sl.putConstraint(SpringLayout.WEST,notecardPanel,150,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,interactionPanel,10,SpringLayout.SOUTH,notecardPanel);
        sl.putConstraint(SpringLayout.WEST,interactionPanel,100,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,buttonsPanel,10,SpringLayout.SOUTH,interactionPanel);
        sl.putConstraint(SpringLayout.WEST,buttonsPanel,150,SpringLayout.WEST,getContentPane());

        sl.putConstraint(SpringLayout.NORTH,timerPanel,70,SpringLayout.NORTH,notecardPanel);
        sl.putConstraint(SpringLayout.WEST,timerPanel,25,SpringLayout.EAST,notecardPanel);

        sl.putConstraint(SpringLayout.NORTH,statsPanel,50,SpringLayout.NORTH,notecardPanel);
        sl.putConstraint(SpringLayout.WEST,statsPanel,15,SpringLayout.WEST,getContentPane());
    }
	
    private void setupStationLogic() {
        running = false;
        minutes = 0;
        seconds = 0;
        timed = true;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == start || e.getSource() == startButton) {
            startReviewSession();
        }
        else if(e.getSource() == pause || e.getSource() == pauseButton) {
            pauseReviewSession();
        }
        else if(e.getSource() == stop || e.getSource() == stopButton) {
            endReviewSession();
        }
        else if(e.getSource() == addAccentButton || e.getSource() == buttonsPanel.getAddAccentButton()) {
            if(focusedComponent != null) {
                try {
                    focusedComponent.setText(Accent.accentFinalCharacter(focusedComponent.getText()));
                }
                catch(IllegalArgumentException iae) {
					// if timed, and setting is such that it should stop, then stop the timer
                    JOptionPane.showMessageDialog(this,iae.getMessage(),"Accenting character error!",JOptionPane.ERROR_MESSAGE);
                }
                focusedComponent.requestFocusInWindow();
            }
        }
        else if(e.getSource() == settings || e.getSource() == settingsButton) {            
            settingsFrame.setState(JFrame.NORMAL);
            settingsFrame.setVisible(true);
            settingsFrame.toFront();
        }
        else if(e.getSource() == viewReport || e.getSource() == viewReportButton) {
            viewReports();
        }
        else if(e.getSource() == exit) {
            handleFrameClosing();
        }
        else if(e.getSource() == help || e.getSource() == helpButton) {
            System.out.println("help pressed");
        }
        else if(e.getSource() == about) {
            System.out.println("about pressed");
        }
        else if(e.getSource() == buttonsPanel.getEnterButton()) {
            confirmUserInput();
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

            if(reviewMode != ReviewMode.TEST_MODE) { // only allow to change if not a test
                timerPanel.getOnePassButton().setEnabled(true);
            }
            timerPanel.getOnePassButton().setVisible(true);
        }
        else if(e.getSource() == flipMode) {
            reviewMode = ReviewMode.FLIP_MODE;
            timerPanel.setVisible(false);
            statsPanel.showStats(false);
        }
        else if(e.getSource() == practiceMode) {
            reviewMode = ReviewMode.PRACTICE_MODE;
            timerPanel.setVisible(true);
            statsPanel.showStats(true);
            timerPanel.getOnePassButton().setEnabled(true);
        }
        else if(e.getSource() == testMode) {
            reviewMode = ReviewMode.TEST_MODE;
            timerPanel.setVisible(true);
            statsPanel.showStats(true);
            timerPanel.getOnePassButton().setEnabled(false);
            timerPanel.getOnePassButton().setSelected(true);
        }
        else if(e.getSource() == reportButtonsPanel.getSaveReportButton()) {
            saveCurrentReport();
            reportListFrame.updateFrame();
            reportButtonsPanel.getSaveReportButton().setText("Saved!");
            reportButtonsPanel.getSaveReportButton().setEnabled(false);
        }
        else if(e.getSource() == reportButtonsPanel.getPrintReportButton()) {
            System.out.println("print report pressed");
        }
        else if(e.getSource() == printSessionPanel.getPrintReportButton()) {
            System.out.println("print report button pressed");
        }
        else if(interactionPanel.getUserInputPanel() instanceof FlipsCard) {
            if(e.getSource() == ((FlipsCard)interactionPanel.getUserInputPanel()).getNextCardButton()) {
                goToNextCardFlipMode();
            }
            else if(e.getSource() == ((FlipsCard)interactionPanel.getUserInputPanel()).getPreviousCardButton()) {
                goToPreviousCardFlipMode();
            }
			
            if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                if(e.getSource() == ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).getFlipLeftButton()) {
                    flipCard3DLeft();
                }
                else if(e.getSource() == ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).getFlipRightButton()) {
                    flipCard3DRight();
                }
            }
            else if(interactionPanel.getUserInputPanel() instanceof FlipCardTwoSidesPanel) {
                if(e.getSource() == ((FlipCardTwoSidesPanel)interactionPanel.getUserInputPanel()).getFlipCardButton()) {
                    flipCard2D();
                }
            }
        }
    }

    public ArrayList<ReviewSessionReport> getReports() {
        return owner.getReports();
    }

    private void goToPreviousCardFlipMode() {
        advanceCardBackwardFlipMode();
        updateFlipModeComponents();
        if((stackArrangement != StackRepresentation.NON_MIXED_STACKS && currentCardIndex == 0) || (stackArrangement == StackRepresentation.NON_MIXED_STACKS && currentListIndex == 0 && currentCardIndex == 0)) {
            if(interactionPanel.getUserInputPanel() instanceof FlipsCard) {
                ((FlipsCard)interactionPanel.getUserInputPanel()).getPreviousCardButton().setEnabled(false);
            }
        }
        else {
            if(interactionPanel.getUserInputPanel() instanceof FlipsCard) {
                ((FlipsCard)interactionPanel.getUserInputPanel()).getPreviousCardButton().setEnabled(true);
            }
        }
    }

    private void goToNextCardFlipMode() {
		 // move to next card and update to the default side with everything (erase all flipping procedures done earlier)
		 // or ignore current side - need to go to default side
        advanceCardForwardFlipMode();
        updateFlipModeComponents();
        if(interactionPanel.getUserInputPanel() instanceof FlipsCard) {
            ((FlipsCard)interactionPanel.getUserInputPanel()).getPreviousCardButton().setEnabled(true);
        }
    }

    private void updateFlipModeComponents() {
        if(currentCard != null) {
            interactionPanel.refreshTo(currentCard.hasThreeSides()?(UserInteractionPanel.CardDimension.THREE_SIDES):(UserInteractionPanel.CardDimension.TWO_SIDES),reviewMode);

            // update the notecard to the default side of this new card
            if(currentCard.hasThreeSides()) {
                switch(findReviewInfo(currentCard.getNotecardStack()).get3DDefaultSide()) {
                    case SIDE_1:
                        notecardPanel.setTitleText(currentCard.getSide1Title());
                        notecardPanel.setNotecardText(currentCard.getSide1Text());
                        if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                            ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide3Title());
                            ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide2Title());
                        }
                        break;
                    case SIDE_2:
                        notecardPanel.setTitleText(currentCard.getSide2Title());
                        notecardPanel.setNotecardText(currentCard.getSide2Text());
                        if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                            ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide1Title());
                            ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide3Title());
                        }
                        break;
                    case SIDE_3:
                        notecardPanel.setTitleText(currentCard.getSide3Title());
                        notecardPanel.setNotecardText(currentCard.getSide3Text());
                        if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                            ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide2Title());
                            ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide1Title());
                        }
                        break;
                }
            }
            else {
                if(findReviewInfo(currentCard.getNotecardStack()).get2DDefaultSide() == NotecardSides.SIDE_1) {
                    notecardPanel.setTitleText(currentCard.getSide1Title());
                    notecardPanel.setNotecardText(currentCard.getSide1Text());
                    if(interactionPanel.getUserInputPanel() instanceof FlipCardTwoSidesPanel) {
                        ((FlipCardTwoSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide2Title());
                    }
                }
                else {
                    notecardPanel.setTitleText(currentCard.getSide2Title());
                    notecardPanel.setNotecardText(currentCard.getSide2Text());
                    if(interactionPanel.getUserInputPanel() instanceof FlipCardTwoSidesPanel) {
                        ((FlipCardTwoSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide1Title());
                    }
                }
            }

            interactionPanel.getUserInputPanel().requestDefaultComponentFocus();

            statsPanel.setStackTitleLabel("Stack: " + currentCard.getNotecardStack().toString());

            String indexString = "Card " + (currentCardIndex+1) + " of ";
            switch(stackArrangement) {
                case NON_MIXED_STACKS:
                    indexString += cardsToUsePartitioned.get(currentListIndex).size();
                    break;
                case ONE_STACK: case MIXED_STACKS:
                    indexString += cardsToUseMixed.size();
                    break;
            }
            statsPanel.setCurrentCardIndexLabel(indexString);
        }
    }    

    private void startReviewSession() {
        settingsFrame.updateAllStackSettings();
        settingsFrame.dispose();

        reportListFrame.dispose();

        loadNotecardsForReview();

        if(thereAreNoCardsLoaded()) {
            JOptionPane.showMessageDialog(this,"Error! No notecards selected for practice!","No cards to review!",JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            timerPanel.getTimerEntry().setValue(timerPanel.makeGoodFormat());
        }
        catch(NumberFormatException nfe) {
            return;
        }       

        toggleComponentsForReview();

        shuffleCards();
        initializeFirstCard();

        setVisualComponents();
        interactionPanel.setVisible(true);
        interactionPanel.getUserInputPanel().requestDefaultComponentFocus();

        statsPanel.setReviewModeLabel(reviewMode);        

        if(reviewMode != ReviewMode.FLIP_MODE) {
            initializeNewReport();
            running = true;
        }
        else {
            if(interactionPanel.getUserInputPanel() instanceof FlipsCard) {
                ((FlipsCard)interactionPanel.getUserInputPanel()).getPreviousCardButton().setEnabled(false);
            }
        }
		
        reviewTimer.start();
    }

    private void initializeNewReport() {
        report = new ReviewSessionReport();
        report.setDateAndTime();
        if(reviewMode == reviewMode.TEST_MODE) {
            report.setReviewMode("Test");
        }
        else {
            report.setReviewMode("Practice");
        }

        for(NotecardStack ns : stacksToUse) {
            report.stacksUsed.add(new String(ns.toString()));
        }

        report.setTimeLimit(timed?(String.valueOf(minutes)+":"+((seconds<10)?"0"+seconds:String.valueOf(seconds))):"N/A");
        report.onePassUsed = timerPanel.getOnePassButton().isSelected();
    }
	
    private void pauseReviewSession() {
        if(running) {
            running = false;
            interactionPanel.setVisible(false);

            notecardPanel.showTitleText(false);
            notecardPanel.showNotecardText(false);
            buttonsPanel.getEnterButton().setEnabled(false);
            buttonsPanel.getAddAccentButton().setEnabled(false);
            addAccentButton.setEnabled(false);
            pause.setText("Resume");
            pauseButton.setToolTipText("Resume current conjugation practice");
            setTitle("Paused - Notecard Review");
            report.numberOfPauses++;
        }
        else {
            interactionPanel.setVisible(true);
            interactionPanel.getUserInputPanel().requestDefaultComponentFocus();
            notecardPanel.showTitleText(true);
            notecardPanel.showNotecardText(true);
            buttonsPanel.getEnterButton().setEnabled(true);
            buttonsPanel.getAddAccentButton().setEnabled(true);
            addAccentButton.setEnabled(true);
            pause.setText("Pause");
            pauseButton.setToolTipText("Pause current conjugation practice");
            setTitle("Notecard Review");
            running = true;
        }
    }

    private void endReviewSession() {
        reviewTimer.stop();
        if(answerDialog.isActive()) {
            answerDialog.dispose();
        }

        pauseButton.setToolTipText("Pause notecard review");
        running = false;

        if(reviewMode != ReviewMode.FLIP_MODE && statsPanel.getNumCorrect()+statsPanel.getNumIncorrect() > 0) {
            if(JOptionPane.showConfirmDialog(null,"You got " + statsPanel.getNumCorrect() + " out of " + (statsPanel.getNumCorrect()+statsPanel.getNumIncorrect()) + " for a score of " + (statsPanel.getPercentage()) + "%.\n" + "Would you like to view your session report?","Review Session Terminated!",JOptionPane.YES_NO_OPTION,JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                report.numberCorrect = statsPanel.getNumCorrect();
                report.numberIncorrect = statsPanel.getNumIncorrect();

                reportButtonsPanel.restoreSaveButton();

                JFrame reportFrame = new JFrame("Notecard Review Session Report");
                reportFrame.setSize(762,626);
                reportFrame.setResizable(false);
                SpringLayout sl = new SpringLayout();
                reportFrame.setLayout(sl);

                ReviewSessionPanel rsp = new ReviewSessionPanel(report);

                reportFrame.add(rsp);
                reportFrame.add(reportButtonsPanel);
                reportFrame.add(copyrightPanel);

                sl.putConstraint(SpringLayout.NORTH,rsp,0,SpringLayout.NORTH,reportFrame.getContentPane());
                sl.putConstraint(SpringLayout.WEST,rsp,0,SpringLayout.WEST,reportFrame.getContentPane());

                sl.putConstraint(SpringLayout.NORTH,reportButtonsPanel,0,SpringLayout.SOUTH,rsp);
                sl.putConstraint(SpringLayout.WEST,reportButtonsPanel,0,SpringLayout.WEST,reportFrame.getContentPane());

                sl.putConstraint(SpringLayout.NORTH,copyrightPanel,0,SpringLayout.SOUTH,reportButtonsPanel);
                sl.putConstraint(SpringLayout.WEST,copyrightPanel,0,SpringLayout.WEST,reportFrame.getContentPane());

                reportFrame.setLocationRelativeTo(null);
                reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                reportFrame.setVisible(true);
            } // if user wants to see the report, open a new window to show it to them
        }

        statsPanel.setStackTitleLabel("");
        statsPanel.setCurrentCardIndexLabel("");
        statsPanel.setReviewModeLabel(null);

        notecardPanel.setTitleText("");
        notecardPanel.setNotecardText("");
        notecardPanel.showTitleText(true);
        notecardPanel.showNotecardText(true);

        viewReportButton.setEnabled(true);
        viewReport.setEnabled(true);
        startButton.setEnabled(true);
        start.setEnabled(true);
        pauseButton.setEnabled(false);

        pause.setText("Pause");
        pause.setEnabled(false);
        stopButton.setEnabled(false);
        stop.setEnabled(false);
        buttonsPanel.getEnterButton().setEnabled(false);
        buttonsPanel.getAddAccentButton().setEnabled(false);
        addAccentButton.setEnabled(false);

        timerPanel.enableComponents();        
        if(reviewMode == ReviewMode.TEST_MODE) {
            timerPanel.getOnePassButton().setEnabled(false);
        }

        settingsMenu.setEnabled(true);
        settingsButton.setEnabled(true);

        currentCard = null;
        currentCardIndex = 0;
        currentListIndex = 0;
        if(interactionPanel.getUserInputPanel() instanceof EntersText) {
            ((EntersText)interactionPanel.getUserInputPanel()).clearUserInput();
        }
        interactionPanel.setVisible(false);

        minutes = timerPanel.getMinutes().intValue();
        seconds = timerPanel.getSeconds().intValue();

        statsPanel.resetScores();

        timerPanel.getTimeLabel().setForeground(Color.WHITE);
        if(timed) {
            timerPanel.getTimeLabel().setText(minutes + " : " + ((seconds < 10)?"0"+seconds:""+seconds));
        }
        else {
            timerPanel.getTimeLabel().setText("");
            timerPanel.getTimerEntry().setEnabled(false);
        }
    }
	
    private void confirmUserInput() {
        if(!isRunning()) {
            return;
        }

        if(settingsFrame.shouldStopClock()) {
            running = false; // will set running to true when user clicks the dialog box to confirm the result of this entry
        }

        if(currentCard.hasThreeSides()) {
            if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
                if(reviewMode == ReviewMode.TEST_MODE || (reviewMode == ReviewMode.PRACTICE_MODE && settingsFrame.shouldUseCaseSensitivity())) {
                    correct2ndSide = ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().getText().equals(currentCard.getSide2Text());
                    correct3rdSide = ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide3Entry().getText().equals(currentCard.getSide3Text());
                }
                else {
                    correct2ndSide = ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().getText().equalsIgnoreCase(currentCard.getSide2Text());
                    correct3rdSide = ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide3Entry().getText().equalsIgnoreCase(currentCard.getSide3Text());
                }

                if(correct2ndSide && correct3rdSide) {
                    handleCorrectAnswer();

                    if(secondSideIncorrectFlag || thirdSideIncorrectFlag) {
                        statsPanel.addCorrectAnswer();
                    }
                    else {
                        statsPanel.addCorrectAnswer(2);
                    }

                    secondSideIncorrectFlag = false;
                    thirdSideIncorrectFlag = false;
                    statsPanel.updateVisualScores();

                    if((stackArrangement != StackRepresentation.NON_MIXED_STACKS && currentCardIndex == cardsToUseMixed.size()) || (stackArrangement == StackRepresentation.NON_MIXED_STACKS && currentListIndex == cardsToUsePartitioned.size()-1 && currentCardIndex == cardsToUsePartitioned.get(currentListIndex).size())) {
                        endReviewSession();
                    }
                    else {
                        setVisualComponents();
                    }
                }
                else { // incorrect entry by user on a 3-D card
                    handleIncorrectAnswer();
                }
            }
        }
        else { // testing cards with two dimensions
            if(interactionPanel.getUserInputPanel() instanceof EnterTextTwoSidesPanel) {
                if(reviewMode == ReviewMode.TEST_MODE || (reviewMode == ReviewMode.PRACTICE_MODE && settingsFrame.shouldUseCaseSensitivity())) {
                    correct2ndSide = ((EnterTextTwoSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().getText().equals(currentCard.getSide2Text());
                }
                else { // practice mode and no case sensitivity enabled
                    correct2ndSide = ((EnterTextTwoSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().getText().equalsIgnoreCase(currentCard.getSide2Text());
                }

                if(correct2ndSide) {
                    handleCorrectAnswer();

                    statsPanel.addCorrectAnswer();
                    statsPanel.updateVisualScores();

                    if((stackArrangement != StackRepresentation.NON_MIXED_STACKS && currentCardIndex == cardsToUseMixed.size()) || (stackArrangement == StackRepresentation.NON_MIXED_STACKS && currentListIndex == cardsToUsePartitioned.size()-1 && currentCardIndex == cardsToUsePartitioned.get(currentListIndex).size())) {
                        endReviewSession();
                    }
                    else {
                        setVisualComponents();
                    }
                }
                else { // incorrect entry by user
                    handleIncorrectAnswer();
                }
            }
        }
    }
   	
    private void handleCorrectAnswer() {
        answerDialog.setTitle("Correct!");
        answerDialog.setMessage("Correct!");
        if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
            answerDialog.setMessageLine2("Correct!");
        }
        else {
            answerDialog.setMessageLine2("");
        }
        answerDialog.setMessageInFocus();
        answerDialog.setLocationRelativeTo(null);
        answerDialog.setVisible(true);

        if(interactionPanel.getUserInputPanel() instanceof EntersText) {
            ((EntersText)interactionPanel.getUserInputPanel()).clearUserInput(); // clears no matter what for a correct answer
            ((EntersText)interactionPanel.getUserInputPanel()).enableSides();
        }
        
        if(reviewMode == ReviewMode.PRACTICE_MODE) {
            advanceCardPracticeMode(true);
        }
        else {
            advanceCardTestMode();
        }
    }
	
    private void handleIncorrectAnswer() {
        boolean setThirdSideFocus = false;

        if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
            if(!correct2ndSide) {
                report.missedEntries.add(new MissedEntry(currentCard.getNotecardStack().toString(),currentCard.getSide1Title(),currentCard.getSide1Text(),currentCard.getSide2Title(),currentCard.getSide2Text(),((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().getText()));
            }
            if(!correct3rdSide) {
                report.missedEntries.add(new MissedEntry(currentCard.getNotecardStack().toString(),currentCard.getSide1Title(),currentCard.getSide1Text(),currentCard.getSide3Title(),currentCard.getSide3Text(),((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide3Entry().getText()));
            }
        }
        else {
            // add a missed entry for 2-D
            report.missedEntries.add(new MissedEntry(currentCard.getNotecardStack().toString(),currentCard.getSide1Title(),currentCard.getSide1Text(),currentCard.getSide2Title(),currentCard.getSide2Text(),((EnterTextTwoSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().getText()));
        }

        // inform the user of their incorrect answer(s)
        answerDialog.setTitle("Incorrect");
		
        if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
            if(!correct2ndSide && !correct3rdSide) {
                answerDialog.setMessage("The correct " + currentCard.getSide2Title() + " answer is \"" + currentCard.getSide2Text() + "\".");
                answerDialog.setMessageLine2("The correct " + currentCard.getSide3Title() + " answer is \"" + currentCard.getSide3Text() + "\".");
            }
            else if(!correct2ndSide) {
                answerDialog.setMessage("The correct " + currentCard.getSide2Title() + " answer is \"" + currentCard.getSide2Text() + "\".");
                answerDialog.setMessageLine2("");
            }
            else {
                answerDialog.setMessage("The correct " + currentCard.getSide3Title() + " answer is \"" + currentCard.getSide3Text() + "\".");
                answerDialog.setMessageLine2("");
            }
        }
        else { // 2-D
            answerDialog.setMessage("The correct " + currentCard.getSide2Title() + " answer is \"" + currentCard.getSide2Text() + "\".");
            answerDialog.setMessageLine2("");
        }
        answerDialog.setMessageInFocus();
        answerDialog.setLocationRelativeTo(null);
        answerDialog.setVisible(true);        

        if(reviewMode == ReviewMode.TEST_MODE) {
            advanceCardTestMode();
            if(interactionPanel.getUserInputPanel() instanceof EntersText) {
                ((EntersText)interactionPanel.getUserInputPanel()).clearUserInput();
            }

            // scoring for test mode
            if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
                if(!correct2ndSide && !correct3rdSide) {
                    statsPanel.addIncorrectAnswer(2);
                }
                else { // one and only one side must be wrong here, since not both correct (handleCorrectAnswer) and bypass condition above
                    statsPanel.addCorrectAnswer();
                    statsPanel.addIncorrectAnswer();
                }
            }
            else { // 2-D case
                statsPanel.addIncorrectAnswer();
            }

            statsPanel.updateVisualScores();

        }
        else { // practice mode - more tests on settings
            if(settingsFrame.shouldSkipWrongAnswers()) {
                advanceCardPracticeMode(false);

                if(interactionPanel.getUserInputPanel() instanceof EntersText) {
                    ((EntersText)interactionPanel.getUserInputPanel()).clearUserInput();
                }

                // handle scoring here: (move on to next card, but in practice mode)
                if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
                    if(!correct2ndSide && !correct3rdSide) {
                        statsPanel.addIncorrectAnswer(2);
                    }
                    else { // one and only one side must be wrong here, since not both correct (handleCorrectAnswer) and bypass condition above
                        statsPanel.addCorrectAnswer();
                        statsPanel.addIncorrectAnswer();
                    }
                }
                else if(interactionPanel.getUserInputPanel() instanceof EnterTextTwoSidesPanel) {
                    statsPanel.addIncorrectAnswer();
                }

                statsPanel.updateVisualScores();
            }
            else {
                if(!correct2ndSide && !correct3rdSide && interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
                    // both incorrect, so clear both fields if user specifies it - in practice mode, after all
                    if(settingsFrame.shouldClearAnswer()) {
                        ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).clearUserInput();
                    }
                    interactionPanel.getUserInputPanel().requestDefaultComponentFocus();

                    statsPanel.addIncorrectAnswer(2);
                    statsPanel.updateVisualScores();
                }
                else if(!correct2ndSide) { // if 3-D
                    if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
                        if(settingsFrame.shouldClearAnswer()) {
                            ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).clearSide2Entry();
                        }
                        ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide3Entry().setEnabled(false);
                        ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().requestFocusInWindow();

                        if(!secondSideIncorrectFlag) {
                            statsPanel.addCorrectAnswer();
                        }
                        statsPanel.addIncorrectAnswer();
                        statsPanel.updateVisualScores();

                        secondSideIncorrectFlag = true;
                    }
                    else if(interactionPanel.getUserInputPanel() instanceof EnterTextTwoSidesPanel) {
                        if(settingsFrame.shouldClearAnswer()) {
                            ((EnterTextTwoSidesPanel)interactionPanel.getUserInputPanel()).clearUserInput();
                        }
						
                        ((EnterTextTwoSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().requestFocusInWindow();
                        statsPanel.addIncorrectAnswer();
                        statsPanel.updateVisualScores();
                    }
                }
                else if(!correct3rdSide && interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
                    if(settingsFrame.shouldClearAnswer()) {
                        ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).clearSide3Entry();
                    }
                    
                    ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).getSide2Entry().setEnabled(false);
                    setThirdSideFocus = true;

                    if(!thirdSideIncorrectFlag) {
                        statsPanel.addCorrectAnswer();
                    }
                    statsPanel.addIncorrectAnswer();
                    statsPanel.updateVisualScores();

                    thirdSideIncorrectFlag = true;
                }
            }
        }

        if((stackArrangement != StackRepresentation.NON_MIXED_STACKS && currentCardIndex == cardsToUseMixed.size()) || (stackArrangement == StackRepresentation.NON_MIXED_STACKS && currentListIndex == cardsToUsePartitioned.size()-1 && currentCardIndex == cardsToUsePartitioned.get(currentListIndex).size())) {
            endReviewSession();
        }
        else {
            setVisualComponents();
            if(setThirdSideFocus) { // must set third side focus down here, since setVisualComponents calls setDefaultComponentFocus
                if(interactionPanel.getUserInputPanel() instanceof EnterTextThreeSidesPanel) {
                    ((EnterTextThreeSidesPanel)interactionPanel.getUserInputPanel()).requestSide3Focus();
                }
            }
        }
    }
	
    private void advanceCardPracticeMode(boolean correct) {
        if(stackArrangement != StackRepresentation.NON_MIXED_STACKS) {
            if(timed || (!timed && !timerPanel.getOnePassButton().isSelected())) {
                if(settingsFrame.shouldSkipWrongAnswers() || correct) {
                    if(currentCardIndex == cardsToUseMixed.size()-1) {
                        if(settingsFrame.shouldShuffleAtEnd()) { // at last card, so test if should shuffle
                            shuffleCards();
                        }
                        currentCardIndex = 0;
                    }
                    else {
                        currentCardIndex++;
                    }
                }
            }
            else if(!timed && timerPanel.getOnePassButton().isSelected()) {
                if(settingsFrame.shouldSkipWrongAnswers() || correct) {
                    currentCardIndex++;
                }
            }

            if(currentCardIndex < cardsToUseMixed.size()) {
                currentCard = cardsToUseMixed.get(currentCardIndex);
            }            
        }
        else { // stacks are not mixed = partitioned
            if(timed || (!timed && !timerPanel.getOnePassButton().isSelected())) { // timed, or going on forever, so don't check for bad cards - just keep going
                if(settingsFrame.shouldSkipWrongAnswers() || correct) {
                    if(currentCardIndex == cardsToUsePartitioned.get(currentListIndex).size()-1) {
                        if(currentListIndex == cardsToUsePartitioned.size()-1) {
                            if(settingsFrame.shouldShuffleAtEnd()) { // see if user wants to shuffle at very end
                                shuffleCards();
                            }
                            currentListIndex = 0;
                        }
                        else {
                            currentListIndex++;
                        }
                        currentCardIndex = 0;
                    }
                    else {
                        currentCardIndex++; // normal scenario - just move on to next card
                    }
                }
            }
            else if(!timed && timerPanel.getOnePassButton().isSelected()) {
                if(settingsFrame.shouldSkipWrongAnswers() || correct) {
                    if(currentCardIndex == cardsToUsePartitioned.get(currentListIndex).size()-1) {
                        if(currentListIndex == cardsToUsePartitioned.size()-1) {
                            currentCardIndex++;
                        }
                        else {
                            currentCardIndex = 0;
                            currentListIndex++;
                        }
                    }
                    else {
                        currentCardIndex++;
                    }
                }
            }

            // assign card if valid
            if(currentCardIndex < cardsToUsePartitioned.get(currentListIndex).size()) {
                currentCard = cardsToUsePartitioned.get(currentListIndex).get(currentCardIndex);
            }
        }
    }
	
    private void advanceCardTestMode() {
        if(stackArrangement != StackRepresentation.NON_MIXED_STACKS) { // one stack or mixed stacks
            if(currentCardIndex == cardsToUseMixed.size()-1 && timed) {
                shuffleCards();
                currentCardIndex = 0;
            }
            else { // one pass only
                currentCardIndex++;
            }
            if(currentCardIndex < cardsToUseMixed.size()) {
                currentCard = cardsToUseMixed.get(currentCardIndex);
            }
        }
        else { // partitioned stacks
            if(currentCardIndex == cardsToUsePartitioned.get(currentListIndex).size()-1) {
                if(currentListIndex == cardsToUsePartitioned.size()-1) {
                    if(timed) {
                        shuffleCards();
                        currentCardIndex = 0;
                        currentListIndex = 0;
                    }
                    else {
                        currentCardIndex++;
                    }
                }
                else {
                    currentListIndex++;
                    currentCardIndex = 0;
                }
            }
            else {
                currentCardIndex++;
            }
            if(currentCardIndex < cardsToUsePartitioned.get(currentListIndex).size()) {
                currentCard = cardsToUsePartitioned.get(currentListIndex).get(currentCardIndex);
            }
        }
    }
	
    private void advanceCardForwardFlipMode() {
        if(stackArrangement != StackRepresentation.NON_MIXED_STACKS) {
            if(currentCardIndex == cardsToUseMixed.size()-1) {
                endReviewSession();
                return;
            }
            else {
                currentCardIndex++;
            }
            currentCard = cardsToUseMixed.get(currentCardIndex);
        }
        else {
            if(currentCardIndex == cardsToUsePartitioned.get(currentListIndex).size()-1) {
                if(currentListIndex == cardsToUsePartitioned.size()-1) {
                    endReviewSession();
                    return;
                }
                else {
                    currentListIndex++;
                    currentCardIndex = 0;
                }
            }
            else {
                currentCardIndex++;
            }

            currentCard = cardsToUsePartitioned.get(currentListIndex).get(currentCardIndex);
        }
    }
	
    private void advanceCardBackwardFlipMode() { // there is no need to shuffle things again in flip mode
        if(stackArrangement != StackRepresentation.NON_MIXED_STACKS) {
            if(currentCardIndex > 0) {
                currentCardIndex--;
            }

            currentCard = cardsToUseMixed.get(currentCardIndex);
        }
        else {
            if(currentListIndex > 0) {
                if(currentCardIndex == 0) {
                    currentListIndex--;
                    currentCardIndex = cardsToUsePartitioned.get(currentListIndex).size()-1;
                }
                else {
                    currentCardIndex--;
                }
            }
            else { // at first deck
                if(currentCardIndex > 0) {
                    currentCardIndex--;
                }
            }

            currentCard = cardsToUsePartitioned.get(currentListIndex).get(currentCardIndex);
        }
    }

    // helper method for transformNotecardForReview
    private NotecardStackReviewInfo findReviewInfo(NotecardStack ns) {
        for(NotecardStackReviewInfo nsri : stackAndSettingsList) {
            if(nsri.stack.equals(ns)) {
                return nsri;
            }
        }
		
        return null;
    }
	
    private ReviewNotecard transformNotecardForReview(Notecard n) {
        NotecardStackReviewInfo nsri = findReviewInfo(n.getNotecardStack());

        ReviewNotecard rn = null;
        if(n instanceof Notecard3D) {
            if(nsri.getSidesToTest().size() == 3) { // all three selected
                switch(nsri.get3DDefaultSide()) {
                    case SIDE_1:
                        rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString(),n.getNotecardStack().getSide3Title(),((Notecard3D)n).getThirdSide().toString());
                        break;
                    case SIDE_2:
                        rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString(),n.getNotecardStack().getSide3Title(),((Notecard3D)n).getThirdSide().toString(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString());
                        break;
                    case SIDE_3:
                        rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide3Title(),((Notecard3D)n).getThirdSide().toString(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString());
                        break;
                }
            }
            else if(nsri.getSidesToTest().size() == 2) {
                // b+c
                if(!nsri.getSidesToTest().contains(NotecardSides.SIDE_1)) {
                    if(nsri.get3DDefaultSide() == NotecardSides.SIDE_2) {
                        rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString(),n.getNotecardStack().getSide3Title(),((Notecard3D)n).getThirdSide().toString());
                    }
                    else {
                        rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide3Title(),((Notecard3D)n).getThirdSide().toString(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString());
                    }
                }
                else {
                    if(nsri.getSidesToTest().contains(NotecardSides.SIDE_2)) {
                        if(nsri.get3DDefaultSide() == NotecardSides.SIDE_1) {
                            rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString());
                        }
                        else { // side 2 is the default (front side)
                            rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString());
                        }
                    }
                    else {
                        if(nsri.get3DDefaultSide() == NotecardSides.SIDE_1) {
                            rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString(),n.getNotecardStack().getSide3Title(),((Notecard3D)n).getThirdSide().toString());
                        }
                        else {
                            rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide3Title(),((Notecard3D)n).getThirdSide().toString(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString());
                        }
                    }
                }
            }
        }
        else { // the card to be loaded is 2-D
            if(nsri.get2DDefaultSide() == NotecardSides.SIDE_1) {
                rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString());
            }
            else {
                rn = new ReviewNotecard(n.getNotecardStack(),n.getNotecardStack().getSide2Title(),n.getSecondSide().toString(),n.getNotecardStack().getSide1Title(),n.getFirstSide().toString());
            }
        }

        return rn;
    }

    private void loadNotecardsForReview() {
        cardsToUsePartitioned.clear();
        cardsToUseMixed.clear();

        ArrayList<NotecardStack> stacks = getStacks();
        stacksToUse.clear();

        for(int i = 0; i < settingsFrame.getListsPanel().getStacksModel().getSize(); i++) {
            if(((NotecardStackCheckboxComponent)settingsFrame.getListsPanel().getStacksModel().getElementAt(i)).isSelected()) {
                stacksToUse.add(((NotecardStackCheckboxComponent)settingsFrame.getListsPanel().getStacksModel().getElementAt(i)).getNotecardStack());
            }
        }
        
        settingsFrame.getListsPanel().updateCurrentList();
        
        if(stacksToUse.size() == 1) {
            stackArrangement = StackRepresentation.ONE_STACK;
            int stackIndex = -1;
            for(int i = 0; i < settingsFrame.getListsPanel().getStacksModel().getSize(); i++) {
                if(((NotecardStackCheckboxComponent)settingsFrame.getListsPanel().getStacksModel().getElementAt(i)).getNotecardStack().equals(stacksToUse.get(0))) {
                    stackIndex = i;
                    break;
                }
            }
			
            for(Integer j : settingsFrame.getListsPanel().getSelectedCardIndices().get(stackIndex)) {
                Notecard n = stacks.get(stackIndex).getNotecards().get(j.intValue());
                cardsToUseMixed.add(transformNotecardForReview(n));
            }
        }
        else {
            if(settingsFrame.shouldMixMultipleDecks()) {
                stackArrangement = StackRepresentation.MIXED_STACKS;
                for(int i = 0; i < settingsFrame.getListsPanel().getSelectedCardIndices().size(); i++) {
                    if(!((NotecardStackCheckboxComponent)settingsFrame.getListsPanel().getStacksModel().getElementAt(i)).isSelected()) {
                        continue;
                    }

                    for(Integer j : settingsFrame.getListsPanel().getSelectedCardIndices().get(i)) {
                        Notecard n = stacks.get(i).getNotecards().get(j.intValue());
                        cardsToUseMixed.add(transformNotecardForReview(n));
                    }

                }
            }
            else {
                stackArrangement = StackRepresentation.NON_MIXED_STACKS;

                for(int i = 0; i < settingsFrame.getListsPanel().getSelectedCardIndices().size(); i++) {
                    if(!((NotecardStackCheckboxComponent)settingsFrame.getListsPanel().getStacksModel().getElementAt(i)).isSelected()) {
                        continue;
                    }

                    ArrayList<ReviewNotecard> stack = new ArrayList<ReviewNotecard>();
                    for(Integer j : settingsFrame.getListsPanel().getSelectedCardIndices().get(i)) {
                        Notecard n = stacks.get(i).getNotecards().get(j.intValue());
                        stack.add(transformNotecardForReview(n));
                    }

                    cardsToUsePartitioned.add(stack);
                }
            }
        }
    }

    private void initializeFirstCard() {
        // test the arrangement and set the current card
        if(stackArrangement == StackRepresentation.ONE_STACK || stackArrangement == StackRepresentation.MIXED_STACKS) {
            currentCard = cardsToUseMixed.get(0);
        }
        else {
            currentCard = cardsToUsePartitioned.get(0).get(0);
        }
    }

    private boolean thereAreNoCardsLoaded() {
        if(stackArrangement != StackRepresentation.NON_MIXED_STACKS) {
            if(cardsToUseMixed.isEmpty()) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            for(ArrayList<ReviewNotecard> innerList : cardsToUsePartitioned) {
                if(!innerList.isEmpty()) {
                    return false;
                }
            }
            return true;
        }


    }

    private void toggleComponentsForReview() {
        if(reviewMode == ReviewMode.PRACTICE_MODE || reviewMode == ReviewMode.TEST_MODE) {
            buttonsPanel.getEnterButton().setEnabled(true);
            buttonsPanel.getAddAccentButton().setEnabled(true);
            addAccentButton.setEnabled(true);
        }        
		
        viewReport.setEnabled(false);
        start.setEnabled(false);
        if(reviewMode != ReviewMode.FLIP_MODE) {
            pause.setEnabled(true);
        }
        stop.setEnabled(true);
        settingsMenu.setEnabled(false);

        // toolbar
        viewReportButton.setEnabled(false);
        startButton.setEnabled(false);
        if(reviewMode != ReviewMode.FLIP_MODE) {
            pauseButton.setEnabled(true);
        }
        stopButton.setEnabled(true);
        settingsButton.setEnabled(false);

        statsPanel.showStats(reviewMode != ReviewMode.FLIP_MODE); // if entry mode, show how many right, wrong, etc.
		
        timerPanel.disableComponents();
        minutes = timerPanel.getMinutes().intValue();
        seconds = timerPanel.getSeconds().intValue();
        timerPanel.getTimeLabel().setForeground(Color.WHITE);
    }
	
    private void setVisualComponents() {
        if(currentCard != null) {
            interactionPanel.refreshTo(currentCard.hasThreeSides()?(UserInteractionPanel.CardDimension.THREE_SIDES):(UserInteractionPanel.CardDimension.TWO_SIDES),reviewMode);
            interactionPanel.getUserInputPanel().requestDefaultComponentFocus();

            notecardPanel.setTitleText(currentCard.getSide1Title());
            notecardPanel.setNotecardText(currentCard.getSide1Text());

            statsPanel.setStackTitleLabel("Stack: " + currentCard.getNotecardStack().toString());

            String indexString = "Card " + (currentCardIndex+1) + " of ";
            switch(stackArrangement) {
                case NON_MIXED_STACKS:
                    indexString += cardsToUsePartitioned.get(currentListIndex).size();
                    break;
                case ONE_STACK: case MIXED_STACKS:
                    indexString += cardsToUseMixed.size();
                    break;
            }
            statsPanel.setCurrentCardIndexLabel(indexString);

            interactionPanel.getUserInputPanel().setSide2Title(currentCard.getSide2Title());
            if(interactionPanel.getUserInputPanel() instanceof HasThreeSides) {
                ((HasThreeSides)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide3Title());
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
    
    private void handleFrameClosing() {        
        cleanUp();
        
        for(NotecardStackTreeObject nsto : treeNodes) {
            try {
                nsto.releaseReviewFrame(this);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        dispose();
    }

    public void cleanUp() {
        reviewTimer.stop();
        settingsFrame.dispose();
        reportListFrame.dispose();
    }

    public Set<NotecardStackTreeObject> getTreeObjects() {
        return treeNodes;
    }
    
    public void windowClosing(WindowEvent e) {
        handleFrameClosing();
    }
	
    public void windowClosed(WindowEvent e) {
    }
	
    public void windowIconified(WindowEvent e) {
    }
	
    public void windowDeiconified(WindowEvent e) {
    }
	
    public void windowActivated(WindowEvent e) {
    }
	
    public void windowDeactivated(WindowEvent e) {
    }
	
    public void windowOpened(WindowEvent e) {
    }

    class TimerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(running) {
                pauseButton.setToolTipText("Pause current notecard review");

                if(timed) {
                    seconds--;
                    if(seconds == -1) {
                        seconds = 59;
                        minutes--;
                        if(minutes < 0) {
                            endReviewSession();
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
            else {
                pauseButton.setToolTipText("Resume current notecard review");
            }
        }
    }

    public void setTimerRunning(boolean value) {
        running = value;
    }

    public ArrayList<NotecardStackReviewInfo> getStacksAndSettings() {
        return stackAndSettingsList;
    }

    public ArrayList<NotecardStack> getStacks() {
        ArrayList<NotecardStack> stacks = new ArrayList<NotecardStack>();
        for(NotecardStackReviewInfo ss : stackAndSettingsList) {
            stacks.add(ss.stack);
        }
        return stacks;
    }

    public void focusGained(FocusEvent e) {
        if(e.getSource() instanceof JTextField) {
            focusedComponent = (JTextField)e.getSource();
        }
    }

    public void focusLost(FocusEvent e) {
    }

    private void shuffleCards() {
        switch(stackArrangement) {
            case ONE_STACK: case MIXED_STACKS:
                Collections.shuffle(cardsToUseMixed);
                break;
            case NON_MIXED_STACKS:
                for(ArrayList<ReviewNotecard> stack : cardsToUsePartitioned) {
                    Collections.shuffle(stack);
                }
                break;
        }
    }

    private void flipCard2D() {
        if(notecardPanel.getSideTitleText().equals(currentCard.getSide1Title())) {
            notecardPanel.setTitleText(currentCard.getSide2Title());
            notecardPanel.setNotecardText(currentCard.getSide2Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardTwoSidesPanel) {
                ((FlipCardTwoSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide1Title());
            }
        }
        else {
            notecardPanel.setTitleText(currentCard.getSide1Title());
            notecardPanel.setNotecardText(currentCard.getSide1Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardTwoSidesPanel) {
                ((FlipCardTwoSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide2Title());
            }
        }
    }
	
    private void flipCard3DLeft() {
        String title = notecardPanel.getSideTitleText();
        if(title.equals(currentCard.getSide1Title())) {
            notecardPanel.setTitleText(currentCard.getSide3Title());
            notecardPanel.setNotecardText(currentCard.getSide3Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide2Title());
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide1Title());
            }
        }
        else if(title.equals(currentCard.getSide2Title())) {
            notecardPanel.setTitleText(currentCard.getSide1Title());
            notecardPanel.setNotecardText(currentCard.getSide1Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide3Title());
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide2Title());
            }
        }
        else {
            notecardPanel.setTitleText(currentCard.getSide2Title());
            notecardPanel.setNotecardText(currentCard.getSide2Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide1Title());
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide3Title());
            }
        }
    }
	
    private void flipCard3DRight() {
        String title = notecardPanel.getSideTitleText();
        if(title.equals(currentCard.getSide1Title())) {
            notecardPanel.setTitleText(currentCard.getSide2Title());
            notecardPanel.setNotecardText(currentCard.getSide2Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide1Title());
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide3Title());
            }
        }
        else if(title.equals(currentCard.getSide2Title())) {
            notecardPanel.setTitleText(currentCard.getSide3Title());
            notecardPanel.setNotecardText(currentCard.getSide3Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide2Title());
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide1Title());
            }
        }
        else {
            notecardPanel.setTitleText(currentCard.getSide1Title());
            notecardPanel.setNotecardText(currentCard.getSide1Text());

            if(interactionPanel.getUserInputPanel() instanceof FlipCardThreeSidesPanel) {
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide2Title(currentCard.getSide3Title());
                ((FlipCardThreeSidesPanel)interactionPanel.getUserInputPanel()).setSide3Title(currentCard.getSide2Title());
            }
        }
    }

    public boolean isRunning() {
        return reviewTimer.isRunning();
    }
}

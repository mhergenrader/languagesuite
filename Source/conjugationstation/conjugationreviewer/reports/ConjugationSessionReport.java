package conjugationstation.conjugationreviewer.reports;

import java.io.*;
import java.util.*;
import java.text.*;
import languagesuite.*;

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
 * Model class for a report on how well a conjugation session went for the user.
 * @author Michael Hergenrader
 */
public class ConjugationSessionReport implements Serializable {

    public StringBuilder userName;
    public StringBuilder dateAndTime;
    public StringBuilder language;
    public int numberCorrect;
    public int numberIncorrect;
    public int numberOfPauses;

    public java.util.List<String> verbSetsUsed;
    public StringBuilder timeLimit;
    public boolean onePassUsed;
    public StringBuilder conjugationMode;

    public java.util.List<MissedVerb> missedVerbs;
    
    public java.util.List<String> stacksUsed;

    public ConjugationSessionReport() {
        userName = new StringBuilder("Michael Hergenrader"); // at the very beginning, need to ask for user name
        dateAndTime = new StringBuilder();
        language = new StringBuilder();
        numberCorrect = 0;
        numberIncorrect = 0;
        numberOfPauses = 0;

        verbSetsUsed = new ArrayList<String>();
        timeLimit = new StringBuilder();
        onePassUsed = false;
        conjugationMode = new StringBuilder();

        missedVerbs = new ArrayList<MissedVerb>();
    }

    public ConjugationSessionReport(ConjugationSessionReport report) {
        this.userName = new StringBuilder(report.userName.toString());
        this.dateAndTime = new StringBuilder(report.dateAndTime.toString());
        this.language = new StringBuilder(report.language.toString());
        this.numberCorrect = report.numberCorrect;
        this.numberIncorrect = report.numberIncorrect;
        this.numberOfPauses = report.numberOfPauses;

        this.verbSetsUsed = new ArrayList<String>();
        for(String s : report.verbSetsUsed) {
            this.verbSetsUsed.add(new String(s));
        }

        this.timeLimit = new StringBuilder(report.timeLimit.toString());
        this.onePassUsed = report.onePassUsed;
        this.conjugationMode = new StringBuilder(report.conjugationMode.toString());

        this.missedVerbs = new ArrayList<MissedVerb>();
        for(MissedVerb mv : report.missedVerbs) {
            this.missedVerbs.add(mv);
        }
    }

    public String toString() {
        return language.toString() + "; " + dateAndTime.toString() + ", " + userName.toString();
    }

    public void setName(String name) {
        this.userName.delete(0,this.userName.length());
        this.userName.append(name);
    }

    public void setDateAndTime() {
        this.dateAndTime.delete(0,this.dateAndTime.length());

        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy    HH:mm:ss");
        Date date = new Date();
        this.dateAndTime.append(dateFormat.format(date));
    }

    public void setLanguage(String language) {
        this.language.delete(0,this.language.length());
        this.language.append(language);
    }

    public void setConjugationMode(String mode) {
        this.conjugationMode.delete(0,this.conjugationMode.length());
        this.conjugationMode.append(mode);
    }

    public void setTimeLimit(String limit) {
        this.timeLimit.delete(0,this.timeLimit.length());
        this.timeLimit.append(limit);
    }
}


package notecardstation.notecardreviewer.reports;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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
 * High-level report of a notecard review session, showing how the user performed
 * @author Michael Hergenrader
 */
public class ReviewSessionReport implements Serializable {

    public StringBuilder userName;
    public StringBuilder dateAndTime;
    public java.util.List<String> stacksUsed;

    public int numberCorrect;
    public int numberIncorrect;
    public int numberOfPauses;
    public StringBuilder timeLimit;
    public boolean onePassUsed;
    public StringBuilder reviewMode;

    public java.util.List<MissedEntry> missedEntries;

    public ReviewSessionReport() {
        userName = new StringBuilder("Michael Hergenrader"); // just a dummy name for now
        dateAndTime = new StringBuilder();
        numberCorrect = 0;
        numberIncorrect = 0;
        numberOfPauses = 0;

        stacksUsed = new ArrayList<String>();
        timeLimit = new StringBuilder();
        onePassUsed = false;
        reviewMode = new StringBuilder();

        missedEntries = new ArrayList<MissedEntry>();
    }

    public ReviewSessionReport(ReviewSessionReport report) {
        this.userName = new StringBuilder(report.userName.toString());
        this.dateAndTime = new StringBuilder(report.dateAndTime.toString());
        this.numberCorrect = report.numberCorrect;
        this.numberIncorrect = report.numberIncorrect;
        this.numberOfPauses = report.numberOfPauses;

        this.stacksUsed = new ArrayList<String>();
        for(String s : report.stacksUsed) {
            this.stacksUsed.add(new String(s));
        }

        this.timeLimit = new StringBuilder(report.timeLimit.toString());
        this.onePassUsed = report.onePassUsed;
        this.reviewMode = new StringBuilder(report.reviewMode.toString());

        this.missedEntries = new ArrayList<MissedEntry>();
        for(MissedEntry me : report.missedEntries) {
            this.missedEntries.add(me);
        }
    }

    public String toString() {
        return stacksUsed.toString() + "; " + dateAndTime.toString() + ", " + userName.toString();
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

    public void setReviewMode(String mode) {
        this.reviewMode.delete(0,this.reviewMode.length());
        this.reviewMode.append(mode);
    }

    public void setTimeLimit(String limit) {
        this.timeLimit.delete(0,this.timeLimit.length());
        this.timeLimit.append(limit);
    }
}

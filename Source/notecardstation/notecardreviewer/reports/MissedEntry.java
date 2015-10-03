package notecardstation.notecardreviewer.reports;

import java.io.*;

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
 * Represents an incorrect answer provided by a user, aggregated into reports
 * @author Michael Hergenrader
 */
public class MissedEntry implements Serializable {

    private String notecardStack;
    private String frontSideTitle;
    private String frontSideText;

    private String testedSideTitle;
    private String testedSideCorrectAnswer; // what was on the other side of this (could be "2nd" or "3rd" side depending on notecard dimensions) 
    private String testedSideUserEntry;

    public MissedEntry(String notecardStack, String frontSideTitle, String frontSideText, String testedSideTitle, String testedSideCorrectAnswer, String testedSideUserEntry) {
        this.notecardStack = new String(notecardStack);
        this.frontSideTitle = new String(frontSideTitle);
        this.frontSideText = new String(frontSideText);
        this.testedSideTitle = new String(testedSideTitle);
        this.testedSideCorrectAnswer = new String(testedSideCorrectAnswer);
        this.testedSideUserEntry = new String(testedSideUserEntry);
    }

    public String getNotecardStack() {
        return notecardStack;
    }

    public String getFrontSideTitle() {
        return frontSideTitle;
    }

    public String getFrontSideText() {
        return frontSideText;
    }

    public String getTestedSideTitle() {
        return testedSideTitle;
    }

    public String getTestedSideCorrectAnswer() {
        return testedSideCorrectAnswer;
    }

    public String getTestedSideUserAnswer() {
        return testedSideUserEntry;
    }
}

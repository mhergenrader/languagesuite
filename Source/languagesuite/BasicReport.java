package languagesuite;

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
 * Simple report structure shared across conjugation and notecard practice
 * @author Michael Hergenrader
 */
public class BasicReport implements Serializable {
    public StringBuilder userName;
    public StringBuilder dateAndTime;

    public int numberCorrect;
    public int numberIncorrect;
    public int numberOfPauses;

    public StringBuilder timeLimit;
    public boolean onePassUsed;

    public BasicReport() { // just default strings for now
        userName = new StringBuilder("Michael Hergenrader");
        dateAndTime = new StringBuilder("now");
        numberCorrect = 0;
        numberIncorrect = 0;
        numberOfPauses = 1;
        timeLimit = new StringBuilder("5:00");
        onePassUsed = false;
    }

    @Override
    public String toString() {
        return new String("Student Name: " + userName.toString() + "\n" +
                          "Date: " + dateAndTime.toString() + "\n" +
                          "Number Correct: " + numberCorrect + "\n" +
                          "Number Incorrect: " + numberIncorrect + "\n" +
                          "Times Paused: " + numberOfPauses + "\n" +
                          "Time Limit: " + timeLimit.toString() + "\n" +
                          "One Pass Used? " + onePassUsed);                
    }
}

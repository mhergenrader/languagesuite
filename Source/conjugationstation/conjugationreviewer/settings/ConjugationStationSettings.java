package conjugationstation.conjugationreviewer.settings;

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
 * Customizing settings for conjugation practice
 * @author Michael Hergenrader
 */
public class ConjugationStationSettings implements Serializable {
    private boolean wrongAnswerCleared; // TODO: what about skipping wrong answers?
    private boolean betweenClockPause;
    private boolean shuffleAtWraparound;

    public ConjugationStationSettings() {
        wrongAnswerCleared = false;
        betweenClockPause = false;
        shuffleAtWraparound = true;
    }
    public ConjugationStationSettings(boolean wrongAnswerCleared, boolean betweenClockPause, boolean shuffleAtWraparound) {
        this.wrongAnswerCleared = wrongAnswerCleared;
        this.betweenClockPause = betweenClockPause;
        this.shuffleAtWraparound = shuffleAtWraparound;
    }
    public boolean shouldWrongAnswerBeCleared() {
        return wrongAnswerCleared;
    }
    public boolean shouldClockPauseBetweenAnswers() {
        return betweenClockPause;
    }
    public boolean shouldShuffleAtWraparound() {
        return shuffleAtWraparound;
    }
    public void setWrongAnswerClearedOption(boolean value) {
        wrongAnswerCleared = value;
    }
    public void setBetweenClockPauseOption(boolean value) {
        betweenClockPause = value;
    }
    public void setShuffleAtWraparoundOption(boolean value) {
        shuffleAtWraparound = value;
    }
}

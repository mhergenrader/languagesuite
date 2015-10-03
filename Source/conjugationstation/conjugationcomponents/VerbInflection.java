package conjugationstation.conjugationcomponents;

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
 * One instance of a verb conjugation: a particular inflection object for a subject-verb combo
 * @author Michael Hergenrader
 */
public class VerbInflection implements Serializable {

    private StringBuilder text;

    public VerbInflection() {
        this.text = new StringBuilder("");
    }

    public VerbInflection(String text) {
        this.text = new StringBuilder(text.trim());
    }

    public VerbInflection(VerbInflection other) {
        this.text = new StringBuilder(other.toString());
    }

    public String toString() { // = getText()
        return text.toString();
    }

    public void setText(String text) {
        this.text.delete(0,this.text.length());
        this.text.append(text.trim());
    }

    public boolean equals(Object o) {
        if(o instanceof VerbInflection) {
            if(((VerbInflection)o).toString().equals(text.toString())) {
                return true;
            }
        }
        return false;
    }
}

package notecardstation.notecard;

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
 * Basic Notecard object (2-sided)
 * @author Michael Hergenrader
 */
public class Notecard implements Serializable {

    protected StringBuilder title;

    protected NotecardSide firstSide;
    protected NotecardSide secondSide;

    protected NotecardStack stackReference;

    public Notecard() {
        createBlankNotecard();
        title = new StringBuilder("");
    }

    public Notecard(String title) {
        createBlankNotecard();
        this.title = new StringBuilder(title.trim());
    }

    public Notecard(String title, String side1Text, String side2Text) {
        this(title);
        firstSide.setText(side1Text);
        secondSide.setText(side2Text);
    }

    // copy constructor for general notecards: 2-D or 3-D
    public Notecard(Notecard other) {
        firstSide = new NotecardSide(other.getFirstSide().toString());
        secondSide = new NotecardSide(other.getSecondSide().toString());
        title = new StringBuilder(other.getTitle().trim());
        this.stackReference = other.getNotecardStack();
    }

    protected void createBlankNotecard() {
        firstSide = new NotecardSide("");
        secondSide = new NotecardSide("");
    }

    public String getTitle() {
        return title.toString().trim();
    }

    public void setTitle(String text) {
        title.delete(0,title.length());
        title.append(text.trim());
    }

    @Override
    public String toString() {
        return title.toString();
    }

    @Override
    public boolean equals(Object o) { // same card if has 2 dimensions and the title and sides are the same
        return (o instanceof Notecard) && !(o instanceof Notecard3D) && this.title.toString().equals(((Notecard)o).getTitle().toString()) && this.firstSide.toString().equals(((Notecard)o).getFirstSide().toString()) && this.secondSide.toString().equals(((Notecard)o).getSecondSide().toString()) && this.stackReference.toString().equals(((Notecard)o).getNotecardStack().toString());
    }

    public NotecardSide getFirstSide() {
        return firstSide;
    }

    public NotecardSide getSecondSide() {
        return secondSide;
    }

    public void setStackReference(NotecardStack ns) {
        this.stackReference = ns;
    }

    public NotecardStack getNotecardStack() {
        return stackReference;
    }
}

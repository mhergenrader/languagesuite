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
 * 3-sided notecard: helpful for languages with glyphs, characters, or radicals
 * Can be helpful with the characters, Latin character set translations, and pronunciations
 * @author Michael Hergenrader
 */
public class Notecard3D extends Notecard implements Serializable {

    private NotecardSide thirdSide;

    public Notecard3D() {
        super(); // might not need to call this
        thirdSide = new NotecardSide("");
    }

    public Notecard3D(String title) {
        super(title);
        thirdSide = new NotecardSide("");
    }

	// can use this to transform a 2-D notecard into a 3-D one
    public Notecard3D(String title, String side1Text, String side2Text, String side3Text) {
        super(title,side1Text,side2Text);
        thirdSide = new NotecardSide(side3Text);
    }

    public Notecard3D(Notecard other) {
        super(other);
        thirdSide = new NotecardSide("");
    }

    public Notecard3D(Notecard3D other) {
        super(other);
        thirdSide = new NotecardSide(other.getThirdSide().toString());
    }

    public NotecardSide getThirdSide() {
        return thirdSide;
    }

    @Override
    public boolean equals(Object o) { // same card if has 2 dimensions and the title and sides are the same
        return (o instanceof Notecard3D) && this.title.toString().equals(((Notecard3D)o).getTitle().toString()) && this.firstSide.toString().equals(((Notecard3D)o).getFirstSide().toString()) && this.secondSide.toString().equals(((Notecard3D)o).getSecondSide().toString()) && this.thirdSide.toString().equals(((Notecard3D)o).getThirdSide().toString()) && this.stackReference.toString().equals(((Notecard3D)o).getNotecardStack().toString());
    }
}

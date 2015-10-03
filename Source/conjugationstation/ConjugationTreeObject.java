package conjugationstation;

import conjugationstation.conjugationcomponents.*;
import conjugationstation.conjugationeditor.*;
import conjugationstation.conjugationreviewer.*;
import java.util.*;

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
 * Wrapper class for a verb set and open editor frame
 * @author Michael Hergenrader
 */
public class ConjugationTreeObject {
    private ConjugatedVerbSet conjugationVerbSet;
    public ConjugationEditorFrame editorFrame;
    private java.util.List<ConjugationReviewFrame> reviewFrames;

    public ConjugationTreeObject(ConjugatedVerbSet conjugationVerbSet) {
        this.conjugationVerbSet = conjugationVerbSet;
        reviewFrames = new ArrayList<ConjugationReviewFrame>();
    }

    public void overwriteVerbSetWith(ConjugatedVerbSet newSet) {
        this.conjugationVerbSet.setTitle(newSet.toString());
        this.conjugationVerbSet.setAuthor(newSet.getAuthor());

        conjugationVerbSet.getVerbs().clear();
        for(ConjugatedVerb cv : newSet.getVerbs()) {
            conjugationVerbSet.addConjugatedVerb(new ConjugatedVerb(cv));
        }
    }

    public String toString() {
        return conjugationVerbSet.toString();
    }

    public String fullInfo() {
        return this.toString() + "; editor frame open? " + (editorFrame != null) + "; number of review frames = " + reviewFrames.size();
    }

    public ConjugatedVerbSet getVerbSet() {
        return conjugationVerbSet;
    }

    public int getNumberOfReviewFrames() {
        return reviewFrames.size();
    }

    public boolean canEditStack() {
        return reviewFrames.isEmpty();
    }

    public void addReviewFrame(ConjugationReviewFrame rf) {
        reviewFrames.add(rf);
    }

    public void releaseReviewFrame(ConjugationReviewFrame rf) throws Exception {
        if(reviewFrames.size() - 1 < 0) {
            throw new Exception("Error!! Number of frames reviewing this stack can't be negative!");
        }
        reviewFrames.remove(rf);
    }

    public void clearReviewFrames() {
        reviewFrames.clear();
    }

    public List<ConjugationReviewFrame> getReviewFrames() {
        return reviewFrames;
    }

    public ConjugationEditorFrame getEditorFrame() {
        return editorFrame;
    }
}
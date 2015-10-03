package conjugationstation.conjugationcomponents;

import java.io.*;
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
 * Class representing a conjugated verb (one with inflections)
 * @author Michael Hergenrader
 */
public class ConjugatedVerb implements Serializable {

    private StringBuilder infinitive;
    private int numberOfSubjectPronouns;
    private String setReference;

    private ArrayList<VerbInflection> conjugations;

    public ConjugatedVerb(String infinitive, int numberOfSubjectPronouns, String setReference) {
        this.infinitive = new StringBuilder(infinitive.trim());
        this.numberOfSubjectPronouns = numberOfSubjectPronouns;
        this.setReference = setReference;

        conjugations = new ArrayList<VerbInflection>();
        for(int i = 0; i < this.numberOfSubjectPronouns; i++) {
            conjugations.add(new VerbInflection());
        }
    }

    // copy constructor
    public ConjugatedVerb(ConjugatedVerb cv) {
        this.infinitive = new StringBuilder(cv.getInfinitive());
        this.numberOfSubjectPronouns = cv.getNumberOfSubjectPronouns();
        this.setReference = cv.getSetReference();
        this.conjugations = new ArrayList<VerbInflection>();
		
        for(int i = 0; i < this.numberOfSubjectPronouns; i++) {
            conjugations.add(new VerbInflection(cv.getConjugations().get(i).toString()));
        }
    }

    public boolean equals(Object o) {
        if(o instanceof ConjugatedVerb) {
            if(((ConjugatedVerb)o).getInfinitive().equals(infinitive.toString()) && ((ConjugatedVerb)o).getSetReference().equals(setReference) && ((ConjugatedVerb)o).getNumberOfSubjectPronouns() == this.numberOfSubjectPronouns) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return infinitive.toString();
    }

    public String getInfinitive() {
        return infinitive.toString();
    }

    public void setInfinitive(String text) {
        infinitive.delete(0,infinitive.length());
        infinitive.append(text.trim());
    }

    public int getNumberOfSubjectPronouns() {
        return numberOfSubjectPronouns;
    }

    public void addInflection(VerbInflection inflection) {
        conjugations.add(inflection);
    }

    public void setInflection(int index, String text) {
        conjugations.get(index).setText(text.trim());
    }

    public VerbInflection getInflection(int index) {
        return conjugations.get(index);
    }

    public String getSetReference() {
        return setReference;
    }

    public ArrayList<VerbInflection> getConjugations() {
        return conjugations;
    }
}

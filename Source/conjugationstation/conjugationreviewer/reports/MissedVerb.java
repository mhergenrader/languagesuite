package conjugationstation.conjugationreviewer.reports;

import java.io.*;
import conjugationstation.conjugationcomponents.*;

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
 * Instance of an incorrect conjugation during user practice.
 * @author Michael Hergenrader
 */
public class MissedVerb implements Serializable {
    private ConjugatedVerb verb; // used to also get the verb set it belongs to and, with subject pronoun, the correct answer
    private SubjectPronoun subjectPronoun;
    private String correctAnswer;
    private String userAnswer;
	
    public MissedVerb(ConjugatedVerb verb, SubjectPronoun subjectPronoun, String correctAnswer, String userAnswer) {
        this.verb = new ConjugatedVerb(verb.getInfinitive(),verb.getNumberOfSubjectPronouns(),verb.getSetReference());
        this.subjectPronoun = new SubjectPronoun(subjectPronoun.toString());
        this.correctAnswer = new String(correctAnswer);
        this.userAnswer = new String(userAnswer);
    }
    private MissedVerb() { // do not allow default instance of missed verbs: each must have the verb itself and the two answers (correct and incorrect)
    }

    public ConjugatedVerb getConjugatedVerb() {
        return verb;
    }
    public SubjectPronoun getSubjectPronoun() {
        return subjectPronoun;
    }
    public String getCorrectAnswer() {
        return correctAnswer;
    }
    public String getUserAnswer() {
        return userAnswer;
    }
}
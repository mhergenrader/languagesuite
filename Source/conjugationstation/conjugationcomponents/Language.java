package conjugationstation.conjugationcomponents;

import java.util.*;
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
 * Represents a language that can have conjugated verbs and its possible subject pronouns for conjugation practice.
 * @author Michael Hergenrader
 */
public class Language implements Serializable {

    private String title;
    private String author;
    private String createDate; // this is fine
    private ArrayList<SubjectPronoun> subjectPronouns;

    private TreeSet<ConjugatedVerbSet> conjugatedVerbSets;

    public Language(String title, String author, String createDate, ArrayList<String> pronounTextList) {
        this.title = title;
        this.author = author;
        this.createDate = createDate;
        subjectPronouns = new ArrayList<SubjectPronoun>();
        for(String s : pronounTextList) {
            subjectPronouns.add(new SubjectPronoun(s));
        } // copy the list into a new one of subject pronouns

        conjugatedVerbSets = new TreeSet<ConjugatedVerbSet>(new ConjugatedVerbSetComparator());
    }

    // copy constructor
    public Language(Language other) {
        this.title = new String(other.getTitle());
        this.author = new String(other.getAuthor());
        this.createDate = new String(other.getCreateDate());
        subjectPronouns = new ArrayList<SubjectPronoun>();
        for(SubjectPronoun sp : other.getSubjectPronouns()) {
            subjectPronouns.add(new SubjectPronoun(sp.toString()));
        }

        conjugatedVerbSets = new TreeSet<ConjugatedVerbSet>(new ConjugatedVerbSetComparator());
        for(ConjugatedVerbSet cvs : other.getConjugatedVerbSets()) {
            conjugatedVerbSets.add(new ConjugatedVerbSet(cvs));
        }
    }

    private Language() { // disallow default construction
    }

    public void addConjugatedVerbSet(ConjugatedVerbSet set) {
        conjugatedVerbSets.add(set);
    }

    public void removeConjugatedVerbSet(ConjugatedVerbSet set) {
        conjugatedVerbSets.remove(set);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String toString() {
        return title;
    }

    public boolean equals(Object o) {
        if(o instanceof Language) {
            if(((Language)o).getTitle().equals(title) && ((Language)o).getAuthor().equals(author) && ((Language)o).getCreateDate().equals(createDate) && ((Language)o).getSubjectPronouns().size() == subjectPronouns.size() && conjugatedVerbSets.size() == ((Language)o).getConjugatedVerbSets().size()) {
                return true;
            }
        }
        return false;
    }

    public String fullInfo() {
        StringBuilder str = new StringBuilder(title + ", created by " + author + " on " + createDate + "; Subject Pronouns:\n");
        for(SubjectPronoun sp : subjectPronouns) {
            str.append(sp.toString()+"\n");
        }

        return str.toString();
    }

    public ArrayList<SubjectPronoun> getSubjectPronouns() {
        return subjectPronouns;
    }

    public int getNumberOfSubjectPronouns() {
        return subjectPronouns.size();
    }

    public TreeSet<ConjugatedVerbSet> getConjugatedVerbSets() {
        return conjugatedVerbSets;
    }
}

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
 * Custom structure that contains a set of ConjugatedVerb objects
 * @author Michael Hergenrader
 */
public class ConjugatedVerbSet implements Serializable {

    private String title; // present indicative, etc.
    private String author;
    private ArrayList<ConjugatedVerb> conjugatedVerbs;
    private Language language;

    public ConjugatedVerbSet(String title, String author) {
        this.title = title.trim();
        this.author = author.trim();
        this.language = null;
        conjugatedVerbs = new ArrayList<ConjugatedVerb>();
    }

    public ConjugatedVerbSet(String title, String author, Language language) {
        this(title,author);
        this.language = language; // this should work the same
    }

    public ConjugatedVerbSet(ConjugatedVerbSet other) {
        this.title = new String(other.toString());
        this.author = new String(other.getAuthor());
        this.language = other.getLanguage();
        conjugatedVerbs = new ArrayList<ConjugatedVerb>();
        for(ConjugatedVerb cv : other.getVerbs()) {
            conjugatedVerbs.add(new ConjugatedVerb(cv)); // invokes other copy constructor
        }
    }

    public ArrayList<ConjugatedVerb> getVerbs() {
        return conjugatedVerbs;
    }

    public Language getLanguage() {
        return language;
    }

    public boolean equals(Object o) {
        if(o instanceof ConjugatedVerbSet) {
            if(((ConjugatedVerbSet)o).toString().equals(title) && ((ConjugatedVerbSet)o).getAuthor().equals(author) && ((ConjugatedVerbSet)o).getLanguage().toString().equals(language.toString()) && ((ConjugatedVerbSet)o).getVerbs().size() == conjugatedVerbs.size()) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        return title;
    }

    public void setTitle(String text) {
        this.title = text.trim();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String text) {
        this.author = text.trim();
    }

    public void addConjugatedVerb(ConjugatedVerb verb) {
        conjugatedVerbs.add(verb);
    }

    public void removeConjugatedVerb(ConjugatedVerb verb) {
        conjugatedVerbs.remove(verb);
    }

    public void clear() { // removes all verbs
        conjugatedVerbs.clear();
    }

    public String fullInfo() {
        StringBuilder str = new StringBuilder(title+"; verbs:\n");
        
        Iterator iter = conjugatedVerbs.iterator();
        while(iter.hasNext()) {
            ConjugatedVerb c = (ConjugatedVerb)iter.next();
            str.append(c.getInfinitive()+"\n");
        }

        return str.toString();
    }

    public void sortVerbsAlphabetically() {
        Collections.sort(conjugatedVerbs,new ConjugatedVerbComparator());
    }

    public void shuffleVerbs() {
        Collections.shuffle(conjugatedVerbs);
    }

}

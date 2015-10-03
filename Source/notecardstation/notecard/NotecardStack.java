package notecardstation.notecard;

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
 * Aggregate class for notecard objects (can mix 2-D and 3-D in the same stack/deck)
 * @author Michael Hergenrader
 */
public class NotecardStack implements Serializable {
    private ArrayList<Notecard> stack;

    private StringBuilder stackTitle;
    private StringBuilder author;
    private StringBuilder version;
    private StringBuilder lastModified;
    private StringBuilder language;

    private StringBuilder side1Title; // usually represents languages
    private StringBuilder side2Title;
    private StringBuilder side3Title;

    public NotecardStack() {
        stack = new ArrayList<Notecard>();

        this.stackTitle = new StringBuilder();
        this.author = new StringBuilder();
        this.version = new StringBuilder();
        this.lastModified = new StringBuilder();
        this.language = new StringBuilder();

        this.side1Title = new StringBuilder();
        this.side2Title = new StringBuilder();
        this.side3Title = new StringBuilder();
    }

    public NotecardStack(String stackTitle, String author, String version, String lastModified, String language, String side1Title, String side2Title, String side3Title, NotecardSide defaultSide) {
        stack = new ArrayList<Notecard>();

        this.stackTitle = new StringBuilder(stackTitle);
        this.author = new StringBuilder(author);
        this.version = new StringBuilder(version);
        this.lastModified = new StringBuilder(lastModified);
        this.language = new StringBuilder(language);

        this.side1Title = new StringBuilder(side1Title);
        this.side2Title = new StringBuilder(side2Title);
        this.side3Title = new StringBuilder(side3Title);
    }
	
    public NotecardStack(NotecardStack other) {
        this();

        for(Notecard n : other.getNotecards()) {
            if(n instanceof Notecard3D) {
                this.stack.add(new Notecard3D((Notecard3D)n));
            }
            else {
                this.stack.add(new Notecard(n));
            }
        }

        setTitle(other.getTitle());
        setAuthor(other.getAuthor());
        setVersion(other.getVersion());
        setLastModified(other.getLastModified());
        setLanguage(other.getLanguage());

        setSide1Title(other.getSide1Title());
        setSide2Title(other.getSide2Title());
        setSide3Title(other.getSide3Title());
    }

    public void addNotecard(Notecard notecard) {
        if(notecard instanceof Notecard3D) {
            stack.add(new Notecard3D((Notecard3D)notecard));
        }
        else {
            stack.add(new Notecard(notecard));
        }
    }

    public String getTitle() {
        return stackTitle.toString();
    }

    public String getAuthor() {
        return author.toString();
    }

    public String getVersion() {
        return version.toString();
    }

    public String getLastModified() {
        return lastModified.toString();
    }

    public String getLanguage() {
        return language.toString();
    }

    public String getSide1Title() {
        return side1Title.toString();
    }

    public String getSide2Title() {
        return side2Title.toString();
    }

    public String getSide3Title() {
        return side3Title.toString();
    }
	
    public void setTitle(String text) {
        stackTitle.delete(0,stackTitle.length());
        stackTitle.append(text);
    }

    public void setAuthor(String text) {
        author.delete(0,author.length());
        author.append(text);
    }

    public void setVersion(String text) {
        version.delete(0,version.length());
        version.append(text);
    }

    public void setLastModified(String text) {
        lastModified.delete(0,lastModified.length());
        lastModified.append(text);
    }

    public void setLanguage(String text) {
        language.delete(0,language.length());
        language.append(text);
    }

    public void setSide1Title(String text) {
        side1Title.delete(0,side1Title.length());
        side1Title.append(text);
    }

    public void setSide2Title(String text) {
        side2Title.delete(0,side2Title.length());
        side2Title.append(text);
    }

    public void setSide3Title(String text) {
        side3Title.delete(0,side3Title.length());
        side3Title.append(text);
    }
	
    public String toString() {
        return stackTitle.toString();
    }

    public ArrayList<Notecard> getNotecards() {
        return stack;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof NotecardStack) && this.toString().equals(obj.toString());
    }

    public void sortStackAlphabetically() {
        Collections.sort(stack,new NotecardComparator());
    }

    public void shuffleStack() {
        Collections.shuffle(stack);
    }
}

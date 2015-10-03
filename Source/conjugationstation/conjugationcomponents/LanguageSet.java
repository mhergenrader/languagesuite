package conjugationstation.conjugationcomponents;

import java.util.*;
import java.io.*;
import javax.swing.tree.*;

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
 * Stores a group of languages in alphabetical order by last name
 * @author Michael Hergenrader
 */
public class LanguageSet implements Serializable {

    private TreeSet<Language> languages;
    private int numberOfLanguages = 0;

    public LanguageSet() {
        languages = new TreeSet<Language>(new LanguageComparator());
        numberOfLanguages = 0;
    }

    public LanguageSet(List<Language> languagesToAdd) {
        this();
        addLanguages(languagesToAdd);
    }

    // used for saving
    public LanguageSet(DefaultTreeModel treeModel, DefaultMutableTreeNode rootNode) {
        this();
        setLanguages(treeModel,rootNode);
    }

    public void addLanguage(Language languageToAdd) { // used for loading
        languages.add(languageToAdd);
        numberOfLanguages++;
    }

    public void addLanguages(List<Language> languagesToAdd) {
        Iterator iter = languagesToAdd.iterator();
        while(iter.hasNext()) {
            Language l = (Language)iter.next();
            languages.add(l);
            numberOfLanguages++;
        }
    }

    public void setLanguages(List<Language> languagesToAdd) {
        languages.clear();
        addLanguages(languagesToAdd);
    }

    public void setLanguages(DefaultTreeModel treeModel, DefaultMutableTreeNode rootNode) {
        languages.clear();
        for(int i = 0; i < rootNode.getChildCount(); i++) {
            if(((DefaultMutableTreeNode)treeModel.getChild(rootNode,i)).getUserObject() instanceof Language) {
                Language l = (Language)((DefaultMutableTreeNode)treeModel.getChild(rootNode,i)).getUserObject();
                languages.add(l);
                numberOfLanguages++;
            }
        }
    }

    public int getNumberOfLanguages() {
        return numberOfLanguages;
    }

    public TreeSet<Language> getLanguages() {
        return languages;
    }

    
}

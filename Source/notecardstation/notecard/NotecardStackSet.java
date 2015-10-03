package notecardstation.notecard;

import java.io.*;
import java.util.*;
import javax.swing.tree.*;
import notecardstation.general.NotecardStation.NotecardStackTreeObject;

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
 * Represents a set of notecard stacks for a particular language
 * @author Michael Hergenrader
 */
public class NotecardStackSet implements Serializable {
    private int numberOfStacks; // used to determine quickly during loading how many stacks to get through readInt()
    private Set<NotecardStack> notecardStacks;    

    public NotecardStackSet() {
        notecardStacks = new TreeSet<NotecardStack>(new NotecardStackComparator());
        numberOfStacks = 0;
    }

    public NotecardStackSet(List<NotecardStack> stacksToAdd) {
        this();
        addStacks(stacksToAdd);
    }

    public NotecardStackSet(DefaultTreeModel stacksModel, DefaultMutableTreeNode root) {
        this();
        setStacks(stacksModel,root);
    }

    public void addStacks(List<NotecardStack> stacksToAdd) {
        for(NotecardStack ncs : stacksToAdd) {
            notecardStacks.add(ncs);
            numberOfStacks++;
        }
    }

    public void setStacks(DefaultTreeModel stacksModel, DefaultMutableTreeNode root) {
        notecardStacks.clear();
        for(int i = 0; i < root.getChildCount(); i++) {
            if(((DefaultMutableTreeNode)stacksModel.getChild(root,i)).getUserObject() instanceof NotecardStackTreeObject) {
                NotecardStackTreeObject nsto = (NotecardStackTreeObject)((DefaultMutableTreeNode)stacksModel.getChild(root,i)).getUserObject();
                notecardStacks.add(nsto.getNotecardStack());
                numberOfStacks++;
            }
        }
    }

    public void addStack(NotecardStack stackToAdd) {
        notecardStacks.add(stackToAdd);
        numberOfStacks++;
    }

    public int getNumberOfStacks() {
        return numberOfStacks;
    }

    public Set<NotecardStack> getStacks() {
        return notecardStacks;
    }
}

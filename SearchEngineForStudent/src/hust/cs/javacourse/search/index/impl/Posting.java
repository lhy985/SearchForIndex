package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;


public class Posting extends AbstractPosting {
    public Posting()//构造函数
    {

    }

    public Posting(int docId, int freq, List<Integer> positions) {
        this.docId = docId;
        this.freq = freq;
        this.positions = positions;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this==obj) return true;
        if(obj==null||getClass()!=obj.getClass()) return false;
        Posting posting = (Posting) obj;
        return  docId == posting.docId && freq == posting.freq && positions.equals(posting.positions);
    }


    @Override
    public String toString() {
        return
                "docId=" + docId +
                ", freq=" + freq +
                ", positions=" + positions;
    }

    public int getDocId() {
        return docId;
    }

    public void setDocId(int docId) {
        this.docId = docId;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }


    public int compareTo(AbstractPosting o) {
        Posting obj = (Posting) o;
        return this.docId - obj.docId;
    }


    public void sort() {
        Collections.sort(this.positions);
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
    try {
         out.writeObject(this.docId);
         out.writeObject(this.freq);
         out.writeObject(this.positions);

    }catch (IOException e)
    {
        e.printStackTrace();
    }
    }

    @Override
    public void readObject(ObjectInputStream in) {
    try {
        this.docId = (int)(in.readObject());
        this.freq = (int)(in.readObject());
        this.positions = (List<Integer>) (in.readObject());
    }catch (IOException e)
    {
        e.printStackTrace();
    }
    catch (ClassNotFoundException e)
    {
        e.printStackTrace();
    }
    }
}

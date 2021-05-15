package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;

public class PostingList extends AbstractPostingList {
    @Override
    public void add(AbstractPosting posting) {
        list.add(posting);
    }

    @Override
    public String toString()
    {
        return list.toString();
    }

    @Override
    public void add(List<AbstractPosting> postings) {
        for(AbstractPosting posting:postings)
        {
            if(!list.contains(posting))
            {
                list.add(posting);
            }
        }

    }

    @Override
    public AbstractPosting get(int index) {
        return list.get(index);
    }

    @Override
    public int indexOf(AbstractPosting posting) {
        return list.indexOf(posting);
    }

    @Override
    public int indexOf(int docId) {
        int i=0;
       for(i=0;i<list.size();i++)
       {
           if(list.get(i).getDocId()==docId)
           {
               return i;
           }
       }
       return -1;
    }

    @Override
    public boolean contains(AbstractPosting posting) {
        if(list.contains(posting))
            return true;
        else
            return false;
    }

    @Override
    public void remove(int index) {
        list.remove(index);
    }

    @Override
    public void remove(AbstractPosting posting) {
        list.remove(posting);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void sort() {
        Collections.sort(list);
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.list);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void readObject(ObjectInputStream in) {

        try {
            this.list = (List<AbstractPosting>) (in.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

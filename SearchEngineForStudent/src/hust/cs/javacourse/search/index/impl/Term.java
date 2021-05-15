package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTerm;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Term extends AbstractTerm {
    public Term() {

    }


    @Override
    public int hashCode() {

        return this.content.hashCode();
    }



    public Term(String content) {
        this.content = content;
    }

    @Override
    public  boolean equals(Object obj) {
      if(this == obj) return  true;
      if(obj == null && getClass()!= obj.getClass()) return  false;
      Term term =(Term) obj;
      return content.equals(term.content);
    }


    @Override
    public String toString()
    {
        return  this.content;
    }

    public String getContent(){
        return  this.content;
    }

    public void setContent(String content)
    {
        this.content=content;
    }

    public  int compareTo(AbstractTerm o){
        return this.content.compareTo(o.getContent());
    }

    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this.content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readObject(ObjectInputStream in) {

        try {
            this.content = (String) (in.readObject());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

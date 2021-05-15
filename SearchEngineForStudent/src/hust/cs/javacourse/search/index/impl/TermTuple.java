package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;

public class TermTuple extends AbstractTermTuple {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null && getClass() != obj.getClass()) return  false;
        TermTuple termTuple = (TermTuple) obj;
        return term.equals(termTuple.term) && curPos == termTuple.curPos;
    }

    @Override
    public String toString() {
        return "term=" + term +
                ", freq=" + freq +
                ", curPos=" + curPos;
    }
}

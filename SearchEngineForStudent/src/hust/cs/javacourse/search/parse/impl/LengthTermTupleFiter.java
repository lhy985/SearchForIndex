package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

public class LengthTermTupleFiter extends AbstractTermTupleFilter {
    public LengthTermTupleFiter(AbstractTermTupleStream input) {
        super(input);
    }

    /**
     * 获得下一个三元组
     * 过滤长度小于3或大于20的单词
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple lengthFilterTerm = input.next();
        if(lengthFilterTerm == null) return null;
        while (lengthFilterTerm.term.getContent().length()< Config.TERM_FILTER_MINLENGTH||
        lengthFilterTerm.term.getContent().length()> Config.TERM_FILTER_MAXLENGTH)
        {
            lengthFilterTerm = input.next();
            if (lengthFilterTerm == null) return null;
        }
        return  lengthFilterTerm;
    }
}

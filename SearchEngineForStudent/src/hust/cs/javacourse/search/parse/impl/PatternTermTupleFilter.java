package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleFilter;
import hust.cs.javacourse.search.parse.AbstractTermTupleStream;
import hust.cs.javacourse.search.util.Config;

/**
 * 三元组过滤器
 * 把字符串过滤成单词
 */
public class PatternTermTupleFilter extends AbstractTermTupleFilter {
    private  String pattern;    //过滤模式

    public PatternTermTupleFilter(AbstractTermTupleStream input)
    {
        super(input);
        pattern = Config.TERM_FILTER_PATTERN;
    }

    /**
     * 获得下一个三元组
     * 基于正则表达式，过滤非英文字符
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        AbstractTermTuple patterntuple = input.next();
        if (patterntuple == null) return  null;
        while (!patterntuple.term.getContent().matches(pattern))
        {
            patterntuple = input.next();
            if (patterntuple == null) return  null;
        }
        return patterntuple;

    }
}

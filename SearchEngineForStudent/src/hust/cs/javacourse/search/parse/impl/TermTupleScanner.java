package hust.cs.javacourse.search.parse.impl;

import hust.cs.javacourse.search.index.AbstractTermTuple;
import hust.cs.javacourse.search.index.impl.Term;
import hust.cs.javacourse.search.index.impl.TermTuple;
import hust.cs.javacourse.search.parse.AbstractTermTupleScanner;
import hust.cs.javacourse.search.util.Config;
import hust.cs.javacourse.search.util.StringSplitter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TermTupleScanner extends AbstractTermTupleScanner {
    private List<String> termList = new ArrayList<>();
    private int curTermPos = 0; // 计算当前term位置

    public TermTupleScanner() {
        super();
    }

    public TermTupleScanner(BufferedReader input) {
        super(input);
        String strLine = null;//用来存储文件的每一行字符串
        StringSplitter splitter = new StringSplitter();
        splitter.setSplitRegex(Config.STRING_SPLITTER_REGEX);
        //循环读取每一行，进行分词
        while (true) {
            try {
                strLine = this.input.readLine();
                if (strLine != null) {
                    if (!strLine.isEmpty()) {
                        termList.addAll(splitter.splitByRegex(strLine));
                    }

                }
                else
                    break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获得下一个三元组
     *
     * @return: 下一个三元组；如果到了流的末尾，返回null
     */
    @Override
    public AbstractTermTuple next() {
        TermTuple termTuple = null;
        if (!termList.isEmpty()) {
            termTuple = new TermTuple();
            termTuple.term = new Term(termList.get(0).toLowerCase());
            termTuple.curPos = ++curTermPos;
            termList.remove(0);
        }
        return termTuple;

    }
}

package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.Sort;
import java.util.Collections;
import java.util.List;

public class SimpleSorter implements Sort {
    @Override
    public void sort(List<AbstractHit> hits) {
        Collections.sort(hits,(AbstractHit h1,AbstractHit h2)->(-(int)Math.round(score(h1)-score(h2))));
    }


/**
    *功能描述：
    *@param:AbstractHit hit
    *@return:分数
    *@date:
 **/
    @Override
    public double score(AbstractHit hit) {
        double score = 0;
        for (AbstractPosting posting:hit.getTermPostingMapping().values())
        {
            score = score +posting.getFreq();
        }
        return score;
    }
}

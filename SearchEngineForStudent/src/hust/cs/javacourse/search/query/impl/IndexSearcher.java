package hust.cs.javacourse.search.query.impl;

import hust.cs.javacourse.search.index.AbstractPosting;
import hust.cs.javacourse.search.index.AbstractPostingList;
import hust.cs.javacourse.search.index.AbstractTerm;
import hust.cs.javacourse.search.query.AbstractHit;
import hust.cs.javacourse.search.query.AbstractIndexSearcher;
import hust.cs.javacourse.search.query.Sort;
import java.io.File;
import java.util.*;

public class IndexSearcher extends AbstractIndexSearcher {
    @Override
    public void open(String indexFile) {
        File file = new File(indexFile);
        this.index.load(file);  //把文件加载到倒排索引结构中去
        this.index.optimize(); // 对索引内部进行排序
    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm, Sort sorter) {
        AbstractPostingList postingList = this.index.search(queryTerm);
        if (postingList == null) return null;
        List<AbstractHit> hits = new ArrayList<>();
        int n;
        for (n=0;n< postingList.size();n++)//对所以命中的文档创建hit对象，加入到hits中去
        {
            AbstractPosting posting = postingList.get(n);
            String path = this.index.getDocName(posting.getDocId());
            Map<AbstractTerm, AbstractPosting> map = new HashMap<>();
            map.put(queryTerm, posting);
            AbstractHit hit = new Hit(posting.getDocId(), path, map);
            hit.setScore(sorter.score(hit));
            hits.add(hit);
        }
        sorter.sort(hits);//对命中的文档排序
        return (AbstractHit[]) hits.toArray(new AbstractHit[0]);
        //将集合强制转换成数组类型

    }

    @Override
    public AbstractHit[] search(AbstractTerm queryTerm1, AbstractTerm queryTerm2, Sort sorter, LogicalCombination combine) {
        AbstractPostingList postingList_1 = this.index.search(queryTerm1);
        AbstractPostingList postingList_2 = this.index.search(queryTerm2);

        AbstractHit[] hits1 = this.search(queryTerm1, sorter);
        AbstractHit[] hits2 = this.search(queryTerm2, sorter);
        List<AbstractHit> hits_3 = new ArrayList<>();
        int size1 = 0;
        int size2 = 0;
        if (hits1 != null) size1 = hits1.length;
        if (hits2 != null) size2 = hits2.length;
        if (size1 == 0 && size2 == 0) return null;
        if (size1 == 0 && size2 != 0) {
            switch (combine) {
                case AND:
                    return null;
                case OR:
                    return hits2;
            }
        }
        if (size2 == 0 && size1 != 0) {
            switch (combine) {
                case AND:
                    return null;
                case OR:
                    return hits1;
            }
        }
        List<AbstractHit> hits_1 = (Arrays.asList(hits1));
        List<AbstractHit> hits_2 = (Arrays.asList(hits2));
        int i, j, k;
        switch (combine) {
            case OR:
                //对两个List进行公共的
                for (i = 0; i < hits_2.size(); i++) {
                    for (j = 0; j < hits_1.size(); j++) {
                        Map<AbstractTerm, AbstractPosting> new_map = new HashMap<>();
                        new_map.putAll(hits_1.get(j).getTermPostingMapping());
                        new_map.putAll(hits_2.get(i).getTermPostingMapping());
                        AbstractHit new_hit = new Hit(hits_2.get(i).getDocId(),hits_2.get(i).getDocPath(),new_map);
                        new_hit.setScore(hits_1.get(j).getScore() + hits_2.get(i).getScore());
                        hits_3.add(new_hit);
                    }
                }
                //对两个List各自的
                List<Integer> docId = new ArrayList<>();
                for (i = 0; i < hits_3.size(); i++) {
                    docId.add(hits_3.get(i).getDocId());
                }
                for (i = 0; i < hits_2.size(); i++) {
                    if (!docId.contains(hits_2.get(i).getDocId())) {
                        hits_3.add(hits_2.get(i));
                    }
                }
                for (i = 0; i < hits_1.size(); i++) {
                    if (!docId.contains(hits_1.get(i).getDocId())) {
                        hits_3.add(hits_1.get(i));
                    }
                }
                sorter.sort(hits_3);
                break;
            case AND:
                for (i = 0; i < hits_2.size(); i++) {
                    for (j = 0; j < hits_1.size(); j++) {
                        if (hits_2.get(i).getDocId() == hits_1.get(j).getDocId()) {
                            Map<AbstractTerm, AbstractPosting> new_map = new HashMap<>();
                            new_map.putAll(hits_1.get(j).getTermPostingMapping());
                            new_map.putAll(hits_2.get(i).getTermPostingMapping());
                            AbstractHit new_hit = new Hit(hits_2.get(i).getDocId(), hits_2.get(i).getDocPath(),new_map);
                            new_hit.setScore(hits_1.get(j).getScore() + hits_2.get(i).getScore());
                            hits_3.add(new_hit);
                        }
                    }
                }
                sorter.sort((hits_3));
                break;
        }
        return (AbstractHit[]) hits_3.toArray(new AbstractHit[0]);
    }
}

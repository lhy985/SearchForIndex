package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.*;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Index extends AbstractIndex {

    public Index() {
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("dictionary: " + this.getDictionary().toString());
        /**/
        buf.append("\n\ndocPath mapping:\n");
        Iterator<Map.Entry<Integer,String>> it1 = this.docIdToDocPathMapping.entrySet().iterator();
        while (it1.hasNext()){
            Map.Entry<Integer,String> entry = it1.next();
            buf.append(entry.getKey() + " => " + entry.getValue()).append("\n");
        }
        /**/
        buf.append("\nPostingList:\n");
        Iterator<Map.Entry<AbstractTerm,AbstractPostingList>> iter = this.termToPostingListMapping.entrySet().iterator();
        while( iter.hasNext() ) {
            Map.Entry<AbstractTerm,AbstractPostingList> entry = iter.next();
            buf.append(entry.getKey() + ": " + entry.getValue()).append("\n");
        }
        return buf.toString();
    }

    @Override
    public void addDocument(AbstractDocument document) {
        //docIdToDocPathMapping: <文档编号docId, 文档路径docPath>
        if (!this.docIdToDocPathMapping.containsKey(document.getDocId())) {
            this.docIdToDocPathMapping.put(document.getDocId(), document.getDocPath());
        }
        /**/
        for (int k = 0; k < document.getTupleSize(); k++) {
            //termToPostingListMapping: <term(单词、出现次数、出现位置), Posting列表(文档编号、出现次数、出现位置的列表)>
            AbstractTermTuple tt = document.getTuple(k);
            if (this.termToPostingListMapping.containsKey(tt.term)) {
                //map中包含当前term(单词)
                AbstractPostingList poslist = this.termToPostingListMapping.get(tt.term);
                int index = poslist.indexOf(document.getDocId());
                if (index >= 0) {
                    //map中包含当前term(单词),且是当前文档
                    AbstractPosting o = poslist.get(index);
                    o.setFreq(o.getFreq() + 1);
                    o.getPositions().add(tt.curPos);
                } else {
                    //虽然map中包含当前term(单词),但该单词属于别的文档
                    AbstractPosting p = new Posting();
                    p.setDocId(document.getDocId());
                    p.getPositions().add(tt.curPos);
                    p.setFreq(tt.freq);
                    poslist.add(p);
                }
            } else {
                //map中没有当前term(单词)
                AbstractPosting p = new Posting();
                p.setDocId(document.getDocId());
                p.setFreq(tt.freq);
                p.getPositions().add(tt.curPos);
                PostingList pl = new PostingList();
                pl.add(p);
                this.termToPostingListMapping.put(tt.term, pl);
            }
        }

    }

    /**
     * <pre>
     * 从索引文件里加载已经构建好的索引.内部调用FileSerializable接口方法readObject即可
     * @param file ：索引文件
     * </pre>
     */
    @Override
    public void load(File file) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            this.readObject(ois);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <pre>
     * 将在内存里构建好的索引写入到文件. 内部调用FileSerializable接口方法writeObject即可
     * @param file ：写入的目标索引文件
     * </pre>
     */
    @Override
    public void save(File file) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            this.writeObject(oos);
        } catch (IOException e) {
            e.printStackTrace();
        };
    }

    @Override
    public AbstractPostingList search(AbstractTerm term) {

        Set<AbstractTerm> termSet = termToPostingListMapping.keySet();
        for(AbstractTerm i : termSet){
            if(i.equals(term))
                return this.termToPostingListMapping.get(i);
        }
        return null;
    }

    /**
     * 返回索引的字典.字典为索引里所有单词的并集
     *
     * @return ：索引中Term列表
     */
    @Override
    public Set<AbstractTerm> getDictionary() {
        Set<AbstractTerm> termSet = termToPostingListMapping.keySet();
        return termSet;
    }

    /**
     * <pre>
     * 对索引进行优化，包括：
     *      对索引里每个单词的PostingList按docId从小到大排序
     *      同时对每个Posting里的positions从小到大排序
     * 在内存中把索引构建完后执行该方法
     * </pre>
     */
    @Override
    public void optimize() {
        Set<AbstractTerm> termSet = this.getDictionary();
        for(AbstractTerm i : termSet){
            AbstractPostingList postingList = this.search(i);
            int n;
            for(n=0;n<postingList.size();n++){
                AbstractPosting p =postingList.get(n);
                p.sort();
            }//对posting的position排序
            postingList.sort();//postinglist按docid排序
        }

    }

    /**
     * 根据docId获得对应文档的完全路径名
     *
     * @param docId ：文档id
     * @return : 对应文档的完全路径名
     */
    @Override
    public String getDocName(int docId) {
        Set<Integer> docIdset = docIdToDocPathMapping.keySet();
        for(Integer i:docIdset){
            if(i == docId)
                return docIdToDocPathMapping.get(i);
        }
        return null;
    }

    /**
     * 写到二进制文件
     *
     * @param out :输出流对象
     */
    @Override
    public void writeObject(ObjectOutputStream out) {
        try {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从二进制文件读
     *
     * @param in ：输入流对象
     */
    @Override
    public void readObject(ObjectInputStream in) {
        try {
            Index index =(Index) (in.readObject());
            this.docIdToDocPathMapping = index.docIdToDocPathMapping;
            this.termToPostingListMapping = index.termToPostingListMapping;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

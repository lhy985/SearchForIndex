package hust.cs.javacourse.search.index.impl;

import hust.cs.javacourse.search.index.AbstractDocument;
import hust.cs.javacourse.search.index.AbstractDocumentBuilder;
import hust.cs.javacourse.search.index.AbstractIndex;
import hust.cs.javacourse.search.index.AbstractIndexBuilder;
import hust.cs.javacourse.search.util.Config;
import java.io.File;
import java.io.IOException;

public class IndexBuilder extends AbstractIndexBuilder {
    public IndexBuilder(AbstractDocumentBuilder docBuilder) {
        super(docBuilder);
    }

    /**
     *
     * @param rootDirectory ：指定构建索引的目录
     * @return 构建好的索引对象
     */
    @Override
    public AbstractIndex buildIndex(String rootDirectory) {
        if(rootDirectory==null)return null;

        Index index = new Index();
        File rootDic = new File(rootDirectory);
        if(rootDic==null)return null;

        File[] files = rootDic.listFiles();
        if(files.length==0) return null;
        for(File f:files){
            AbstractDocument doc =  this.docBuilder.build(++docId,f.getPath(),f);
            index.addDocument(doc);
        }
        File saveFile = new File(Config.INDEX_DIR + "index.dat");
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        index.save(saveFile);
        return index;
    }
}

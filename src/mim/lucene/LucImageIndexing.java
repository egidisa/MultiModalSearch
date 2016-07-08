package mim.lucene;

import mim.DNNExtractor;
import mim.ImgDescriptor;
import mim.Parameters;
import mim.tools.FeaturesStorage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.FSDirectory;

public class LucImageIndexing {
	
	private Pivots pivots;
	
	private List<ImgDescriptor> idsDataset;
	private int topKIdx;
	
	private IndexWriter indexWriter;
		
	public static void main(String[] args) throws ClassNotFoundException, IOException, ParseException {
		LucImageIndexing luceneImgIdx = new LucImageIndexing(Parameters.PIVOTS_FILE, Parameters.STORAGE_FILE, Parameters.TOP_K_IDX);
		luceneImgIdx.openIndex(Parameters.LUCENE_INDEX_DIRECTORY);
		luceneImgIdx.index();
		luceneImgIdx.closeIndex();
	}
	
	//TODO
	public LucImageIndexing(File pivotsFile, File datasetFile, int topKIdx) throws IOException, ClassNotFoundException {
		//load the dataset and the pivots, initialize topkidx
		this.idsDataset = FeaturesStorage.load(datasetFile);
		this.pivots = new Pivots(pivotsFile);
		this.topKIdx = topKIdx;
	}
	
	public void openIndex(String lucenePath) throws IOException {
		//from ex1
		Path absolutePath = Paths.get(lucenePath, "");
		FSDirectory index = FSDirectory.open(absolutePath);
		Analyzer analyzer = new WhitespaceAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		conf.setOpenMode(OpenMode.CREATE);
		indexWriter = new IndexWriter(index,conf);
	}
	
	//TODO
	public void closeIndex() throws IOException {
		//close Lucene writer
		indexWriter.close();
	}
	
	public void index() throws ClassNotFoundException, IOException {
		//index all dataset features into Lucene
		Document doc = null;
		for (int i=0;i<idsDataset.size();i++) {
		    String imgTXT = pivots.features2Text(idsDataset.get(i),topKIdx);
		    doc = createDoc(idsDataset.get(i),imgTXT);
		    System.out.println("Indexing file "+idsDataset.get(i).getId());
		    indexWriter.addDocument(doc);
		}
		indexWriter.commit();
		
		//commit Lucene
	}
	
	private Document createDoc(ImgDescriptor imgDes, String imgTXT) throws IOException{
		Document doc = new Document();
		
		// img field
	    FieldType ft = new FieldType(TextField.TYPE_NOT_STORED);
	    ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
	    ft.setStoreTermVectors(true);
	    ft.setStoreTermVectorPositions(true);
	    ft.storeTermVectorOffsets();
	    Field f = new Field(Fields.IMG, imgTXT, ft);    
	    doc.add(f);		
	    
		//ID field
		ft = new FieldType(StringField.TYPE_STORED);
		ft.setIndexOptions(IndexOptions.DOCS);
		f = new Field(Fields.ID,imgDes.getId(),ft);
		doc.add(f);

	    return doc;
	}
}

package multiModal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;

import mim.ImgDescriptor;
import mim.Parameters;
import mim.lucene.DCNNFeaturesQuantization;
import mim.lucene.Fields;
import mim.tools.FeaturesStorage;

//Class to create index on deep features and tags and store it in C:/lucene
public class IndexBuilder {

	private List<ImgDescriptor> idsDataset;
	private IndexWriter indexWriter;
	// private File[] tagFiles;

	public IndexBuilder(File idsFile, File tagFolder) throws ClassNotFoundException, IOException {
		this.idsDataset = FeaturesStorage.load(idsFile);
		// tagFiles = tagFolder.listFiles();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// IndexBuilder index = new IndexBuilder(Parameters.IDS_FILE_7,
		// Parameters.TAG_FOLDER);
		IndexBuilder index = new IndexBuilder(new File("data/deepFeatures/deep7.dat"), Parameters.TAG_FOLDER);
		System.out.println("dataset loaded");
		
		index.openIndex(Parameters.LUCENE_INDEX_DIRECTORY);
		index.createIndex();
		index.closeIndex();
	}

	public void createIndex() throws IOException {
		// index all dataset features and tags into Lucene
		Document doc = null;
		BufferedReader br = new BufferedReader(new FileReader(new File("data/classifiedLabels.txt")));
		for (int i = 0; i < idsDataset.size(); i++) {
			String line = br.readLine();
			String[] tokens = line.split(";");
			System.out.println("Label"+tokens[2]);
			
			String pathTagFile = Parameters.TAG_FOLDER + "/" + idsDataset.get(i).getId().replaceAll("im", "tags").replaceAll(".jpg", ".txt");
			String classLabel = tokens[2];
			System.out.println(pathTagFile);
			String imgTXT = DCNNFeaturesQuantization.quantize(idsDataset.get(i));
			
			String content = readFile(pathTagFile,Charset.defaultCharset());
			String tags = content.replace("\n", " ").replace("\r", " ");
			System.out.println(tags);
			
			doc = createDoc(idsDataset.get(i).getId(), imgTXT, tags, classLabel);
			indexWriter.addDocument(doc);
			System.out.println(idsDataset.get(i).getId() + " indexed");
		}
	// commit Lucene
	indexWriter.commit();
	}

	public void openIndex(String lucenePath) throws IOException {
		// from ex1
		Path absolutePath = Paths.get(lucenePath, "");
		FSDirectory index = FSDirectory.open(absolutePath);
		Analyzer analyzer = new WhitespaceAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		conf.setOpenMode(OpenMode.CREATE);
		indexWriter = new IndexWriter(index, conf);
	}

	public void closeIndex() throws IOException {
		// close Lucene writer
		indexWriter.close();
	}
	
	private Document createDoc(String fileName, String imgTXT, String tags, String classLabel) throws IOException {
		Document doc = new Document();

		// ID field
		FieldType ft = new FieldType(StringField.TYPE_STORED);
		ft.setIndexOptions(IndexOptions.DOCS);
		Field f = new Field(Fields.ID, fileName, ft);
		doc.add(f);

		// imgTXT field
		ft = new FieldType(TextField.TYPE_STORED);
		ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
		ft.setStoreTermVectors(true);
		ft.setStoreTermVectorPositions(true);
		//ft.storeTermVectorOffsets();
		f = new Field(Fields.IMG, imgTXT, ft);
		doc.add(f);

		// tags field
		ft = new FieldType(TextField.TYPE_STORED);
		ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS); 
	    ft.setStoreTermVectors(true);
	    ft.setStoreTermVectorPositions(true);
	    //ft.storeTermVectorOffsets();
		f = new Field(Fields.TAGS, tags, ft);
		doc.add(f);
		
		// tags field
		ft = new FieldType(TextField.TYPE_STORED);
		ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS); 
	    ft.setStoreTermVectors(true);
	    ft.setStoreTermVectorPositions(true);
	    //ft.storeTermVectorOffsets();
		f = new Field(Fields.CLASSLABEL, classLabel, ft);
		doc.add(f);

		return doc;
	}
	
	static String readFile(String path, Charset encoding) throws IOException {
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
}

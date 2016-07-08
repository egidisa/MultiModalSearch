package mim.lucene;

import mim.DNNExtractor;
import mim.ImgDescriptor;
import mim.Parameters;
import mim.tools.FeaturesStorage;
import mim.tools.Output;
import mim.lucene.Fields;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class LucImageSearch {

	private IndexSearcher indexSearcher;
	
	private Pivots pivots;
	
	private int topKSearch;
	
	public static void main(String[] args) throws Exception {
		
		LucImageSearch imgSearch = new LucImageSearch(Parameters.PIVOTS_FILE, Parameters.TOP_K_QUERY);
		
		imgSearch.openIndex(Parameters.LUCENE_PATH);
		
		//Image Query File
		File imgQuery = new File(Parameters.SRC_FOLDER, "b4ce576107cad5dbd0ba40bfaeadae71.jpg");
		
		DNNExtractor extractor = new DNNExtractor();
		
		float[] imgFeatures = extractor.extract(imgQuery, Parameters.DEEP_LAYER);
		
		ImgDescriptor query = new ImgDescriptor(imgFeatures, imgQuery.getName());
				
		List<ImgDescriptor> resLucene = imgSearch.search(query, Parameters.K);
		Output.toHTML(resLucene, Parameters.BASE_URI, Parameters.RESULTS_HTML_LUCENE);

		List<ImgDescriptor> resReordered = imgSearch.reorder(query, resLucene);
		Output.toHTML(resReordered, Parameters.BASE_URI, Parameters.RESULTS_HTML_REORDERED);
	}
	
	//TODO
	public LucImageSearch(File pivotsFile, int topKSearch) throws ClassNotFoundException, IOException {
		//Initialize fields
		this.pivots = new Pivots(pivotsFile);
		this.topKSearch = topKSearch;
	}
	
	//TODO
	public void openIndex(String lucenePath) throws IOException {	
		//Initialize Lucene stuff
		//From Lab1
		Path absolutePath = Paths.get(lucenePath, "");
		FSDirectory index = FSDirectory.open(absolutePath);
		DirectoryReader ir = DirectoryReader.open(index);
		indexSearcher = new IndexSearcher(ir);
	}
	
	//TODO
	public List<ImgDescriptor> search(ImgDescriptor queryF, int k)
			throws ParseException, IOException, ClassNotFoundException {
		List<ImgDescriptor> res = new ArrayList<ImgDescriptor>(k);
		String[] r = null;

		// convert queryF to text and perform Lucene search
		// LOOP
		// for each result reconstruct the ImgDescriptor, set the dist and add
		// it to res
		// Create the Query object
		QueryParser qParser = new QueryParser(null, new WhitespaceAnalyzer());
		Query q = qParser.parse(pivots.features2Text(queryF, Parameters.TOP_K_QUERY));

		// Perform the search
		TopDocs hits = indexSearcher.search(q, k);
		r = new String[hits.scoreDocs.length];

		// Scroll all retrieved docs
		// LOOP TopDocs:
		// get the Fields.ID for each result and copy it in r[]

		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			Explanation x = indexSearcher.explain(q, hits.scoreDocs[i].doc);
			System.out.println(x.toString());
			//res.add(new ImgDescriptor(indexSearcher.doc(doc).get(Fields.BINARY)));
		}

		return res;
	}
	
	//TODO
	public List<ImgDescriptor> reorder(ImgDescriptor queryF, List<ImgDescriptor> res) throws IOException, ClassNotFoundException {
		//Optional Step!!!
		//LOOP
		//for each result evaluate the distance then sort the results
		return res;
	}
}

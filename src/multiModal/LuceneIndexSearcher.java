package multiModal;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import mim.Parameters;
import mim.lucene.Fields;

public class LuceneIndexSearcher {
	private IndexSearcher indexSearcher;

	public static void main(String[] args) throws IOException, ParseException {
		LuceneIndexSearcher indexSrc = new LuceneIndexSearcher();
		indexSrc.openIndex(Parameters.LUCENE_INDEX_DIRECTORY);
		indexSrc.searchTextual("explore");

	}
	
	public void searchTextual(String searchString) throws IOException, ParseException {
		
		System.out.println("Searching for '" + searchString + "'");

		QueryParser queryParser = new QueryParser(Fields.TAGS, new WhitespaceAnalyzer());
		Query query = queryParser.parse(searchString);
		TopDocs hits = indexSearcher.search(query,10);
		System.out.println("Number of hits: " + hits.totalHits);
		String[] r = new String[hits.scoreDocs.length];

		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			Explanation x = indexSearcher.explain(query, hits.scoreDocs[i].doc);
			System.out.println(x.toString());
			//res.add(new ImgDescriptor(indexSearcher.doc(doc).get(Fields.BINARY)));
		}
	}
	
	public void openIndex(String lucenePath) throws IOException {	
		//Initialize Lucene stuff
		Path absolutePath = Paths.get(lucenePath, "");
		FSDirectory index = FSDirectory.open(absolutePath);
		DirectoryReader ir = DirectoryReader.open(index);
		indexSearcher = new IndexSearcher(ir);
	}

}

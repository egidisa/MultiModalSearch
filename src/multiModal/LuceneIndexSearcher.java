package multiModal;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import mim.ImgDescriptor;
import mim.Parameters;
import mim.lucene.DCNNFeaturesQuantization;
import mim.lucene.Fields;
import mim.tools.Output;

/***
 * Class to perform a search on the index. The query is received directly from a
 * servlet, and it consists of 3 fields. The search may either be done by having
 * a textual query, a visual one (an image) or even both
 * 
 * @author Sara
 *
 */
public class LuceneIndexSearcher {

	private IndexSearcher indexSearcher;

	/***
	 * 
	 * @param text
	 *            input text for the query
	 * @param uploadedImg
	 *            image not stored, visual features will be extracted
	 * @param selectedImg
	 *            image already stored, features will be retrieved from the
	 *            index
	 * @return a list of IndexEntry containing result information including the
	 *         score
	 * @throws Exception
	 */
	public List<IndexEntry> search(String text, String uploadedImg, String selectedImg, int deepLayer)
			throws Exception {

		List<IndexEntry> res = new ArrayList<IndexEntry>();
		this.openIndex(Parameters.LUCENE_INDEX_DIRECTORY);

		if (text != null) {
			if (uploadedImg != null) {
				// multimodal search text + uploadedImg
				System.out.println("multimodal text+upimg");
				File img = new File(uploadedImg);
				Descriptors descResult = FeaturesExtractor.extractFeaturesSingle(img);
				ImgDescriptor deep7 = new ImgDescriptor(descResult.featDesc7, img.getName());
				String imgTXT7 = DCNNFeaturesQuantization.quantize(deep7);
				ImgDescriptor deep6 = new ImgDescriptor(descResult.featDesc6, img.getName());
				String imgTXT6 = DCNNFeaturesQuantization.quantize(deep6);
				if (deepLayer == Parameters.DEEP_LAYER7)
					res = this.searchMulti(imgTXT7, text, deepLayer);
				else if (deepLayer == Parameters.DEEP_LAYER6)
					res = this.searchMulti(imgTXT6, text, deepLayer);
			}
			if (selectedImg != null) {
				// multimodal search text + selectedImg
				System.out.println("multimodal text+selectedimg");
				// retrieve img info
				IndexEntry entry = this.retrieveIndexEntryDetails(selectedImg);
				if (deepLayer == Parameters.DEEP_LAYER7)
					res = this.searchVisual(entry.getImgDesc(), deepLayer);
				else if (deepLayer == Parameters.DEEP_LAYER6)
					res = this.searchVisual(entry.getImgDesc6(), deepLayer);
			}
			if (uploadedImg == null && selectedImg == null) {
				// textual search
				System.out.println("textual");
				res = this.searchTextual(text);
			}
		} else {
			if (uploadedImg != null) {
				// visual search with uploadedImg
				System.out.println("visual search with uploadedImg");
				File img = new File(uploadedImg);
				Descriptors descResult = FeaturesExtractor.extractFeaturesSingle(img);
				ImgDescriptor deep7 = new ImgDescriptor(descResult.featDesc7, img.getName());
				String imgTXT7 = DCNNFeaturesQuantization.quantize(deep7);
				ImgDescriptor deep6 = new ImgDescriptor(descResult.featDesc6, img.getName());
				String imgTXT6 = DCNNFeaturesQuantization.quantize(deep6);
				if (deepLayer == Parameters.DEEP_LAYER7)
					res = this.searchVisual(imgTXT7, deepLayer);
				else if (deepLayer == Parameters.DEEP_LAYER6)
					res = this.searchVisual(imgTXT6, deepLayer);
			}
			if (selectedImg != null) {
				// visual search with selectedImg
				System.out.println("visual search with selectedImg");
				IndexEntry entry = this.retrieveIndexEntryDetails(selectedImg);
				if (deepLayer == Parameters.DEEP_LAYER7)
					res = this.searchVisual(entry.getImgDesc(), deepLayer);
				else if (deepLayer == Parameters.DEEP_LAYER6) {
					System.out.println("FEATURES6" + entry.getImgDesc6());
					res = this.searchVisual(entry.getImgDesc6(), deepLayer);
				}

			}
		}
		if (deepLayer == Parameters.DEEP_LAYER7)
			Output.searchResultsToHTML(res, Parameters.BASE_URI, Parameters.RESULTS_HTML7);
		else if (deepLayer == Parameters.DEEP_LAYER6)
			Output.searchResultsToHTML(res, Parameters.BASE_URI, Parameters.RESULTS_HTML6);

		return res;
	}

	// just for testing purposes
	public static void main(String[] args) throws Exception {
		System.out.println("CHARSET" + Charset.defaultCharset());
		LuceneIndexSearcher indexSrc = new LuceneIndexSearcher();
		// indexSrc.openIndex(Parameters.LUCENE_INDEX_DIRECTORY);

		// indexSrc.search(null,
		// "C:/Users/Sara/workspaceEE/multiModal/WebContent/work/uploadedImages/im159.jpg",
		// null);
		indexSrc.search("cat", Parameters.SERVER_WORKSPACE + "/im159.jpg", null, 7);

		// List<IndexEntry> res = indexSrc.search(null,null,"im5185.jpg");
		// System.out.println(res.get(0).getId());

		// IndexEntry entry = indexSrc.retrieveIndexEntryDetails("im1000.jpg");
		// indexSrc.searchTextual("sea");
		// indexSrc.searchVisual("A2 A2 A3 A6 A14 A16 A25 A28 A38 A48 A55 A56
		// A59 A70 A75 A81 A81 A93 A98 A101 A101 A105 A105 A120 A121 A129 A131
		// A138 A138 A147 A170 A176 A181 A181 A185 A187 A189 A192 A201 A207 A212
		// A223 A229 A234 A243 A248 A252 A253 A254 A257 A263 A264 A268 A268 A269
		// A269 A277 A280 A282 A282 A290 A301 A301 A315 A319 A322 A322 A325 A329
		// A337 A337 A338 A344 A345 A346 A349 A352 A361 A365 A365 A368 A368 A369
		// A382 A386 A398 A407 A407 A414 A420 A425 A428 A433 A441 A441 A442 A447
		// A462 A466 A477 A485 A490 A494 A494 A505 A513 A517 A523 A526 A543 A555
		// A562 A575 A575 A580 A583 A583 A590 A605 A607 A609 A618 A623 A625 A633
		// A641 A642 A650 A653 A656 A667 A678 A680 A696 A705 A706 A706 A713 A719
		// A726 A727 A749 A753 A753 A764 A767 A775 A779 A787 A790 A803 A812 A814
		// A815 A816 A816 A818 A820 A822 A828 A829 A834 A847 A854 A857 A857 A858
		// A859 A874 A877 A878 A883 A883 A883 A898 A899 A910 A920 A927 A929 A932
		// A936 A937 A944 A945 A945 A953 A960 A961 A967 A977 A1004 A1012 A1018
		// A1027 A1050 A1052 A1062 A1069 A1070 A1075 A1077 A1080 A1092 A1097
		// A1105 A1107 A1109 A1121 A1126 A1127 A1134 A1138 A1138 A1139 A1139
		// A1141 A1141 A1145 A1150 A1153 A1157 A1162 A1165 A1171 A1186 A1193
		// A1193 A1194 A1194 A1198 A1206 A1213 A1219 A1219 A1224 A1227 A1240
		// A1243 A1244 A1247 A1251 A1252 A1272 A1280 A1288 A1293 A1294 A1297
		// A1298 A1308 A1309 A1317 A1322 A1333 A1335 A1335 A1340 A1342 A1343
		// A1348 A1351 A1359 A1363 A1378 A1378 A1379 A1385 A1387 A1387 A1388
		// A1395 A1395 A1396 A1401 A1417 A1418 A1430 A1431 A1431 A1431 A1436
		// A1439 A1443 A1444 A1449 A1455 A1459 A1463 A1467 A1480 A1494 A1494
		// A1498 A1501 A1507 A1518 A1534 A1534 A1541 A1541 A1545 A1561 A1563
		// A1568 A1585 A1590 A1593 A1594 A1602 A1602 A1607 A1607 A1608 A1608
		// A1615 A1618 A1635 A1635 A1644 A1650 A1666 A1670 A1671 A1683 A1684
		// A1695 A1704 A1711 A1718 A1728 A1736 A1736 A1737 A1740 A1744 A1749
		// A1757 A1757 A1759 A1765 A1769 A1781 A1791 A1792 A1807 A1817 A1818
		// A1830 A1836 A1841 A1843 A1851 A1854 A1856 A1857 A1859 A1903 A1904
		// A1914 A1914 A1923 A1927 A1927 A1932 A1938 A1940 A1958 A1958 A1964
		// A1966 A1975 A1979 A1981 A1989 A1992 A2000 A2002 A2018 A2018 A2020
		// A2024 A2044 A2049 A2051 A2053 A2054 A2069 A2076 A2080 A2082 A2084
		// A2084 A2086 A2092 A2094 A2110 A2120 A2121 A2132 A2137 A2138 A2145
		// A2147 A2155 A2155 A2155 A2155 A2175 A2197 A2202 A2215 A2216 A2219
		// A2224 A2224 A2231 A2248 A2251 A2254 A2255 A2257 A2257 A2260 A2262
		// A2263 A2281 A2283 A2286 A2289 A2290 A2313 A2315 A2333 A2351 A2351
		// A2358 A2363 A2365 A2367 A2388 A2392 A2405 A2405 A2408 A2410 A2412
		// A2414 A2415 A2419 A2422 A2428 A2435 A2437 A2440 A2441 A2443 A2450
		// A2461 A2466 A2468 A2471 A2471 A2473 A2473 A2479 A2487 A2487 A2497
		// A2497 A2497 A2498 A2498 A2508 A2508 A2510 A2514 A2534 A2536 A2538
		// A2551 A2557 A2587 A2607 A2607 A2616 A2623 A2623 A2624 A2644 A2644
		// A2651 A2651 A2653 A2660 A2661 A2665 A2666 A2666 A2668 A2675 A2679
		// A2682 A2683 A2698 A2698 A2700 A2704 A2707 A2709 A2711 A2715 A2720
		// A2724 A2724 A2726 A2750 A2755 A2768 A2769 A2775 A2781 A2781 A2790
		// A2790 A2791 A2791 A2793 A2801 A2809 A2813 A2826 A2835 A2835 A2844
		// A2866 A2868 A2869 A2876 A2878 A2889 A2890 A2898 A2898 A2902 A2906
		// A2911 A2923 A2925 A2925 A2934 A2938 A2952 A2960 A2967 A2967 A2969
		// A2976 A2983 A2987 A2988 A2991 A2999 A3004 A3009 A3028 A3034 A3036
		// A3037 A3037 A3038 A3039 A3039 A3043 A3049 A3057 A3061 A3067 A3079
		// A3087 A3087 A3098 A3103 A3103 A3110 A3114 A3125 A3149 A3155 A3155
		// A3157 A3159 A3166 A3167 A3168 A3172 A3185 A3195 A3200 A3202 A3206
		// A3217 A3222 A3241 A3248 A3249 A3254 A3258 A3259 A3261 A3262 A3262
		// A3272 A3274 A3277 A3282 A3305 A3305 A3332 A3333 A3343 A3353 A3354
		// A3359 A3364 A3368 A3371 A3372 A3381 A3381 A3396 A3400 A3403 A3404
		// A3408 A3418 A3421 A3423 A3428 A3428 A3441 A3462 A3471 A3479 A3494
		// A3496 A3503 A3521 A3522 A3522 A3530 A3540 A3553 A3559 A3559 A3570
		// A3570 A3574 A3578 A3578 A3581 A3581 A3581 A3586 A3590 A3608 A3608
		// A3609 A3613 A3623 A3629 A3637 A3637 A3643 A3649 A3652 A3655 A3663
		// A3665 A3674 A3677 A3678 A3681 A3689 A3690 A3690 A3690 A3705 A3709
		// A3724 A3726 A3726 A3740 A3743 A3750 A3756 A3758 A3764 A3776 A3776
		// A3778 A3786 A3795 A3799 A3799 A3840 A3844 A3847 A3858 A3859 A3866
		// A3871 A3890 A3896 A3904 A3915 A3922 A3924 A3926 A3935 A3940 A3951
		// A3951 A3955 A3955 A3963 A3965 A3969 A3969 A3982 A3990 A3999 A4004
		// A4010 A4015 A4016 A4021 A4021 A4055 A4061 A4061 A4072 A4091 ",10);
		// indexSrc.searchMulti("A1","explore",10);

	}

	/***
	 * Performs a textual search on the index
	 * 
	 * @param tagString
	 *            textual query
	 * @return returns a list on IndexEntry objects containing the first k
	 *         results together with their score
	 * @throws IOException
	 * @throws ParseException
	 */
	private List<IndexEntry> searchTextual(String tagString) throws IOException, ParseException {
		List<IndexEntry> result = new ArrayList<IndexEntry>();
		System.out.println("Searching for '" + tagString + "'");

		QueryParser queryParser = new QueryParser(Fields.TAGS, new WhitespaceAnalyzer());
		Query query = queryParser.parse(tagString);
		TopDocs hits = indexSearcher.search(query, Parameters.K);
		System.out.println("Number of hits: " + hits.totalHits);
		String[] r = new String[hits.scoreDocs.length];
		ScoreDoc[] filterScoreDosArray = hits.scoreDocs;
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			System.out.println("Result image: " + r[i]);
			String score = Float.toString(filterScoreDosArray[i].score);
			result.add(new IndexEntry(indexSearcher.doc(doc).get(Fields.ID), indexSearcher.doc(doc).get(Fields.TAGS),
					indexSearcher.doc(doc).get(Fields.IMG6), indexSearcher.doc(doc).get(Fields.CLASSLABEL), score));
		}
		return result;
	}

	/***
	 * Performs a visual search on the index
	 * 
	 * @param imgString
	 *            input visual feature (already quantised)
	 * @return returns a list on IndexEntry objects containing the first k
	 *         results together with their score
	 * @throws IOException
	 * @throws ParseException
	 */
	private List<IndexEntry> searchVisual(String imgString, int deepLayer) throws IOException, ParseException {
		List<IndexEntry> result = new ArrayList<IndexEntry>();
		QueryParser queryParser = null;

		System.out.println("Searching for '" + imgString + "'");

		if (deepLayer == Parameters.DEEP_LAYER7)
			queryParser = new QueryParser(Fields.IMG, new WhitespaceAnalyzer());
		else if (deepLayer == Parameters.DEEP_LAYER6)
			queryParser = new QueryParser(Fields.IMG6, new WhitespaceAnalyzer());

		Query query = queryParser.parse(imgString);
		TopDocs hits = indexSearcher.search(query, Parameters.K);
		System.out.println("Number of hits: " + hits.totalHits);
		String[] r = new String[hits.scoreDocs.length];
		ScoreDoc[] filterScoreDosArray = hits.scoreDocs;
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			System.out.println("Result image: " + r[i]);
			Explanation x = indexSearcher.explain(query, hits.scoreDocs[i].doc);
			String score = Float.toString(filterScoreDosArray[i].score);
			result.add(new IndexEntry(indexSearcher.doc(doc).get(Fields.ID), indexSearcher.doc(doc).get(Fields.TAGS),
					indexSearcher.doc(doc).get(Fields.IMG), indexSearcher.doc(doc).get(Fields.IMG6),
					indexSearcher.doc(doc).get(Fields.CLASSLABEL), score));
		}
		return result;
	}

	/***
	 * Performs a multi-field search on the index
	 * 
	 * @param imgString
	 *            input visual feature (already quantised)
	 * @param tagString
	 *            textual query
	 * @return returns a list on IndexEntry objects containing the first k
	 *         results together with their score
	 * @throws IOException
	 * @throws ParseException
	 */
	private List<IndexEntry> searchMulti(String imgString, String tagString, int deepLayer)
			throws IOException, ParseException {
		List<IndexEntry> result = new ArrayList<IndexEntry>();
		Query query = null;
		// prepare the query
		if (deepLayer == Parameters.DEEP_LAYER7)
			query = MultiFieldQueryParser.parse(new String[] { imgString, tagString },
					new String[] { Fields.IMG, Fields.TAGS }, new WhitespaceAnalyzer());
		else if (deepLayer == Parameters.DEEP_LAYER6)
			query = MultiFieldQueryParser.parse(new String[] { imgString, tagString },
					new String[] { Fields.IMG6, Fields.TAGS }, new WhitespaceAnalyzer());

		System.out.println("Searching for '" + query.toString() + "'");
		// perform search
		TopDocs hits = indexSearcher.search(query, Parameters.K);
		System.out.println("Number of hits: " + hits.totalHits);
		String[] r = new String[hits.scoreDocs.length];
		ScoreDoc[] filterScoreDosArray = hits.scoreDocs;
		for (int i = 0; i < hits.scoreDocs.length; i++) {
			// fetch score
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			System.out.println("Result image: " + r[i]);
			String score = Float.toString(filterScoreDosArray[i].score);
			// add the result to the list
			result.add(new IndexEntry(indexSearcher.doc(doc).get(Fields.ID), indexSearcher.doc(doc).get(Fields.TAGS),
					indexSearcher.doc(doc).get(Fields.IMG), indexSearcher.doc(doc).get(Fields.IMG6),
					indexSearcher.doc(doc).get(Fields.CLASSLABEL), score));
		}
		return result;
	}

	/***
	 * Retrieves the index entry for a given image name. Used to avoid
	 * extracting the visual features when the image is already present in the
	 * index.
	 * 
	 * @param imgName
	 *            name of the image (with extension)
	 * @return returns a IndexEntry object containing all the fields fetched in
	 *         the index
	 * @throws IOException
	 * @throws ParseException
	 */
	public IndexEntry retrieveIndexEntryDetails(String imgName) throws IOException, ParseException {
		System.out.println("Retriving '" + imgName + "' index entry");

		QueryParser queryParser = new QueryParser(Fields.ID, new WhitespaceAnalyzer());
		Query query = queryParser.parse(imgName);
		TopDocs hits = indexSearcher.search(query, 1);
		int doc = hits.scoreDocs[0].doc;

		String id = new String((indexSearcher.doc(doc).get(Fields.ID)));
		String tags = new String((indexSearcher.doc(doc).get(Fields.TAGS)));
		String imgDesc = new String((indexSearcher.doc(doc).get(Fields.IMG)));
		String imgDesc6 = new String((indexSearcher.doc(doc).get(Fields.IMG6)));
		String classLabel = new String((indexSearcher.doc(doc).get(Fields.CLASSLABEL)));
		IndexEntry result = new IndexEntry(imgName, tags, imgDesc, imgDesc6, classLabel);
		System.out.println("result deep6------" + imgDesc6);
		System.out.println("Found imgName: " + id + " tags: " + tags + " img: " + imgDesc + " class: " + classLabel);

		return result;
	}

	/***
	 * Opens the Lucene index, given the index path
	 * 
	 * @param lucenePath
	 *            String containing the path to the index
	 * @throws IOException
	 */
	private void openIndex(String lucenePath) throws IOException {
		// Initialize Lucene index
		Path absolutePath = Paths.get("C:/Users/Sara/workspaceEE/multiModal/" + lucenePath, "");
		System.out.println("abspath: " + absolutePath);
		FSDirectory index = FSDirectory.open(absolutePath);
		DirectoryReader ir = DirectoryReader.open(index);
		indexSearcher = new IndexSearcher(ir);
	}

}

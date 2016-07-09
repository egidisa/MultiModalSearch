package multiModal;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
		//indexSrc.searchTextual("explore",10);
		indexSrc.searchVisual("A5 A9 A10 A18 A27 A30 A33 A39 A46 A46 A59 A60 A60 A64 A78 A91 A94 A107 A109 A126 A141 A141 A142 A152 A157 A158 A177 A184 A186 A199 A206 A208 A208 A211 A222 A244 A267 A269 A282 A282 A282 A286 A289 A306 A316 A318 A324 A327 A333 A336 A336 A342 A347 A353 A358 A359 A360 A361 A361 A362 A362 A362 A362 A364 A369 A371 A373 A384 A384 A393 A395 A395 A399 A401 A401 A405 A410 A427 A427 A428 A433 A440 A445 A446 A446 A446 A449 A451 A451 A471 A471 A477 A503 A508 A508 A510 A514 A514 A524 A528 A540 A542 A552 A552 A557 A557 A559 A561 A564 A565 A601 A601 A609 A612 A616 A635 A635 A635 A638 A657 A668 A676 A691 A696 A703 A714 A714 A716 A716 A717 A726 A728 A745 A753 A753 A760 A762 A763 A785 A785 A789 A790 A790 A794 A797 A798 A798 A804 A805 A809 A814 A814 A824 A826 A828 A832 A836 A838 A840 A840 A856 A858 A858 A862 A867 A870 A877 A878 A883 A920 A926 A929 A937 A940 A944 A944 A946 A955 A956 A957 A959 A959 A961 A976 A978 A994 A1005 A1009 A1013 A1019 A1027 A1049 A1054 A1054 A1056 A1059 A1059 A1063 A1072 A1079 A1081 A1094 A1097 A1106 A1107 A1111 A1113 A1120 A1143 A1149 A1152 A1152 A1154 A1175 A1179 A1179 A1180 A1183 A1191 A1224 A1225 A1241 A1247 A1255 A1257 A1265 A1276 A1277 A1289 A1307 A1309 A1316 A1319 A1326 A1331 A1339 A1355 A1362 A1376 A1382 A1393 A1394 A1399 A1403 A1411 A1414 A1418 A1423 A1424 A1426 A1426 A1438 A1438 A1440 A1444 A1444 A1447 A1449 A1454 A1460 A1460 A1508 A1508 A1513 A1526 A1526 A1527 A1529 A1539 A1544 A1547 A1547 A1547 A1580 A1584 A1591 A1610 A1610 A1615 A1629 A1629 A1629 A1642 A1646 A1649 A1651 A1652 A1653 A1676 A1677 A1688 A1695 A1701 A1707 A1715 A1716 A1716 A1718 A1725 A1729 A1730 A1735 A1743 A1747 A1747 A1749 A1754 A1786 A1793 A1793 A1796 A1804 A1808 A1816 A1836 A1840 A1857 A1863 A1869 A1869 A1869 A1873 A1876 A1876 A1877 A1884 A1887 A1889 A1898 A1898 A1899 A1917 A1918 A1918 A1918 A1919 A1925 A1927 A1928 A1928 A1947 A1974 A1975 A1976 A1988 A2008 A2009 A2009 A2009 A2012 A2018 A2049 A2054 A2060 A2060 A2071 A2075 A2082 A2083 A2086 A2086 A2104 A2104 A2116 A2117 A2117 A2121 A2132 A2137 A2147 A2168 A2176 A2177 A2179 A2179 A2184 A2202 A2204 A2208 A2213 A2213 A2213 A2214 A2226 A2235 A2235 A2238 A2239 A2249 A2251 A2256 A2258 A2264 A2266 A2276 A2276 A2290 A2296 A2299 A2305 A2306 A2321 A2323 A2325 A2342 A2348 A2364 A2368 A2369 A2373 A2377 A2383 A2395 A2405 A2406 A2406 A2419 A2427 A2450 A2454 A2462 A2463 A2464 A2499 A2499 A2501 A2508 A2509 A2526 A2538 A2538 A2541 A2544 A2564 A2564 A2577 A2578 A2584 A2586 A2589 A2591 A2598 A2609 A2611 A2612 A2614 A2626 A2627 A2633 A2633 A2635 A2636 A2636 A2642 A2659 A2668 A2671 A2682 A2685 A2685 A2692 A2717 A2722 A2726 A2732 A2737 A2740 A2747 A2758 A2765 A2768 A2791 A2792 A2795 A2797 A2801 A2805 A2807 A2807 A2825 A2827 A2827 A2829 A2835 A2838 A2838 A2838 A2846 A2850 A2853 A2853 A2864 A2864 A2868 A2871 A2872 A2886 A2890 A2903 A2911 A2912 A2916 A2927 A2933 A2935 A2945 A2948 A2948 A2951 A2958 A2958 A2960 A2961 A2964 A2966 A2966 A2968 A2986 A2986 A2996 A3000 A3005 A3007 A3018 A3022 A3035 A3037 A3038 A3046 A3048 A3067 A3067 A3069 A3069 A3079 A3086 A3091 A3091 A3094 A3137 A3147 A3153 A3155 A3155 A3155 A3157 A3165 A3167 A3180 A3194 A3194 A3201 A3206 A3206 A3209 A3214 A3218 A3218 A3218 A3238 A3240 A3290 A3295 A3300 A3305 A3305 A3306 A3309 A3309 A3312 A3319 A3322 A3333 A3339 A3343 A3344 A3347 A3351 A3358 A3372 A3379 A3386 A3398 A3400 A3408 A3409 A3412 A3412 A3413 A3416 A3416 A3421 A3441 A3456 A3464 A3478 A3481 A3481 A3481 A3482 A3498 A3498 A3504 A3508 A3509 A3510 A3510 A3527 A3528 A3528 A3528 A3541 A3542 A3547 A3549 A3561 A3574 A3588 A3589 A3594 A3601 A3603 A3603 A3605 A3607 A3610 A3615 A3624 A3624 A3626 A3631 A3634 A3641 A3645 A3645 A3649 A3652 A3667 A3678 A3680 A3685 A3700 A3711 A3721 A3722 A3727 A3728 A3733 A3736 A3741 A3746 A3757 A3768 A3777 A3789 A3808 A3812 A3815 A3825 A3841 A3848 A3859 A3865 A3865 A3878 A3881 A3882 A3902 A3902 A3902 A3910 A3911 A3920 A3922 A3924 A3928 A3932 A3947 A3948 A3952 A3956 A3969 A3979 A4001 A4003 A4010 A4016 A4021 A4024 A4038 A4050 A4058 A4062 A4066 A4066 A4070 A4092 A4092 ",10);
		
		//indexSrc.searchMulti("explore","explore",10);

	}
	
	public void searchTextual(String tagString, int k) throws IOException, ParseException {
		
		System.out.println("Searching for '" + tagString + "'");
		
		QueryParser queryParser = new QueryParser(Fields.TAGS, new WhitespaceAnalyzer());
		Query query = queryParser.parse(tagString);
		TopDocs hits = indexSearcher.search(query,k);
		System.out.println("Number of hits: " + hits.totalHits);
		String[] r = new String[hits.scoreDocs.length];

		for (int i = 0; i < hits.scoreDocs.length; i++) {
			
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			System.out.println("Result image: "+r[i]);
			Explanation x = indexSearcher.explain(query, hits.scoreDocs[i].doc);
			System.out.println(x.toString());
			//res.add(new ImgDescriptor(indexSearcher.doc(doc).get(Fields.BINARY)));
		}
	}
	
	public void searchVisual(String imgString, int k) throws IOException, ParseException {
		System.out.println("Searching for '" + imgString + "'");
		QueryParser queryParser = new QueryParser(Fields.IMG, new WhitespaceAnalyzer());
		Query query = queryParser.parse(imgString);
		TopDocs hits = indexSearcher.search(query,k);
		System.out.println("Number of hits: " + hits.totalHits);
		String[] r = new String[hits.scoreDocs.length];

		for (int i = 0; i < hits.scoreDocs.length; i++) {
			
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			System.out.println("Result image: "+r[i]);
			Explanation x = indexSearcher.explain(query, hits.scoreDocs[i].doc);
			//System.out.println(x.toString());
			//res.add(new ImgDescriptor(indexSearcher.doc(doc).get(Fields.BINARY)));
		}
	}
	
	public void searchMulti(String imgString, String tagString, int k) throws IOException, ParseException {
		
		Query query = MultiFieldQueryParser.parse(new String[] {imgString,tagString},   new String[] {Fields.IMG,Fields.TAGS},new WhitespaceAnalyzer());
		System.out.println("Searching for '" + query.toString() + "'");

		TopDocs hits = indexSearcher.search(query,k);
		System.out.println("Number of hits: " + hits.totalHits);
		String[] r = new String[hits.scoreDocs.length];

		for (int i = 0; i < hits.scoreDocs.length; i++) {
			
			int doc = hits.scoreDocs[i].doc;
			r[i] = new String((indexSearcher.doc(doc).get(Fields.ID)));
			System.out.println("Result image: "+r[i]);
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

package mim;

import java.io.File;

public class Parameters {
	
	//DEEP parameters
	public static final String DEEP_PROTO = "C:/Users/Sara/workspaceEE/multiModal/data/caffe/train_val.prototxt";
	public static final String DEEP_MODEL = "C:/Users/Sara/workspaceEE/multiModal/data/caffe/bvlc_reference_caffenet.caffemodel";
	public static final String DEEP_MEAN_IMG = "C:/Users/Sara/workspaceEE/multiModal/data/caffe/meanImage.png";
	
	public static final String DEEP_LAYER = "fc7";
	public static final int IMG_WIDTH = 227;
	public static final int IMG_HEIGHT = 227;
	
	//lucene output folder
	public static final String LUCENE_INDEX_DIRECTORY = "C:\\lucene";
	
	//Image Source Folder
	public static final File SRC_FOLDER = new File("C:/Users/Sara/workspaceEE/multiModal/data/img");
	public static final File SRC_FOLDER_TEST = new File("C:/Users/Sara/workspaceEE/multiModal/data/imgFlickr");
	
	//Features Storage File
	//public static final File STORAGE_FILE = new File("data/deep.seq.dat");
	public static final File STORAGE_FILE = new File("C:/Users/Sara/workspaceEE/multiModal/WebContent/WEB-INF/classes/deep.seq.dat");
	public static final File STORAGE_FILE_6 = new File("C:/Users/Sara/workspaceEE/multiModal/WebContent/WEB-INF/classes/deep6.dat");
	public static final File STORAGE_FILE_7 = new File("C:/Users/Sara/workspaceEE/multiModal/WebContent/WEB-INF/classes/deep7.dat");
	//k-Nearest Neighbors
	public static final int K = 30;
	
	//Pivots File
	public static final File  PIVOTS_FILE = new File("C:/Users/Sara/workspaceEE/multiModal/out/deep.pivots.dat");
	
	//Number Of Pivots
	public static final int NUM_PIVOTS = 100;

	//Top K pivots For Indexing
	public static final int TOP_K_IDX = 10;
	
	//Top K pivots For Searching
	public static final int TOP_K_QUERY = 10;
	
	//Lucene Index
	public static final String  LUCENE_PATH = "C:/Users/Sara/workspaceEE/multiModal/out/"  + "Lucene_Deep";
	
	//HTML Output Parameters
	public static final  String BASE_URI = "file:///" + Parameters.SRC_FOLDER.getAbsolutePath() + "/";
	public static final File RESULTS_HTML = new File("C:/Users/Sara/workspaceEE/multiModal/WebContent/deep.seq.html");
	public static final File RESULTS_HTML_LUCENE = new File("C:/Users/Sara/workspaceEE/multiModal/out/deep.lucene.html");
	public static final File RESULTS_HTML_REORDERED = new File("C:/Users/Sara/workspaceEE/multiModal/out/deep.reordered.html");

}

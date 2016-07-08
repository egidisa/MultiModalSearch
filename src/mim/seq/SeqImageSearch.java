package mim.seq;

import mim.DNNExtractor;
import mim.ImgDescriptor;
import mim.Parameters;
import mim.tools.FeaturesStorage;
import mim.tools.Output;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class SeqImageSearch {
	
	public SeqImageSearch(){}
	
	public SeqImageSearch(String inputImg) throws Exception{
		this.open(Parameters.STORAGE_FILE);
		
		//Image Query File
		File img = new File(Parameters.SRC_FOLDER, inputImg);
		
		DNNExtractor extractor = new DNNExtractor();
		
		float[] features = extractor.extract(img, Parameters.DEEP_LAYER);
		ImgDescriptor query = new ImgDescriptor(features, img.getName());
				
		long time = -System.currentTimeMillis();
		List<ImgDescriptor> res = this.search(query, Parameters.K);
		time += System.currentTimeMillis();
		System.out.println("Sequential search time: " + time + " ms");
		
		Output.toHTML(res, Parameters.BASE_URI, Parameters.RESULTS_HTML);
	}

	private List<ImgDescriptor> descriptors;
		
	public static void main(String[] args) throws Exception {

		SeqImageSearch searcher = new SeqImageSearch();
		
		searcher.open(Parameters.STORAGE_FILE);
		
		//Image Query File
		File img = new File(Parameters.SRC_FOLDER, "b402c97071eea022f2d8fd700eed04ad.jpg");
		
		DNNExtractor extractor = new DNNExtractor();
		
		float[] features = extractor.extract(img, Parameters.DEEP_LAYER);
		ImgDescriptor query = new ImgDescriptor(features, img.getName());
				
		long time = -System.currentTimeMillis();
		List<ImgDescriptor> res = searcher.search(query, Parameters.K);
		time += System.currentTimeMillis();
		System.out.println("Sequential search time: " + time + " ms");
		
		Output.toHTML(res, Parameters.BASE_URI, Parameters.RESULTS_HTML);

	}
	
	/***
	 * performs a search based just on the filename of the locally stored image
	 * @param inputImg name of the local image that acts as a query
	 * @throws Exception
	 */
	public void search(String inputImg) throws Exception{
		SeqImageSearch searcher = new SeqImageSearch();
		searcher.open(Parameters.STORAGE_FILE);
		
		//Image Query File
		File img = new File(Parameters.SRC_FOLDER, inputImg);
		
		DNNExtractor extractor = new DNNExtractor();
		//TODO - se l'img è già presente nell'indice non serve ri-estrarre le features - Gennaro
		
		//TODO - retrieve img's tags <- the future SIS should do it
		float[] features = extractor.extract(img, Parameters.DEEP_LAYER);
		ImgDescriptor query = new ImgDescriptor(features, img.getName());
				
		long time = -System.currentTimeMillis();
		List<ImgDescriptor> res = searcher.search(query, Parameters.K);
		time += System.currentTimeMillis();
		System.out.println("Sequential search time: " + time + " ms");
		
		Output.toHTML(res, Parameters.BASE_URI, Parameters.RESULTS_HTML);
	}
		
	public void open(File storageFile) throws ClassNotFoundException, IOException {
		descriptors = FeaturesStorage.load(storageFile );
	}
	
	public List<ImgDescriptor> search(ImgDescriptor queryF, int k) {
		for (int i=0;i<descriptors.size();i++){
			descriptors.get(i).distance(queryF);
		}

		Collections.sort(descriptors);
		
		return descriptors.subList(0, k);
	}

	public void searchByPath(String inputPath) throws ClassNotFoundException, IOException {
		SeqImageSearch searcher = new SeqImageSearch();
		searcher.open(Parameters.STORAGE_FILE);
		
		//Image Query File
		File img = new File(inputPath);
		
		DNNExtractor extractor = new DNNExtractor();
		//TODO - se l'img è già presente nell'indice non serve ri-estrarre le features - Gennaro
		
		//TODO - retrieve img's tags <- the future SIS should do it
		float[] features = extractor.extract(img, Parameters.DEEP_LAYER);
		ImgDescriptor query = new ImgDescriptor(features, img.getName());
				
		long time = -System.currentTimeMillis();
		List<ImgDescriptor> res = searcher.search(query, Parameters.K);
		time += System.currentTimeMillis();
		System.out.println("Sequential search time: " + time + " ms");
		
		Output.toHTML(res, Parameters.BASE_URI, Parameters.RESULTS_HTML);
	}

}

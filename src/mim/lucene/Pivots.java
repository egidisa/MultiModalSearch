package mim.lucene;

import mim.ImgDescriptor;
import mim.Parameters;
import mim.seq.SeqImageSearch;
import mim.tools.FeaturesStorage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pivots {
	
	private SeqImageSearch seqPivots = new SeqImageSearch();
	
	//TODO
	public Pivots(File pivotsFile) throws ClassNotFoundException, IOException {
		//Load the pivots file
		seqPivots.open(pivotsFile);
	}

	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		List<ImgDescriptor> ids = FeaturesStorage.load(Parameters.STORAGE_FILE);
		List<ImgDescriptor> pivs = Pivots.makeRandomPivots(ids, Parameters.NUM_PIVOTS);
		FeaturesStorage.store(pivs, Parameters.PIVOTS_FILE);
	}
	
	//TODO
	public static List<ImgDescriptor> makeRandomPivots(List<ImgDescriptor> ids, int nPivs) {
		ArrayList<ImgDescriptor> pivots = new ArrayList<ImgDescriptor>();
		//randomize objs
		Collections.shuffle(ids);
		pivots.addAll(ids.subList(0, nPivs));
		return pivots;
	}
	
	//TODO
	public String features2Text(ImgDescriptor imgF, int topK) {
		StringBuilder sb = new StringBuilder();
		//perform a sequential search to get the topK most similar pivots
		//usare come query
		List<ImgDescriptor> result = seqPivots.search(imgF, topK);
		//compose the text string using pivot ids
		for(int i=0;i<topK;i++){
			//for each topk feature found
			String id = result.get(i).getId();
			
			for (int j=0;j<topK-i;j++){
				sb.append(i).append(" ");
			}
		}
		return sb.toString();
	}
	
}
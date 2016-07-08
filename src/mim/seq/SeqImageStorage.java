package mim.seq;

import mim.DNNExtractor;
import mim.ImgDescriptor;
import mim.Parameters;
import mim.tools.FeaturesStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SeqImageStorage {

	public static void main(String[] args) throws Exception {
				
		SeqImageStorage indexing = new SeqImageStorage();
				
		List<ImgDescriptor> descriptors = indexing.extractFeatures(Parameters.SRC_FOLDER);
		
		FeaturesStorage.store(descriptors, Parameters.STORAGE_FILE);
	}
	
	private List<ImgDescriptor> extractFeatures(File imgFolder){
		List<ImgDescriptor>  descs = new ArrayList<ImgDescriptor>();

		File[] files = imgFolder.listFiles();
		
		DNNExtractor extractor = new DNNExtractor();

		for (int i = 0; i < files.length; i++) {
			System.out.println(i + " - extracting " + files[i].getName());
			try {
				long time = -System.currentTimeMillis();
				float[] features = extractor.extract(files[i], Parameters.DEEP_LAYER);
				time += System.currentTimeMillis();
				System.out.println(time);
				descs.add(new ImgDescriptor(features, files[i].getName()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return descs;	
	}		
}
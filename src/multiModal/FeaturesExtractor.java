package multiModal;
import mim.DNNExtractor; 
import mim.ImgDescriptor;
import mim.Parameters;
import mim.tools.FeaturesStorage;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.comparator.NameFileComparator;

public class FeaturesExtractor {
	private final int BATCH_SIZE = 5000;
	//main to test extractor. extracts the features and saves them in data/deep.dat
	public static void main (String[] args) throws Exception{
		
		//instantiate extractor
		FeaturesExtractor indexing = new FeaturesExtractor();
		indexing.extractFeatures(Parameters.SRC_FOLDER_TEST);
		//fill the list of global descriptors for each image 
		//List<ImgDescriptor> descriptors6 = indexing.extractFeatures(Parameters.SRC_FOLDER_TEST);
		//List<ImgDescriptor> descriptors = indexing.extractFeatures(Parameters.SRC_FOLDER_TEST);
		
		//store descriptors in data/deep.dat
		//FeaturesStorage.store(descriptors6, Parameters.STORAGE_FILE_6);
		//FeaturesStorage.store(descriptors, Parameters.STORAGE_FILE_7);
		
	}
	
	//extract features given a folder
	private void extractFeatures(File imgFolder) throws Exception{
		DNNLabelExtractor extractor = new DNNLabelExtractor();
		File[] files = imgFolder.listFiles();
		Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                int n1 = extractNumber(o1.getName());
                int n2 = extractNumber(o2.getName());
                return n1 - n2;
            }

            private int extractNumber(String name) {
                int i = 0;
                try {
                    int s = name.indexOf('m')+1;
                    int e = name.lastIndexOf('.');
                    String number = name.substring(s, e);
                    i = Integer.parseInt(number);
                } catch(Exception e) {
                    i = 0; // if filename does not match the format
                           // then default to 0
                }
                return i;
            }
		});
		//parse files in batches of 5000
		List<ImgDescriptor> descs6; 
		List<ImgDescriptor> descs7; 

		for(int j = 1; j <= 5; j++){
			System.out.println("Batch "+ j);
			//prepare descriptors lists
			descs6 = new ArrayList<ImgDescriptor>();
			descs7 = new ArrayList<ImgDescriptor>();

			//File[] files = imgFolder.listFiles();
			PrintWriter writer = new PrintWriter("data/classifiedLabels"+j+".txt", "UTF-8");

			for (int i = (j-1)*BATCH_SIZE; i <= ((j)*BATCH_SIZE)-1; i++) {
				System.out.println(i + " - extracting " + files[i].getName());
				try {
					long time = -System.currentTimeMillis();
					Descriptors descriptors = extractor.extract(files[i], Parameters.DEEP_LAYER);
					time += System.currentTimeMillis();
					System.out.println(time);
					descs6.add(new ImgDescriptor(descriptors.featDesc6, files[i].getName()));
					descs7.add(new ImgDescriptor(descriptors.featDesc7, files[i].getName()));
					writer.println(files[i].getName()+";"+descriptors.classLabel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}		
		FeaturesStorage.store(descs6, new File("data/ids6/deep6"+j+".dat"));
		FeaturesStorage.store(descs7, new File("data/ids7/deep7"+j+".dat"));
		writer.close();
		}
	}		
}

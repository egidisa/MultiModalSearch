package multiModal;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import mim.ImgDescriptor;
import mim.Parameters;

public class FeaturesExtractor {

	// main to test extractor. extracts the features and saves them in
	// data/deep.dat
	public static void main(String[] args) throws Exception {

		// instantiate extractor
		FeaturesExtractor indexing = new FeaturesExtractor();
		indexing.extractFeatures(Parameters.SRC_FOLDER_TEST);
		// fill the list of global descriptors for each image
		// List<ImgDescriptor> descriptors6 =
		// indexing.extractFeatures(Parameters.SRC_FOLDER_TEST);
		// List<ImgDescriptor> descriptors =
		// indexing.extractFeatures(Parameters.SRC_FOLDER_TEST);

		// store descriptors in data/deep.dat
		//FeaturesStorage.store(descriptors6, Parameters.STORAGE_FILE_6);
		//FeaturesStorage.store(descriptors, Parameters.STORAGE_FILE_7);

	}

	// extract features given a folder
	private void extractFeatures(File imgFolder) throws Exception {
		List<ImgDescriptor> descs6 = new ArrayList<ImgDescriptor>();
		List<ImgDescriptor> descs7 = new ArrayList<ImgDescriptor>();

		File[] files = imgFolder.listFiles();
		PrintWriter writer = new PrintWriter("data/classifiedLabels.txt", "UTF-8");

		DNNLabelExtractor extractor = new DNNLabelExtractor();

		for (int i = 0; i < files.length; i++) {
			System.out.println(i + " - extracting " + files[i].getName());
			long time = -System.currentTimeMillis();
			Descriptors descriptors = extractor.extract(files[i], Parameters.DEEP_LAYER);
			time += System.currentTimeMillis();
			System.out.println(time);
			// descs6.add(new ImgDescriptor(descriptors.featDesc6,
			// files[i].getName()));
			// descs7.add(new ImgDescriptor(descriptors.featDesc7,
			// files[i].getName()));
			writer.println(files[i].getName() + ";" + descriptors.classLabel);
		}
		//FeaturesStorage.store(descs6, Parameters.STORAGE_FILE_6);
		//FeaturesStorage.store(descs7, Parameters.STORAGE_FILE_7);
		writer.close();
	}
	
	/***
	 * Extracts visual features from both levels 6 and 7, given a image file
	 * @param img
	 * @return
	 * @throws Exception
	 */
	static Descriptors extractFeaturesSingle(File img) throws Exception {
		DNNLabelExtractor extractor = new DNNLabelExtractor();
		System.out.println("Extracting " + img.getAbsolutePath());
		long time = -System.currentTimeMillis();
		Descriptors descriptor = extractor.extract(img, Parameters.DEEP_LAYER);
		time += System.currentTimeMillis();
		System.out.println(time);
		return descriptor;
		}
	
}

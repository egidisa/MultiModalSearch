package mim.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeatureMerger {
	private List<mim.ImgDescriptor> descriptors1;
	private List<mim.ImgDescriptor> descriptors2;
	
	public static final File PART_1 = new File("data/deep6_1.dat");
	public static final File PART_2 = new File("data/deep6_2.dat");
	public static final File MERGED = new File("data/deep6merged.dat");
	
	public static void main(String[] args) throws Exception {
		FeatureMerger merger = new FeatureMerger();
		merger.open(PART_1,PART_2);
		System.out.println("Opened both!");
		merger.merge();		
		System.out.println("Saved merged file");
	}
	
	public void open(File part1, File part2) throws ClassNotFoundException, IOException {
		descriptors1 = FeaturesStorage.load(part1);
		descriptors2 = FeaturesStorage.load(part2);
	}
	public void merge() throws IOException{
		List<mim.ImgDescriptor> newList = new ArrayList<mim.ImgDescriptor>(descriptors1);
		newList.addAll(descriptors2);
		//store to new file
		FeaturesStorage.store(newList, MERGED);
	}
}

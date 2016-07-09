package mim.tools;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeatureMerger {
	private List<mim.ImgDescriptor> descriptors1;
	private List<mim.ImgDescriptor> descriptors2;
	private List<mim.ImgDescriptor> descriptors3;
	private List<mim.ImgDescriptor> descriptors4;
	private List<mim.ImgDescriptor> descriptors5;
	
	public static final File PART_1 = new File("data/deep6_1.dat");
	public static final File PART_2 = new File("data/deep6_2.dat");
	public static final File PART_3 = new File("data/deep6_3.dat");
	public static final File PART_4 = new File("data/deep6_4.dat");
	public static final File PART_5 = new File("data/deep6_5.dat");
	
	public static final File MERGED = new File("data/deepFeatures/deep6.dat");
	
	public static void main(String[] args) throws Exception {
		FeatureMerger merger = new FeatureMerger();
		merger.open(PART_1,PART_2, PART_3,PART_4, PART_5);
		System.out.println("Opened both!");
		merger.merge();		
		System.out.println("Saved merged file");
	}
	
	public void open(File part1, File part2,File part3, File part4,File part5) throws ClassNotFoundException, IOException {
		descriptors1 = FeaturesStorage.load(part1);
		descriptors2 = FeaturesStorage.load(part2);
		descriptors3 = FeaturesStorage.load(part3);
		descriptors4 = FeaturesStorage.load(part4);
		descriptors5 = FeaturesStorage.load(part5);
	}
	public void merge() throws IOException{
		List<mim.ImgDescriptor> newList = new ArrayList<mim.ImgDescriptor>(descriptors1);
		newList.addAll(descriptors2);
		newList.addAll(descriptors3);
		newList.addAll(descriptors4);
		newList.addAll(descriptors5);
		//store to new file
		FeaturesStorage.store(newList, MERGED);
	}
}

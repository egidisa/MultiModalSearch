package mim.lucene;

import java.util.Arrays;

import mim.ImgDescriptor;

public class DCNNFeaturesQuantization {

	private static float Q = 30.0f;

	public static String quantize(ImgDescriptor iDesc) {
		float[] features = iDesc.getFeatures();

		//System.out.println(Arrays.toString(features));
		int[] repetitions = new int[features.length];

		StringBuilder sb = new StringBuilder("");

		for (int i = 0; i < features.length; ++i)
			repetitions[i] = Math.round(Q * features[i]);
		
		//System.out.println(Arrays.toString(repetitions));

		for (int i = 0; i < features.length; ++i)
			while (repetitions[i] > 0) {
				sb.append("A" + i + " ");
				repetitions[i]--;
			}

		return sb.toString();
	}
}
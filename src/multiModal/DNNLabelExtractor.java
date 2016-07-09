package multiModal;

import mim.Parameters;
import static org.bytedeco.javacpp.opencv_core.minMaxLoc;
import static org.bytedeco.javacpp.opencv_core.CV_32FC3;
import static org.bytedeco.javacpp.opencv_core.subtract;
import static org.bytedeco.javacpp.opencv_dnn.createCaffeImporter;
import static org.bytedeco.javacpp.opencv_imgcodecs.CV_LOAD_IMAGE_UNCHANGED;
import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgproc.resize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.Point;
import org.bytedeco.javacpp.opencv_core.Size;
import org.bytedeco.javacpp.opencv_dnn.Blob;
import org.bytedeco.javacpp.opencv_dnn.Importer;
import org.bytedeco.javacpp.opencv_dnn.Net;

public class DNNLabelExtractor {

	private Mat meanImg;
	private Net net;
	private Size imgSize;
	
    public static void getMaxClass(Blob probBlob, Point classId, double[] classProb) {
    	//reshape the blob to 1x1000 matrix
        Mat probMat = probBlob.matRefConst().reshape(1, 1);
        
        //find the minimum and maximum element values and their positions
        minMaxLoc(probMat, null, classProb, null, classId, null);
    }
	
	public DNNLabelExtractor() {		
			//Create the importer of Caffe framework network
			Importer importer = createCaffeImporter(new File(Parameters.DEEP_PROTO).getAbsolutePath(), new File(Parameters.DEEP_MODEL).getAbsolutePath());
			
			//Initialize the network
			net = new Net();
			
			//Add loaded layers into the net and sets connections between them
			importer.populateNet(net);
	        importer.close();
	        
	        imgSize = new Size(Parameters.IMG_WIDTH, Parameters.IMG_HEIGHT);

			meanImg = imread(new File(Parameters.DEEP_MEAN_IMG).getAbsolutePath());
			meanImg.convertTo(meanImg, CV_32FC3);
			resize(meanImg, meanImg, imgSize);
	}

	public Descriptors extract(File image, String layer) {
		Mat img = imread(image.getAbsolutePath(), CV_LOAD_IMAGE_UNCHANGED);
		return extract(img, layer);
	}

	//reads class names from file
	public static List<String> readClassNames() {
        String filename = "data/synset_words.txt";
        List<String> classNames = null;

        try (BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
            classNames = new ArrayList<String>();
            String name = null;
            while ((name = br.readLine()) != null) {
                classNames.add(name.substring(name.indexOf(' ')+1));
            }
        } catch (IOException ex) {
            System.err.println("File with classes labels not found " + filename);
            System.exit(-1);
        }
        return classNames;
    }
	
	public Descriptors extract(Mat img, String layer) {
		
		img.convertTo(img, CV_32FC3);
		resize(img, img, imgSize);
		if (meanImg != null) {
			subtract(img, meanImg, img);
		}
		
		// Convert Mat to dnn::Blob image batch
		Blob inputBlob = new Blob(img);

		// set the network input
		net.setBlob(".data", inputBlob);
		
		// compute output
		net.forward();

		// gather output of "fc7" layer
		Blob prob = net.getBlob(layer);
		Blob prob6 = net.getBlob("fc6");
		//a questo punto è possibile fare getMaxClass() come fa Bolettieri qui o_o https://github.com/bytedeco/javacv/blob/master/samples/CaffeGooglenet.java

		//gather output of "prob" layer
        Blob probClass = net.getBlob("prob");      

        Point classId = new Point();
        double[] classProb = new double[1];
        
        //find the best class
        getMaxClass(probClass, classId, classProb);
        
        //print class labels
        //! [Print results]
        List<String> classNames = readClassNames();
        System.out.println("Best class: #" + classId.x() + " '" + classNames.get(classId.x()) + "'");
        System.out.println("Probability: " + classProb[0] * 100 + "%");

		//Returns pointer to the blob element with the specified position, stored in CPU memory.
		FloatPointer fp = prob.ptrf();
		FloatPointer fp6 = prob6.ptrf();
		float[] features = new float[(int) prob.total()];
		float[] features6 = new float[(int) prob6.total()];
		fp.get(features);
		fp6.get(features6);
		String classLabelProb = classId.x()+";"+classNames.get(classId.x())+";"+String.valueOf(classProb[0] * 100);
		Descriptors descResult = new Descriptors(features6,features,classLabelProb);
		return descResult;
	}

	public Descriptors extractPath(File img, String deepLayer) {
		// TODO Auto-generated method stub
		return null;
	}
}

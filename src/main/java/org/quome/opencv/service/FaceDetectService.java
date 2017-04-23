package org.quome.opencv.service;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FaceDetectService {
	
	static{
		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	public void detectFace(MultipartFile file, String targetName) {

		
		try {
			// Create a face detector from the cascade file in the resources
			// directory.
			CascadeClassifier faceDetector = new CascadeClassifier(getClass()
					.getResource("/lbpcascade/lbpcascade_frontalface.xml").getPath().substring(1).replace("%20", " "));
			BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
			int rows = bufferedImage.getWidth();
			int cols = bufferedImage.getHeight();
			int type = CvType.CV_8UC3;
			Mat image = new Mat(rows,cols,type);

			// Detect faces in the image.
			// MatOfRect is a special container class for Rect.
			MatOfRect faceDetections = new MatOfRect();
			faceDetector.detectMultiScale(image, faceDetections);

			System.out.println(String.format("Detected %s faces",
					faceDetections.toArray().length));

			// Draw a bounding box around each face.
			for (Rect rect : faceDetections.toArray()) {
				Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
						+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
			}

			// Save the visualized detection.
			System.out.println(String.format("Writing %s", targetName));
			Imgcodecs.imwrite(targetName, image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

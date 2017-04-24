package org.quome.opencv.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class BufImgToMatUtil {

	/**
	 * 
	 * @param image
	 * @param imgType
	 *            bufferedImage的类型 如 BufferedImage.TYPE_3BYTE_BGR
	 * @param matType
	 *            转换成mat的type 如 CvType.CV_8UC3
	 */
	public static Mat getMat(BufferedImage original, int imgType, int matType) {
		if (original == null) {
			throw new IllegalArgumentException("original == null");
		}

		// Don't convert if it already has correct type
		if (original.getType() != imgType) {

			// Create a buffered image
			BufferedImage image = new BufferedImage(original.getWidth(),
					original.getHeight(), imgType);

			// Draw the image onto the new buffer
			Graphics2D g = image.createGraphics();
			try {
				g.setComposite(AlphaComposite.Src);
				g.drawImage(original, 0, 0, null);
			} finally {
				g.dispose();
			}
		}

		byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer())
				.getData();
		Mat mat = Mat.eye(original.getHeight(), original.getWidth(), matType);
		mat.put(0, 0, pixels);
		return mat;
	}

	// The file extension string should be ".jpg", ".png", etc
	public static BufferedImage getImage(Mat amatrix, String fileExtension) {
		MatOfByte mob = new MatOfByte();
		// convert the matrix into a matrix of bytes appropriate for
		// this file extension
		Imgcodecs.imencode(fileExtension, amatrix, mob);
		// convert the "matrix of bytes" into a byte array
		byte[] byteArray = mob.toArray();
		BufferedImage bufImage = null;
		try {
			InputStream in = new ByteArrayInputStream(byteArray);
			bufImage = ImageIO.read(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bufImage;
	}
	
	// The file extension string should be ".jpg", ".png", etc
	public static InputStream getImageInputStream(Mat amatrix, String fileExtension) {
		MatOfByte mob = new MatOfByte();
		// convert the matrix into a matrix of bytes appropriate for
		// this file extension
		Imgcodecs.imencode(fileExtension, amatrix, mob);
		// convert the "matrix of bytes" into a byte array
		byte[] byteArray = mob.toArray();
		InputStream in = null;
		try {
			in = new ByteArrayInputStream(byteArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return in;
	}
}
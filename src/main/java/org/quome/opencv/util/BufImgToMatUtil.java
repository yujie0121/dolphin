package org.quome.opencv.util;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Mat;

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
}
package fileHandling;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import metrics.Metrics;

/**
 * Handles files for the animation creation project.
 * 
 * @author Richard Jenkin
 * 
 */
public class AnimationFileHandler {
	private FileHandler fh;
	private List<Metrics> allMetrics;

	/**
	 * Constructor. Loads metrics ready for analysis on creation.
	 */
	public AnimationFileHandler() {
		this.fh = new FileHandler();
		this.allMetrics = loadMetrics();

		fh.setOutputFolder("output");
	}


	/**
	 * Gets all the metrics currently loaded as a <code>List</code> of
	 * <code>Metric</code>s.
	 * 
	 * @return All the metrics currently loaded.
	 */
	public List<Metrics> getAllMetrics() {
		return allMetrics;
	}


	public void saveMatrixImage(int[][] matrix, String name) {
		int width = matrix.length;
		int height = matrix[0].length;
		BufferedImage matrixImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x == y) {
					matrixImage.setRGB(y, height - x - 1, Color.GREEN.getRGB());
				} else if (matrix[x][y] > 0) {
					matrixImage.setRGB(y, height - x - 1, Color.BLACK.getRGB());
				} else {
					matrixImage.setRGB(y, height - x - 1, Color.WHITE.getRGB());
				}
			}
		}
		fh.saveImage(matrixImage, name);
	}

	public void saveMatrixImage(List<List<Integer>> matrix, String name) {
		int width = matrix.size();
		int height = matrix.get(0).size();
		BufferedImage matrixImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (x == y) {
					matrixImage.setRGB(y, height - x - 1, Color.GREEN.getRGB());
				} else if (matrix.get(x).get(y) > 0) {
					matrixImage.setRGB(y, height - x - 1, Color.BLACK.getRGB());
				} else {
					matrixImage.setRGB(y, height - x - 1, Color.WHITE.getRGB());
				}
			}
		}
		fh.saveImage(matrixImage, name);
	}

	/**
	 * One-time run on load. Loads all metrics currently in the file
	 * /output/metrics.csv
	 * 
	 * @return
	 */
	private List<Metrics> loadMetrics() {
		CSVHandler csvh = new CSVHandler();
		csvh.setInputFolder("output");
		csvh.setFileName("metrics");
		List<int[]> rawData = csvh.readCSVint();
		List<Metrics> metrics = new ArrayList<Metrics>();
		for (int[] rawMet : rawData) {
			Metrics met = new Metrics(rawMet[0], rawMet[1], rawMet[2], rawMet[3]);
			met.setRelCentroidX(rawMet[4]);
			met.setRelEccentricityX(rawMet[5]);
			met.setRelVelocityX(rawMet[6]);
			metrics.add(met);
		}
		return metrics;
	}
}

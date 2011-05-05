package animationCreator;

import java.util.ArrayList;
import java.util.List;

import metrics.Metrics;

/**
 * 
 * @author Richard Jenkin
 * @version 0.5
 *
 */
public class AnalyseMetrics {

	public AnalyseMetrics() {

	}

	public void Analyse(List<int[]> boundingBoxes) {

	}

	/**
	 * Takes a list of coordinates (startX, startY) (endX, endY), and converts
	 * them to aspect ratios.
	 * 
	 * @param boundingBoxes
	 *            <code>List</code> of <code>Metrics</code>.
	 * @return <code>List</code> of <code>Float</code> of aspect ratio (width
	 *         divided by height).
	 */
	public List<Double> getAspectRatios(List<Metrics> metrics) {
		List<Double> aspectRatios = new ArrayList<Double>();

		for (Metrics m : metrics) {
			aspectRatios.add(((double) (m.getAbsEndX() - m.getAbsStartX())) / ((double) ((m.getAbsEndY() - m.getAbsStartY()))));
		}

		return aspectRatios;
	}

	public List<Integer> getXCentroid(List<Metrics> metrics) {
		List<Integer> xCentroids = new ArrayList<Integer>();

		for (Metrics m : metrics) {
			xCentroids.add(m.getRelCentroidX());
		}
		return xCentroids;
	}

	public List<Integer> getXEccentricity(List<Metrics> metrics) {
		List<Integer> xEccentricities = new ArrayList<Integer>();

		for (Metrics m : metrics) {
			xEccentricities.add(m.getRelEccentricityX());
		}
		return xEccentricities;
	}

	public List<Integer> getXVelocities(List<Metrics> metrics) {
		List<Integer> xVelocities = new ArrayList<Integer>();

		for (Metrics m : metrics) {
			xVelocities.add(m.getRelVelocityX());
		}
		return xVelocities;
	}
}

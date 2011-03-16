package animationCreator;

import java.util.ArrayList;
import java.util.List;

public class AnalyseBoundingBoxes {

	public AnalyseBoundingBoxes() {

	}

	public void Analyse(List<int[]> boundingBoxes) {

	}


	/**
	 * Takes a list of coordinates (startX, startY) (endX, endY), and converts
	 * them to aspect ratios.
	 * 
	 * @param boundingBoxes
	 *            <code>List</code> of array of <code>int</code> in the form
	 *            (startX, startY, endX, endY).
	 * @return <code>List</code> of <code>Float</code> of aspect ratio (width
	 *         divided by height).
	 */
	public List<Float> getAspectRatios(List<int[]> boundingBoxes) {
		List<Float> aspectRatios = new ArrayList<Float>();

		for (int[] boundingBox : boundingBoxes) {
			aspectRatios.add((float) ((boundingBox[2] - boundingBox[0]) / (boundingBox[3] - boundingBox[1])));
		}

		return aspectRatios;
	}
}

package animationCreator;

import java.util.ArrayList;
import java.util.List;

public class AnalyseBoundingBoxes {

	public AnalyseBoundingBoxes() {

	}

	public void Analyse(List<int[]> boundingBoxes) {

	}


	public List<Float> getAspectRatios(List<int[]> boundingBoxes) {
		List<Float> aspectRatios = new ArrayList<Float>();

		for (int[] boundingBox : boundingBoxes) {
			aspectRatios.add((float) ((boundingBox[2] - boundingBox[0]) / (boundingBox[3] - boundingBox[1])));
		}

		return aspectRatios;
	}
}

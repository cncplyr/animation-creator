package animationCreator;

import blackBox.BlackBox;
import blackBox.BlackBoxProbMotif;
import fileHandling.CSVHandler;


/**
 * This class acts as the entry point to the project.
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class AnimationCreator {
	private static AnalyseBoundingBoxes analyseBBoxes;
	private static CSVHandler csvHandler;
	private static BlackBox blackBox;

	public static void main(String[] args) {
		analyseBBoxes = new AnalyseBoundingBoxes();
		// Create our csv handler
		csvHandler = new CSVHandler();
		// TODO: Set up csv handling to point to what we want

		// Create our black box, with the data.
		try {
			blackBox = new BlackBoxProbMotif(analyseBBoxes.getAspectRatios(csvHandler.readCSVint()), 10, 1, 4, 0, 20, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Run that black box!
		blackBox.iterate(10);

		int[][] array = blackBox.getMatrixArray();
		int x = array.length;
		int y = array[0].length;
	}
}

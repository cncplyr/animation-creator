package animationCreator;

import java.util.ArrayList;
import java.util.List;

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
		/* TEST CODE */
		//
		// HashMap<String, Integer> tastyHash = new HashMap<String, Integer>(50,
		// 50);
		//
		// tastyHash.put("ABAB", 0);
		// tastyHash.put("CDAC", 1);
		// tastyHash.put("ABAB", 2);
		// tastyHash.put("DADA", 3);
		// tastyHash.put("AADA", 4);
		//
		//
		// // Print out contents of hashMap
		// System.out.println("HashMap: ");
		// Set<?> set = tastyHash.entrySet();
		// Iterator<?> i = set.iterator();
		//
		// while (i.hasNext()) {
		// Map.Entry me = (Map.Entry) i.next();
		// System.out.println(me.getKey() + " : " + me.getValue());
		// }
		/* END TEST CODE */

		analyseBBoxes = new AnalyseBoundingBoxes();
		// Create our csv handler
		csvHandler = new CSVHandler();
		// TODO: Set up csv handling to point to what we want

		// Create our black box, with the data.
		blackBox = new BlackBoxProbMotif();
		blackBox.setData(analyseBBoxes.getAspectRatios(csvHandler.readCSVint()));

		// Run that black box!
		blackBox.createAnimations();

		int[][] array = blackBox.getMatrixArray();
		int x = array.length;
		int y = array[0].length;
	}
}

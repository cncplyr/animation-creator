package animationCreator;

import java.util.ArrayList;
import java.util.List;

import metrics.Metrics;

import blackBox.BlackBox;
import blackBox.BlackBoxBruteForce;
import blackBox.BlackBoxBruteForceInt;
import blackBox.BlackBoxProbMotif;
import blackBox.BlackBoxProbMotifInt;
import blackBox.CopyOfBlackBoxBruteForce;
import fileHandling.AnimationFileHandler;
import fileHandling.CSVHandler;


/**
 * This class acts as the entry point to the project.
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class AnimationCreator {
	private static AnimationFileHandler animFH;
	private static AnalyseMetrics analyseMetrics;
	private static BlackBox blackBox;
	private static List<Metrics> metrics;

	private static int ALPHASIZE = 10;
	private static int FRAMESPERLETTER = 4;
	private static int MASKSIZE = 4;
	private static int MOTIFS = 0;
	private static int SUBSEQUENCE = 5;
	private static int ERROR = 0;

	public static void main(String[] args) {
		/* Setup */
		animFH = new AnimationFileHandler();
		analyseMetrics = new AnalyseMetrics();
		/* Get all the metrics */
		metrics = animFH.getAllMetrics();

		// List<Double> data, int alphaSize, int framesPerLetter, int maskSize,
		// int kMotifs, int subsequenceLength, int errorRange
		// Create our black box, with the data.
		try {
			 blackBox = new
			 BlackBoxProbMotif(analyseMetrics.getAspectRatios(metrics), 5, 1,
			 0, 0, 5, 0);
			// blackBox = new
			// BlackBoxProbMotifInt(analyseMetrics.getXCentroid(metrics), 10, 1,
			// 4, 0, 20, 0);
			// blackBox = new
			// BlackBoxProbMotifInt(analyseMetrics.getXEccentricity(metrics),
			// 10, 1, 4, 0, 20, 0);
			// blackBox = new
			// BlackBoxProbMotifInt(analyseMetrics.getXVelocities(metrics), 10,
			// 1, 4, 0, 20, 0);

			// Brute Force Motif Search
//			 blackBox = new BlackBoxBruteForce(generateSine(1000), ALPHASIZE,
//			 FRAMESPERLETTER, MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);
//			 blackBox = new CopyOfBlackBoxBruteForce(generateSine(1000),
//			 ALPHASIZE, FRAMESPERLETTER, MASKSIZE, MOTIFS, SUBSEQUENCE,
//			 ERROR);

//			blackBox = new BlackBoxBruteForce(analyseMetrics.getAspectRatios(metrics), ALPHASIZE, FRAMESPERLETTER, MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);


		} catch (Exception e) {
			e.printStackTrace();
		}

		// Run that black box!
		blackBox.iterate(100);

		int[][] array = blackBox.getMatrixArray();
		animFH.saveMatrixImage(array, "bf-" + ALPHASIZE + "-" + FRAMESPERLETTER + "-" + MASKSIZE + "-" + SUBSEQUENCE);

		// try {
		// for (int i = 1; i < 11; i++) {
		// System.out.println(i);
		// blackBox = new
		// BlackBoxBruteForce(analyseMetrics.getAspectRatios(metrics),
		// ALPHASIZE, i, MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);
		// blackBox.iterate(0);
		// int[][] matrixArray = blackBox.getMatrixArray();
		// animFH.saveMatrixImage(matrixArray, "bf-" + ALPHASIZE + "-" + i + "-"
		// + MASKSIZE + "-" + SUBSEQUENCE);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		generateSine(100);
	}

	private static List<Double> generateSine(int size) {
		List<Double> sine = new ArrayList<Double>();
		for (double i = 0.0d; i < (double) size; i++) {
			sine.add(Math.sin(i));
		}
		return sine;
	}
}

package animationCreator;

import java.util.List;

import metrics.Metrics;

import blackBox.BlackBox;
import blackBox.BlackBoxProbMotif;
import blackBox.BlackBoxProbMotifInt;
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

	public static void main(String[] args) {
		/* Setup */
		animFH = new AnimationFileHandler();
		analyseMetrics = new AnalyseMetrics();
		/* Get all the metrics */
		metrics = animFH.getAllMetrics();


		// Create our black box, with the data.
		try {
			 blackBox = new
			 BlackBoxProbMotif(analyseMetrics.getAspectRatios(metrics), 10, 1,
			 4, 0, 20, 0);
//			blackBox = new BlackBoxProbMotifInt(analyseMetrics.getXCentroid(metrics), 10, 1, 4, 0, 20, 0);
//			 blackBox = new
//			 BlackBoxProbMotifInt(analyseMetrics.getXEccentricity(metrics),
//			 10, 1, 4, 0, 20, 0);
			/* TODO: velocities doesn't work very well yet */
			// blackBox = new
			// BlackBoxProbMotifInt(analyseMetrics.getXVelocities(metrics), 10,
			// 1, 4, 0, 20, 0);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Run that black box!
		blackBox.iterate(100);

		int[][] array = blackBox.getMatrixArray();
		animFH.saveMatrixImage(array);
	}
}

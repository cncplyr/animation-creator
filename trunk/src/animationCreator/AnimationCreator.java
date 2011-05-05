package animationCreator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import character.CharacterState;

import metrics.Metrics;

import animation.Animation;
import animation.CharacterAnimation;
import blackBox.BlackBox;
import blackBox.BlackBoxBruteForce;
import blackBox.BlackBoxBruteForceInt;
import blackBox.BlackBoxProbMotif;
import blackBox.BlackBoxProbMotifInt;
import fileHandling.AnimationFileHandler;
import fileHandling.CSVHandler;
import fileHandling.FileHandler;
import fileHandling.FileName;
import gui.GUI;


/**
 * This class acts as the entry point to the project.
 * 
 * @author Richard Jenkin
 * @version 0.5
 * 
 */
public class AnimationCreator {
	private static FileHandler fh;
	private static AnimationFileHandler animFH;
	private static AnalyseMetrics analyseMetrics;
	private static BlackBox blackBox;
	private static List<Metrics> metrics;

	private static int ALPHASIZE = 20;
	private static int FRAMESPERLETTER = 1;
	private static int MASKSIZE = 4;
	private static int MOTIFS = 5;
	private static int SUBSEQUENCE = 20;
	private static int ERROR = 0;

	public static void main(String[] args) {
		/* Setup */
		fh = new FileHandler();
		animFH = new AnimationFileHandler();
		analyseMetrics = new AnalyseMetrics();
		/* Get all the metrics */
		metrics = animFH.getAllMetrics();

		// List<Double> data, int alphaSize, int framesPerLetter, int maskSize,
		// int kMotifs, int subsequenceLength, int errorRange
		// Create our black box, with the data.
		try {
			// blackBox = new
			// BlackBoxProbMotif(analyseMetrics.getAspectRatios(metrics),
			// ALPHASIZE, FRAMESPERLETTER,
			// MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);
			// blackBox = new
			// BlackBoxProbMotifInt(analyseMetrics.getXCentroid(metrics),
			// ALPHASIZE, FRAMESPERLETTER,
			// MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);
			// blackBox = new
			// BlackBoxProbMotifInt(analyseMetrics.getXEccentricity(metrics),
			// ALPHASIZE, FRAMESPERLETTER, MASKSIZE, MOTIFS, SUBSEQUENCE,
			// ERROR);
			// blackBox = new
			// BlackBoxProbMotifInt(analyseMetrics.getXVelocities(metrics),
			// ALPHASIZE,
			// FRAMESPERLETTER, MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);

			// Brute Force Motif Search
			// blackBox = new BlackBoxBruteForce(generateSine(1000), ALPHASIZE,
			// FRAMESPERLETTER, MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);

			blackBox = new BlackBoxBruteForce(analyseMetrics.getAspectRatios(metrics), ALPHASIZE, FRAMESPERLETTER, MASKSIZE, MOTIFS, SUBSEQUENCE, ERROR);


		} catch (Exception e) {
			e.printStackTrace();
		}

		// Run that black box!
		blackBox.iterate(100);

		int[][] array = blackBox.getMatrixArray();
		animFH.saveMatrixImage(array, "bf-" + ALPHASIZE + "-" + FRAMESPERLETTER + "-" + MASKSIZE + "-" + SUBSEQUENCE);

		/*  */
		HashMap<String, List<Integer>> hash = blackBox.getHash();
		System.out.println("Hash size: " + hash.size());
		Set<String> keys = hash.keySet();
		for (String key : keys) {
			System.out.print(key + ": ");
			for (Integer frame : hash.get(key)) {
				System.out.print(frame + ", ");
			}
			System.out.println("");
		}

		GUI g = new GUI();
		g.addAnimations(createAnimations(hash));
		g.showAnimationChooser();
	}

	private static Map<String, CharacterAnimation> createAnimations(HashMap<String, List<Integer>> hash) {
		System.out.println(hash.size());
		fh.setInputFolder("output" + File.separator + "p1");
		Map<String, CharacterAnimation> animations = new HashMap<String, CharacterAnimation>();
		Set<String> keys = hash.keySet();
		int totalFrames = FRAMESPERLETTER * SUBSEQUENCE;

		String[] keyArray = keys.toArray(new String[0]);
		for (int i = 0; i < 20; i++) {
			String key = keyArray[i];
			// for (String key : keys) {
			Integer initialFrame = hash.get(key).get(0);
			List<BufferedImage> frames = new ArrayList<BufferedImage>();

			for (int frameNumber = initialFrame; frameNumber < initialFrame + totalFrames; frameNumber++) {
				frames.add(fh.loadImage(FileName.formatFileName("frame", frameNumber, ".png")));
			}
			animations.put(key, new CharacterAnimation(null, frames, null, 0, 0, 0));
		}

		// String[] keyStrings = keys.toArray(new String[0]);
		//
		// Integer initialFrame = hash.get(keyStrings[0]).get(0);
		// List<BufferedImage> frames = new ArrayList<BufferedImage>();
		// for(int frame = initialFrame; frame < initialFrame + totalFrames;
		// frame++){
		// frames.add(fh.loadImage(FileName.formatFileName("frame", frame,
		// ".png")));
		// }
		// animations.put(keyStrings[0], new CharacterAnimation(null, frames,
		// null, 0, 0, 0));
		return animations;
	}
}

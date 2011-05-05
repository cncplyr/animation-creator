package blackBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import fileHandling.CSVHandler;

import maths.AverageFinder;
import maths.StandardDeviation;

/**
 * 
 * @author Richard Jenkin
 * @version 0.5
 *
 */
public class BlackBoxBruteForce implements BlackBox {
	/* Data Variables */
	private List<Double> data;
	private List<String> symbolData;
	HashMap<String, List<Integer>> hash;

	/* PAA & Symbolise Variables */
	private int alphaSize; // size of alphabet
	private int framesPerLetter; // size of symbolisation

	/* Mask Variables */
	private int maskSize; // number of masked vars

	/* Motif Variables */
	private int kMotifs; // Return motifs with greater than k matches
	private int subsequenceLength; // Length of motif

	/* Collision Matrix Variables */
	private int[][] matrixArray;

	/* TODO: Unknown */
	private int errorRange;


	private List<String> alphabet = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z");

	public BlackBoxBruteForce(List<Double> data, int alphaSize, int framesPerLetter, int maskSize, int kMotifs, int subsequenceLength, int errorRange)
			throws Exception {
		/* Initialise Variables */
		this.data = data;
		this.hash = new HashMap<String, List<Integer>>();
		this.alphaSize = alphaSize;
		this.framesPerLetter = framesPerLetter;
		this.maskSize = maskSize;
		this.kMotifs = kMotifs;
		this.subsequenceLength = subsequenceLength;
		this.errorRange = errorRange;

		/* Symbolise input data */
		System.out.print("Symbolising... ");
		symbolData = symboliseGaussian(data);
		System.out.println("Done!");

		int matrixSize = symbolData.size() - subsequenceLength;
		matrixArray = new int[matrixSize][matrixSize];
		for (int x = 0; x < matrixSize; x++) {
			for (int y = 0; y < matrixSize; y++) {
				matrixArray[x][y] = 0;
			}
		}

		/* Perform Checks */
		// Data length longer than single subsequence
		if (matrixSize < 0) {
			throw new Exception("Not enough data to analyse!");
		}
		// Subsequence longer than mask
		if (maskSize > subsequenceLength) {
			throw new Exception("Mask cannot be larger than a subsequence!");
		}
		// Subsequence longer than symbolised data
		if (subsequenceLength > symbolData.size()) {
			throw new Exception("Subsequence cannot be larger than symbolised data!");
		}
	}

	@Override
	public void iterate(int iterations) {
		System.out.println("Finding Motifs!");
		findMotif();
	}

	public void findMotif() {
		int finalFrame = matrixArray.length;
		// Sliding window
		for (int sequenceStart = 0; sequenceStart < finalFrame; sequenceStart++) {
			// Get the sequence we are searching for
			String searchSequence = new String();
			String currentSequence = new String();
			for (int i = sequenceStart; i < sequenceStart + subsequenceLength; i++) {
				searchSequence += symbolData.get(i);
			}
			System.out.println("Searching for: " + searchSequence);
			putIntoBucket(searchSequence, sequenceStart);
			// Search for the sequence!
			for (int searchStart = 0; searchStart < finalFrame; searchStart++) {
				// Get the current sequence
				for (int i = searchStart; i < searchStart + subsequenceLength; i++) {
					currentSequence += symbolData.get(i);
				}
				// Compare them
				if (searchSequence.equals(currentSequence)) {
					matrixArray[sequenceStart][searchStart]++;
					putIntoBucket(searchSequence, searchStart);
				}
				// Clear our current sequence for next iteration
				currentSequence = "";
			}
		}
	}

	@Override
	public int[][] getMatrixArray() {
		return matrixArray;
	}

	public HashMap<String, List<Integer>> getHash() {
		Set<String> keys = hash.keySet();
		HashMap<String, List<Integer>> kHash = new HashMap<String, List<Integer>>();
		for (String key : keys) {
			if (hash.get(key).size() > kMotifs) {
				kHash.put(key, hash.get(key));
			}
		}

		return kHash;
	}


	/**
	 * Takes a list of doubles, and symbolises it by Piecewise Aggregate
	 * Approximation, followed by labelling it with a discrete alphabet, using a
	 * normal distribution.
	 * 
	 * @param data
	 * @return
	 */
	private List<String> symboliseGaussian(List<Double> data) {
		AverageFinder avgFinder = new AverageFinder();
		StandardDeviation sdFinder = new StandardDeviation();
		List<Double> paa = new ArrayList<Double>();
		List<String> symbolData = new ArrayList<String>();
		int finalFrame = data.size() - framesPerLetter;

		// Get break points
		List<Double> breakPoints = findBreakPoints();

		// Create Piecewise Aggregate Approximation (PAA)
		int startFrame = 0;
		Double currentAvg;
		Double currentFrame;
		// Sliding window through data
		while (startFrame < finalFrame) {
			// Calculate average of current window
			currentAvg = 0.0d;
			for (int f = startFrame; f < startFrame + framesPerLetter; f++) {
				currentFrame = data.get(f);
				currentAvg += currentFrame;
			}
			currentAvg = currentAvg / framesPerLetter;

			// Store current average to PAA
			paa.add(currentAvg);

			// Move sliding window
			startFrame += framesPerLetter;
		}

		// Normalise data
		Double mean = avgFinder.findMeanDouble(paa);
		Double sd = sdFinder.findSDDouble(paa);
		System.out.println("mean: " + mean);
		System.out.println("sd: " + sd);
		for (int i = 0; i < paa.size(); i++) {
			paa.set(i, (paa.get(i) - mean) / sd);
		}

		// Symbolise data using Normal Distribution
		boolean assigned = false;
		for (Double avg : paa) {
			for (int i = 0; i < breakPoints.size(); i++) {
				if (avg <= breakPoints.get(i)) {
					symbolData.add(alphabet.get(i));
					assigned = true;
					break;
				}
			}
			if (!assigned) {
				symbolData.add(alphabet.get(breakPoints.size() - 1));
			}
			assigned = false;
		}

		// Return symbolised data
		return symbolData;
	}

	private List<Double> findBreakPoints() {
		CSVHandler csvh = new CSVHandler();
		csvh.setInputFolder("data");
		List<List<Double>> breakpoints = csvh.readCSVdouble("normalBreakPoints");
		return breakpoints.get(alphaSize - 1);
	}

	private void putIntoBucket(String key, Integer value) {
		List<Integer> bucket;
		if (hash.containsKey(key)) {
			bucket = hash.get(key);
		} else {
			bucket = new ArrayList<Integer>();
		}
		if (!bucket.contains(value)) {
			bucket.add(value);
		}
		hash.put(key, bucket);
	}

}

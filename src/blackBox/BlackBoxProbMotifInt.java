package blackBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import fileHandling.CSVHandler;

import maths.AverageFinder;
import maths.StandardDeviation;

/**
 * 
 * @author Richard Jenkin
 * @version 0.5
 *
 */
public class BlackBoxProbMotifInt implements BlackBox {
	/* Data Variables */
	List<Integer> data;
	List<String> symbolData;

	/* PAA & Symbolise Variables */
	int alphaSize; // size of alphabet
	int framesPerLetter; // size of symbolisation

	/* Mask Variables */
	int maskSize; // number of masked vars

	/* Motif Variables */
	int kMotifs; // Iterations to perform
	int subsequenceLength; // Length of motif

	/* Collision Matrix Variables */
	int[][] matrixArray;

	/* TODO: Unknown */
	int errorRange;

	List<String> alphabet;

	/**
	 * Default Constructor.
	 */
	public BlackBoxProbMotifInt() {
		this.alphaSize = 5;
		this.framesPerLetter = 4;
		this.maskSize = subsequenceLength / 2;
		this.kMotifs = 5;
		this.subsequenceLength = 5;
		this.errorRange = 5;
	}

	/**
	 * Full Constructor with all variables.
	 * 
	 * @param data
	 *            The data to analyse.
	 * @param alphaSize
	 *            The size of the alphabet to use for the symbolisation of the
	 *            input data.
	 * @param framesPerLetter
	 *            The number of frames to assign per letter.
	 * @param maskSize
	 *            The number of frames to mask.
	 * @param kMotifs
	 *            The number of times to iterate over the data. NOT USED!
	 * @param subsequenceLength
	 *            The length of subsequence to search for.
	 * @param errorRange
	 *            NOT USED!
	 * @throws Exception
	 */
	public BlackBoxProbMotifInt(List<Integer> data, int alphaSize, int framesPerLetter, int maskSize, int kMotifs, int subsequenceLength, int errorRange)
			throws Exception {
		/* Initialise Variables */
		this.data = data;
		this.alphaSize = alphaSize;
		this.framesPerLetter = framesPerLetter;
		this.maskSize = maskSize;
		this.kMotifs = kMotifs;
		this.subsequenceLength = subsequenceLength;
		this.errorRange = errorRange;
		alphabet = new ArrayList<String>();
		alphabet.add("A");
		alphabet.add("B");
		alphabet.add("C");
		alphabet.add("D");
		alphabet.add("E");
		alphabet.add("F");
		alphabet.add("G");
		alphabet.add("H");
		alphabet.add("I");
		alphabet.add("J");
		alphabet.add("K");
		alphabet.add("L");
		alphabet.add("M");
		alphabet.add("N");
		alphabet.add("O");
		alphabet.add("P");
		alphabet.add("Q");
		alphabet.add("R");
		alphabet.add("S");
		alphabet.add("T");
		alphabet.add("U");
		alphabet.add("V");
		alphabet.add("W");
		alphabet.add("X");
		alphabet.add("Y");
		alphabet.add("Z");


		/* Symbolise input data */
		symbolData = symboliseGaussian(data);
		int matrixSize = symbolData.size() - subsequenceLength;
		matrixArray = new int[matrixSize][matrixSize];

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
		try {
			for (int i = 0; i < iterations; i++) {
				findMotif(symbolData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void findMotif(List<String> symbolisedData) throws Exception {
		System.out.println("Finding motif!");
		int matrixSize = symbolisedData.size() - subsequenceLength;
		Random randomGen = new Random();
		Boolean[] mask = new Boolean[subsequenceLength];
		HashMap<String, List<Integer>> hashMap = new HashMap<String, List<Integer>>();

		/* Convert Data to Symbols */
		// Already computed in symbolise

		/* Generate Random Mask */
		System.out.println("Generating mask!");
		// Initialise the mask
		for (int maskNumber = 0; maskNumber < subsequenceLength; maskNumber++) {
			mask[maskNumber] = true;
		}
		// randomise mask (set to false)
		for (int i = 0; i < maskSize; i++) {
			int maskItem = randomGen.nextInt(subsequenceLength);
			if (!mask[maskItem]) {
				// try again
				i--;
			} else {
				mask[maskItem] = false;
			}
		}

		/* Sliding Window Hash */
		System.out.println("Sliding window!");
		String value;
		// For each starting frame
		for (int startFrame = 0; startFrame < matrixSize; startFrame++) {
			value = "";
			// get each of the frames of the correct length
			for (int subframe = 0; subframe < subsequenceLength; subframe++) {
				// apply the mask
				if (mask[subframe]) {
					value += symbolisedData.get(startFrame + subframe);
				}
			}
			System.out.println("Hashing (K, V): " + value + ", " + startFrame);
			// hash it
			putIntoBucket(hashMap, value, startFrame);
		}
	}

	public int[][] getMatrixArray() {
		return matrixArray;
	}

	/**
	 * Takes a list of integers, and symbolises it by Piecewise Aggregate
	 * Approximation, followed by labelling it with a discrete alphabet, using a
	 * normal distribution.
	 * 
	 * @param data
	 * @return
	 */
	private List<String> symboliseGaussian(List<Integer> data) {
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
				currentFrame = (double) data.get(f);
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
				symbolData.add(alphabet.get(breakPoints.size()));
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

	private void putIntoBucket(HashMap<String, List<Integer>> hash, String key, Integer value) {
		// Create a bucket
		List<Integer> bucket = new ArrayList<Integer>();
		// If bucket already exists
		if (hash.containsKey(key)) {
			System.out.println("Collision! New: " + value);
			List<Integer> oldVals = hash.get(key);
			System.out.println("Collides with " + oldVals.size() + " values");
			// Copy existing bucket into new bucket
			bucket.addAll(hash.get(key));
			// update collision matrix
			matrixArray[hash.get(key).get(0)][value] += 1;
			System.out.println(matrixArray[hash.get(key).get(0)][value]);
		}
		// Add our new value
		bucket.add(value);
		// Replace old bucket with new bucket
		hash.put(key, bucket);
	}

	@Override
	public HashMap<String, List<Integer>> getHash() {
		// TODO Auto-generated method stub
		return null;
	}
}

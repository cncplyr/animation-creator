package blackBox;

import java.util.ArrayList;
import java.util.List;

import fileHandling.CSVHandler;

import maths.AverageFinder;
import maths.StandardDeviation;

public class BlackBoxBruteForce implements BlackBox {
	/* Data Variables */
	List<Double> data;
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

	public BlackBoxBruteForce() {
		this.alphaSize = 5;
		this.framesPerLetter = 4;
		this.maskSize = subsequenceLength / 2;
		this.kMotifs = 5;
		this.subsequenceLength = 5;
		this.errorRange = 5;
	}

	public BlackBoxBruteForce(List<Double> data, int alphaSize, int framesPerLetter, int maskSize, int kMotifs, int subsequenceLength, int errorRange)
			throws Exception {
		/* Initialise Variables */
		this.data = data;
		this.alphaSize = alphaSize;
		this.framesPerLetter = framesPerLetter;
		this.maskSize = maskSize;
		this.kMotifs = kMotifs;
		this.subsequenceLength = subsequenceLength;
		this.errorRange = errorRange;

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
	public int[][] getMatrixArray() {
		return matrixArray;
	}

	@Override
	public void iterate(int iterations) {
		System.out.println("Finding Motifs!");

	}

	public void findMotif() {
		int matrixSize = symbolData.size();

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

		// Generate Alphabet
		List<String> alphabet = new ArrayList<String>();
		for (int i = 0; i < alphaSize; i++) {
			alphabet.add(Integer.toString(i));
		}

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
		csvh.setCSVFolder("data");
		List<List<Double>> breakpoints = csvh.readCSVdouble("normalBreakPoints");
		return breakpoints.get(alphaSize - 1);
	}

}

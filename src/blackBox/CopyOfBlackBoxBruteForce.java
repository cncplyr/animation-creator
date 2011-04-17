package blackBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fileHandling.CSVHandler;

import maths.AverageFinder;
import maths.StandardDeviation;

public class CopyOfBlackBoxBruteForce implements BlackBox {
	/* Data Variables */
	private List<String> symbolData;


	private int framesPerLetter; // size of symbolisation



	private int subsequenceLength; // Length of motif

	/* Collision Matrix Variables */
	private int[][] matrixArray;




	private List<String> alphabet = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z");

	public CopyOfBlackBoxBruteForce() {
		this.framesPerLetter = 4;
		this.subsequenceLength = 5;
	}

	public CopyOfBlackBoxBruteForce(List<Double> data, int alphaSize, int framesPerLetter, int maskSize, int kMotifs, int subsequenceLength, int errorRange)
			throws Exception {
		/* Initialise Variables */
		this.framesPerLetter = framesPerLetter;
		this.subsequenceLength = subsequenceLength;

		/* Symbolise input data */
		System.out.print("Symbolising... ");
		symbolData = symboliseGaussian(data);
		System.out.println("Done!");

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
			// Search for the sequence!
			for (int searchStart = 0; searchStart < finalFrame; searchStart++) {
				// Get the current sequence
				for (int i = searchStart; i < searchStart + subsequenceLength; i++) {
					currentSequence += symbolData.get(i);
				}
				// Compare them
				if (searchSequence.equals(currentSequence)) {
					matrixArray[sequenceStart][searchStart] = 5;
				}
				// Clear our current sequence for next iteration
				currentSequence = "";
			}
		}
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
		List<Double> paa = new ArrayList<Double>();
		List<String> symbolData = new ArrayList<String>();
		int finalFrame = data.size() - framesPerLetter;

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

		// Symbolise data
		for (Double avg : paa) {
			if (avg < 0.0d) {
				symbolData.add(alphabet.get(0));
			} else {
				symbolData.add(alphabet.get(1));
			}
		}

		// Return symbolised data
		return symbolData;
	}
}

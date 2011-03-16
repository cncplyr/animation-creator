package blackBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlackBoxProbMotif implements BlackBox {
	/* Data Variables */
	List<Float> data;
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

	/* Unknown */
	int errorRange;


	/**
	 * Default Constructor.
	 */
	public BlackBoxProbMotif() {
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
	public BlackBoxProbMotif(List<Float> data, int alphaSize, int framesPerLetter, int maskSize, int kMotifs, int subsequenceLength, int errorRange)
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
		symbolData = symbolise(data);
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


	/**
	 * Takes a list of floats, and symbolises it by Piecewise Aggregate
	 * Approximation, followed by labelling it with a discrete alphabet.
	 * 
	 * @param data
	 * @return
	 */
	private List<String> symbolise(List<Float> data) {
		List<Float> paa = new ArrayList<Float>();
		List<String> symbolData = new ArrayList<String>();
		int finalFrame = data.size() - framesPerLetter;
		Float high = data.get(0);
		Float low = data.get(0);

		/* Create Piecewise Aggregate Approximation (PAA) */
		int startFrame = 0;
		Float currentAvg;
		Float currentFrame;
		// Sliding window through data
		while (startFrame < finalFrame) {
			// Calculate average of current window
			currentAvg = 0.0f;
			for (int f = startFrame; f < startFrame + framesPerLetter; f++) {
				currentFrame = data.get(f);
				currentAvg += currentFrame;
				if (currentFrame > high) {
					high = currentFrame;
				} else if (currentFrame < low) {
					low = currentFrame;
				}
			}
			currentAvg = currentAvg / framesPerLetter;

			// Store current average to PAA
			paa.add(currentAvg);

			// Move sliding window
			startFrame += framesPerLetter;
		}

		/* Group into discrete symbols */
		// TODO: use gaussian for symbols
		List<String> alphabet = new ArrayList<String>();
		for (int i = 0; i < alphaSize; i++) {
			alphabet.add(Integer.toString(i));
		}

		Float size = high - low;
		Float boundary = size / alphaSize;

		for (Float avg : paa) {
			for (int i = 0; i < alphaSize; i++) {
				if (avg < low + ((i + 1) * boundary)) {
					symbolData.add(alphabet.get(i));
					break;
				}
			}
		}
		return symbolData;
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
			// TODO: Change to use data symbols, not actual data
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

	// TODO: Should these be edit-able at all after initialisation? Or should
	// each instance of this class only support one data set?
	//
	// public void setAlphaSize(int size) {
	// this.alphaSize = size;
	// }
	//
	// @Override
	// public void setData(List<Float> dataList) {
	// System.out.println("set data: " + dataList.size());
	// data = dataList;
	// symbolData = symbolise(data);
	// }
	//
	// public void setFramesPerLetter(int w) {
	// this.framesPerLetter = w;
	// }
	//
	// public void setMaskSize(int size) {
	// this.maskSize = size;
	// }
	//
	// public void setSubLength(int length) {
	// this.subsequenceLength = length;
	// }

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
}

package blackBox;

import java.util.List;

public class BlackBoxBruteForce implements BlackBox {
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

	public BlackBoxBruteForce(List<Float> data, int alphaSize, int framesPerLetter, int maskSize, int kMotifs, int subsequenceLength, int errorRange)
			throws Exception {
		/* Initialise Variables */
		this.data = data;
		this.alphaSize = alphaSize;
		this.framesPerLetter = framesPerLetter;
		this.maskSize = maskSize;
		this.kMotifs = kMotifs;
		this.subsequenceLength = subsequenceLength;
		this.errorRange = errorRange;
		//
		// /* Symbolise input data */
		// // symbolData = symbolise(data);
		// int matrixSize = symbolData.size() - subsequenceLength;
		// matrixArray = new int[matrixSize][matrixSize];
		//
		// /* Perform Checks */
		// // Data length longer than single subsequence
		// if (matrixSize < 0) {
		// throw new Exception("Not enough data to analyse!");
		// }
		// // Subsequence longer than mask
		// if (maskSize > subsequenceLength) {
		// throw new Exception("Mask cannot be larger than a subsequence!");
		// }
		// // Subsequence longer than symbolised data
		// if (subsequenceLength > symbolData.size()) {
		// throw new
		// Exception("Subsequence cannot be larger than symbolised data!");
		// }
	}

	@Override
	public int[][] getMatrixArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void iterate(int iterations) {
		System.out.println("Finding Motifs!");

	}

}

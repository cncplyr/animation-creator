package animationCreator;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import imageProcessing.BoundingBoxer;
import fileHandling.FileHandler;

/**
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class CreatorBoundingBoxes {
	private FileHandler fileHandler;
	private BoundingBoxer boundingBoxer;
	private String nameFilter;

	/**
	 * Constructor
	 */
	public CreatorBoundingBoxes() {
		fileHandler = new FileHandler();
		boundingBoxer = new BoundingBoxer();
		nameFilter = "outputImage";
	}

	/**
	 * Gets all images from the given folder, and calls boundingBoxer to get the
	 * bounding box of each image in turn, storing the (x, y) coordinates of the
	 * top left and bottom right corners of each in a list. It then returns this
	 * list.
	 * 
	 * @return
	 */
	public List<int[]> generateBoundingBoxes() {
		System.out.println("Getting bounding boxes: ");

		fileHandler.setInputFolder("output");
		List<String> imageNames = fileHandler.getAllImageNamesMatching(nameFilter);
		List<int[]> imageBoundingBoxCoords = new ArrayList<int[]>();
		BufferedImage currentImage;

		for (String name : imageNames) {
			System.out.print(name + "...");
			currentImage = fileHandler.loadImage(name);
			imageBoundingBoxCoords.add(boundingBoxer.getBoundingBox(currentImage));
			System.out.println("\tDone!");
		}
		System.out.println("Completed!");
		return imageBoundingBoxCoords;
	}
}

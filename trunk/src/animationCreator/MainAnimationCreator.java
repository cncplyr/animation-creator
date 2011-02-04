package animationCreator;


/**
 * This class acts as the entry point to the project.
 * 
 * @author cncplyr
 * @version 0.1
 * 
 */
public class MainAnimationCreator {
	private static CreatorBoundingBoxes creatorBB;

	public static void main(String[] args) {
		creatorBB = new CreatorBoundingBoxes();

		creatorBB.generateBoundingBoxes();

	}
}

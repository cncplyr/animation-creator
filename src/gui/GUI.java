package gui;

import java.util.Map;

import javax.swing.JFrame;

import animation.CharacterAnimation;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	AnimationChooser ac;

	private static int WIDTH = 1280;
	private static int HEIGHT = 720;

	public GUI() {
		// Setup
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setTitle("Animation Chooser");
		setResizable(false);

		// Initialise the animation area
		this.ac = new AnimationChooser(WIDTH, HEIGHT);

		// Make it visible!
		setVisible(true);
	}

	public static void main(String[] args) {
		new GUI();
	}

	/**
	 * Add animations to the animation chooser!
	 * 
	 * @param animations
	 *            The animations we want to look at.
	 */
	public void addAnimations(Map<String, CharacterAnimation> animations) {
		ac.setAnimations(animations);
		System.out.println("Animations added!");
	}

	/**
	 * Show the animation chooser after we've added the animations.
	 */
	public void showAnimationChooser() {
		add(ac);
		setVisible(false);
		setVisible(true);
		System.out.println("Animation chooser ready!");
	}
}

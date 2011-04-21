package gui;

import javax.swing.JFrame;

public class GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	AnimationChooser ac;

	private static int WIDTH = 1280;
	private static int HEIGHT = 720;

	public GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setTitle("Animation Chooser");
		setResizable(false);

		this.ac = new AnimationChooser();
		add(ac);
		setVisible(true);
	}

	public static void main(String[] args) {
		new GUI();
	}
}

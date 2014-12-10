import java.awt.Toolkit;

import javax.swing.JFrame;


public class DrawFrame extends JFrame {
	public DrawFrame(){
		setTitle("Convex Hull Problem");
		setSize(D_WIDTH, D_HEIGHT);
		
		int w = 0;
		int h = 0;
		setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-D_WIDTH)/2, (Toolkit.getDefaultToolkit().getScreenSize().height-D_HEIGHT)/2);
		
		add(new MyPanel());
	}
	
	public static final int D_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width - 100;
	public static final int D_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height - 100;
}

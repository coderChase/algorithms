import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;


public class ConvexHull {
	public static void main(String[] arg){
		List<Point> test = new ArrayList<Point>();
		test.add(new Point(0,0));
		test.add(new Point(1,1));
		test.add(new Point(-1,1));
		test.add(new Point(0,1));
		test.add(new Point(1,2));
		test.add(new Point(2,4));
		test.add(new Point(-2,4));
		
//		PointsSet ps = new PointsSet(test);
//		ps.grahamScan();
		
		DrawFrame frame = new DrawFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

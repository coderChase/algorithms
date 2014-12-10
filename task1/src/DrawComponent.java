import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D.Double;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;


class DrawComponent extends JComponent {
	public DrawComponent(){
		circles = new ArrayList<Ellipse2D>();
		chpoints = new ArrayList<Point>();
	}
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
//		Ellipse2D circle = new Ellipse2D.Double();
//		circle.setFrameFromCenter(100, 100, 150, 150);
//		g2.draw(circle);
		for(Ellipse2D c : circles){
			g2.draw(c);
		}
		Line2D line = new Line2D.Double();
		for(int i = 0; i < this.getWidth(); ++i){
			if(i % 50 == 0)
				line.setLine(i,0,i,5);
			if(i % 100 == 0)
				line.setLine(i,0,i,10);
			g2.draw(line);
		}
		for(int i = 0; i < this.getHeight(); ++i){
			if(i % 50 == 0)
				line.setLine(0,i,5,i);
			if(i % 100 == 0)
				line.setLine(0,i,10,i);
			g2.draw(line);
		}
		if(chpoints.size() > 1){
			g2.setStroke(new BasicStroke(2.0f));
			g2.setColor(Color.red);
			int i = 0;
			for(; i < chpoints.size()-1; ++i){
				line.setLine(chpoints.get(i).getX(), chpoints.get(i).getY(), chpoints.get(i+1).getX(), chpoints.get(i+1).getY());
				
				g2.draw(line);
			}
			line.setLine(chpoints.get(i).getX(), chpoints.get(i).getY(), chpoints.get(0).getX(), chpoints.get(0).getY());
			g2.draw(line);
			
			g2.setStroke(new BasicStroke(1.0f));
			g2.setColor(Color.black);
		}

	}
	
	public void paintCH(List<Point> p){
		if(p == null)
			return;
		chpoints = new ArrayList<Point>();
		for(Point pp : p)
			chpoints.add(pp);
		repaint();
	}
	
	public Ellipse2D find(Point2D p){
		for(Ellipse2D c : circles){
			if(c.contains(p)) return c;
		}
		return null;
	}
	public void add(Point2D p){
		double x = p.getX();
		double y = p.getY();
		Ellipse2D c = new Ellipse2D.Double();
		c.setFrameFromCenter(x, y, x+RADIUS, y+RADIUS);
		circles.add(c);
		repaint();
	}
	public void remove(Ellipse2D c){
		if(c == null) return;
		circles.remove(c);
		repaint();
	}
	public void clear(){
		circles = new ArrayList<Ellipse2D>();
		chpoints = new ArrayList<Point>();
		repaint();
	}
	private List<Ellipse2D> circles;
	private List<Point> chpoints;
	private final double RADIUS = 5;
}

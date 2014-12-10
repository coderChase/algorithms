import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


class MyPanel extends JPanel {
	public MyPanel(){
		points = new ArrayList<Point>();
		setLayout(new BorderLayout());
		
		eastPanel = new JPanel();
		panel = new JPanel();
		eastPanel.setPreferredSize(new Dimension(150,300));
		panel.setLocation(0,0);
		
		panel.setLayout(new GridLayout(3,1));
		
		ActionListener listener = new BAction();
		addButton("Brute Force", listener);
		addButton("Graham Scan", listener);
		addButton("Devide&Conquer", listener);
		
		genPointPanel = new JPanel();
		genPointPanel.setBorder(BorderFactory.createTitledBorder("Generate points"));
		JButton button = new JButton("Generate");
		button.addActionListener(listener);
		JButton clear = new JButton("Clear");
		clear.addActionListener(listener);
		genPointPanel.setPreferredSize(new Dimension(150,150));
		genePointLabel = new JLabel("Input a number:");
		genePointNum = new JTextField("1000");
		genePointNum.setPreferredSize(new Dimension(140,20));
		
		genPointPanel.add(genePointLabel);
		genPointPanel.add(genePointNum);
		genPointPanel.add(button);
		genPointPanel.add(clear);
		
		
		eastPanel.add(panel);
		eastPanel.add(genPointPanel);
		
		
		drawCom = new DrawComponent();
		drawCom.addMouseListener(new MouseHandler());
		
		JPanel tPanel = new JPanel();
		timeCost = new JLabel();
		JButton analysis = new JButton("Performace Analysis");
		analysis.addActionListener(listener);
		tPanel.add(analysis);
		tPanel.add(timeCost);
		
		add(eastPanel, BorderLayout.EAST);
		add(drawCom, BorderLayout.CENTER);
		add(tPanel, BorderLayout.SOUTH);
	}
	private void addButton(String label, ActionListener listener){
		JButton button = new JButton(label);
		button.addActionListener(listener);
		panel.add(button);
		button.setPreferredSize(new Dimension(150,140));
	}
	private class BAction implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String commend = e.getActionCommand();
			if(commend.equals("Graham Scan")){
				long begin = System.currentTimeMillis();
				
				GrahamScan ps = new GrahamScan(points);
				drawCom.paintCH(ps.grahamScan());
				
				long end = System.currentTimeMillis();
				timeCost.setText("The run time of "+commend+" is: "+(end - begin)+"ms.");
			} else if(commend.equals("Brute Force")){
				long begin = System.currentTimeMillis();
				
				BruteForce bf = new BruteForce(points);
				drawCom.paintCH(bf.bruteForce());
				
				long end = System.currentTimeMillis();
				timeCost.setText("The run time of "+commend+" is: "+(end - begin)+"ms.");
			}else if(commend.equals("Devide&Conquer")){
				long begin = System.currentTimeMillis();
				
				DevideConquer dc = new DevideConquer(points);
				drawCom.paintCH(dc.devideAndConquer());
				
				long end = System.currentTimeMillis();
				timeCost.setText("The run time of "+commend+" is: "+(end - begin)+"ms.");
			} else if(commend.equals("Generate")){
				try{
					int i = Integer.parseInt(genePointNum.getText());
					randomPoins(i);
				} catch(Exception ex){
					JOptionPane.showMessageDialog(null,"Please input a valid number.");
					genePointNum.setText("1000");
				}
			} else if(commend.equals("Clear")){
				points = new ArrayList<Point>();
				drawCom.clear();
				genePointNum.setText("1000");
				timeCost.setText("");
			}else if(commend.equals("Performace Analysis")){
				timeCost.setText("Performace Analysis is begins.");
				performaceAnalysis();
				timeCost.setText("Performace Analysis is done.");
			}
		}
		
	}
	private class MouseHandler extends MouseAdapter{
		public void mousePressed(MouseEvent event){
			if(drawCom.find(event.getPoint()) == null) {
				drawCom.add(event.getPoint());
				points.add(new Point((int) event.getPoint().getX(), (int) event.getPoint().getY()));
			}
		}
		public void mouseClicked(MouseEvent event){
			Ellipse2D e = drawCom.find(event.getPoint());
			if(e != null && event.getClickCount() >= 2) {
				drawCom.remove(e);
				removePoint((int)e.getCenterX(), (int)e.getCenterY());
			}
		}
	}
	
	private void performaceAnalysis(){
		int count = 30; //each one is performed for count times.
		int bfcost = 0;
		int gscost = 0;
		int dccost = 0;
		long begin,end;
		FileWriter fw;
		try {
			fw = new FileWriter("performance_analysis.txt");
			
			fw.write("number\tBrute Force\tGraham-Scan\tDevide and Conquer\n");
			for(int i = 1; i < 20; ++i){
				timeCost.setText("Analysing a set of "+i*1000+" random points.");
				System.out.println("Analysing a set of "+i*1000+" random points.");
				bfcost = 0;
				gscost = 0;
				dccost = 0;
				for(int j = 0; j < count; ++j){
					randomPoinsForAnalysis(i * 1000);
					BruteForce bf = new BruteForce(points);
					begin = System.currentTimeMillis();
					bf.bruteForce();
					end = System.currentTimeMillis();
					bfcost += end-begin; 
					
					GrahamScan gs = new GrahamScan(points);
					begin = System.currentTimeMillis();
					gs.grahamScan();
					end = System.currentTimeMillis();
					gscost += end-begin; 
					
					DevideConquer dc = new DevideConquer(points);
					begin = System.currentTimeMillis();
					dc.devideAndConquer();
					end = System.currentTimeMillis();
					dccost += end-begin; 
					
				}
				fw.write(i*1000+"\t"+bfcost/count);
				fw.write("\t"+gscost/count);
				fw.write("\t"+dccost/count+"\n");
			}
			for(int i = 2; i <= 10; ++i){
				timeCost.setText("Analysing a set of "+i*10000+" random points.");
				System.out.println("Analysing a set of "+i*10000+" random points.");
				bfcost = 0;
				gscost = 0;
				dccost = 0;
				for(int j = 0; j < count; ++j){
					randomPoinsForAnalysis(i * 10000);
					BruteForce bf = new BruteForce(points);
					begin = System.currentTimeMillis();
					bf.bruteForce();
					end = System.currentTimeMillis();
					bfcost += end-begin; 
					
					GrahamScan gs = new GrahamScan(points);
					begin = System.currentTimeMillis();
					gs.grahamScan();
					end = System.currentTimeMillis();
					gscost += end-begin; 
					
					DevideConquer dc = new DevideConquer(points);
					begin = System.currentTimeMillis();
					dc.devideAndConquer();
					end = System.currentTimeMillis();
					dccost += end-begin; 
					
				}
				fw.write(i*10000+"\t"+bfcost/count);
				fw.write("\t"+gscost/count);
				fw.write("\t"+dccost/count+"\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void removePoint(int x, int y){
		for(Point p : points){
			if(p.getX() == x && p.getY() == y){
				points.remove(p);
				return;
			}
		}
	}
	public void randomPoinsForAnalysis(int count){
		points = new ArrayList<Point>();
		Random random = new Random();
		int x,y;
		int min = 0;
		int max = 1000;
		for(int i = 0; i < count; ++i){
			x = randomNum(min, max, random);
			y = randomNum(min, max, random);
			points.add(new Point(x, y));
		}
	}
	public void randomPoins(int count){
		Random random = new Random();
		int x,y;
		Point2D p = new Point2D.Double();
		int min = 15;
		int max = 500;
		for(int i = 0; i < count; ++i){
			x = randomNum(min, drawCom.getWidth()-15, random);
			y = randomNum(min, drawCom.getHeight()-15, random);
			p.setLocation(x, y);
			drawCom.add(p);
			points.add(new Point(x, y));
		}
	}
	
	public int randomNum(int min, int max, Random r){
		return r.nextInt(max)%(max-min+1) + min;
	}
	
	private JPanel panel;
	private JLabel timeCost;
	private DrawComponent drawCom;
	private List<Point> points;
	private JPanel genPointPanel;
	private JPanel eastPanel;
	private JLabel genePointLabel;
	private JTextField genePointNum;
}

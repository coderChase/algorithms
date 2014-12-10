import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


class GrahamScan {
	private List<Point> points;
	

	GrahamScan(List<Point> points){
		this.setPoints(points);
	}
	
	public List<Point> grahamScan(){
		Stack<Point> stack = new Stack<Point>();
		if(points.size() == 1)
			return new ArrayList<Point>();
		List<Point> sPoints = getSortedPoints();
		if(sPoints.size() < 3)
			return sPoints;
		
		stack.push(sPoints.get(0));
		stack.push(sPoints.get(1));
		stack.push(sPoints.get(2));
		for(int i=3; i < sPoints.size(); ++i){
			while(crossProduct(sPoints.get(i), stack.peek(), getNextToTop(stack)) >= 0)
				stack.pop(); 
			stack.push(sPoints.get(i));
		}
		List<Point> result = new ArrayList<Point>();
		for(Point p : stack){
			result.add(p);
		}
		return result;
	}
	
	public List<Point> getSortedPoints(){
		if(points == null || points.size() == 0)
			return null;
		List<Point> sortedSet = new ArrayList<Point>();
		List<Point> result = new ArrayList<Point>(); 
		sortedSet.add(points.get(0));
		for(int i=1; i < points.size(); ++i)
			if(points.get(i).getY() < sortedSet.get(0).getY()){
				sortedSet.add(sortedSet.get(0));
				sortedSet.set(0, points.get(i));
			} else
				sortedSet.add(points.get(i));
		sortedSet = quickSortPolarAngle(sortedSet, sortedSet.get(0), 1, sortedSet.size()-1);
		result.add(sortedSet.get(0));
		result.add(sortedSet.get(1));
		Point s0 = sortedSet.get(0);
		for(int i = 2; i < sortedSet.size(); ++i){
			Point r = result.get(result.size()-1);
			Point s = sortedSet.get(i);
			if(comparePolarAngle(r, s, s0)==0){
				if((s.getX()-s0.getX())*(s.getX()-s0.getX()) > (r.getX()-s0.getX())*(r.getX()-s0.getX()))
					result.set(result.size()-1, s);
			} else{
				result.add(s);
			}
		}
		return result;
	}
	
	public List<Point> quickSortPolarAngle(List<Point> pList, Point p0, int start, int end) {
		int i = start;
		int j = end;
		Point key = pList.get(start);
		
		while(i < j){
			while(i < j && comparePolarAngle(pList.get(j), key, p0) >= 0)
				--j;
			if(i < j)
				pList.set(i,pList.get(j));
			while(i < j && comparePolarAngle(pList.get(i), key, p0) <= 0)
				++i;
			if(i < j)
				pList.set(j, pList.get(i));
		}
		
		pList.set(i, key);
		if(i-start > 1)
			pList = quickSortPolarAngle(pList, p0, start, i-1);
		if(end - j > 1)
			pList = quickSortPolarAngle(pList, p0, j+1, end);
		
		return pList;
	}
	
	public int comparePolarAngle(Point p1, Point p2, Point p0){
		int result = crossProduct(p1, p2, p0);
		if(result > 0)
			return -1; //p1 < p2
		else if(result < 0)
			return 1; //p1 > p2
		
		if(result == 0){ //p1 p2 on the same line
			if(p1.getX()-p0.getX() > 0 && p2.getX() - p0.getX() < 0)
				return -1;
			else if(p1.getX()-p0.getX() < 0 && p2.getX() - p0.getX() > 0)
				return 1;
		}
			
		return 0; //p1 == p2
	}
	
	public int crossProduct(Point p1, Point p2, Point p0){
		return (p1.getX()-p0.getX())*(p2.getY()-p0.getY())-(p2.getX()-p0.getX())*(p1.getY()-p0.getY());
	}
	
	public Point getNextToTop(Stack<Point> stack){
		Point t = stack.pop();
		Point p = stack.peek();
		stack.push(t);
		return p;
	}
	
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	public List<Point> getPoints() {
		return points;
	}
}

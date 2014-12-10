import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


class DevideConquer {
	private List<Point> points;
	public DevideConquer(List<Point> p){
		this.points = p;
	}
	
	public List<Point> devideAndConquer(){
		return devideConquer(points);
	} 
	
	public List<Point> devideConquer(List<Point> ps){
		Point minX = ps.get(0);
		Point maxX = ps.get(0);
		for(Point p : ps){
			if(p.getX() > maxX.getX())
				maxX = p;
			if(p.getX() < minX.getX())
				minX = p;
		}
		List<Point> left = new ArrayList<Point>();
		List<Point> right = new ArrayList<Point>();
		int middle = (maxX.getX() + minX.getX()) / 2;
		for(Point p : ps){
			if(p.getX() <= middle)
				left.add(p);
			else
				right.add(p);
		}
		if(left.size() == ps.size()){
			List<Point> tt = new ArrayList<Point>();
			Point minY = ps.get(0);
			Point maxY = ps.get(0);
			for(Point p : ps){
				if(p.getY() > maxY.getY())
					maxY = p;
				if(p.getY() < minY.getY())
					minY = p;
			}
			tt.add(maxY);
			tt.add(minY);
			return tt;
		}
		if(left.size() > 1)
			left = devideConquer(left);
		if(right.size() > 1)
			right = devideConquer(right);
			
		//conquer
		int leftSumX = 0;
		int leftSumY = 0;
		for(Point p : left){
			leftSumX += p.getX();
			leftSumY += p.getY();
		}
		Point midOfLeft = new Point(leftSumX/left.size(), leftSumY/left.size());
		
		//sort left by midOfLeft
		int i = 0;
		if(left.size() >= 2){
			Point pp;
			int j = 1;
			while(compare(midOfLeft, left.get(j), left.get(i)) >= 0){
				i = j;
				j = (i==left.size()-1?0:i+1);
			}
			for(i = 0; i < j; ++i){
				pp = left.get(0);
				left.remove(0);
				left.add(pp);
			}
		}
		
		
		int rightMinY = 0;
		int rightMaxY = 0;
		Point min = right.get(0);
		Point max = right.get(0);
		for(i =0; i < right.size();++i){
			if(compare(midOfLeft, right.get(i), min) < 0){
				min = right.get(i);
				rightMinY = i;
			}
			if(compare(midOfLeft, right.get(i), max) > 0){
				max = right.get(i);
				rightMaxY = i;
			}
		}
		List<Point> rightLeft = new ArrayList<Point>();
		List<Point> rightRight = new ArrayList<Point>();
		
		i = rightMinY;
		while(i != rightMaxY){
			rightRight.add(right.get(i));
			i = (i+1)%right.size();
		}
		rightRight.add(right.get(rightMaxY));
		
		i = (rightMinY==0?right.size()-1:rightMinY-1);
		while(i != rightMaxY){
			rightLeft.add(right.get(i));
			i = (i==0?right.size()-1:i-1);
		}
		
		if(left.size() == 1){
			List<Point> result = rightRight;
			result.add(left.get(0));
			return result;
		}
		
		List<Point> merge = new ArrayList<Point>();
		int pLeft = 0;
		int pRightRight = 0;
		int pRightLeft = 0;
		while(pLeft != left.size() && pRightRight != rightRight.size() && pRightLeft != rightLeft.size()){
			if(compare(midOfLeft, left.get(pLeft), rightRight.get(pRightRight)) <= 0){
				merge.add(left.get(pLeft));
	
				if(compare(midOfLeft, left.get(pLeft), rightLeft.get(pRightLeft)) > 0){
					merge.set(merge.size()-1, rightLeft.get(pRightLeft));
					++pRightLeft;
				} else{
					++pLeft;
				}
			} else {
				merge.add(rightRight.get(pRightRight));
				
				if(compare(midOfLeft, rightRight.get(pRightRight), rightLeft.get(pRightLeft)) >= 0){
					merge.set(merge.size()-1, rightLeft.get(pRightLeft));
					++pRightLeft;
				} else {
					++pRightRight;
				}
			}
		}
		List<Point> temp1, temp2;
		int pTemp1, pTemp2;
		if(pLeft == left.size()){
			temp1 = rightRight;
			pTemp1 = pRightRight;
			temp2 = rightLeft;
			pTemp2 = pRightLeft;
		} else if(pRightRight == rightRight.size()){
			temp1 = left;
			pTemp1 = pLeft;
			temp2 = rightLeft;
			pTemp2 = pRightLeft;
		} else{
			temp1 = left;
			pTemp1 = pLeft;
			temp2 = rightRight;
			pTemp2 = pRightRight;
		}
		
		while(pTemp1 != temp1.size() && pTemp2 != temp2.size()){
			if(compare(midOfLeft, temp1.get(pTemp1), temp2.get(pTemp2)) <= 0){
				merge.add(temp1.get(pTemp1));
				++pTemp1;
			} else {
				merge.add(temp2.get(pTemp2));
				++pTemp2;
			}
		}
		if(pTemp1 == temp1.size())
			for(int n=pTemp2; n < temp2.size(); ++n)
				merge.add(temp2.get(n));
		else
			for(int n=pTemp1; n < temp1.size(); ++n)
				merge.add(temp1.get(n));
	
		if(merge.size() <= 2){
			return merge;
		}
		
		Stack<Point> stack = new Stack<Point>();
		stack.push(midOfLeft);
		stack.push(merge.get(0));
		stack.push(merge.get(1));
		for(i=2; i < merge.size(); ++i){
			
			while(stack.size()>=2 && crossProduct(merge.get(i), stack.peek(), getNextToTop(stack)) <= 0){
				stack.pop();
				if(stack.size() == 1){
					int m  = 0;
				}
			}
			stack.push(merge.get(i));
		}
		i = 1;
		if(right.size() > 2){
			while(i < stack.size() && crossProduct(stack.get(i+1), stack.get(i), stack.get(stack.size()-1)) <= 0)
				++i;
		}
		List<Point> result = new ArrayList<Point>();
		for(int n = i; n < stack.size(); ++n)
			result.add(stack.get(n));
		return result;
	}
	
	private int compare(Point o, Point x, Point y){
		Point xx = new Point(x.getX()-o.getX(), x.getY()-o.getY());
		Point yy = new Point(y.getX()-o.getX(), y.getY()-o.getY());
		int quadrantX = quadrant(xx), quadrantY = quadrant(yy);
		if(quadrantX == 0)
			return -1;
		if(quadrantY == 0)
			return 1;
		if(quadrantX != quadrantY)
			return quadrantX > quadrantY ? 1 : -1;
		
		//quadrantX == quadrantY
		if(quadrantX == 2 || quadrantX == 4){ //compare (sinX)2 and (sinY)2, in 1 or 3 quadrant, if (sinX)2 > (sinY)2, then x > y
			return compareSinSquare(xx, yy);
		} else {                              //compare (sinX)2 and (sinY)2, in 2 or 4 quadrant, if (sinX)2 > (sinY)2, then x < y
			int c = compareSinSquare(xx, yy);
			return -c;
		}
	}
	private int quadrant(Point p){
		if(p.getX()>=0 && p.getY()>0)
			return 1;
		if(p.getX()>0 && p.getY()<=0)
			return 2;
		if(p.getX()<=0 && p.getY()<0)
			return 3;
		if(p.getX()<0 && p.getY()>=0)
			return 4;
		return 0;
	}
	
	private int compareSinSquare(Point x, Point y){ //if (x2*y1-x1*y2)*(x2*y1 + x1*y2) > 0 then (sinX)2 > (sinY)2
 		int x1 = x.getX();
		int y1 = x.getY();
		int x2 = y.getX();
		int y2 = y.getY();
		
		if((x2*y1-x1*y2) > 0 && (x2*y1 + x1*y2) > 0) //Avoid multiplication of large numbers, prevent overflow
			return 1;
		if((x2*y1-x1*y2) < 0 && (x2*y1 + x1*y2) < 0) //Avoid multiplication of large numbers, prevent overflow
			return 1;
		if((x2*y1-x1*y2) > 0 && (x2*y1 + x1*y2) < 0)
			return -1;
		if((x2*y1-x1*y2) < 0 && (x2*y1 + x1*y2) > 0)
			return -1;
		if((x2*y1-x1*y2) == 0 && (x2*y1 + x1*y2) == 0)
			return 0;
		return 0;
	}
	public List<Point> getPoints() {
		return points;
	}

	public void setPoints(List<Point> points) {
		this.points = points;
	}
	public int crossProduct(Point p1, Point p2, Point p0){
		return (p1.getX()-p0.getX())*(p2.getY()-p0.getY())-(p2.getX()-p0.getX())*(p1.getY()-p0.getY());
	}
	public Point getNextToTop(Stack<Point> stack){
		return stack.get(stack.size()-2);
	}
}

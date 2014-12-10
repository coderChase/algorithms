import java.util.ArrayList;
import java.util.List;


class BruteForce {
	private List<Point> points;
	public BruteForce(List<Point> points){
		this.points = points;
	}
	
	public List<Point> bruteForce(){
		
		if(points.size() < 4)
			return points;
		Point min = points.get(0);
		int m = 0;
		List<Point> temp = new ArrayList<Point>();
		Point p;
		for(int i = 0; i < points.size(); ++i){
			p = points.get(i);
			temp.add(p);
			if(min.getY() > p.getY()){
				min = p;
				m = i;
			}
		}
		
		int b, c, d;
		Point pa = min, pb, pc, pp;
		for(b = 0; b < temp.size(); ++b){
			if(b == m || temp.get(b) == null)
				continue;
			for(c = 0; c < temp.size(); ++c){
				if(c == m || c == b || temp.get(c) == null)
					continue;
				for(d = 0; d < temp.size(); ++d){
					if(d == m || d == b || d == c || temp.get(d) == null)
						continue;
					pb = temp.get(b);
					pc = temp.get(c);
					pp = temp.get(d);
					
					if(isInTriangle(pa, pb, pc, pp)){
						temp.set(d, null);
					}
				}
			}
		}
		List<Point> sort = new ArrayList<Point>();
		for(Point po : temp){
			if(po != null)
				sort.add(po);
		}
		if(sort.size() <3)
			return sort;
		
		sort = quickSortX(sort, 0, sort.size()-1);
		List<Point> up = new ArrayList<Point>();
		List<Point> down = new ArrayList<Point>();
		
		for(int i = 1; i < (sort.size()-1); ++i){
			if(pointToLine(sort.get(i), sort.get(0), sort.get(sort.size()-1)) >=0)
				up.add(sort.get(i));
			else
				down.add(sort.get(i));
		}
		List<Point> result = new ArrayList<Point>();
		result.add(sort.get(0));
		for(int i=0; i < up.size(); ++i)
			result.add(up.get(i));
		result.add(sort.get(sort.size()-1));
		for(int i=down.size()-1; i > -1; --i)
			result.add(down.get(i));
		
		return result;
	}
	
	public boolean isInTriangle(Point pa, Point pb, Point pc, Point pp) {
		if(pointToLine(pa,pb,pc) == 0){
			if(pointToLine(pp,pa,pb) == 0){
				if(pa.getX() == pb.getX() && pb.getX() == pc.getX())
					return !(pp.getY()<pa.getY()&&pp.getY()<pb.getY()&&pp.getY()<pc.getY()
						|| pp.getY()>pa.getY()&&pp.getY()>pb.getY()&&pp.getY()>pc.getY());
				return !(pp.getX()<pa.getX()&&pp.getX()<pb.getX()&&pp.getX()<pc.getX()
						|| pp.getX()>pa.getX()&&pp.getX()>pb.getX()&&pp.getX()>pc.getX());
			} 
						
		}
		else{
			if(((pointToLine(pp,pa,pb)*pointToLine(pc,pa,pb))>=0) 
					&& ((pointToLine(pp,pa,pc)*pointToLine(pb,pa,pc))>=0)
					&& ((pointToLine(pp,pb,pc)*pointToLine(pa,pb,pc))>=0))
				return true;
		}
		return false;
	}
	
	public List<Point> quickSortX(List<Point> pList, int start, int end) {
		int i = start;
		int j = end;
		Point key = pList.get(start);
		
		while(i < j){
			while(i < j && pList.get(j).getX() >= key.getX())
				--j;
			if(i < j)
				pList.set(i,pList.get(j));
			while(i < j && pList.get(i).getX() <= key.getX())
				++i;
			if(i < j)
				pList.set(j, pList.get(i));
		}
		
		pList.set(i, key);
		if(i-start > 1)
			pList = quickSortX(pList, start, i-1);
		if(end - j > 1)
			pList = quickSortX(pList, j+1, end);
		
		return pList;
	}
	public int pointToLine(Point p, Point l1, Point l2){
		int r = (l1.getY()-l2.getY())*(p.getX()-l1.getX())-(l1.getX()-l2.getX())*(p.getY()-l1.getY());
		if(r == 0)
			return 0;
		return r > 0 ? 1 : -1;
	}
}

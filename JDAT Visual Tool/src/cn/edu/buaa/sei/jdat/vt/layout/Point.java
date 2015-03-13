package cn.edu.buaa.sei.jdat.vt.layout;

public class Point {
	
	public double x, y;
	
	public Point() {
		this(0.0, 0.0);
	}
	
	public Point(Point p) {
		this(p.x, p.y);
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void set(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void add(Point p) {
		this.x += p.x;
		this.y += p.y;
	}
	
	public void multiply(double k) {
		this.x *= k;
		this.y *= k;
	}
	
	public double getLengthSquare() {
		return x * x + y * y;
	}
	
	public static Point minus(Point p1, Point p2) {
		Point rst = new Point(p1);
		rst.x -= p2.x;
		rst.y -= p2.y;
		return rst;
	}

}

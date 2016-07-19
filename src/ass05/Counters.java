package ass05;

import lab05.lost_updates.UnsafeCounter;

public class Counters {

	private UnsafeCounter c1;
	private UnsafeCounter c2;
	private UnsafeCounter c3;
	private UnsafeCounter c4;
	
	public Counters(int c1base, int c2base, int c3base, int c4base) {
		super();
		this.c1 = new UnsafeCounter(c1base);
		this.c2 = new UnsafeCounter(c2base);
		this.c3 = new UnsafeCounter(c3base);
		this.c4 = new UnsafeCounter(c4base);
	}
	
	public UnsafeCounter getC1() {
		return c1;
	}
	
	public UnsafeCounter getC2() {
		return c2;
	}
	
	public UnsafeCounter getC3() {
		return c3;
	}
	
	public UnsafeCounter getC4() {
		return c4;
	}
	
}

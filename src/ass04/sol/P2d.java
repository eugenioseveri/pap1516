package ass04.sol;

public class P2d {

    private final float x,y;

    public P2d(float x,float y){
        this.x=x;
        this.y=y;
    }

    public float getX(){
    	return x;
    }
    
    public float getY(){
    	return y;
    }

    public P2d sum(V2d v){
        return new P2d(x+v.getX(),y+v.getY());
    }

    public static float distance(P2d p0, P2d p1){
    	return new V2d(p0,p1).module();
    }
    
    public String toString(){
        return "P2d("+x+","+y+")";
    }

	public boolean equals(Object obj){
		if (obj instanceof P2d){
			P2d p = (P2d) obj;
			return p.x == x && p.y == y;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return (toString()).hashCode();
	}
}

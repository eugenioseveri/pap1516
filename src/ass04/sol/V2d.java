package ass04.sol;

public class V2d  {

    private final float x,y;

    public V2d(float x,float y){
        this.x = x;
        this.y = y;
    }

    public V2d(double rad){
        this.x = (float) Math.sin(rad);
        this.y = (float) Math.cos(rad);
    }

    public V2d(P2d p1, P2d p0){
    	x = p1.getX() - p0.getX();
    	y = p1.getY() - p0.getY();
    }
    
    public V2d sum(V2d v){
        return new V2d(x+v.x,y+v.y);
    }

    public V2d mul(float k){
        return new V2d(k*x,k*y);
    }

    public float getX(){
    	return x;
    }
    
    public float getY(){
    	return y;
    }
    
    public float module(){
        return (float)Math.sqrt(x*x+y*y);
    }

    public String toString(){
        return "V2d("+x+","+y+")";
    }
    
	public boolean equals(Object obj){
		if (obj instanceof P2d){
			V2d v = (V2d) obj;
			return v.x == x && v.y == y;
		} else {
			return false;
		}
	}
	
	public int hashCode() {
		return toString().hashCode();
	}
}

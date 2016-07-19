package ass04.sol;

import ass04.*;
import static ass04.TextLib.Color;

public class BouncingWord extends Thread {
	
	private P2d pos;
	private int oldX, oldY;
	private String text;
	private V2d direction;
	private float speed;
	private Screen screen;
	private long period = 20; /* 20 ms = 50 frame al secondo */
	private String blank;
	private Color color;
	
	public BouncingWord(String text, int x, int y, V2d direction, float speed, Color c, Screen s){
		this.text = text;
		this.pos = new P2d(x,y);
		this.direction = direction;
		this.speed = speed;
		this.screen = s;
		this.color = c;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++){
			sb.append(" ");
		}
		blank = sb.toString();
	}
	
	public void run(){
		TextLib lib = TextLibFactory.getInstance();
		long lastTime = System.currentTimeMillis();
	
		oldX = Math.round(pos.getX());;
		oldY = Math.round(pos.getY());;
		
		while (true){
			long current = System.currentTimeMillis();
			int elapsed = (int)(current - lastTime);
			
			pos = pos.sum(direction.mul(speed*elapsed));
			checkBounds();
			
			lib.writeAt(oldX, oldY, blank);
			int x = Math.round(pos.getX());
			int y = Math.round(pos.getY());
			lib.writeAt(x, y, text, color);			
			oldX = x;
			oldY = y;
			
			long dt = System.currentTimeMillis() - current;
			if (dt < period){
				waitTime(period-dt);
			}
			lastTime = current;
		}
	}
	
	
	private void waitTime(long ms){
		try {
			Thread.sleep(ms);
		} catch (Exception ex){}
	}
	
	private void checkBounds(){
		if (pos.getX() < screen.getMinX()){
			pos = new P2d(screen.getMinX(), pos.getY());
			direction = new V2d(-direction.getX(), direction.getY());
		} else if (pos.getX() > screen.getMaxX()){
			pos = new P2d(screen.getMaxX(), pos.getY());
			direction = new V2d(-direction.getX(), direction.getY());
		} 
		
		if (pos.getY() < screen.getMinY()){
			pos = new P2d(pos.getX(),screen.getMinY());
			direction = new V2d(direction.getX(), -direction.getY());
		} else if (pos.getY() > screen.getMaxY()){
			pos = new P2d(pos.getX(),screen.getMaxY());
			direction = new V2d(direction.getX(), -direction.getY());
		} 
	}

}

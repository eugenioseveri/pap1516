package ass03;

public class WordLen {

	final private String word;
	final private int len;
	
	public WordLen(String w, int l){
		this.word = w;
		this.len = l;
	}
	
	public String getWord(){
		return word;
	}
	
	public int getLength(){
		return len;
	}
}

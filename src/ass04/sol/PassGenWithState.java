package ass04.sol;

/**
 * Password generator with state (incremental password generator)
 * 
 * @author aricci
 *
 */
public class PassGenWithState {
	private int nDigits;
	private char[] currentPwd;
	
	public PassGenWithState(int nDigits, long startValue){
		this.nDigits = nDigits;
	
		currentPwd = new char[nDigits];
		for (int i = 0; i < nDigits; i++){
			currentPwd[i] = (char) 32;
		}
	
		int i = 0;	
		long v = startValue;
		while (v > 0){
			currentPwd[i] = (char) ((v % 96) + 32);
			v = v / 96;
			i++;
		}
	}

	public char[] getCurrent(){
		return currentPwd;
	}
	
	public void next(){
		int i = 0;	
		while (i < nDigits){
			currentPwd[i] = (char)(currentPwd[i] + 1);
			if (currentPwd[i] <= 127){
				break;
			} else {
				currentPwd[i] = 32; 
				i++;
			}
		}
	}	
	
	public static String toStringVal(char[] pass){
		StringBuffer sb = new StringBuffer("[");
		for (char p: pass){
			sb.append((int)p+" ");
		}
		sb.append("]");
		return sb.toString();
	}
	
}

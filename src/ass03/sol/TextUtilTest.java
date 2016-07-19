package ass03.sol;

import java.util.Optional;

public class TextUtilTest {
	public static void main(String[] args) {
		String text = "aa bb aa b ccc b dddd ccc aa";

		TextUtil.getWordsLength(text).forEach(System.out::println);
	
		Optional<String> max1 = TextUtil.getWordWithMaxLen("   ");
		System.out.println(max1);

		Optional<String> max2 = TextUtil.getWordWithMaxLen(text);
		System.out.println(max2.get());
		
		System.out.println(TextUtil.getWordFreq(text, "xxx"));
		System.out.println(TextUtil.getWordFreq(text, "aa"));
		
		TextUtil.getWordsPos("   ").forEach(System.out::println);
		TextUtil.getWordsPos(text).forEach(System.out::println);
	}
	

}

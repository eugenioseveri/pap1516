package ass03;

import static ass03.TextUtils.getWordsLength;
import static ass03.TextUtils.getWordWithMaxLen;
import static ass03.TextUtils.getWordFreq;
import static ass03.TextUtils.getWordsPos;

import java.util.List;
import java.util.Optional;

public class TextUtilTest {

	public static void main(String[] args) {
		final String testString1 = "Hello from the.other:side";
		final String testString2 = "Hello from the.other:side from";
		List<WordLen> test1 = getWordsLength(testString1);
		for(WordLen item: test1) {
			System.out.println(item.getWord() + " " + item.getLength());
		}
		Optional<String> test2 = getWordWithMaxLen(testString1);
		System.out.println(test2);
		int test3 = getWordFreq(testString2, "from");
		System.out.println(test3);
		List<WordPos> test4 = getWordsPos(testString2);
		for(WordPos item: test4) {
			System.out.println(item.getWord() + " " + item.getPos());
		}
	}

}

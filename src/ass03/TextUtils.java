package ass03;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.Comparator.comparing;

public class TextUtils {
	
	private static final String delimiters = " |\\.|\\,|\\;|\\:"; 

	static List<WordLen> getWordsLength(String text) {
		List<String> textlist = Arrays.asList(text.split(delimiters)); // Converte la stringa in input in un array di stringhe, separate da segni di punteggiatura
		return textlist.stream()
				.map(s -> new WordLen(s, s.length())) // Mappa ogni parola in una WordLen
				.collect(toList()); // Restituisce il risultato come List<WordLen>
	};
	
	static Optional<String> getWordWithMaxLen(String text){
		List<String> textlist = Arrays.asList(text.split(delimiters)); // Converte la stringa in input in un array di stringhe, separate da segni di punteggiatura
		return textlist.stream()
				.map(s -> new WordLen(s, s.length())) // Mappa ogni parola in una WordLen
				.max(comparing(WordLen::getLength)) // Ottiene la parola di lunghezza maggiore
				.map(WordLen::getWord); // Mantiene solo la parola, e non tutta la WordLen		 
	}

	static int getWordFreq(String text, String word){
		List<String> textlist = Arrays.asList(text.split(delimiters)); // Converte la stringa in input in un array di stringhe, separate da segni di punteggiatura
		return (int) textlist.stream()
				.filter(d -> d.compareTo(word)==0) // Filtra solo le parole uguali a quella fornita
				.count(); // e le conta
	}

	static List<WordPos> getWordsPos(String text){
		List<String> textlist = Arrays.asList(text.split(delimiters)); // Converte la stringa in input in un array di stringhe, separate da segni di punteggiatura
		List<WordPos> textlistPos = new LinkedList<>(); // Lista di appoggio per l'output
		textlist.stream()
		.distinct() // Elimina le ripetizioni
		.forEach(word -> textlistPos.add(new WordPos() { // Aggiunge le posizioni delle parole (non ripetute) alla lista
            @Override
            public String getWord() {
                return word;
            }
            @Override
            public List<Integer> getPos() {
                return IntStream.range(0, textlist.size()) // Scorre la lista
                		.filter(i -> textlist.get(i).equals(word)) // Trova i match tra le parole
                		.map(i -> i+1) // Incrementa di 1 il contatore corrispondente per ogni match trovato
                		.boxed()
                		.collect(toList());
            }
        }));
		return textlistPos;
	}
	
}

-- mapLen (Es01) -- Modificato con String invece di Word a causa di un conflitto
--type Word = String
mapLen :: [String] -> [(String,Int)]
mapLen l = map (\w -> (w, len w)) l
    where
        len w = foldr(\_ c -> c+1) 0 w
-- In alternativa si poteva anche usare la list comprehension
mapLen' :: [String] -> [(String,Int)]
mapLen' l = [(w,length w) | w <- l]

-- selectedLen (Es02)
selectedLen :: [String] -> Int -> [(String,Int)]
selectedLen l w = filter (\(_,l') -> l'>w)(mapLen l)
--  Anche questa era fattibile con list comprehension. Notare che la signature non è richiesta, in quanto inferita da Haskell. Di solito la si mette per documentazione e sicurezza.
selectedLen' l w = [(w',length w') | w' <- l, length w'>w] -- Notare che qui non c'è lazy evaluation: Haskell non sa che la stessa espressione è presente due volte: in alternativa si può creare un ambiente locale in un linguaggio a vincoli tipo Prolog. Qui non si riesce a fare senza eliminare la list comprehension.
-- Alternativa con composizione di funzioni
selectedLen'' :: Int -> ([String] -> [(String,Int)])
selectedLen'' w = (filter (\(_,l') -> l'>w).mapLen)

-- wordOcc (Es03)
wordOcc :: [String] -> String -> Int
wordOcc l w = foldr (\w' c->if(w'==w) then c+1 else c) 0 l -- In questo caso la foldr ha un utilizzo simile al mantenimento di uno stato (si porta dietro un valore aggregando)

-- wordsOcc (Es04) (TODO c'è un errore di battitura)
member _ [] = False
member v (x:xs)
    | v == x = True
	| otherwise = member v xs
wordsOcc ::[String] -> [(Int, [String])]
wordsOcc l = (myfold . getOccs) l
    where
	    getOccs l = map (\w -> (wordOcc l w, w)) l
		myfold l = foldr (\occ l' -> updateList occ l') [] l
		where
        updateList (o, w) [] = [(o, [w])]
		updateList (o, w) ((o', lw):xs)
		    | o == o' && not (member w lw) = (o, w:lw) : xs
			| o == o' = (o,lw):xs
			| otherwise = (o', lw) : updateList (o, w) xs

-- printElems (Es05) (Fatto su stringhe e non generici)
printElems :: [String] -> IO()
printElems [] = return ()
printElems (x:xs) = (putStrLn ("    " ++ x)) >> printElems xs
-- Si poteva anche fare usando la foldr
-- countElems (Es01)
countElems :: [Int] -> Int -> Int
countElems [] _ = 0
countElems (x:xs) v
    | x == v = 1 + countElems xs v -- Si potrebbero mettere le parentesi tra countElems e la fine, ma non è necessario
    | otherwise = countElems xs v

{- -- countElems alternativo (Es01)
countElems :: [Int] -> Int -> Int
countElems [] _ = 0
countElems (x:xs) v
    | x == v = 1 + rest
    | otherwise = rest
	where
		rest = (countElems xs v) -}

-- countElemsWithPred (Es02)
countElemsWithPred :: [Int] -> (Int -> Bool) -> Int
countElemsWithPred [] _ = 0
countElemsWithPred (x:xs) p
    | p x = 1 + (countElemsWithPred xs p) -- "p x" è come scrivere (p x)==True
    | otherwise = countElemsWithPred xs p

-- countDots (Es03)
data Elem = Dot | Star
countDots :: [Elem] -> Int
countDots [] = 0
countDots (Dot:xs) = 1 + countDots xs
countDots (_:xs) = countDots xs -- Non specifichiamo il primo tipo, anche se in questo caso, se non è Dot è Star

-- rev (Es04)
rev :: [a] -> [a]
rev [] = []
rev (x:xs) = (rev xs) ++ [x] -- Non è possibile scrivere ""rev (x:xs) = xs:x"", perché è sbagliato il tipo: l'operatore ":" si aspetta un elemento e una lista.

-- removeAll (Es05) (supponiamo una lista di interi, e non parametrizzata come scritto nel testo dell'esercizio)
--removeAll :: [a] -> a -> [a] -- In realtà c'è un problema con la versione polimorfica che vedremo nella prossima lezione: la type variable potrebbe avere ulteriori vincoli nel corpo della funzione.
removeAll [] _ = []
removeAll (x:xs) v
    | x == v = removeAll xs v
	| otherwise = x : (removeAll xs v)

-- isPresentInBST (Es07)
data BSTree = Nil | Node Int BSTree BSTree -- Anche qui lo modifichiamo per lavorare solo con gli Int per lo stesso problema dell'Es05
isPresentInBST :: Int -> BSTree -> Bool
isPresentInBST _ Nil = False
isPresentInBST v (Node x l r)  -- Valore del nodo (x), sottoalbero sinistro (l) e destro (r)
    | x == v = True
    | x < v = isPresentInBST v r -- Ricerca nel sottoalbero destro (sappiamo che i valori minori sono a sinistra e quelli maggiori a destra, per definizione di BST)
    | otherwise = isPresentInBST v l -- RIcerca nel sottoalbero sinistro
-- Per testare questa funzione si crea un testare (creiamo un albero, ma nessuno controlla che sia giusto)
tt :: BSTree
tt = (Node 10
       (Node 5
	     (Node 1 Nil Nil)
		 Nil) 
       (Node 13
         (Node 11 Nil Nil)
		 Nil))

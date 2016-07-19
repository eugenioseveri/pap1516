{- ================================================
    ASSIGNMENT #1 - PAP 2015/2016 - Eugenio Severi
   ================================================ -}

-- revList (Funzione accessoria che inverte una lista)
rev :: [a] -> [a]
rev [] = []
rev (x:xs) = (rev xs) ++ [x]

-- isSorted (Es01) - Data una lista di interi l restituisce vero o falso a seconda che l sia ordinata in ordine crescente oppure non lo sia.
isSorted :: [Int] -> Bool
isSorted [] = True -- Caso base: una lista vuota è sempre ordinata
isSorted (x:[]) = True -- Caso base: una lista di un elemento è sempre ordinata
isSorted (x:xs)
    | x <= (head xs) = isSorted xs -- Se l'elemento corrente è minore del successivo, la lista (parziale) è ordinata
    | otherwise = False -- altrimenti non lo è

-- occSimple (Es02) - Data una lista di stringhe l e una stringa s, determina tutte le posizioni (occorrenze) in cui s compare in l. La prima posizione ha indice zero.
occSimple :: [String] -> String -> [Int]
occSimple (xs) s = matchFinder (rev xs) s
    where
        matchFinder [] _ = [] -- Caso base: la lista di stringhe è vuota
        matchFinder [_] [] = [] -- Caso base: la stringa da ricercare è vuota
        matchFinder (x:xs) s
            | x == s = matchFinder (xs) s ++ [length xs] -- Stringa trovata
            | otherwise = matchFinder (xs) s -- Stringa non trovata

-- occFull (Es03) - Data una lista di stringhe l determina le occorrenze di tutte le stringhe contenute in l, determinando, per ogni stringa, tutte le posizioni in cui essa compare in l.
quicksort :: [String] -> [String]  -- Funzione che implementa l'algoritmo quick sort per l'ordinamento della lista
quicksort [] = [] -- Caso base: lista vuota
quicksort (x:xs) -- Algoritmo quicksort (ricorsivo)
    = quicksort([ y | y<-xs, y < x])
        ++ [x]
        ++ quicksort([ y | y<-xs, y >= x])
removeRepetitions :: [String] -> [String] -- Funzione che gestisce le ripetizioni di elementi: in output si stampa una sola occorrenza per ogni stringa
removeRepetitions [] = [] -- Caso base: lista vuota
removeRepetitions [a] = [a] -- Caso base: lista di un solo elemento
removeRepetitions (x:y:xs)
    | x == y = removeRepetitions (y:xs) -- Ripedizione individuata
    | otherwise = [x] ++ removeRepetitions (y:xs) -- Elemento non ripetuto
occFull :: [String] -> [(String, [Int])]
occFull [] = [] -- Caso base: lista vuota
occFull (x:xs) = occTemp (x:xs) (removeRepetitions(quicksort(x:xs))) -- Confronta ricorsivamente la stringa in input con se stessa, privata degli elementi ripetuti e ordinata
    where
        occTemp [] _ = []  -- Caso base: lista vuota
        occTemp _ [] = [] -- Caso base: lista vuota
        occTemp (x:xs) (z:zs) = [(z,occSimple(x:xs) z)] ++ occTemp (x:xs) (zs)  -- Individua le occorrenze di una stringa all'interno di una lista di stringhe, richiamando occSimple (vedere Es02)

--countDash (Es04) - Dato un valore di tipo Code, conta il numero di elementi Dash in esso presenti.
data Sym = Dot | Dash
data Code = Single Sym | Comp Sym Code
countDash :: Code -> Int
countDash (Single Dash) = 1 -- Caso base (Dash singolo =1)
countDash (Single Dot) = 0 -- Caso base (Dot singolo =0)
countDash (Comp Dash comp) = 1 + countDash comp -- Caso composto (Dash singolo +1 e ricorsione)
countDash (Comp Dot comp) = countDash comp -- Caso composto (Dot singolo =0 e ricorsione)

-- showCode (Es05) - Restituisce una rappresentazione testuale di Code, ove Dot è rappresentato dal carattere ‘.’ e Dash dal carattere ‘-’.
showCode :: Code -> String
showCode (Single Dash) = "-" -- Caso base (Dash singolo)
showCode (Single Dot) = "." -- Caso base (Dot singolo)
showCode (Comp Dash comp) = "-" ++ showCode comp -- Caso composto (Dash singolo e ricorsione)
showCode (Comp Dot comp) = "." ++ showCode comp -- Caso composto (Dot singolo e ricorsione)

-- equalsBNum (Es06) - Dati due BNum determina se rappresentano lo stesso numero binario.
data Digit = Zero | One deriving (Eq, Show)
type BNum = [Digit]
equalsBNum :: BNum -> BNum -> Bool
x `equalsBNum` y = rev(x) `checkIfEqual` rev(y) -- I backtick sono necessari ai fini dell'utilizzo infisso della funzione. Le liste sono invertite per gestire correttamente l'ordine delle cifre significative
    where
        [] `checkIfEqual` [] = True -- Caso base: liste vuote
        (Zero:xs) `checkIfEqual` [] = xs `checkIfEqual` [] -- Gli zeri iniziali non modificano il valore di un numero binario
        [] `checkIfEqual` (Zero:ys) = [] `checkIfEqual` ys -- Analogamente per la seconda espressione
        xs `checkIfEqual` [] = False -- Caso base: elementi non nulli confrontati con la lista vuota
        [] `checkIfEqual` ys = False -- Analogamente per la seconda espressione
        (x:xs) `checkIfEqual` (y:ys) -- Caso non base: controlla ricorsivamente i due numeri binari
            | x == y = xs `checkIfEqual` ys
            | otherwise = False

-- convBNum (Es07) - Determina il valore di un BNum come numero intero.
convBNum :: BNum -> Int
convBNum [] = 0 -- Caso base: lista vuota
convBNum (x:xs)
    | x == One = 2^(length xs) + convBNum xs -- Nel caso il numero sia 1, lo si moltiplica per la corrispondente potenza di 2, in base alla posizione
    | otherwise = convBNum xs -- Altrimenti, se il numero è 0, lo si ignora e si prosegue a convertire il resto della lista

-- sumBNum (Es08) - Dati due numeri binari determina il numero binario che rappresenta la somma.
sumBNum :: BNum -> BNum -> BNum
sumBNum [] [] = [Zero]
sumBNum (xs) (ys) = carry 0 (rev(xs)) (rev(ys)) -- Gestisce i numeri binari al contrario
    where
        carry 0 [] [] = [] -- Caso base: riporto 0 e liste vuote
        carry 1 [] [] = [One] -- Caso base: riporto 1 e liste vuote
        carry c [] ys = carry c [Zero] ys -- Nel caso la prima lista sia vuota, la si gestisce come se fosse di soli 0
        carry c xs [] = carry c xs [Zero] -- Analogamente per la seconda lista
        carry c (x:xs) (y:ys) -- Nel caso in cui le due liste siano non banali si devono valutare le 8 combinazioni derivanti dai 3 bit da sommare (lista1, lista2 e riporto)
            | x == Zero && y == Zero && c == 0 = carry 0 xs ys ++ [Zero] -- 0+0 e riporto 0
            | x == Zero && y == Zero && c == 1 = carry 0 xs ys ++ [One] -- 0+0 e riporto 1
            | x == Zero && y == One && c == 0 = carry 0 xs ys ++ [One] -- 0+1 e riporto 0
            | x == Zero && y == One && c == 1 = carry 1 xs ys ++ [Zero] -- 0+1 e riporto 1
            | x == One && y == Zero && c == 0 = carry 0 xs ys ++ [One] -- 1+0 e riporto 0
            | x == One && y == Zero && c == 1 = carry 1 xs ys ++ [Zero] -- 1+0 e riporto 1
            | x == One && y == One && c == 0 = carry 1 xs ys ++ [Zero] -- 1+1 e riporto 0
            | x == One && y == One && c == 1 = carry 1 xs ys ++ [One] -- 1+1 e riporto 1

-- countZeroInTree (Es09) - Conta il numero di elementi Zero presenti nell’albero passato come parametro.
data BTree a = Nil | Node a (BTree a) (BTree a)
countZeroInTree :: BTree Digit -> Int
countZeroInTree Nil = 0 -- Caso base: albero vuoto
countZeroInTree (Node value left right)
    | value == Zero = 1 + countZeroInTree left + countZeroInTree right -- Se il valore del nodo corrente è 1, aumenta il conteggio di zeri e prosegue ricorsivamente nei sottoalberi
    | otherwise = countZeroInTree left + countZeroInTree right -- Altrimenti, se il valore del nodo corrente è 0, prosegue la ricerca senza incrementare il conteggio

-- getValuesLessThan (Es10) - Dato un albero binario di ricerca t e un valore v, determina la lista degli elementi presenti in t che hanno un valore inferiore a v.
getValuesLessThan :: BTree Int -> Int -> [Int]
getValuesLessThan Nil _ = [] -- Caso base: albero vuoto
getValuesLessThan (Node value left right) v
    | value < v = getValuesLessThan left v ++ [value] ++ getValuesLessThan right v -- Se il valore del nodo rispetta la condizione, lo si riporta in output
    | otherwise = getValuesLessThan left v ++ getValuesLessThan right v -- Altrimenti si prosegue la ricerca ricorsivamente nei sottoalberi

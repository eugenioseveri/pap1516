{- ================================================
    ASSIGNMENT #2 - PAP 2015/2016 - Eugenio Severi
   ================================================ -}

import Screen -- Modulo utilizzato nell'Es04

-- longestDashSeq (Es01) - Determina la lunghezza della sequenza di Dash più lunga nel Code specificato.
data Sym = Dot | Dash
data Code = Single Sym | Comp Sym Code
longestDashSeq :: Code -> Maybe Int
longestDashSeq c = longestDashSeqWrap c 0 0 -- Ad ogni iterazione la funzione porta con sè il valore massimo e quello corrente (inizialmente entrambi a 0)
    where
        longestDashSeqWrap (Single Dash) maxValue currentValue -- Caso base: singolo Dash
            | maxValue >= (currentValue + 1) = Just maxValue
            | otherwise = Just (currentValue + 1)
        longestDashSeqWrap (Single Dot) maxValue currentValue -- Caso base: singolo Dot
            | (maxValue >= currentValue) && (maxValue /= 0) = Just maxValue
            | currentValue == 0 = Nothing
        longestDashSeqWrap (Comp Dash c) maxValue currentValue -- Caso composto con Dash
            | maxValue >= (currentValue + 1) = longestDashSeqWrap c maxValue (currentValue + 1)
            | otherwise = longestDashSeqWrap c (currentValue + 1) (currentValue + 1)
        longestDashSeqWrap (Comp Dot c) maxValue currentValue = longestDashSeqWrap c maxValue 0 -- Caso composto con Dot

-- findMax (Es02) - Data una lista di interi l, determina - se esiste - il valore massimo utilizzando esclusivamente funzioni high-order di mapping o folding.
findMax :: [Int] -> Maybe Int
findMax [] = Nothing -- Caso base: lista vuota (nessun massimo)
findMax (x:xs) = Just (foldl (\val1 val2 -> if (val1 > val2) then val1 else val2) x xs) -- Confronta l'elemento attuale con il massimo locale e mantiene il maggiore dei due

-- querySortedTransList, queryBuyerCities, queryExistBuyerFrom, queryAmountsFromCity (Es03)
type BuyerId = String
type City = String
type Year = Int
type Amount = Int
data Buyer = Buyer BuyerId City
    deriving (Show)
data Transaction = Trans BuyerId  Year Amount
    deriving (Show)
data DBase = DBase [Buyer] [Transaction]
buyers = [
    Buyer "maria" "Milano",
    Buyer "stefano" "Roma",
    Buyer "laura" "Cesena",
    Buyer "alice" "Cesena"]
transactions = [
    Trans "alice" 2011 300,
    Trans "maria" 2012 1000,
    Trans "maria" 2011 400,
    Trans "laura" 2012 710,
    Trans "stefano" 2011 700,
    Trans "stefano" 2012 950,
    Trans "alice" 2015 1000,
    Trans "laura" 2016 2000]
db = DBase buyers transactions
--querySortedTransList - Ottiene la lista delle transazioni riferite all’anno specificato, ordinate per amount.
querySortedTransList :: DBase -> Year -> [Transaction]
querySortedTransList (DBase _ trans) year = quicksort (filter (\(Trans _ selectedYear _) -> selectedYear == year) trans) -- Filtra le transazioni dell'anno corrente, e le ordina utilizzando l'algoritmo quicksort
    where
        quicksort [] = []
        quicksort ((Trans bId year amount):xs) = quicksort([(Trans bId2 year2 amount2) | (Trans bId2 year2 amount2) <- xs, amount2 < amount])
            ++ [(Trans bId year amount)]
            ++ quicksort([(Trans bId2 year2 amount2) | (Trans bId2 year2 amount2) <- xs, amount2 >= amount])
-- queryBuyerCities - Ottiene l’elenco delle città distinte dei buyer.
queryBuyerCities :: DBase -> [City]
queryBuyerCities (DBase buyers _) = getDistinctBuyerCities buyers []
    where
        getDistinctBuyerCities [] distinctCities = distinctCities
        getDistinctBuyerCities (Buyer bId city : bs) distinctCities -- Se la città corrente non è stata già aggiunta, lo fa
            | city `elem` distinctCities = getDistinctBuyerCities bs distinctCities
            | otherwise = getDistinctBuyerCities bs (distinctCities ++ [city])
-- queryExistBuyerFrom - Verifica se esistono buyer di una certa città.
queryExistBuyerFrom :: DBase -> City -> Bool
queryExistBuyerFrom (DBase buyers _) city = (checkExist . filter (\(Buyer bId city2) -> city == city2)) buyers -- Filtra i buyer della città specificata
    where
        checkExist [] = False -- Se non esistono buyer per la città selezionata, resituisce False,
        checkExist x = True -- altrimenti True
--queryAmountsFromCity -  Determina la somma delle transazioni eseguite da buyer di una certa città.
queryAmountsFromCity :: DBase -> City -> Amount
queryAmountsFromCity (DBase buyers transactions) city = foldl (\x (Trans _ _ amount) -> x + amount) 0 ((filtTrans . filter (\(Buyer _ c) -> c == city)) buyers transactions) -- Filtra i buyer in base alla città specificata
    where
        filtTrans [] _ = []
        filtTrans (Buyer bId _ : xs) transactions = filter (\(Trans tId _ _) -> bId == tId) transactions ++ filtTrans xs transactions -- Controlla il match tra BuyerId dei buyer e BuyerId delle transazioni.

-- render, renderWithClipping, renderAll (Es04)
data TObj = Text String Color Pos | HorLine Int Pos Color | VerLine Int Pos Color |
    Rectangle Pos Int Int Color | Box Pos Int Int Color
    deriving(Show)
class Viewable o where
    render :: Viewport -> o -> IO ()
    renderWithClipping :: Viewport -> o -> IO ()
    renderAll :: Viewport -> [o] -> IO ()
instance Viewable TObj where
    -- Funzione render
    render (Viewport (vx,vy) _ _) (Text s c (tx,ty)) = do
        setFColor c
        writeAt (vx+tx, vy+ty) s
        goto (vx+tx, vy+ty+1) -- Necessario per evitare che il prompt sovrascriva l'output
    render (Viewport (vx,vy) _ _) (HorLine n (tx,ty) c) = do
        setFColor c
        printDashLength n 0 -- Disegna "-" ricorsivamente. Il secondo parametro rappresenta l'offset verso destra.
            where
                printDashLength 0 _ = do -- Caso base: termine delle n stampe
                    goto (vx+tx, vy+ty+1) -- Necessario per evitare che il prompt sovrascriva l'output
                    return ()
                printDashLength n offset = do -- Caso non base: stampa un "-" nella posizione corrente
                    writeAt (vx+tx+offset, vy+ty) "-"
                    printDashLength (n-1) (offset+1) -- Richiama ricorsivamente la funzione spostandosi di un carattere in avanti
    render (Viewport (vx,vy) _ _) (VerLine n (tx,ty) c) = do
        setFColor c
        printPipeLength n 0 -- Disegna "|" ricorsivamente. Il secondo parametro rappresenta l'offset verso il basso.
            where
                printPipeLength 0 _ = do -- Caso base: termine delle n stampe
                    goto (vx+tx, vy+ty+n+1) -- Necessario per evitare che il prompt sovrascriva l'output
                    return ()
                printPipeLength n offset = do -- Caso non base: stampa un "|" nella posizione corrente
                    writeAt (vx+tx, vy+ty+offset) "|"
                    printPipeLength (n-1) (offset+1) -- Richiama ricorsivamente la funzione spostandosi di una riga verso il basso
    render (Viewport (vx,vy) _ _) (Rectangle (tx,ty) w h c) = do
        setFColor c
        render viewport (HorLine w (tx+1, ty) c) -- Sfrutta le funzioni precedentemente create. Gli offset sono necessari per allineare le linee verticali e orizzontali
        render viewport (VerLine h (tx, ty) c)
        render viewport (HorLine w (tx+1, ty+h-1) c)
        render viewport (VerLine h (tx+w+1, ty) c)
    render (Viewport (vx,vy) _ _) (Box (tx,ty) w h c) = do
        setFColor c
        setBColor c
        renderHorLineWithBackground 0
            where
                renderHorLineWithBackground endLine
                    | endLine == h = setBColor BLACK -- Imposta nuovamente il colore di sfondo a BLACK (ipotizzando che fosse il colore precedente)
                    | otherwise = render viewport (HorLine (w+2) (tx, ty+endLine) c) >> renderHorLineWithBackground (endLine+1) -- +2 per dare la corretta forma al box-}
    -- Funzione renderWithClipping
    renderWithClipping (Viewport (vx,vy) maxDimX maxDimY) (Text s c (tx,ty)) = do
        setFColor c
        printStringByChar (length s) 0 -- Stampa la stringa carattere per carattere invece che tutta insieme. Necessario per gestire il clipping. Il secondo parametro rappresenta l'offset verso destra.
            where
                printStringByChar 0 _ = do -- Caso base: termine delle n stampe
                    goto (vx+tx, vy+ty+1) -- Necessario per evitare che il prompt sovrascriva l'output
                    return ()
                printStringByChar n offset = do -- Caso non base: stampa il carattere attuale nella posizione corrente
                    goto (vx+tx+offset, vy+ty) -- Spostamento alla posizione successiva
                    if (tx+offset <= maxDimX && ty <= maxDimY) -- Verifica se il carattere corrente è contenuto nella viewport, e nel caso lo stampa
                        then putChar (s !! offset)
                        else return ()
                    printStringByChar (n-1) (offset+1) -- Richiama ricorsivamente la funzione spostandosi di un carattere in avanti
    renderWithClipping (Viewport (vx,vy) maxDimX maxDimY) (HorLine n (tx,ty) c) = do
        setFColor c
        printDashLength n 0 -- Disegna "-" ricorsivamente. Il secondo parametro rappresenta l'offset verso destra.
            where
                printDashLength 0 _ = do -- Caso base: termine delle n stampe
                    goto (vx+tx, vy+ty+1) -- Necessario per evitare che il prompt sovrascriva l'output
                    return ()
                printDashLength n offset = do -- Caso non base: stampa un "-" nella posizione corrente
                    if (tx+offset < maxDimX && ty < maxDimY) -- Verifica se il carattere corrente è contenuto nella viewport, e nel caso lo stampa
                        then writeAt (vx+tx+offset, vy+ty) "-"
                        else return ()
                    printDashLength (n-1) (offset+1) -- Richiama ricorsivamente la funzione spostandosi di un carattere in avanti
    renderWithClipping (Viewport (vx,vy) maxDimX maxDimY) (VerLine n (tx,ty) c) = do
        setFColor c
        printPipeLength n 0 -- Disegna "|" ricorsivamente. Il secondo parametro rappresenta l'offset verso il basso.
            where
                printPipeLength 0 _ = do -- Caso base: termine delle n stampe
                    goto (vx+tx, vy+ty+n+1) -- Necessario per evitare che il prompt sovrascriva l'output
                    return ()
                printPipeLength n offset = do -- Caso non base: stampa un "|" nella posizione corrente
                    if (tx+offset < maxDimX && ty < maxDimY) -- Verifica se il carattere corrente è contenuto nella viewport, e nel caso lo stampa
                        then writeAt (vx+tx, vy+ty+offset) "|"
                        else return ()
                    printPipeLength (n-1) (offset+1) -- Richiama ricorsivamente la funzione spostandosi di una riga verso il basso
    renderWithClipping (Viewport (vx,vy) maxDimX maxDimY) (Rectangle (tx,ty) w h c) = do
        setFColor c
        renderWithClipping viewport (HorLine w (tx+1, ty) c) -- Sfrutta le funzioni precedentemente create. Gli offset sono necessari per allineare le linee verticali e orizzontali
        renderWithClipping viewport (VerLine h (tx, ty) c)
        renderWithClipping viewport (HorLine w (tx+1, ty+h-1) c)
        renderWithClipping viewport (VerLine h (tx+w+1, ty) c)
    renderWithClipping (Viewport (vx,vy) maxDimX maxDimY) (Box (tx,ty) w h c) = do
        setFColor c
        setBColor c
        renderHorLineWithBackground 0
            where
                renderHorLineWithBackground endLine
                    | endLine == h = setBColor BLACK -- Imposta nuovamente il colore di sfondo a BLACK (ipotizzando che fosse il colore precedente)
                    | otherwise = renderWithClipping viewport (HorLine (w+2) (tx, ty+endLine) c) >> renderHorLineWithBackground (endLine+1)
    -- Funzione renderAll
    renderAll v [] = return ()
    renderAll v (x:xs) = do
        render v x -- Richiama la funzione render su tutti gli argomenti passati
        renderAll v xs
-- Dati di esempio per il test delle funzioni dell'Es04
viewport = Viewport (5,5) 5 5
text = Text "stringa" RED (0,3)
horline = HorLine 5 (0,1) GREEN
verline = VerLine 5 (0,3) GREEN
rectangle = Rectangle (2,2) 3 4 GREEN
box = Box (2,2) 3 4 GREEN

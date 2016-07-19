{- ================================================
    ASSIGNMENT #3 - PAP 2015/2016 - Eugenio Severi
   ================================================ -}

data BSTree a = Nil | Node a (BSTree a) (BSTree a)

-- bstMap (Es03.1)
bstMap :: (a -> b) -> BSTree a -> BSTree b
bstMap _ Nil = Nil
bstMap function (Node value left right) = Node (function value) (bstMap function left) (bstMap function right)

-- bstFold (Es03.2)
bstFold :: (b -> a -> b) -> b -> BSTree a -> b
bstFold _ value Nil = value
bstFold function value (Node value' left right) = function left' value'
    where
        left' = bstFold function right' left
        right' = bstFold function value right

-- bstFilter (Es03.3)
bstFilter :: (a -> Bool) -> BSTree a -> BSTree a
bstFilter _ Nil = Nil
bstFilter function (Node value Nil Nil) -- Caso base: nodo senza figli
    | function value == True = Node value Nil Nil -- Se c'è un valore non nullo, lo considera
    | otherwise = Nil -- altrimenti no
bstFilter function (Node value left Nil) -- Nodo con il solo sottoalbero di sinistra
    | function value == True = Node value (bstFilter function left) Nil -- Se c'è un valore non nullo, lo considera e richiama ricorsivamente la funzione
    | otherwise = bstFilter function left -- altrimenti richiama solo la funzione
bstFilter function (Node value Nil right) -- Nodo con il solo sottoalbero di destra
    | function value == True = Node value Nil (bstFilter function right) -- Se c'è un valore non nullo, lo considera e richiama ricorsivamente la funzione
    | otherwise = bstFilter function right -- altrimenti richiama solo la funzione
bstFilter function (Node value left right) -- Nodo con sottoalbero sia sinistro che destro
    | function value == True = Node value (bstFilter function left) (bstFilter function right) -- Se il valore del nodo non è nullo richiamo ricorsivamente la funzione sui due sottoalberi
    | otherwise = bstFilter function (Node (subsequent right) left (remove right)) -- altrimenti considero il nodo minore del sottoalbero destro e lo sostituisco a quello rimosso (rimuovendolo anche dal sottoalbero destro)
        where
            -- Funzione accessoria che restituisce il nodo minore, che corrisponde a quello all'estrema sinistra
            subsequent (Node value Nil _) = value
            subsequent (Node value left _) = subsequent left
            -- Funzione accessoria che elimina il nodo minore (a sinistra)
            remove(Node value Nil Nil) = Nil -- Caso base: elimina il nodo
            remove(Node value Nil right) = right -- Se è presente solo il sottoalbero destro, il nodo è necessariamente quello con minor minore
            remove(Node value left right) = Node value (remove left) right -- Se sono presenti sia il sottoalbero sinistro che destro, scendo sul sinistro (con valore minore)

-- bstForEach (Es03.4)
bstForEach :: (a -> IO ()) -> BSTree a -> IO ()
bstForEach _ Nil = return()
bstForEach function (Node value left right) = (bstForEach function left) >> (function value) >> (bstForEach function right)

type Age = Int
data Person = Person String Age

-- printPers (Es03.5)
printPers :: BSTree Person -> Int -> Int -> IO()
printPers Nil _ _ = return()
printPers bstree startAge endAge = (bstForEach output . bstFilter checkAge) bstree
    where
        output = \(Person name _ ) -> putStrLn name
        checkAge = \(Person _ age) -> startAge <= age && age <= endAge

-- Dati e funzioni di test
tree1 = (Node 9
            (Node 4
                (Node 2 Nil Nil)
                (Node 7 Nil
                    (Node 8 Nil Nil)))
            (Node 12
                (Node 11 Nil Nil)
                (Node 15 Nil
                    (Node 18
                        (Node 17 Nil Nil)
                    Nil)
            )
        ))
testBstMap = (bstForEach output . bstMap function) tree1
    where
        output = \x -> print x
        function = \x -> x * 2
testBstFold = print (bstFold function 0 tree1)
    where
        function = \x y -> x + y
testBstFilter = (bstForEach output . bstFilter function) tree1
    where
        output = \x -> print x
        function = \x -> x < 8
tree2 = Node (Person "Ilaria" 88)
    (Node (Person "Alessandro" 22) Nil
        (Node (Person "Andrea" 19) Nil Nil))
    (Node (Person "Pierluigi" 79)
        (Node (Person "Lorenzo" 46)
            (Node (Person "Leonardo" 54) Nil Nil)
            (Node (Person "Mauro" 6)
                (Node (Person "Luciana" 51)
                    (Node (Person "Luca" 22) Nil Nil)
                    Nil)
                Nil))
        (Node (Person "Vittoria" 19) Nil Nil)
    )
testPrintPers = printPers tree2 30 60

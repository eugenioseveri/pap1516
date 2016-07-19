-- Versioni alternative degli esercizi

--countDash (Es04)
data Sym = Dot | Dash deriving (Eq)
data Code = Single Sym | Comp Sym Code deriving (Eq)
countDash :: Code -> Int
countDash (Single sing) -- Caso base (elemento singolo)
    | sing == Dash = 1
    | otherwise = 0
countDash (Comp sing comp) -- Caso non base (elemento composto)
--    | sing == Dash && (comp == (Comp sing comp) || comp == (Single sing)) = 1 + countDash (comp) -- Conta ricorsivamente i Dash
    | sing == Dash = 1 + countDash (comp)
    | otherwise = 0 + countDash (comp)

-- showCode (Es05)
showCode :: Code -> String
showCode (Single sing) -- Caso base (elemento singolo)
    | sing == Dash = "-"
    | otherwise = "."
showCode (Comp sing comp) -- Caso non base (elemento composto)
    | sing == Dash  = "-" ++ showCode (comp)
    | otherwise = "." ++ showCode (comp)

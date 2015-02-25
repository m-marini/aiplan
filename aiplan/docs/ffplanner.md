# FFPlanner

L'algoritmo FFPlanner espande il grafo dei piani RPG (forma ridotta del problema senza deleting propositions) fino a comprendere l'obiettivo finale (se arriva al punto di espansione massimo senza contenere l'obiettivo significa che non c'è soluzione).

Una volta espanso il grafo si prende solamente il sottografo che contiene almeno i livelli fino ad contenere tutte le singole proposizione dell'obiettivo in almeno uno dei livelli.
In pratica si cerca per ogni proposizione il sottografo che la contiene per la prima volta e tra questi si prende il grafo con più layer.

Poi per ogni livello si formano gli insiemi degli obiettivi da raggiungere cioè le proposizione dell'obiettivo 
principale che compaiono per la prima volta nel levello selezionato.

Poi si inizia la ricerca a ritroso, per ogni layer si crea un nuovo insieme di goal per i layer precedenti:
per ogni proposizione degli obiettivi del layer si seleziona un operatore che ha come effetto la proposizione e che appare
per la prima volta nello specifico operatore selezionato vengono inserite nel nuovo insieme degli obiettivi al livello che appare per
la prima volta nel grafo RPG.

## Grafo dei sottobiettivi e degli operatori

E' utile nella backward search usare un grafo che contiene solo le operazioni e le azioni che appaiono solo per la prima volta.
Questo grafo si può ottenere mantenendo solo le differenze tra i vari layer



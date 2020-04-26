package it.insidecode.core;

/**
 * Rappresentazione di un grafo non diretto tramite liste di adiacenza. La
 * rappresentazione della struttura consente di mantenere informazioni riguardo
 * il concetto di svolta grazie al peso degli archi tra i nodi e alla loro
 * disposizione. Nella visione complessiva il grafo e' organizzato come una
 * griglia di altezza h e larghezza w. Un punto di questo reticolo e' detto
 * {@code BigNode}, ed ha la caratteristica di essere composto, al piu', da
 * quattro nodi semplici ({@code node}). Ad ogni nodo del grafo ({@code node})
 * e' associata una direzione cardinale (Nord, Sud, Est, Ovest), questa
 * rappresenta la posizione del nodo all'interno del relativo {@code BigNode}.
 * Gli archi connettono nodi interni ai {@code BigNode} e vari {@code BigNode}
 * tra loro con pesi appropriati, rendendo possibile il conteggio delle svolte e
 * degli incroci.
 * 
 * @author fscozzafava
 * 
 */
public class Graph {
	/**
	 * Il peso di un arco che incrementa di una unita' la lunghezza di un
	 * percorso. (001 in rappresentazione binaria)
	 */
	public static final int LENGHT = 1; // 001

	/**
	 * Il peso di un arco che incrementa di una unita' le svolte in un percorso.
	 * (010 in rappresentazione binaria)
	 */
	public static final int TURN = 2; // 010

	/**
	 * Il peso di un arco che incrementa di una unita' le svolte in un percorso
	 * (100 in rappresentazione binaria)
	 */
	public static final int INTERSECTION = 4; // 100

	private int width;
	private int height;

	/**
	 * Matrice bidimensionale nella quale il grafo e' mantenuto il memtoria
	 */
	private int[][] matrix;

	/**
	 * Istanzia un nuovo grafo
	 * 
	 * @param width
	 *            larghezza della griglia
	 * @param height
	 *            altezza della griglia
	 */
	public Graph(int width, int height) {
		this.width = width;
		this.height = height;
		matrix = new int[width * height * 4][]; // alloco lo spazio necessario
		initGraph(); // inizializzo il grafo
	}

	/**
	 * Inizializza il grafo
	 */
	private void initGraph() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int index = (i * height + j) * 4; // ricaviamo l'indice del nodo
													// i,j nell'array
				createNodes(i, j); // creiamo i nodi nord, sud, est, ovest del
									// bigNode (i, j) inserendoli direttamente
									// nella matrice
				if (i > 0) // se stiamo analizzando un bigNode non appartemente
							// alla prima colonna
				{
					int nodeA = index; // prendiamo il nodo west del bigNode
										// attuale (il primo nella notazione
										// WNSE)
					int nodeB = (((i - 1) * height + j) * 4) + 3; // prendiamo
																	// il nodo
																	// east del
																	// bigNode
																	// adiacente
																	// sinistro
																	// (l'ultimo
																	// nella
																	// notazione
																	// WNSE)
					addEdge(nodeA, nodeB, LENGHT); // creiamo un arco tra i nodi
													// di costo LENGHT
					// east n west
				}
				if (j > 0) // se stiamo analizzando un bigNode non appartemente
							// alla prima riga
				{
					int nodeA = index + 1; // prendiamo il nodo nord del bigNode
											// attuale (il secondo nella
											// notazione WNSE)
					int nodeB = ((i * height + j - 1) * 4) + 2; // prendiamo il
																// nodo sud del
																// bigNode
																// adiacente
																// verso l'alto
																// (il terzo
																// nella
																// notazione
																// WNSE)
					addEdge(nodeA, nodeB, LENGHT); // creiamo un arco tra i nodi
													// di costo LENGHT
				}
			}
		}
	}

	/**
	 * Procedura responsabile della creazione dei nodi componenti del bigNode di
	 * coordinate x,y
	 * 
	 * @param x
	 * @param y
	 */
	private void createNodes(int x, int y) {
		int index = (x * height + y) * 4;
		int counter = 0;
		/*
		 * Con questi cicli annidati ricaviamo solamente le posizioni che ci
		 * interessano in una griglia di adiacenza immaginaria 3x3 queste sono
		 * le posizioni della croce escluse le diagonali ed il punto centrale.
		 * Questa procedura istanzia solamente i nodi necessari nel caso in cui
		 * stessimo analizzando bigNode di bordo inserendo null come valore
		 * nella matrice nel caso in cui un nodo non esista
		 */
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == j || (i == -j))
					continue;
				if (x + i < 0 || j + y < 0 || i + x >= width || j + y >= height) {
					counter++;
					continue;
				}
				matrix[index + counter] = new int[5]; // istanziamo una nuova
														// lista di adiacenza
														// per il nodo

				/*
				 * Questo ciclo e' responsabile di effettuare i collegamenti tra
				 * i nodi interni al bigNode appena definito. Ogni nodo e'
				 * connesso con opportuni costi a quelli immediatamente
				 * precedenti nella matrice (definiti dall'ordine WNSE).
				 */
				for (int k = counter - 1; k >= 0; k--) {
					if (matrix[index + k] == null)
						continue;
					int weight = LENGHT; // il peso di default e' LENGHT
					if (k != 3 - counter)
						weight += TURN; // nel caso in cui l'altro nodo non
										// fosse in direzione opposta (pos 3-i)
										// al costo viene aggiunta una svolta
										// (TURN)
					addEdge(index + counter, index + k, weight); // viene
																	// aggiunto
																	// l'arco
				}
				counter++;
			}
		}
	}

	/**
	 * Data in ingresso una lista di adiacenza e la definizione di un nodo,
	 * aggiunge il nodo come adiacente alla lista
	 * 
	 * @param adjList
	 * @param adjTo
	 */
	private void addAdjacent(int[] adjList, int adjTo) {
		if (adjList[0] == adjList.length - 1)
			throw new RuntimeException(
					"Adjacent list lenght limit (4) reached for this node");
		// indice dell'ultimo adiacente aggiunto
		adjList[++adjList[0]] = adjTo; // incrementiamo il primo valore
										// dell'array indicante il numero di
										// adiacenti nella lista (posizione
										// dell'ultimo adiacente)
	}

	/**
	 * Data in ingresso una lista di adiacenza e la definizione di un nodo,
	 * rimuove il nodo come adiacente dalla lista
	 * 
	 * @param adjList
	 * @param node
	 */
	private void removeAdjacent(int[] adjList, int node) {
		if (adjList[0] == 0)
			return; // se la lista di adiacenza risulta vuota ritorno
		for (int x = 1; x <= adjList[0]; x++) // scorriamo tutti gli adiacenti
												// in lista sfruttando l'indice
												// in posizione [0]
		{
			if ((adjList[x] & 0xFFFFFFF) == node) // se abbiamo trovato la
													// posizione del nodo in
													// lista (il confronto viene
													// effettuato rimuovento la
													// parte relativa al peso
													// dell'arco)
			{
				/*
				 * Nel caso in cui il nodo da rimuovere non fosse in ultima
				 * posizione non sarebbe sufficiente decrementare l'indice in
				 * posizione [0]. Prendiamo quindi l'ultimo elemento della lista
				 * e lo spostiamo in posizione x per mantenere compatta la
				 * rappresentazione della lista di adiacenza.
				 */
				if (x != adjList[0]) {
					adjList[x] = adjList[adjList[0]];
					adjList[adjList[0]] = 0;
				}
				adjList[0]--; // avendo rimosso un adiacente decrementiamo
								// l'indice in posizione [0]
				return;
			}
		}
	}

	/**
	 * Rimuove dal grafo un nodo
	 * 
	 * @param node
	 *            il nodo da rimuovere
	 */
	public void removeNode(int node) {
		if (matrix[node] == null)
			return; // se il nodo non esiste ritorno
		for (int i = 1; i <= matrix[node][0]; i++) // scorriamo tutti gli
													// adiacenti del nodo e
													// rimuoviamo in cui questo
													// e' coinvolto
		{
			removeAdjacent(matrix[matrix[node][i] & 0xFFFFFF], node);
		}
		matrix[node] = null; // deallochiamo la lista di adiacenza per il nodo
								// appena rimosso
	}

	/**
	 * Aggiunge un arco non diretto tra i nodi {@code nodeA} e {@code nodeB} con
	 * peso weight
	 * 
	 * @param nodeA
	 * @param nodeB
	 * @param weight
	 *            peso dell'arco
	 */
	public void addEdge(int nodeA, int nodeB, int weight) {
		addEdge(nodeA, nodeB, weight, false);
	}

	/**
	 * Aggiunge un arco tra i nodi {@code nodeA} e {@code nodeB} con peso
	 * weight. E' inoltre possibile specificare se l'arco debba essere diretto o
	 * meno
	 * 
	 * @param nodeA
	 * @param nodeB
	 * @param weight
	 *            il peso dell'arco
	 * @param direct
	 *            se true crea un arco diretto, indiretto altrimenti
	 */
	public void addEdge(int nodeA, int nodeB, int weight, boolean direct) {
		int[] adjA = matrix[nodeA];
		int[] adjB = matrix[nodeB];
		addAdjacent(adjA, (weight << 28) | nodeB);
		if (!direct)
			addAdjacent(adjB, (weight << 28) | nodeA);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * Dato un nodo, ritorna la lista dei nodi adiacenti
	 * 
	 * @param node
	 * @return la lista dei nodi adiacenti.
	 */
	public int[] getAdjacents(int node) {
		int[] res = getAdjacentsRaw(node);
		for (int i = 0; i < res.length; i++)
			res[i] &= 0xFFFFFFF;
		return res;
	}

	/**
	 * Dato un nodo, ritorna la lista degli adiacenti cosi' come e' memorizzata
	 * nella matrice del grafo
	 * 
	 * @param node
	 * @return l'array degli adiacenti
	 */
	private int[] getAdjacentsRaw(int node) {
		int[] res = new int[matrix[node][0]];
		for (int i = 1; i <= matrix[node][0]; i++)
			res[i - 1] = matrix[node][i];
		return res;
	}

	/**
	 * Dati due nodi, ritorna un long rappresentante il peso dell'arco che li
	 * connette in questo modo: il long e' diviso in 3 intervalli da 21 bit
	 * ciascuno (il bit piu' significativo e' ignorato per evitare cambi di
	 * segno) di cui il primo rappresenta la parte di peso rappresentante
	 * LENGHT, il secondo la parte rappresentante TURN ed il terzo INTERSECTIONS
	 * 
	 * @param nodeA
	 * @param nodeB
	 * @return il peso
	 */
	public long getWeight(int nodeA, int nodeB) {
		if (matrix[nodeA] == null)
			throw new IllegalArgumentException("nodeA is not in the graph!");
		if (matrix[nodeB] == null)
			throw new IllegalArgumentException("nodeB is not in the graph!");
		long weight = 0;
		for (long x : getAdjacentsRaw(nodeA)) {
			// prendo i 3 bit rappresentanti il peso dell'arco e li shifto in
			// posizione opportuna
			if ((x & 0xFFFFFFF) == nodeB) {
				weight |= (x >> 30) << 42;
				weight |= ((x >> 29) & 1) << 21;
				weight |= ((x >> 28) & 1);
				return weight;
			}
		}
		return 0;
	}

	/**
	 * Dati due nodi, incrementa il peso dell'arco non diretto tra di essi del
	 * valore indicato
	 * 
	 * @param nodeA
	 * @param nodeB
	 * @param weight
	 */
	public void increaseWeight(int nodeA, int nodeB, int weight) {
		if (matrix[nodeA] == null || matrix[nodeB] == null)
			return; // non esiste l'arco da incrementare perche gia rimosso in
					// precedenza, ritorno
		increaseWeightDirect(nodeA, nodeB, weight);
		increaseWeightDirect(nodeB, nodeA, weight);
	}

	/**
	 * Dati due nodi, incrementa il peso dell'arco diretto tra di essi del
	 * valore indicato
	 * 
	 * @param nodeA
	 * @param nodeB
	 * @param weight
	 */
	public void increaseWeightDirect(int nodeA, int nodeB, int weight) {
		if (matrix[nodeA] == null || matrix[nodeB] == null)
			return; // non esiste l'arco da incrementare perche gia rimosso in
					// precedenza, ritorno
		int[] adj = matrix[nodeA];
		for (int i = 1; i <= adj[0]; i++) {
			if ((adj[i] & 0xFFFFFFF) == nodeB) {
				adj[i] |= (weight << 28);
				return;
			}
		}
	}

	/**
	 * Data una coppia di nodi, ritorna il peso dell'arco tra essi cosi' come e'
	 * memorizzato nella rappresentazione del grafo (rappresentato come 3 bit di
	 * controllo)
	 * 
	 * @param nodeA
	 * @param nodeB
	 * @return il peso
	 */
	public int getWeightRaw(int nodeA, int nodeB) {

		if (matrix[nodeA] == null)
			throw new IllegalArgumentException("nodeA is not in the graph!");
		if (matrix[nodeB] == null)
			throw new IllegalArgumentException("nodeB is not in the graph!");
		int weight = 0;
		for (int x : getAdjacentsRaw(nodeA)) {
			if ((x & 0xFFFFFFF) == nodeB) {
				weight = x >> 28;
				return weight;
			}
		}
		return 0;
	}

	/**
	 * Dato un nodo, rimuove tutti i nodi facenti parte dello stesso bigNode
	 * 
	 * @param node
	 */
	public void removeBigNode(int node) {
		int bigNode = ((node >> 2) * 4);
		for (int i = 0; i < 4; i++)
			removeNode(bigNode + i);
	}

	/**
	 * Ritorna un valore che indica se il nodo e' presente nel grafo o meno
	 * 
	 * @param node
	 * @return se il nodo esiste
	 */
	public boolean exists(int node) {
		return node < matrix.length && (matrix[node] != null);
	}

	/**
	 * Dato un nodo, rimuove tutti gli archi diagonali del bigNode di cui il
	 * nodo fa parte
	 * 
	 * @param node
	 */
	public void removeDiagonalEdges(int node) {
		int bigNode = (node & 0xFFFFFFFC);
		for (int i = 0; i < 4; i++) {
			if (matrix[bigNode + i] == null)
				continue;
			for (int x : getAdjacents(bigNode + i))
				if (this.getWeightRaw(bigNode + i, x) == LENGHT + TURN) {
					this.removeAdjacent(matrix[bigNode + i], x);
					this.removeAdjacent(matrix[x], bigNode + i);
				}
		}
	}

	/**
	 * Ritorna un valore che indica se il nodo {@code BigNode} e' presente nel
	 * grafo o meno (se e' presente almeno un {@code node} ad esso relativo)
	 * 
	 * @param node
	 * @return se il nodo esiste
	 */
	public boolean existsBig(int node) {
		node = node & 0xFFFFFFFC;
		boolean b = false;
		for (int i = node; i < node + 4; i++) {
			b |= exists(i);
		}
		return b;
	}
}

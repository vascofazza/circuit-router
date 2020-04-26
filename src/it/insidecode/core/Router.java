package it.insidecode.core;

import it.insidecode.parser.Sequence;

public class Router 
{
	
	/**
	 * Larghezza della griglia
	 */
	private int width;
	
	/**
	 * Altezza della griglia
	 */
	private int height;
	
	/**
	 * Vettore delle distanze
	 */
	private long[] dist;
	
	/**
	 * Heap di priorita'
	 */
	private MyPriorityQueue<Integer> h;
	
	/**
	 * Vettore dei visitati
	 */
	private boolean[] visited;
	
	/**
	 * Vettore dei padri
	 */
	private int[] p;
	
	/**
	 * Un nuovo {@link Router} e' costruito con l'istanza di un grafo {@link Graph}
	 * 
	 * @param g the {@link Graph}
	 */
	public Router(Graph g)
	{
		//inizializzo tutte le strutture dati necessarie
		width = g.getWidth();
		height = g.getHeight();
		p = new int[width*height*4];
		dist = new long[width*height*4];
		visited = new boolean[width*height*4];
	}
	
	/**
	 * Calcola il percorso di costo minimo tra una coppia di nodi usando un'implementazione dell'algoritmo di Dijkstra
	 * 
	 * @param g il grafo {@link Graph}
	 * @param source nodo sorgente
	 * @param target nodo destinazione
	 * @return un oggetto {@link Sequence} rappresentante il cammino. Se questo non esiste l'oggetto non conterra' nodi
	 */
	public Sequence dijkstra(Graph g, int source, int target)
	{		
		// se il nodo target e/o il nodo sorgente non esistono (a causa anche del vincolo di svolta) il cammino non esiste
		if(!(g.existsBig(source) && g.existsBig(target)))
		{
			return new Sequence();
		}
		// inizializzo il vettore dei visitati
		for(int i = 0; i < visited.length; i++) visited[i] = false;
		// inizializzo il vettore delle distanze al valore massimo (+ infinito)
		for(int i = 0; i < dist.length ; i++) dist[i] = Long.MAX_VALUE;
		// inizializzo la coda di priorita' con dimensione di default il comparatore per i nodi
		h = new MyPriorityQueue<Integer>();
		/* aggiungo i {@code node} del {@code BigNode} source nell'heap in modo che la visita cominci 
		 * contemporaneamente in tutte le direzioni impostando al tempo stesso i padri.
		 */
		for (int d = 0; d < 4; d++)
		{
			if(g.exists(source+d))
				{
					dist[source+d] = 0;
					h.offer(source+d,0);
					p[source+d] = source+d;
				}
		}
		
		while (!h.isEmpty())
		{
			// estraggo il nodo dalla coda
			int v = h.getMin();
			/* se il nodo estratto e' gia' stato visitato continuo.
			 * Questo controllo e' necessario dal momento che per questioni implementative relative all'efficienza
			 * si e' deciso di implementando l'operazione di decreaseKey nell'heap semplicemente aggiungendo di nuovo il nodo
			 * in coda senza prima rimuoverlo. Accade cosi' che un nodo puo' essere estratto piu' volte dalla coda.
			 */
			if (visited[v]) continue;
			/* se abbiamo estratto dalla coda (e quindi gia' impostato il costo) 
			 * per un {@code node} appartenente al {@code BigNode} target la ricerca termina.
			 */
			if((v & 0xFFFFFFFC) == target)
			{
				// ritorniamo il cammino generato dalla procedura trace dal nodo target al nodo source tramite il vettore dei padri
				return trace(g, v, source);
			}
			for(int w : g.getAdjacents(v))
			{
				/* se un adiacente e' gia' stato visitato, il nodo e' stato estratto dalla coda, 
				 * quindi visitato, e il suo costo minimo gia' impostato. Inoltre questo impedisce di
				 * analizzare il padre del nodo da cui e' partita la visita dal momento che questo sarebbe
				 * gia' stato visitato in precedenza
				 */
				if(visited[v]) continue;
				//if(dist[w] == -1) continue;//if (visited[w]) continue;// || w == p[v]) continue;
				long weight = g.getWeight(v, w);
				if (dist[w] > (dist[v] + weight))
				{
					dist[w] = dist[v] + weight;
					p[w] = v;
					/* se posso raggiungere il nodo w tramite v con costo minore lo aggiungo in coda
					 * con l'adeguato valore di priorita'. Nel caso in cui il nodo fosse gia' presente in coda
					 * ne aggiungiamo semplicemente una copia (che avendo priorita' inferiore verra' estratta prima)
					 * ignorando le successive occorrenze grazie al vettore dei visitati
					 */
					h.offer(w, dist[w]);
				}
					
			}
			visited[v] = true;
		}
		// a seguito di una ricerca esaustiva nel grafo non e' stato trovato un cammino tra il nodo sorgente e destinazione
		return new Sequence();
	}

	/**
	 * Traccia un cammino a ritroso nel grafo dal nodo target al nodo sorgente risalendo di padre in padre usando il vettore padri
	 * 
	 * @param g
	 * @param target
	 * @param source
	 * @return il cammino
	 */
	private Sequence trace(Graph g, int target, int source) {
		Sequence s = new Sequence(g.getHeight());
		/* applichiamo la seguente proceura ai nodi target e source:
		 * -ricaviamo il BigNode relativo al node
		 * -prendiamo l'arco ortogonale tra gli altri due nodi incrementando il suo costo di un incrocio
		 *  (tramite la formula appostia per cui data una direzione, e quindi una posizione i nell'array, 
		 *  ricaviamo l'indice della sua opposta tramite la formula 3-i)
		 *  (infatti l'unico caso in cui una linea passera' per questo nodo sara' unicamente incrociandola
		 *  per il vincolo di svolta)
		 * -eliminiamo gli archi diagonali per imporre il vincolo di svolta
		 */
		int toIncreaseT = (target%4) % 2 == 0? target + 1: target -1;
		g.increaseWeight(toIncreaseT, (target & 0xFFFFFFFC) +  (3 - (toIncreaseT%4)), Graph.INTERSECTION);
		// analizziamo il nodo e lo eliminiamo
		g.removeNode(target);
		/*
		 * Ora consideriamo coppie di nodi per poi identificare il {@code BigNode} relativo.
		 * Sappiamo con certezza che, ad eccezione dei nodi target e source, un cammino interessa
		 * esattamente due nodi di ogni {@code BigNode} per cui passa.
		 */
		int current = p[target];
		int current1 = p[current];
		s.addNode(target);
		while ((current & 0xFFFFFFFC) != source)
		{
			int weight = g.getWeightRaw(current, current1);
			// se il {@code BigNode} rappresenta una svolta eliminiamo tutto il nodo
			if(weight == 3)
			{
				g.removeBigNode(current);
			}
			// altrimenti nel caso di un incrocio o un nodo che estende solamente in lunghezza il cammino
			else// if (weight == 1 || weight == 5)
			{
				/* Rimuoviamo i nodi interessati per impedire che un'altra linea possa condividere un arco
				 * con quella attuale
				 */
				g.removeNode(current);
				g.removeNode(current1);
				/* inoltre se l'arco non rappresenta una svolta dobbiamo incrementare il peso dell'arco ortogonale
				 * tra i nodi opposti di una svolta
				 */
				if(weight == 1)
				{
					int toIncrease = (current%4) % 2 == 0? current + 1: current -1;
					g.increaseWeight(toIncrease, (current & 0xFFFFFFFC) +  (3 - (toIncrease%4)), Graph.INTERSECTION);
				}
			}
			// aggiungiamo il BigNode alla sequenza e risaliamo di padre in padre
			s.addNode(current);
			current = p[current1];
			current1 = p[current];
		}
		source = current;
		//source
		int toIncreaseS = (source%4) % 2 == 0? source + 1: source -1;
		g.increaseWeight(toIncreaseS, (source & 0xFFFFFFFC) +  (3 - (toIncreaseS%4)), Graph.INTERSECTION);
		g.removeNode(source);
		s.addNode(source);
		g.removeDiagonalEdges(source); //rimuovo gli archi diagonali
		g.removeDiagonalEdges(target);
		return s;
	}

}

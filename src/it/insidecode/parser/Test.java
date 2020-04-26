package it.insidecode.parser;

import it.insidecode.core.Graph;
import it.insidecode.core.Router;

import java.util.ArrayList;
import java.util.List;


/**
 * Rappresenta un caso test comprensivo di: altezza e larghezza della griglia, contorni delle figure, coppie di nodi da collegare.
 * 
 * @author fscozzafava
 *
 */
public class Test 
{
	private List<Pair<Integer>> pairs = new ArrayList<Pair<Integer>>();  //punti inizio/fine da collegare
	private int width;
	private int height;
	private List<Sequence> figures = new ArrayList<Sequence>();
	
	/**
	 * Un nuovo {@link Test} e' costruto con la larghezza e l'altezza della griglia
	 * 
	 * @param gridWidth
	 * @param gridHeight
	 */
	public Test(int gridWidth, int gridHeight) {
		this.width = gridWidth;
		this.height = gridHeight;
		
	}
	
	/**
	 * Esegue l'algoritmo sul test corrente, ritorna in output una collezione di oggetti {@link Sequence} rappresentanti i cammini minimi (seconto la definizione del problema) trovati
	 * 
	 * @return una lista di oggetti {@link Sequence} rappresentante i percorsi minimi
	 */
	public List<Sequence> run()
	{
		Graph g = new Graph(width, height);
		for (Sequence x: figures)
		{
			for (Integer y: x)
				g.removeBigNode(y);
		}
		
		List<Sequence> res = new ArrayList<Sequence>();
		Router r = new Router(g);
		for (Pair<Integer> p: pairs)
		{
			res.add(r.dijkstra(g, p.getFirst(), p.getSecond()));
		}
		return res;
	}

	/**
	 * Aggiunge una coppia ({@link Pair}) di nodi da collegare al test
	 * 
	 * @param pair coppia di nodi
	 * @return true se l'operazione e' andata a buon fine, false altrimenti
	 */
	public boolean addNodePair(Pair<Integer> pair)
		{
			return pairs.add(pair);
		}
	
	/**
	 * Aggiunge un oggetto {@link Sequence} rappresentate il contorno di una figura al test
	 * @param s l'oggetto {@link Sequence} rappresentate il contorno della figura
	 */
	public void addFigure(Sequence s)
	{
		figures.add(s);
	}
	
	/**
	 * @return l'insieme degli oggetti {@link Sequence} rappresentati i contorni delle figure del test
	 */
	public List<Sequence> getFigures() { return figures;}

	/**
	 * @return la larghezza della griglia
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return l'altezza della griglia
	 */
	public int getHeight() {
		return height;
	}
	
}

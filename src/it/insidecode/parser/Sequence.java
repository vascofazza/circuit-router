package it.insidecode.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


/**
 * Rappresenta una sequenza iterabile di nodi del grafo come il contorno di una figura o una linea tracciata.
 * 
 * @author fscozzafava
 *
 */
public class Sequence implements Iterable<Integer>{
	
	/**
	 * Struttura dati che mantiene i nodi nell'ordine dato
	 */
	private List<Integer> seq = new ArrayList<Integer>();
	
	/**
	 * L'altezza della matrice del grafo
	 */
	private int height;
	
	/**
	 * Aggiunge un nodo alla sequenza
	 * 
	 * @param n il nodo da aggiungere
	 */
	public void addNode(Integer n) { seq.add(0, n);}
	
	/**
	 * Aggiunge una collezione di nodi alla sequenza
	 * 
	 * @param nodes collezione di nodi
	 */
	public void addNodes(Integer... nodes) { Collections.addAll(seq, nodes); }

	@Override
	public Iterator<Integer> iterator() 
	{
		return seq.iterator();
	}
	
	/**
	 * Una nuova sequenza e' costruita con l'altezza della griglia rappresentate il grafo
	 * 
	 * @param height
	 */
	public Sequence(int height)
	{
		this.height = height;
	}
	
	/**
	 * Istanzia una nuova sequenza
	 */
	public Sequence() {}
	
	/**
	 * @return l'altezza della griglia del grafo
	 */
	public int getHeight() { return height;}
	
	/**
	 * @return il primo nodo della sequenza, se la sequenza e' vuota ritorna null
	 */
	public Integer getFirst() {if(size() < 1) return null; return seq.get(0);}

	/**
	 * @return l'ultimo nodo della sequenza, se la sequenza e' vuota ritorna null
	 */
	public Integer getLast() {
		if(size() < 1) return null;
		return seq.get(seq.size()-1);
	}

	/**
	 * @return la numero di nodi della sequenza
	 */
	public int size() {
		return seq.size();
	}
}

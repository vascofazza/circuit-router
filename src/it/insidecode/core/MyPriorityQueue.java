package it.insidecode.core;

import java.util.PriorityQueue;

/**
 * Rappresenta una coda di priorita' basata sull'implementazione standard di {@link PriorityQueue} Java.
 * Ogni elemento viene incapsulato in un oggetto contenente un valore di priorita'
 * 
 * @author fscozzafava
 *
 * @param <T> tipo generico della coda
 */
public class MyPriorityQueue <T>
{

	/**
	 * Ogetto nel quale e' incapsulato un elemento della coda, mantiene il valore di priorita' permettendo inoltre 
	 * il confronto degli oggetti
	 * 
	 * @author fscozzafava
	 *
	 * @param <K>
	 */
	private class PriorityObject <K> implements Comparable<PriorityObject<K>>
	{
		private K k;
		private long p;
		public PriorityObject(K k, long p){ this.k = k; this.p = p;}
		public K getElement(){ return k;}
		@Override
		public int compareTo(PriorityObject<K> arg0) {
			return p <= arg0.p? -1 : 1;
		}
	}
	
	/**
	 * Coda interna
	 */
	private PriorityQueue<PriorityObject<T>> queue = new PriorityQueue<PriorityObject<T>>();
	
	/**
	 * Inserisce l'elemento o con priorita' p
	 * 
	 * @param o l'elemento
	 * @param p il valore di priorita'
	 * @return se l'operazione e' andata a buon fine
	 */
	public boolean offer(T o, long p)
	{
		PriorityObject<T> obb = new PriorityObject<T>(o,p);
		return queue.offer(obb);
	}
	
	/**
	 * Restituisce il valore minimo estraendolo dalla coda
	 * 
	 * @return il minimo
	 */
	public T getMin()
	{
		return queue.poll().getElement();
	}
	
	/**
	 * Ritorna un valore indicante se la coda e' vuota o meno
	 * 
	 * @return se la lista e' vuota
	 */
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}

}

package it.insidecode.parser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe responsabile della scrittura e codifica del file binario contenente i risultati dei casi test.
 * 
 * @author Federico Scozzafava
 *
 */
public class OutputParser {
	
	/**
	 * Buffer temporaneo
	 */
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
	
	/**
	 * Effettua il parsing (codifica in binario) della lista di oggetti {@link Sequence} passato in input bufferizzandolo temporaneamente
	 * 
	 * @param sequences
	 * @throws IOException
	 */
	public void parse(List<Sequence> sequences) throws IOException
	{
		for(Sequence s: sequences)
			buffer.write(encodeSequence(s));
	}
	
	/**
	 * Codifica un oggetto di tipo {@link Sequence} in una sequenza di byte definita nel seguente modo:
	 * Ogni oggetto {@link Sequence} e' rappresentato con 4 byte indicanti la coordinata x ed y del primo nodo, una serie di 
	 * byte intermedi (composti da un valore di svolta d per i 3 bit meno significativi e un valore di lunghezza k per i 5 bit piu' significativi) 
	 * rappresentanti k nodi in direzione d dall'ultimo nodo definito fino a quel momento, e un byte terminatore (0).
	 * 
	 * @param s oggetto {@link Sequence} da codificare
	 * @return un array di byte
	 */
	private byte[] encodeSequence(Sequence s)
	{
		if(s.size() == 0) return new byte[] {(byte) 255}; // una sequenza vuota viene codificata con il byte 255
		List<Byte> res = new ArrayList<Byte>();
		Iterator<Integer> i = s.iterator();
		int ppNode = i.next(); // prendiamo il primo elemento della sequenza e codifichiamo le coordinate x e y nei primi 4 byte della codifica
		res.add((byte) ((ppNode/4/s.getHeight())/256));
		res.add((byte) (((ppNode/4)/s.getHeight())%256));
		res.add((byte) (((ppNode/4)%s.getHeight())/256));
		res.add((byte) (((ppNode/4)%s.getHeight())%256));
		int pNode = ppNode;
		int counter = 0; // contatore di lunghezza della serie
		Direction d = null; // direzione della serie corrente di nodi
		while(i.hasNext())
		{
			int x = i.next();
			if (d == null) d = Direction.getDirection(pNode/4/s.getHeight(), (pNode/4)%s.getHeight(), x/4/s.getHeight(), (x/4)%s.getHeight());
			boolean direction = changeDirection(ppNode, pNode, x, s.getHeight()); // valore che indica se analizzando il nodo attuale si giunge ad una svolta
			if(counter == 31 && !direction) // se abbiamo raggiunto il limite di valori per la codifica usata senza incorrere in una svolta
			{
				res.add(make(d, counter)); // scriviamo il valore attuale (max 32 di lunghezza) e separiamo l'intervallo individuato dalla codifica in piu' parti
				counter = 0; // resettiamo il contatore per calcolare la nuova serie
			}
			if(direction) // se il nodo ci porta ad una svolta
			{
				res.add(make(d, counter)); // scriviamo il valore corrente immediatamente prima del nodo svolta e prepariamo le variabili per contare i nodi nella nuova direzione
				d = Direction.getDirection(pNode/4/s.getHeight(), (pNode/4)%s.getHeight(), x/4/s.getHeight(), (x/4)%s.getHeight());
				ppNode = pNode;
				pNode = x;
				counter = 1; // dal momento che abbiamo prelevato un nuovo nodo, il contatore partira' da 1
			}
			else{ 
				counter++;
				ppNode = pNode;
				pNode = x;
			}
		}
		res.add(make(d, counter)); // siamo giunti alla fine della sequenza, scriviamo il valore rappresentante i nodi rimanenti
		res.add((byte) 0); // aggiungiamo il byte terminatore
		byte[] result = new byte[res.size()];
		counter = 0;
		for(byte b: res) result[counter++] = b;
		return result;
	}

	/**
	 * Dati i valori rappresentanti 3 nodi, ritorna vero se i nodi rappresentano una svolta, false altrimenti
	 * 
	 * @param node1
	 * @param node2
	 * @param node3
	 * @param height l'altezza della griglia del caso test
	 * @return se c'e' stato un cambio direzione
	 */
	private boolean changeDirection(int node1, int node2, int node3, int height) {
		node1 = node1/4;
		node2 = node2/4;
		node3 = node3/4;
		int x1 = node1/height;
		int y1 = node1%height;
		int x2 = node2/height;
		int y2 = node2%height;
		int x3 = node3/height;
		int y3 = node3%height;
		return !((x1 == x2 && x2 == x3) || (y1 == y2 && y2 == y3));
	}

	/**
	 * Data un valore di direzione {@link Direction} e un numero k di nodi codifica il byte rappresentante k nodi in direzione d
	 * 
	 * @param d {@link Direction} rappresentante la direzione dei nodi
	 * @param counter il numero di nodi
	 * @return la codifica del byte
	 */
	private byte make(Direction d, int counter) {
		int res = counter;
		res <<= 3;
		res |= d.ordinal();
		return (byte) res;
	}
	
	
	// da rimuovere
	public static String toString(List<Byte> b)
	{
		StringBuffer sb = new StringBuffer();
		for(byte bb : b)
			sb.append(Integer.toHexString(bb&0xff).toUpperCase()).append(" ");
		return sb.toString();
	}

	/**
	 * Scrive su disco il contenuto presente nel buffer temporaneo
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public void write(String fileName) throws IOException {
		File f = new File(fileName);
		FileOutputStream stream = new FileOutputStream(f);
		stream.write(buffer.toByteArray());
		stream.close();
	}

}

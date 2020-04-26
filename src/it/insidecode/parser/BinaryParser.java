package it.insidecode.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BinaryParser
{
	/**
	 * Stream di input del file
	 */
	private FileInputStream stream;
	
	/**
	 * Contatore globale dei byte letti
	 */
	private int currentIndex = 0;
	
	public BinaryParser(String string) {
		File file = new File(string);
		try {
			stream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * Preso in input il path relativo del file da esaminare, restituisce in output la lista di {@link Test} rappresentate i casi test codificati dal file.
	 * 
	 * @param path path del file
	 * @return la lista dei casi test, rappresentati dalla classe {@link Test}
	 * @throws ParsingException nel caso in cui il file di input presenti irregolarita'
	 * @throws IOException nel caso in cui il file non esista o sia danneggiato
	 */
	public List<Sequence> parse(int size, int gridHeight) throws ParsingException, IOException
	{	
		
		List<Sequence> tests = new ArrayList<Sequence>(); //alloco una lista che conterra' i casi test
		int lastVal = 0;
		//int N = parseNextValue(); //numero di casi test (il primo byte del file)
//		while (lastVal != -1)
//		{
//
			currentIndex = 0; //indice di byte letti relativo al singolo caso test
			while (size-- >= 1) //faccio il parsing degli elmenti E1, E2, ... , Ek
			{
				Sequence s = new Sequence(gridHeight); //istanzio una nuova sequenza che rappresentera' il contorno di una figura
				int vv = readByte();
				if (vv == 255) {continue;}
				int xx = 256 * vv + readByte();
				int firstNode = (xx*gridHeight + parseNextValue())*4; 
				/*il primo nodo della sequenza e' rappresentato da una coppia di coordinate x e y
				ogni nodo, nella rappresentazione del grafo, e' identificato da un intero costruito nel seguente modo:
				((x*h+y)*4)*/
				s.addNode(firstNode);
				int node = firstNode;
				int val;
				while((val = readByte()) > 0) // il byte 0 rappresenta la fine di una sequenza
				{
					Integer[] nodes = parseNextNode(node, gridHeight, val);
					if (nodes.length < 1) break;
					node = nodes[nodes.length-1]; // mi salvo l'ultimo nodo della sequenza per il calcolo successivo
					s.addNodes(nodes); // aggiungo alla sequenza la lista parziale dei nodi
				}
				lastVal =val;
				tests.add(s); // aggiungo la figura al test
			}
//		}
//		stream.close(); //chiudo il file stream e rilascio le risorse
		return tests;
	}
	

	/**
	 * Dato un nodo, l'altezza della griglia e il valore da decodificare, crea e restituisce la sequenza di k nodi in
	 * direzione d dal nodo indicato.
	 * 
	 * @param node il nodo di partenza
	 * @param height l'altezza della griglia
	 * @param val il valore da decodificare
	 * @return una sequenza di k nodi in direzione d
	 */
	private Integer[] parseNextNode(int node, int height, int val) {
		Direction d = Direction.values()[val & 0x07]; // prendo i 3 bit meno significativi di val e ricavo la direzione
		int k = val >> 3; // prendo i bit piu' significativi e ricavo il numero di nodi da istanziare
		Integer[] array = new Integer[k];
		int x = node/4/height; // ricavo la componente x dal nodo
		int y = (node/4)%height; // ricavo la componente y dal nodo
		for(int i = 0; i < k; i++) 
			array[i] = ((x+(i+1)*d.getDx())*height + (y+(i+1)*d.getDy()))*4;
		return array;
	}

	/**
	 * Ritorna un valore intero rappresentato da due byte consecutivi
	 * 
	 * @return il prossimo valore nel file binario
	 */
	private int parseNextValue()
	{
		return 256 * readByte() + readByte();
	}
	
	/**
	 * Legge il prossimo byte dallo stream di input
	 * 
	 * @return il prossimo byte nel file binario
	 */
	private int readByte()
	{
		currentIndex++; //incremento l'indice dei byte letti
		try 
		{
			return stream.read() & 0xff; // fix per ovviare all'implementazione dei signed byte in java
		} 
		catch(IOException e) 
		{
			e.printStackTrace();
		}
		return -1; //in caso di errore ritorno -1
	}
}

package it.insidecode.main;

import java.util.Comparator;
import java.util.PriorityQueue;

public class MainTest {
	
	private static int[] dist = new int[10];
	
	private static Integer[] ints = new Integer[10];
	
	public static class IntegerComparator implements Comparator<Integer>
	{

		@Override
		public int compare(Integer arg0, Integer arg1) {
			return dist[arg0] < dist[arg1] ? -1 : dist[arg0] == dist[arg1]? 0: 1;
		}
		
	}
	
	public static void main(String[] args) {
		PriorityQueue<Integer> q = new PriorityQueue<Integer>(11, new IntegerComparator());
		for (int i = 0; i < 10; i++)
			{
				dist[i] = 11-i;
				q.offer(ints[i] = new Integer(i));
			}
		dist[3] = 0;
		//q.offer(ints[3]);
		//int min = q.poll();
		//q.remove(3);
		//dist[5] = 1;
		//q.offer(ints[5]);
//		min = q.poll();
//		min = q.poll();
//		min = q.poll();
		
	}

}

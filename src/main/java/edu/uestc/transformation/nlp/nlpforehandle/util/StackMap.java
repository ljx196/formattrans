package edu.uestc.transformation.nlp.nlpforehandle.util;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Stack;


public class StackMap<K,V> {
	private Stack<K> keyStack=new Stack<>();
	private HashMap<K, V> map=new HashMap<K,V>();
	
	public synchronized void push(K k,V v)
	{
		keyStack.push(k);
		map.put(k, v);
	}
	public Entry<K, V> pop()
	{
		final K k=keyStack.pop();
		final V v=map.get(k);
		map.remove(k);
		return new KeyValuePair<K,V>(k, v);
	}
	public boolean isEmpty()
	{
		return this.keyStack.isEmpty();
	}
}

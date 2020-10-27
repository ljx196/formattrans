package edu.uestc.transformation.nlp.nlpforehandle.util;

import java.util.Map.Entry;

public class KeyValuePair<K,V> implements Entry<K, V>{
	private K k;
	private V v;
	public KeyValuePair(K k,V v) {
		this.k=k;
		this.v=v;
	}

	@Override
	public K getKey() {
		return this.k;
	}

	@Override
	public V getValue() {
		return this.v;
	}

	@Override
	public V setValue(V value) {
		V v=this.v;
		this.v=value;
		return v;
	}
}

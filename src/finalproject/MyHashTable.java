package finalproject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;


public class MyHashTable<K,V> implements Iterable<MyPair<K,V>>{
	// num of entries to the table
	private int size;
	// num of buckets 
	private int capacity = 16;
	// load factor needed to check for rehashing 
	private static final double MAX_LOAD_FACTOR = 0.75;
	// ArrayList of buckets. Each bucket is a LinkedList of HashPair
	private ArrayList<LinkedList<MyPair<K,V>>> buckets; 


	// constructors
	public MyHashTable() {
		this.size = 0 ;
		ArrayList<LinkedList<MyPair<K,V>>> bucket = new ArrayList<LinkedList<MyPair<K,V>>>();
		for( int i = 0 ; i < 16 ; i ++ ){
			bucket.add(new LinkedList<MyPair<K,V>>());
		}
		this.buckets=bucket;
	}

	public MyHashTable(int initialCapacity) {
		this.size = 0 ;
		this.capacity = initialCapacity;
		ArrayList<LinkedList<MyPair<K,V>>> bucket = new ArrayList<LinkedList<MyPair<K,V>>>();
		for( int i = 0 ; i < this.capacity ; i ++ ){
			bucket.add(new LinkedList<MyPair<K,V>>());
		}
		this.buckets=bucket;
	}

	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return this.size == 0;
	}

	public int numBuckets() {
		return this.capacity;
	}

	/**
	 * Returns the buckets variable. Useful for testing  purposes.
	 */
	public ArrayList<LinkedList< MyPair<K,V> > > getBuckets(){
		return this.buckets;
	}

	/**
	 * Given a key, return the bucket position for the key. 
	 */
	public int hashFunction(K key) {
		int hashValue = Math.abs(key.hashCode())%this.capacity;
		return hashValue;
	}

	/**
	 * Takes a key and a value as input and adds the corresponding HashPair
	 * to this HashTable. Expected average run time  O(1)
	 */
	public V put(K key, V value) {
		// get appropriate indexed list
		int index = Math.abs(key.hashCode()%this.capacity);
		MyPair<K,V> element = new MyPair<K,V>(key,value);
		LinkedList<MyPair<K,V>> list = this.buckets.get(index);
		// search through the index
		for (int i = 0; i < list.size(); i++) {
			K currKey = list.get(i).getKey();
			if (currKey.equals(key)) {
				// key already exist so replace value
				V currVal = list.get(i).getValue();
				list.get(i).setValue(value);
				return currVal;
			}
		}
		list.add(element);
		this.size++;
		// if load factor is over the max, rehash
		if(MAX_LOAD_FACTOR<(double)(this.size/this.capacity)){
			this.rehash();
		}return null;
	}


	/**
	 * Get the value corresponding to key. Expected average runtime O(1)
	 */

	public V get(K key) {
		int index = Math.abs(key.hashCode()%this.capacity);
		LinkedList<MyPair<K,V>> list = this.buckets.get(index);
		for(int i = 0 ; i < list.size() ; i ++ ){
			if(list.get(i).getKey().equals(key)){
				return list.get(i).getValue();
			}
		}return null;
	}

	/**
	 * Remove the HashPair corresponding to key . Expected average runtime O(1) 
	 */
	public V remove(K key) {
		V rmValue;
		int index = Math.abs(key.hashCode()%this.capacity);
		LinkedList<MyPair<K,V>> list = this.buckets.get(index);
		for(int i = 0 ; i < list.size() ; i ++ ){
			if(list.get(i).getKey().equals(key)){
				rmValue = list.get(i).getValue();
				list.remove(i);
				this.size--;
				return rmValue;
			}
		}return null;
	}


	/** 
	 * Method to double the size of the hashtable if load factor increases
	 * beyond MAX_LOAD_FACTOR.
	 * Made public for ease of testing.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */

	public void rehash() {
		ArrayList<LinkedList<MyPair<K,V>>> temp = this.buckets;
		ArrayList<LinkedList<MyPair<K,V>>> emptyBucket = new ArrayList<LinkedList<MyPair<K,V>>>();

		for( int i = 0 ; i < this.capacity*2 ; i ++ ){
			emptyBucket.add(new LinkedList<MyPair<K,V>>());
		}this.buckets = emptyBucket;

		for(int i = 0 ; i < this.capacity ; i++ ){
			LinkedList<MyPair<K,V>> list = temp.get(i);
			for( int j = 0 ; j < (list.size()) ; j++) {
				MyPair<K,V> curr =  list.get(j);
				int index = Math.abs(curr.getKey().hashCode())%(this.capacity*2);
				this.buckets.get(index).add(curr);
			}
		}this.capacity+=this.capacity;
	}

	/**
	 * Return a list of all the keys present in this hashtable.
	 * Expected average runtime is O(m), where m is the number of buckets
	 */

	public ArrayList<K> getKeySet() {
		ArrayList<K> keySet = new ArrayList<K>();
		for(int i = 0 ; i < this.capacity ; i++){
			LinkedList<MyPair<K,V>> list = this.buckets.get(i);
			for( int j = 0 ; j < list.size() ; j++ ){
				keySet.add(list.get(j).getKey());
			}
		}return keySet;
	}

	/**
	 * Returns an ArrayList of unique values present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */
	public ArrayList<V> getValueSet() {
		ArrayList<V> valueSet = new ArrayList<V>();
		for(int i = 0 ; i < this.capacity ; i++){
			LinkedList<MyPair<K,V>> list = this.buckets.get(i);
			for( int j = 0 ; j < list.size() ; j++ ){
				if(!valueSet.contains(list.get(j).getValue())) {
					valueSet.add(list.get(j).getValue());
				}
			}
		}return valueSet;
	}


	/**
	 * Returns an ArrayList of all the key-value pairs present in this hashtable.
	 * Expected average runtime is O(m) where m is the number of buckets
	 */
	public ArrayList<MyPair<K, V>> getEntries() {
		ArrayList<MyPair<K, V>> entries = new ArrayList<MyPair<K, V>>();
		for(int i = 0 ; i < this.capacity ; i++){
			LinkedList<MyPair<K,V>> list = this.buckets.get(i);
			for(int j = 0 ; j< list.size() ; j++){
				entries.add(list.get(j));
			}
		}return entries;
	}

	public int getCapacity(){
		return this.capacity;
	}

	
	
	@Override
	public MyHashIterator iterator() {
		return new MyHashIterator();
	}   

	
	private class MyHashIterator implements Iterator<MyPair<K,V>> {

		private int currentBucketIndex;
		private Iterator<MyPair<K,V>> currentBucketIterator;

		private MyHashIterator() {
			this.currentBucketIndex = -1;
			this.currentBucketIterator = null;
			while (++currentBucketIndex < buckets.size()) {
				LinkedList<MyPair<K,V>> currentBucket = buckets.get(currentBucketIndex);
				if (!currentBucket.isEmpty()) {
					currentBucketIterator = currentBucket.iterator();
					return;
				}
			}
			currentBucketIterator = null;
		}

		@Override
		public boolean hasNext() {
			boolean isNull = true;
			if (currentBucketIterator == null){return false;}
			if (currentBucketIterator.hasNext()){return true;}
			while (++currentBucketIndex < buckets.size()) {
				LinkedList<MyPair<K,V>> currentBucket = buckets.get(currentBucketIndex);
				if (!currentBucket.isEmpty()) {
					currentBucketIterator = currentBucket.iterator();
					isNull = false;
					break;
				}
			}if(isNull){currentBucketIterator = null;}
			return currentBucketIterator != null;
		}

		@Override
		public MyPair<K,V> next() {
			if(!hasNext()){return null;}
			MyPair<K,V> pair = currentBucketIterator.next();
			return pair;
		}

	}
	
}

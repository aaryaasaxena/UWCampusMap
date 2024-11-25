// == CS400 Fall 2024 File Header Information ==
// Name: Aarya Saxena
// Email: asaxena26@wisc.edu
// Group: P2.3607
// Lecturer: Florian Heimerl
// Notes to Grader: n/a

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class HashtableMap<KeyType, ValueType> implements MapADT{

  protected LinkedList<Pair>[] table = null;

  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity){
    if (capacity <= 0){
      throw new IllegalArgumentException("capacity should be greater than 0");
    }

    table = (LinkedList<Pair>[]) new LinkedList[capacity];

    System.out.println("init with capacity: " + table.length);
  }

  @SuppressWarnings("unchecked")
  public HashtableMap(){
    this(64);
  }


  protected class Pair {

    public KeyType key;
    public ValueType value;

    public Pair(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
    }

  }


  /**
   * Adds a new key,value pair/mapping to this collection.
   *
   * @param key   the key of the key,value pair
   * @param value the value that key maps to
   * @throws IllegalArgumentException if key already maps to a value
   * @throws NullPointerException     if key is null
   */
  @Override
  public void put(Object key, Object value) throws IllegalArgumentException {

    if (key == null){
      throw new NullPointerException("key cannot be null");
    }
    if (this.containsKey(key)){
      throw new IllegalArgumentException("key already maps to a value!");
    }

    Pair pair = new Pair((KeyType) key, (ValueType) value);


    int index = Math.abs(key.hashCode()) % this.getCapacity();

    if (table[index] == null) {
      table[index] = new LinkedList<>();
    }
    table[index].add(pair);

    rehash();
  }

  private double getLoadFactor() {
    return (double) this.getSize() / this.getCapacity();
  }


  private void rehash(){
    if (getLoadFactor() >= 0.8) {
      LinkedList<Pair>[] doubleCap = (LinkedList<Pair>[]) new LinkedList[table.length * 2];

      for (LinkedList<Pair> pairs : table) {
        if (pairs != null) {
          for (Pair pair : pairs) {
            int index = Math.abs(pair.key.hashCode()) % doubleCap.length;
            if (doubleCap[index] == null) {
              doubleCap[index] = new LinkedList<>();
            }
            doubleCap[index].add(pair);
          }
        }
      }
      table = doubleCap;
    }
  }

  /**
   * Checks whether a key maps to a value in this collection.
   *
   * @param key the key to check
   * @return true if the key maps to a value, and false is the key doesn't map to a value
   */
  @Override
  public boolean containsKey(Object key) {
    try{
      get(key);
      return true;
    }
    catch (NoSuchElementException e){
      return false;
    }
  }

  /**
   * Retrieves the specific value that a key maps to.
   *
   * @param key the key to look up
   * @return the value that key maps to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public Object get(Object key) throws NoSuchElementException {

    for (LinkedList<Pair> pairs : table) {
      if (pairs != null) {
        for (Pair pair : pairs) {
          if (key.equals(pair.key)) {
            return pair.value;
          }
        }
      }
    }
    throw new NoSuchElementException("key does not exist in collection");
  }

  /**
   * Remove the mapping for a key from this collection.
   *
   * @param key the key whose mapping to remove
   * @return the value that the removed key mapped to
   * @throws NoSuchElementException when key is not stored in this collection
   */
  @Override
  public Object remove(Object key) throws NoSuchElementException {
    if (!containsKey(key)){
      throw new NoSuchElementException("key is not in collection!");
    }

    Object value = get(key);

    int index = key.hashCode() % this.getCapacity();

    LinkedList<Pair> pairs = table[index];
    if (pairs != null){
      for (Pair pair: pairs){
        if (key.equals(pair.key)){
          pairs.remove(pair);
          return value;
        }
      }
    }
    throw new NoSuchElementException("key is not in collection!"); // backup, but should never be
    // reached
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {
    for (int i = 0; i < table.length; i++) {
      table[i] = null; // clear all buckets
    }
  }

  /**
   * Retrieves the number of keys stored in this collection.
   *
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    int size = 0;
    for (LinkedList<Pair> bucket : table) {
      if (bucket != null) {
        size += bucket.size();
      }
    }
    return size;
  }

  /**
   * Retrieves this collection's capacity.
   *
   * @return the size of te underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return table.length;
  }



  @Test
  public void testPutAndGet(){
    HashtableMap<Integer, String> hashtable = new HashtableMap<>();

    hashtable.put(100, "data1"); // add keys to map, ensure put works
    hashtable.put(200, "data2");
    hashtable.put(322, "data3");
    hashtable.put(443, "data4");



    assertEquals("data1", hashtable.get(100)); // test that get returns the correct data
    assertEquals("data2", hashtable.get(200));
    assertEquals("data3", hashtable.get(322));
    assertEquals("data4", hashtable.get(443));

  }

  @Test
  public void testDuplicatePut(){
    HashtableMap<Integer, String> hashtable = new HashtableMap<>();
    hashtable.put(100, "key1");
    hashtable.put(200, "key2"); // Uudate value for same key
    assertEquals("key2", hashtable.get(200), "failed to update value for existing key");
  }

  @Test
  public void testContainsKey(){
    HashtableMap<Integer, String> hashtable = new HashtableMap<>();

    hashtable.put(100, "data1"); // add keys to map
    hashtable.put(200, "data2");
    hashtable.put(322, "data3");
    hashtable.put(443, "data4");

    assertTrue(hashtable.containsKey(100), "contains key, should be TRUE"); // true, key is there
    assertFalse(hashtable.containsKey(500), "does not exist, should be FALSE"); // false, key
    // does not exist

  }

  @Test
  public void testRemove(){
    HashtableMap<Integer, String> hashtable = new HashtableMap<>();

    hashtable.put(100, "data1"); // add values to map
    hashtable.put(200, "data2");
    hashtable.put(322, "data3");
    hashtable.put(443, "data4");



    assertEquals("data3", hashtable.remove(322), "failed to remove key"); //should remove key

  }

  @Test
  public void testResize() {
    HashtableMap<Integer, String> hashtable = new HashtableMap<>(2); // Small initial capacity
    hashtable.put(1, "one");
    hashtable.put(2, "two");
    hashtable.put(3, "three"); // resize!


    assertEquals("one", hashtable.get(1), "failed to retrieve value after resizing.");
    assertEquals("two", hashtable.get(2), "failed to retrieve value after resizing.");
    assertEquals("three", hashtable.get(3), "failed to retrieve value after resizing.");
  }

  @Test
  public void testClear(){
    HashtableMap<Integer, String> hashtable = new HashtableMap<>(2);
    hashtable.put(1, "data1");
    hashtable.put(2, "data2");

    hashtable.clear();

    assertEquals(0, hashtable.getSize()); // table size should be 0 after clear

  }

}

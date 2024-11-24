
import org.junit.jupiter.api.Test;

import java.util.Hashtable;
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

  }

  /**
   * Checks whether a key maps to a value in this collection.
   *
   * @param key the key to check
   * @return true if the key maps to a value, and false is the key doesn't map to a value
   */
  @Override
  public boolean containsKey(Object key) {
    return false;
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
    return null;
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
    return null;
  }

  /**
   * Removes all key,value pairs from this collection.
   */
  @Override
  public void clear() {

  }

  /**
   * Retrieves the number of keys stored in this collection.
   *
   * @return the number of keys stored in this collection
   */
  @Override
  public int getSize() {
    return 0;
  }

  /**
   * Retrieves this collection's capacity.
   *
   * @return the size of te underlying array for this collection
   */
  @Override
  public int getCapacity() {
    return 0;
  }



  @Test
  public void testPutAndGet(){
    Hashtable<Integer, String> hashtable = new Hashtable<>();

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
    assertEquals(200, hashtable.get("key1"), "failed to update value for existing key");
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

    assertEquals(322, hashtable.remove("data3"), "failed to remove key"); //should remove key
    assertFalse(hashtable.containsKey("key1"), "key should be null now"); // key should be null now

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

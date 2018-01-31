package com.saphire.iopush.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.map.LRUMap;
import org.springframework.stereotype.Component;
@Component
public class Cache {

	private long timeToLive;
    public LRUMap lRUMap;
 
    /*protected class object {
        public long lastAccessed = System.currentTimeMillis();
        public T value;
 
        protected object(T value) {
            this.value = value;
        }

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
			this.value = value;
		}
        
    }
*/ 
    
    public Cache() {
        lRUMap = new LRUMap();
    }
    
    public Cache(int maxItems) {
        this.timeToLive = timeToLive * 1000;
 
        lRUMap = new LRUMap(maxItems);
 
        /*if (timeToLive > 0 && timerInterval > 0) {
 
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(timerInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });
 
            t.setDaemon(true);
            t.start();
        }*/
    }
 
    @SuppressWarnings("unchecked")
	public void put(Object key, HashMap<String,String> value) {
        	lRUMap.put(key, value);
    }
 
    @SuppressWarnings("unchecked")
    public HashMap<String,String> get(Object key) {
    	HashMap<String,String> c = (HashMap<String, String>) lRUMap.get(key);
 
            if (c == null)
                return null;
           /* else {
                c.setLastAccessed( System.currentTimeMillis());
                return c.getValue();
            }*/
            return c;
    }
 
    @SuppressWarnings("unchecked")
	public Map<Object, HashMap<String,String>> keyValuePair()
    {
    	MapIterator<Object, HashMap<String,String>> itr = lRUMap.mapIterator();
    	Map<Object, HashMap<String,String>> hm = new HashMap<>();
    	while (itr.hasNext()) {
    		Object key = itr.next();
    		HashMap<String,String> c = (HashMap<String,String>) itr.getValue();
            hm.put(key, c);
        }
    	return hm;
    }
    
    public void remove(Object key) {
        	lRUMap.remove(key);
    }
 
    public int size() {
            return lRUMap.size();
    }
 
    @SuppressWarnings("unchecked")
	public Set<Map.Entry<Object, HashMap<String,String>>> entrySet(){
    	return lRUMap.entrySet();
    	
    }
    
    
    @SuppressWarnings("unchecked")
    public void cleanup() {
 
        long now = System.currentTimeMillis();
        ArrayList<Object> deleteKey = null;
 
            MapIterator itr = lRUMap.mapIterator();
 
            deleteKey = new ArrayList<Object>((lRUMap.size() / 2) + 1);
            Object key = null;
            HashMap<String,String> c = null;
 
            while (itr.hasNext()) {
                key = (Object) itr.next();
                c = (HashMap<String, String>) itr.getValue();
 
                if (c != null) {
                    deleteKey.add(key);
                }
            }
 
        for (Object dkey : deleteKey) {
            	lRUMap.remove(dkey);
        }
    }
}

package me.xethh.libs.toolkits.cache;

import org.junit.Test;

import java.util.Date;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.Assert.assertEquals;

public class LRUCacheTest {
    @Test
    public void baseMapTest(){
        LRUCache.LRUMap<String, String> cache = new LRUCache.LRUMap<String, String>(3);
        cache = new LRUCache.LRUMap<String, String>(3);
        assertEquals(0,cache.size());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        assertEquals(1,cache.size());
        assertEquals("{1=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        assertEquals(2,cache.size());
        assertEquals("{1=2, 2=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        cache.put("3","2");
        assertEquals(3,cache.size());
        assertEquals("{1=2, 2=2, 3=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        cache.put("3","2");
        cache.put("4","2");
        assertEquals(3,cache.size());
        assertEquals("{2=2, 3=2, 4=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        cache.put("3","2");
        cache.put("4","2");
        cache.put("5","2");
        assertEquals(3,cache.size());
        assertEquals("{3=2, 4=2, 5=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        cache.put("3","2");
        cache.put("4","2");
        cache.put("5","2");
        cache.get("3");
        assertEquals(3,cache.size());
        assertEquals("{4=2, 5=2, 3=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        cache.put("3","2");
        cache.put("4","2");
        cache.put("5","2");
        cache.getOrDefault("3","7");
        assertEquals(3,cache.size());
        assertEquals("{4=2, 5=2, 3=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        cache.put("3","2");
        cache.put("4","2");
        cache.put("5","2");
        cache.get("8");
        assertEquals(3,cache.size());
        assertEquals("{3=2, 4=2, 5=2}",cache.toString());

        cache = new LRUCache.LRUMap<String, String>(3);
        cache.put("1","2");
        cache.put("2","2");
        cache.put("3","2");
        cache.put("4","2");
        cache.put("5","2");
        cache.getOrDefault("8","1");
        assertEquals(3,cache.size());
        assertEquals("{3=2, 4=2, 5=2}",cache.toString());
    }

    @Test
    public void testCache(){
        LRUCache<Integer, String> cache = null;
        cache = LRUCache.<Integer, String>of(3, (x) -> x+"");
        assertEquals(0,cache.map().size());
        assertEquals(0,cache.size());
        assertEquals(3,cache.map().getMaxEntries());
        assertEquals("LRUCache{map={}}",cache.toString());

        cache = LRUCache.<Integer, String>of(3, (x) -> x+"");
        cache.get(10);
        assertEquals(1,cache.map().size());
        assertEquals(1,cache.size());
        assertEquals(3,cache.map().getMaxEntries());
        assertEquals("LRUCache{map={10=10}}",cache.toString());

        cache = LRUCache.<Integer, String>of(3, (x) -> x+"");
        cache.get(10);
        cache.get(20);
        assertEquals(2,cache.map().size());
        assertEquals(2,cache.size());
        assertEquals(3,cache.map().getMaxEntries());
        assertEquals("LRUCache{map={10=10, 20=20}}",cache.toString());

        cache = LRUCache.<Integer, String>of(3, (x) -> x+"");
        cache.get(10);
        cache.get(20);
        cache.get(10);
        assertEquals(2,cache.map().size());
        assertEquals(2,cache.size());
        assertEquals(3,cache.map().getMaxEntries());
        assertEquals("LRUCache{map={20=20, 10=10}}",cache.toString());

        cache = LRUCache.<Integer, String>of(3, (x) -> x+"");
        cache.get(10);
        cache.get(20);
        cache.get(10);
        cache.get(40);
        assertEquals(3,cache.map().size());
        assertEquals(3,cache.size());
        assertEquals(3,cache.map().getMaxEntries());
        assertEquals("LRUCache{map={20=20, 10=10, 40=40}}",cache.toString());

        cache = LRUCache.<Integer, String>of(3, (x) -> x+"");
        cache.get(10);
        cache.get(20);
        cache.get(10);
        cache.get(40);
        cache.get(100);
        assertEquals(3,cache.map().size());
        assertEquals(3,cache.size());
        assertEquals(3,cache.map().getMaxEntries());
        assertEquals("LRUCache{map={10=10, 40=40, 100=100}}",cache.toString());

        cache = LRUCache.<Integer, String>of(3, (x) -> {
            if(x<1000) return x+"";
            else return null;
        });
        cache.get(10);
        cache.get(20);
        cache.get(10);
        cache.get(1000);
        cache.getOrDefault(1000,"hi");
        cache.getOrDefault(1000,()->"hi");
        cache.get(40);
        cache.get(100);
        assertEquals(3,cache.map().size());
        assertEquals(3,cache.size());
        assertEquals(3,cache.map().getMaxEntries());
        assertEquals("LRUCache{map={10=10, 40=40, 100=100}}",cache.toString());
    }

    @Test
    public void testException(){

        LRUCache<Integer, String> cache = LRUCache.of(3, (x) -> {
            throw new RuntimeException("Exception xxx");
        });
        try {
            cache.get(100);
        }
        catch (RuntimeException ex){
            assertEquals(ex.getMessage(),"Exception xxx");
        }

        cache = new LRUCache(3){
            @Override
            public Function getValueProvider() {
                return (x)->null;
            }

            @Override
            public Supplier ifNull() {
                throw new RuntimeException("Exception yyyy");
            }
        };
        try {
            cache.get(100);
        }
        catch (RuntimeException ex){
            assertEquals(ex.getMessage(),"Exception yyyy");
        }
    }
}

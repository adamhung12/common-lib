package me.xethh.libs.toolkits.cache;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Cache<K,V> {
    default V get(K key){
       V value = getInternal(key);
       if(value!=null) return value;

       value = getValueProvider().apply(key);
       if(value!=null){
           cacheInternal(key, value);
           return value;
       }

       return ifNull().get();
    }
    default V getOrDefault(K key, Supplier<V> valueProvider){
        V value = get(key);
        return value==null?valueProvider.get():value;
    }
    default V getOrDefault(K key, V defaultValue){
        V value = get(key);
        return value==null?defaultValue:value;
    }

    Function<K,V> getValueProvider();

    V getInternal(K key);
    void cacheInternal(K key, V value);

    default Supplier<V> ifNull(){
        return ()->null;
    }

    int size();
}

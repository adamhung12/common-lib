package me.xethh.libs.mtt.MessageTransferTool.commons;

import java.util.Objects;

public class Tuple2<V1,V2> {
    protected V1 v1;
    protected V2 v2;

    protected Tuple2(V1 v1, V2 v2){
        this.v1 = v1;
        this.v2 = v2;
    }
    public static <V1,V2> Tuple2<V1,V2> of(V1 v1, V2 v2){
        return new Tuple2<V1,V2>(v1,v2);
    }
    public V1 getV1() {
        return v1;
    }

    public V2 getV2() {
        return v2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
        return Objects.equals(v1, tuple2.v1) &&
                Objects.equals(v2, tuple2.v2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2);
    }

    @Override
    public String toString() {
        return "Tuple2{" +
                "v1=" + v1 +
                ", v2=" + v2 +
                '}';
    }
}

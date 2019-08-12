package me.xethh.libs.toolkits.webDto.core.category;

import me.xethh.libs.toolkits.webDto.core.request.Request;

public interface Confidential {
    public abstract class ConfidentialAbs implements Confidential{
        @Override public String toString(){
            return confidentialToString();
        }
        public abstract String confidentialToString();
    }

    public abstract class ConfidentialRequest<Data> extends Request<Data> implements Confidential{
        @Override public String toString(){
            return confidentialToString();
        }
        public abstract String confidentialToString();
    }
}

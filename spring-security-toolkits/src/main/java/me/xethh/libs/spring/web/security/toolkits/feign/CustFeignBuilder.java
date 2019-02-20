package me.xethh.libs.spring.web.security.toolkits.feign;

import feign.Feign;
import feign.ReflectiveFeign;
import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;

import java.util.ArrayList;
import java.util.List;

public class CustFeignBuilder<X extends CustFeignBuilder<X>> extends Feign.Builder {
    List<RequestInterceptor> interceptors = new ArrayList<>();

    public interface PreBuilder{
        void operation(CustFeignBuilder builder);
    }
    public static CustFeignBuilder builder(){
        return new CustFeignBuilder();
    }

    public <Interceptor extends MDCInterceptor> X mdcInterceptor(Interceptor interceptor){
        return (X) this.requestInterceptor(interceptor);
    }

    public X basicAuthentication(String username, String password){
        BasicAuthRequestInterceptor interceptor = new BasicAuthRequestInterceptor(username,password);
        requestInterceptor(interceptor);
        return (X) this;
    }

    public X preInterceptor(RequestInterceptor interceptor){
        this.interceptors.add(interceptor);
        return (X) this;
    }

    @Override
    public Feign build() {
        this.interceptors.stream().forEach(x->requestInterceptor(x));
        return super.build();
    }
}

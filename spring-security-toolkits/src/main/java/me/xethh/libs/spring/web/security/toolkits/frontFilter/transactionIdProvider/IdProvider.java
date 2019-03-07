package me.xethh.libs.spring.web.security.toolkits.frontFilter.transactionIdProvider;

@FunctionalInterface
public interface IdProvider {
    String gen();
}

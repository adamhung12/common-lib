package me.xethh.libs.spring.web.security.toolkits.frontFilter;

import java.util.Arrays;
import java.util.List;

public class TracingSystemConst {
    public static String APP_NAME = "APP-NAME";
    public static String TRANSACTION_HEADER = "CUST-TRANSACTION-ID";
    public static String TRANSACTION_LEVEL = "CUST-TRANSACTION-LEVEL";
    public static String TRANSACTION_AGENT = "CUST-TRANSACTION-AGENT";
    public static String TRANSACTION_SESSION_ID = "CUST-TRANSACTION-SESSION-ID";
    public static String TRANSACTION_CLIENT_ID = "CUST-CLIENT-ID";
    public static List<String> TRANSFERRING_MESSAGES = Arrays.asList(
            APP_NAME,TRANSACTION_HEADER,TRANSACTION_LEVEL,
            TRANSACTION_AGENT,TRANSACTION_SESSION_ID
    );

}

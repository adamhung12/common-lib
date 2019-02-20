package me.xethh.libs.spring.web.security.toolkits.feign;

import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import me.xethh.libs.toolkits.logging.WithLogger;

import java.io.IOException;
import java.util.stream.Collectors;

import static feign.Util.decodeOrDefault;
import static java.nio.charset.StandardCharsets.UTF_8;

public class FeignLogger extends Logger implements WithLogger {

    private org.slf4j.Logger logger = logger();
    @Override
    protected void log(String configKey, String format, Object... args) {
        logger.info(String.format(methodTag(configKey) + format, args));
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        StringBuilder sb = new StringBuilder();
        sb.append("request to: "+request.url()).append("\r\n");
        sb.append("request headers: "+"\r\n");
        request.headers().entrySet().stream().forEach(x->{
            sb.append(String.format("%s=%s", x.getKey(),x.getValue().stream().collect(Collectors.joining(",")))).append("\r\n");
        });
        String body = request.requestBody().asString();
        sb.append("request body: "+body).append("\r\n");
        sb.append("request body length: "+body.length()).append("\r\n");
        logger.info(sb.toString());
    }

    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
        int status = response.status();
        StringBuilder sb = new StringBuilder();
        sb.append("response status: "+response.status()).append("\r\n");
        response.headers().entrySet().stream().forEach(x->{
            sb.append(String.format("%s=%s", x.getKey(),x.getValue().stream().collect(Collectors.joining(",")))).append("\r\n");
        });

        int bodyLength = 0;
        if (response.body() != null && !(status == 204 || status == 205)) {
            // HTTP 204 No Content "...response MUST NOT include a message-body"
            // HTTP 205 Reset Content "...response MUST NOT include an entity"
            sb.append("\r\n");
            byte[] bodyData = Util.toByteArray(response.body().asInputStream());
            bodyLength = bodyData.length;
            if (logLevel.ordinal() >= Level.FULL.ordinal() && bodyLength > 0) {
                sb.append(decodeOrDefault(bodyData,UTF_8,"Binary data")).append("\r\n");
            }
            logger.info(sb.toString());
            return response.toBuilder().body(bodyData).build();
        } else {
            logger.info("Not response body");
        }
        return response;
    }
}


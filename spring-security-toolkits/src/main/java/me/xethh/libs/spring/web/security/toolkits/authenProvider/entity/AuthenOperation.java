package me.xethh.libs.spring.web.security.toolkits.authenProvider.entity;

import javax.validation.constraints.NotNull;

public class AuthenOperation {

    public static class RequestMeta{
        private String remoteAddress,remoteHost,path,signature,clientId;

        public String getRemoteAddress() {
            return remoteAddress;
        }

        public void setRemoteAddress(String remoteAddress) {
            this.remoteAddress = remoteAddress;
        }

        public String getRemoteHost() {
            return remoteHost;
        }

        public void setRemoteHost(String remoteHost) {
            this.remoteHost = remoteHost;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        @Override
        public String toString() {
            return "RequestMeta{" +
                    "remoteAddress='" + remoteAddress + '\'' +
                    ", remoteHost='" + remoteHost + '\'' +
                    ", path='" + path + '\'' +
                    ", signature='" + signature + '\'' +
                    ", clientId='" + clientId + '\'' +
                    '}';
        }
    }
    public static class AuthenRequest extends BaseAuthenRequest implements me.xethh.libs.spring.web.security.toolkits.authenProvider.entity.AuthenRequest{
        @NotNull
        private String clientId;
        @NotNull
        private String secret;

        public AuthenRequest() {
            super(OperationType.Authen);
        }


        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        @Override
        public String toString() {
            return "AuthenRequest{" +
                    "metaInfo=" + metaInfo +
                    ", operationType=" + operationType +
                    ", clientId='" + clientId + '\'' +
                    ", secret='" + secret + '\'' +
                    '}';
        }

    }
}

package me.xethh.libs.crawler

import org.apache.http.impl.client.{CloseableHttpClient, HttpClients}

object ClientBuilder {
  val ChromeUserAgentHeader="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36"
  def default:Client=new Client(HttpClients.createDefault())
  def asChrome():Client={
    new Client(HttpClients.custom().setUserAgent(ChromeUserAgentHeader).build())
  }

}

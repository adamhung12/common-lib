package me.xethh.libs.crawler

import java.util.Date

import org.apache.http.client.methods.{CloseableHttpResponse, HttpGet}
import org.apache.http.client.protocol.HttpClientContext
import org.apache.http.client.utils.URIBuilder
import org.apache.http.impl.client.{BasicCookieStore, CloseableHttpClient}
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.util.EntityUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import scala.collection.mutable.ListBuffer

class Client(
              private[crawler] var client: CloseableHttpClient,
              cookieProvider:()=>BasicCookieStore=()=>new BasicCookieStore(),
              contextProvider:()=>BasicHttpContext= ()=>new BasicHttpContext()
            ) {
  val cookie:BasicCookieStore = cookieProvider.apply()
  val context = contextProvider.apply()
  context.setAttribute(HttpClientContext.COOKIE_STORE, cookie)

  def get(_url:String):GetRequest=new GetRequest(_url,this)

  class GetRequest(_url:String,client:Client){
    val headers:ListBuffer[(String,String)] = ListBuffer.empty[(String,String)]
    var params:ListBuffer[(String,String)] = ListBuffer.empty[(String,String)]
    def setHeader(name:String, value:String):GetRequest = {
      headers += Tuple2(name,value)
      this
    }
    def setRefer(value:String):GetRequest=setHeader("Referer",value)
    def execute:CloseableHttpResponse={
      val request = new HttpGet(_url)
      headers.foreach(x=>request.setHeader(x._1,x._2))
      val uriBuilder = new URIBuilder(_url)
      params.foreach(x=>uriBuilder.setParameter(x._1,x._2))
      request.setURI(uriBuilder.build())
      client.client.execute(request,client.context)
    }
    def setParam(key:String,value:String):GetRequest={
      params += Tuple2(key,value)
      this
    }
    def executeAsString:String={
      EntityUtils.toString(execute.getEntity)
    }
    def executeAsJsoup(url:String):Document={
      Jsoup.parse(
        executeAsString,url
      )
    }
    def executeAsJsoup:Document={
      Jsoup.parse(
        executeAsString,_url
      )
    }
  }
}
object Client{

  def main(args: Array[String]): Unit = {
    val client = ClientBuilder.asChrome()
    val res = client
//      .get(s"https://stock360.hkej.com/data/getQuotes/1883")
//      .setRefer("https://stock360.hkej.com/data/getQuotes/1883")
      .get(s"https://stock360.hkej.com/stockList/all/20190222")
//      .setParam("t",new Date().getTime+"")
      .setParam("p",1+"")
      .executeAsJsoup
    val x = res.select("div.pagingWrap").get(1).select("span")
    assert(x.get(0).text().equals("頁數："))
    import collection.JavaConverters._
    val it = x.iterator().asScala

    it.drop(1).foreach(x=>println(x.select("a[href]").text()))
//    for(i <- it) yield {
//      val el = it.next().select("a[href]")
//      println(el.text())
//    }
  }

}


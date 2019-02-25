package me.xethh.libs.crawler

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

}
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
object Client{

  def main(args: Array[String]): Unit = {
  }

}


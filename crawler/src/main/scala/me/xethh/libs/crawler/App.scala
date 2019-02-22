package me.xethh.libs.crawler

import org.apache.http.impl.client.HttpClients

/**
 * @author ${user.name}
 */
object App {

  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)

  def main(args : Array[String]) {
    val client = HttpClients.createDefault()

    println( "Hello World!" )
    println("concat arguments = " + foo(args))
  }

}

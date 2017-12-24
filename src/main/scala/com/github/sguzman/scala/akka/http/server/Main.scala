package com.github.sguzman.scala.akka.http.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn
import scala.util.{Failure, Success}
import scalatags.Text.all._

object Main {
  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext = system.dispatcher

    val routes = path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, h1("Say hello to akka-http").toString))
      }
    }

    val bindingFuture = Http().bindAndHandle(routes, "localhost", util.Try(System.getenv("PORT").toInt) match {
      case Success(v) => v
      case Failure(_) => 8080
    })

    StdIn.readLine()

    bindingFuture.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}

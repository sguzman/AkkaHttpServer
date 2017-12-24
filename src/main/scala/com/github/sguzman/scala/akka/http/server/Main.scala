package com.github.sguzman.scala.akka.http.server

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import io.circe.generic.auto._
import io.circe.parser.decode
import io.circe.syntax._
import org.feijoas.mango.common.base.Preconditions

import scala.concurrent.{ExecutionContextExecutor, Future}
import scala.util.{Failure, Success}

object Main {
  final case class Item(name: String, id: Long)
  final case class Order(items: List[Item])

  implicit val system: ActorSystem = ActorSystem("system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  var orders: List[Item] = Nil

  def fetchItem(itemId: Long): Future[Option[Item]] = Future {
    orders.find(_.id == itemId)
  }

  def saveOrder(order: Order): Future[Done] = {
    orders = order match {
      case Order(items) => items ::: orders
      case _ => orders
    }

    Future { Done }
  }

  def main(args: Array[String]): Unit = {
    val route: Route = get {
      pathPrefix("item" / LongNumber) { id =>
        val maybeItem: Future[Option[Item]] = fetchItem(id)

        onSuccess(maybeItem) {
          case Some(item) => complete(item.asJson.toString)
          case None => complete(StatusCodes.NotFound)
        }
      }
    } ~ post {
      path("create") {
        entity(as[String]) { orderStr =>
          val saved = Future {
            val orderEither = decode[Order](orderStr)
            Preconditions.checkArgument(orderEither.isRight, orderEither.left.e)
            val order: Order = orderEither.right.get
            order
          }

          saved.map(saveOrder)
          onComplete(saved) {
            case Success(_) => complete("order updated")
            case Failure(e) => complete(StatusCodes.InternalServerError)
          }
        }
      }
    }

    val bindingFuture = Http().bindAndHandle(route, "localhost", util.Try({System.getenv("PORT").toInt}) match {
      case Success(v) => v
      case Failure(_) => 8080
    })
  }
}

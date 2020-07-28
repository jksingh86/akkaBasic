package demo.akka.http.example1.server

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.{HttpApp, Route}
import demo.akka.http.example1.server.config.Mongo
import demo.akka.http.example1.server.controller.UserController
import demo.akka.http.example1.server.repo.UserRepository

import scala.concurrent.ExecutionContext.Implicits.{global => ImplicitsGlobal}
import akka.stream.{ActorMaterializer, Materializer}

object Application extends HttpApp with App {
  implicit val system: ActorSystem = ActorSystem("Akka-HTTP-Rest-API")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  override protected def routes: Route =
    new UserController(new UserRepository(Mongo.userCollection)).userRoutes //you can add more routes using the '~' to concatenate: val routes = route1 ~ route2 ~ route3

  startServer("127.0.0.1", 8081, system)
}
package demo.akka.http.example1.server.controller

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.ErrorAccumulatingCirceSupport._
import demo.akka.http.example1.server.model.User
import demo.akka.http.example1.server.repo.UserRepository
import io.circe.syntax._
import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class UserController(repository: UserRepository)(implicit ec: ExecutionContext, mat: Materializer) {

  val userRoutes: Route =
    path("api" / "user" / Segment) { id =>
      get {
        println(id)
        onSuccess(repository.findById(id)) {
          case Some(user) =>
            println("\n\n inside Some case \n\n")
            complete(StatusCodes.OK -> user)
          case None =>
            println("\n\n inside None case \n\n")
            complete(StatusCodes.NotFound)
        }
      }
    } ~ path("api" / "users") {
      get {
        val allUser = repository.findAll().headOption()
        onComplete(allUser) {
          case Success(value) =>
            value match {
              case Some(a) => complete(HttpResponse(StatusCodes.OK, entity = a.asJson.toString()))
              case None => complete(HttpResponse(StatusCodes.NotFound, entity = "No User Found"))
            }
          case Failure(exception) => complete(HttpResponse(StatusCodes.NotFound, entity = exception.getMessage))
        }
      }
    } ~ path("api" / "user") {
      (post & pathEndOrSingleSlash & entity(as[User])) { user =>
        onSuccess(repository.save(user)) { id =>
          respondWithHeader(Location(s"/api/users/$id")) {
            complete(StatusCodes.Created)
          }
        }
      }
    }
}

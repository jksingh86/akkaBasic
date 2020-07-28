package demo.akka.http.example1.server.model
import io.circe.syntax._
import io.circe._
import org.bson.types.ObjectId

case class User(_id: ObjectId, username: String, age: Int) {
  require(username != null, "username not informed")
  require(username.nonEmpty, "username cannot be empty")
  require(age > 0, "age cannot be lower than 1")
}

object User {
  implicit val encoder: Encoder[User] = (a: User) => {
    Json.obj(
      "id" -> a._id.toHexString.asJson,
      "username" -> a.username.asJson,
      "age" -> a.age.asJson
    )
  }

  implicit val decoder: Decoder[User] = (c: HCursor) => {
    for {
      username <- c.downField("username").as[String]
      age <- c.downField("age").as[Int]
    } yield User(ObjectId.get(), username, age)
  }
}
package demo.akka.http.example1.server.repo

import demo.akka.http.example1.server.model.User
import org.mongodb.scala._
import org.mongodb.scala.bson.ObjectId

import scala.concurrent.{ExecutionContext, Future}

class UserRepository(collection: MongoCollection[User])(implicit ec: ExecutionContext) {
  def findById(id: String): Future[Option[User]] = {
    collection.find(Document("_id" -> new ObjectId(id)))
              .first.head.map(Option(_))
  }

  def findAll(): SingleObservable[Seq[User]] = {
    collection.find.collect()
  }

  def save(user: User): Future[String] = {
    collection
      .insertOne(user)
      .head
      .map { _ => user._id.toHexString }
  }
}
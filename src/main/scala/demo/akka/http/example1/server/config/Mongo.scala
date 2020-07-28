package demo.akka.http.example1.server.config

import com.typesafe.config.{Config, ConfigFactory}
import demo.akka.http.example1.server.model.User
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala._
import org.mongodb.scala.bson.codecs.Macros._
import com.mongodb.MongoClientSettings
import org.bson.codecs.configuration.{CodecRegistries, CodecRegistry}
import org.bson.codecs.pojo.PojoCodecProvider

object Mongo {

  val config: Config = ConfigFactory.load()
  val mongoClient: MongoClient = MongoClient(config.getString("mongo.uri"))
  val pojoCodecRegistry: CodecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry, CodecRegistries.fromProviders(PojoCodecProvider.builder.automatic(true).build))
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[User]),pojoCodecRegistry)
  val database: MongoDatabase = mongoClient.getDatabase(config.getString("mongo.database"))
  val userCollection: MongoCollection[User] = database.getCollection[User]("users").withCodecRegistry(codecRegistry)
 }
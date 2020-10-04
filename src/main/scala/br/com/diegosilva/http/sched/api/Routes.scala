package br.com.diegosilva.http.sched.api

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant

import akka.actor.typed.ActorSystem
import akka.cluster.sharding.typed.scaladsl.ClusterSharding
import akka.http.scaladsl.util.FastFuture
import akka.util.Timeout
import br.com.diegosilva.http.sched.database.DatabasePool
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import br.com.diegosilva.http.sched.model._
import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, Json}


object Routes {


}

class Routes(system: ActorSystem[_]) extends FailFastCirceSupport {

  lazy val log = system.log

  implicit val timeout: Timeout = Timeout.create(system.settings.config.getDuration("server.askTimeout"))
  implicit val scheduler = system.scheduler
  implicit val executionContext = system.executionContext
  implicit val classicSystem = system.classicSystem

  private val sharding = ClusterSharding(system)
  protected val db = DatabasePool(system).database


  import akka.http.scaladsl.server._
  import Directives._
  import ch.megard.akka.http.cors.scaladsl.CorsDirectives._

  import io.circe.generic.auto._


  implicit val TimestampFormat : Encoder[Timestamp] with Decoder[Timestamp] = new Encoder[Timestamp] with Decoder[Timestamp] {
    override def apply(a: Timestamp): Json = Encoder.encodeString.apply(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(a))
    override def apply(c: HCursor): Result[Timestamp] = Decoder.decodeString.map(s => new Timestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(s).getTime)).apply(c)
  }


  val routes: Route =

    pathPrefix("api") {
      cors() {
        concat(
          get {concat(
            path("health") {
              complete("Olá de " + system.address)
            },
            path("use") {
              complete(FastFuture.successful(("1" -> User(uid = "fasdfas", name = "Diego", birth = Timestamp.from(Instant.now)))))
            },
            path("use2") {
              complete(Seq(User(uid = "fasdfas", name = "Diego", birth = Timestamp.from(Instant.now))))
            })
          }
        )
      }
    }
}



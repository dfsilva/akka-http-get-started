package br.com.diegosilva.http.sched

import akka.Done
import akka.actor.typed.scaladsl.Behaviors
import akka.actor.typed.{ActorSystem, Behavior}
import br.com.diegosilva.http.sched.api.{Routes, Server}
import com.typesafe.config.ConfigFactory

object Main extends App {
  val system = ActorSystem[Done](Guardian(), "http-sched-system", ConfigFactory.load)
}

object Guardian {
  def apply(): Behavior[Done] = {
    Behaviors.setup[Done] { context =>
      implicit val system = context.system.classicSystem
      val httpPort = context.system.settings.config.getInt("server.http.port")

      val routes = new Routes(context.system)
      new Server(routes.routes, httpPort, context.system).start()
      Behaviors.receiveMessage {
        case Done =>
          Behaviors.stopped
      }
    }
  }
}

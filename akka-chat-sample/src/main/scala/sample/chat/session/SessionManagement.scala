package sample.chat.session

import scala.collection.mutable.HashMap
import akka.actor.{ Actor, ActorRef }
import Actor._
import sample.chat.Login
import akka.event.EventHandler
import sample.chat.Logout

/**
 * Implements user session management.
 * <p/>
 * Uses self-type annotation (this: Actor =>) to declare that it needs to be mixed in with an Actor.
 */
trait SessionManagement { this: Actor =>

  val storage: ActorRef // needs to provide the ChatStorage
  val sessions = new HashMap[String, ActorRef]

  protected def sessionManagement: Receive = {
    case Login(username) =>
      EventHandler.info(this, "User [%s] has logged in".format(username))
      val session = actorOf(new Session(username, storage))
      session.start()
      sessions += (username -> session)

    case Logout(username) =>
      EventHandler.info(this, "User [%s] has logged out".format(username))
      val session = sessions(username)
      session.stop()
      sessions -= username
  }

  protected def shutdownSessions() {
    sessions.foreach { case (_, session) => session.stop() }
  }
}

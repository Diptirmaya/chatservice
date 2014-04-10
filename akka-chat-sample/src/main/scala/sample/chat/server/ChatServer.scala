package sample.chat.server

import akka.actor.{ Actor, ActorRef }
import akka.event.EventHandler
import akka.config.Supervision.OneForOneStrategy

/**
 * Chat server. Manages sessions and redirects all other
 * messages to the Session for the client.
 */
trait ChatServer extends Actor {
  self.faultHandler = OneForOneStrategy(List(classOf[Exception]), 5, 5000)
  val storage: ActorRef

  EventHandler.info(this, "Chat server is starting up...")



  protected def chatManagement: Receive
  protected def sessionManagement: Receive
  
    // actor message handler
  def receive: Receive = sessionManagement orElse chatManagement
  
  protected def shutdownSessions()

  override def postStop() {
    EventHandler.info(this, "Chat server is shutting down...")
    shutdownSessions()
    self.unlink(storage)
    storage.stop()
  }
}
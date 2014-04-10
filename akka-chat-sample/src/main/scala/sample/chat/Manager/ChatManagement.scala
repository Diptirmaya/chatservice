package sample.chat.Manager

import scala.collection.mutable.HashMap
import akka.actor.{ Actor, ActorRef }
import akka.event.EventHandler
import sample.chat.ChatMessage
import sample.chat.GetChatLog
import sample.chat.memory.MemoryChatStorage

/**
 * Implements chat management, e.g. chat message dispatch.
 * <p/>
 * Uses self-type annotation (this: Actor =>) to declare that it needs to be mixed in with an Actor.
 */
trait ChatManagement { this: Actor =>
  val sessions: HashMap[String, ActorRef] // needs someone to provide the Session map

  protected def chatManagement: Receive = {
    case msg @ ChatMessage(from, _) => getSession(from).foreach(_ ! msg)
    case msg @ GetChatLog(from) => getSession(from).foreach(_ forward msg)
  }

  private def getSession(from: String): Option[ActorRef] = {
    if (sessions.contains(from))
      Some(sessions(from))
    else {
      EventHandler.info(this, "Session expired for %s".format(from))
      None
    }
  }
}

/**
 * Creates and links a MemoryChatStorage.
 */
trait MemoryChatStorageFactory { this: Actor =>
  val storage = Actor.actorOf[MemoryChatStorage]
  this.self.startLink(storage) // starts and links ChatStorage
}
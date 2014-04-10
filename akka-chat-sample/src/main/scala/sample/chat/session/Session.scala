package sample.chat.session

import akka.event.EventHandler
import akka.actor.{ Actor, ActorRef }
import sample.chat.ChatMessage
import sample.chat.GetChatLog

/**
 * Internal chat client session.
 */
class Session(user: String, storage: ActorRef) extends Actor {
  private val loginTime = System.currentTimeMillis
  private var userLog: List[String] = Nil

  EventHandler.info(this, "New session for user [%s] has been created at [%s]".format(user, loginTime))

  def receive = {
    case msg @ ChatMessage(from, message) =>
      userLog ::= message
      storage ! msg

    case msg @ GetChatLog(_) =>
      storage forward msg
  }
}
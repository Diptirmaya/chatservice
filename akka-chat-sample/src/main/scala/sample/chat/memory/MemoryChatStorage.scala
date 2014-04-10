package sample.chat.memory

import akka.actor.{ Actor, ActorRef }
import akka.stm._
import akka.event.EventHandler
import akka.config.Supervision.Permanent
import sample.chat.ChatMessage
import sample.chat.ChatLog
import sample.chat.GetChatLog

/**
 * Abstraction of chat storage holding the chat log.
 */
trait ChatStorage extends Actor

/**
 * Memory-backed chat storage implementation.
 */
class MemoryChatStorage extends ChatStorage {
  self.lifeCycle = Permanent

  private var chatLog = TransactionalVector[Array[Byte]]()

  EventHandler.info(this, "Memory-based chat storage is starting up...")

  def receive = {
    case msg @ ChatMessage(from, message) =>
      EventHandler.debug(this, "New chat message [%s]".format(message))
      atomic { chatLog + message.getBytes("UTF-8") }

    case GetChatLog(_) =>
      val messageList = atomic { chatLog.map(bytes => new String(bytes, "UTF-8")).toList }
      self.reply(ChatLog(messageList))
  }

  override def postRestart(reason: Throwable) {
    chatLog = TransactionalVector()
  }
}
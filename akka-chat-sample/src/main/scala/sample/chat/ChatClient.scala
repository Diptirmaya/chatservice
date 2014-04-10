package sample.chat

import scala.collection.mutable.HashMap
import akka.actor.{ Actor, ActorRef }
import Actor._
import akka.event.EventHandler
import sample.chat.constants.ChatConstants

/**
 * ChatServer's internal events.
 */
sealed trait Event
case class Login(user: String) extends Event
case class Logout(user: String) extends Event
case class GetChatLog(from: String) extends Event
case class ChatLog(log: List[String]) extends Event
case class ChatMessage(from: String, message: String) extends Event

/**
 * Chat client.
 */
class ChatClient(val name: String) {
  val chat = Actor.remote.actorFor("chat:service", ChatConstants.HOST_NAME, ChatConstants.PORT)

  def login = chat ! Login(name)
  def logout = chat ! Logout(name)
  def post(message: String) = chat ! ChatMessage(name, name + ": " + message)
  def chatLog = (chat !! GetChatLog(name)).as[ChatLog].getOrElse(throw new Exception("Couldn't get the chat log from ChatServer"))
}
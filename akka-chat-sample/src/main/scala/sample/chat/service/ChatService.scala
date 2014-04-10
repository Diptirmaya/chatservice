package sample.chat.service

import akka.actor.{Actor, ActorRef}
import sample.chat.server.ChatServer
import sample.chat.session.SessionManagement
import sample.chat.Manager.ChatManagement
import sample.chat.Manager.MemoryChatStorageFactory
import Actor._
import sample.chat.constants.ChatConstants
 /**
   * Class encapsulating the full Chat Service.
   * Start service by invoking:
   * <pre>
   * val chatService = Actor.actorOf[ChatService].start()
   * </pre>
   */
  class ChatService extends
    ChatServer with
    SessionManagement with
    ChatManagement with
    MemoryChatStorageFactory {
    override def preStart() {
      remote.start(ChatConstants.HOST_NAME, ChatConstants.PORT);
      remote.register("chat:service", self) //Register the actor with the specified service id
    }
  }
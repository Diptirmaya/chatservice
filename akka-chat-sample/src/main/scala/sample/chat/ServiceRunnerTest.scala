package sample.chat

import akka.actor.{ Actor, ActorRef }
import Actor._
import sample.chat.service.ChatService
/**
 * Test runner emulating a chat session.
 */
object ClientRunner {

  def main(args: Array[String]) { ClientRunner.run() }

  def run() {

    val client1 = new ChatClient("jonas")
    client1.login
    val client2 = new ChatClient("patrik")
    client2.login

    client1.post("Hi there")
    println("CHAT LOG:nt" + client1.chatLog.log.mkString("nt"))

    client2.post("Hello")
    println("CHAT LOG:nt" + client2.chatLog.log.mkString("nt"))

    client1.post("Hi again")
    println("CHAT LOG:nt" + client1.chatLog.log.mkString("nt"))

    client1.logout
    client2.logout
  }
}

object ServiceRunnerTest {
  def main(args: Array[String]) { run() }

  def run() {
    println("Test");
    actorOf[ChatService].start()
  }

}
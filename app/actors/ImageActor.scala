package actors

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, File}
import javax.imageio.ImageIO

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, OneForOneStrategy, Props, SupervisorStrategy}
import akka.actor.OneForOneStrategy
import akka.actor.SupervisorStrategy._
import controllers.routes

import scala.concurrent.duration._

/**
  * Created by ibes on 31.01.17.
  */
object ImageActor {
  def props(out: ActorRef) = Props(new ImageActor(out))
}

class ImageActor(out: ActorRef) extends Actor with akka.actor.ActorLogging{

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.debug("Prestarting")
    super.preStart()
  }

  override def receive: Receive = {
    case arr: String => out ! s"ImageActor: Got ur string"
    case arr: Array[Byte] => {
      val bufImg: BufferedImage = ImageIO.read(new ByteArrayInputStream(arr))
      val file = new File("public/images/screenshot.png")
      ImageIO.write(bufImg, "png", file)
      out ! s"ImageActor: Got ur image ${arr.length} - image prolly".getBytes("UTF-8")
    }
    case other => log.warning("Received unknown message: {}", other)
  }

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 1, withinTimeRange = 20 seconds) {
      case ex: Exception => {

        log.error(ex, "Error")
        out ! s"Got an exception ${ex.getCause}".getBytes("UTF-8")
        Resume
      }
      case _ => {
        log.error("Escalate")
        Escalate
      }
    }
}

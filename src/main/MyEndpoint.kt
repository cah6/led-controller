import rx.Observable
import java.net.URI
import java.nio.ByteBuffer
import javax.websocket.*

/**
 * Created by christian.henry on 11/20/16.
 */
class MyEndpoint(
        val dataStream: Observable<ByteArray>,
        val uri: URI,
        val container: WebSocketContainer = ContainerProvider.getWebSocketContainer()): Endpoint() {

    var session: Session? = null

    fun continuouslyConnect() {
        println("Attempting to connect to $uri...")

        session = null
        while (session == null) {
            try {
                session = container.connectToServer(this, uri)
                println("Connected to server, session is now $session")
            } catch (e: Exception) {
                Thread.sleep(5000)
            }
        }
    }

    override fun onOpen(session: Session, config: EndpointConfig) {
        println("Opened connection!")

        session.addMessageHandler(MessageHandler.Whole<ByteArray> {
            message -> println("Received Message: ${message.map(Byte::toInt)}")
        })

        dataStream.subscribe {
            println("Socket: Sending ${it.map(Byte::toInt)} to $session")
            session.basicRemote.sendBinary(ByteBuffer.wrap(it))
        }

    }

    override fun onClose(session: Session?, closeReason: CloseReason?) {
        println("Socket closing because of: ${closeReason.toString()}. Attempting to reconnect." )
        continuouslyConnect()
    }

    override fun onError(session: Session?, thr: Throwable?) {
        println("Socket error because of: ${thr?.message}. Attempting to reconnect.")
        continuouslyConnect()
    }

}
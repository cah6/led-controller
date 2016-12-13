
import org.junit.Assert
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.net.URI
import javax.websocket.CloseReason
import javax.websocket.EndpointConfig
import javax.websocket.Session
import javax.websocket.WebSocketContainer

/**
 * Created by christian.henry on 11/21/16.
 */
class MyEndpointTest {

    @Mock
    val container: WebSocketContainer = Mockito.mock(WebSocketContainer::class.java)

    @Mock
    val configEndpoint: EndpointConfig = Mockito.mock(EndpointConfig::class.java)

    @Mock
    val session: Session = Mockito.mock(Session::class.java)

    val target: MyEndpoint = MyEndpoint(LedController.createDefaultObservable(), URI("localhost:9080"), container)

    @Test
    fun testAutoReconnect() {
        Mockito.`when`(target.container.connectToServer(Mockito.any(MyEndpoint::class.java), Mockito.any(URI::class.java))).thenReturn(session)

        target.continuouslyConnect()

        Assert.assertEquals(session, target.session)

        target.onClose(session, CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, ""))
        Assert.assertEquals(session, target.session)

        target.onError(session, RuntimeException())
        Assert.assertEquals(session, target.session)
    }

}
package andy

import com.twitter.finagle.http.HttpMuxer
import com.twitter.finagle.{Http, Service}
import org.jboss.netty.handler.codec.http._
import com.twitter.finagle.http.Http
import com.twitter.server.TwitterServer
import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder, Server}
import com.twitter.finagle.{HttpServer, ListeningServer, NullServer}
import com.twitter.util.{Await, Duration, Future, Time}
import java.net.InetSocketAddress
import org.jboss.netty.buffer.ChannelBuffers.copiedBuffer
import org.jboss.netty.handler.codec.http._
import org.jboss.netty.util.CharsetUtil.UTF_8
import com.twitter.finagle.tracing._
import org.slf4j.Logger;
import org.slf4j.LoggerFactory
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
;

object AdvancedServer extends TwitterServer {
  final val logger = LoggerFactory.getLogger("AdvancedServer")
  val flags = Flags().setDebug
  //#flag
  val what = flag("what", "hello, andy", "String to return")
  //#flag
  //#complex_flag
  val addr = flag("bind", new InetSocketAddress(0), "Bind address")
  // val durations = flag("alarms", (1.second, 5.second), "2 alarm durations")
  //#complex_flag
  //#stats
  val counter = statsReceiver.counter("requests_counter")
  //#stats

  val service = new Service[HttpRequest, HttpResponse] {
    def apply(request: HttpRequest) = {
      //Trace.pushTracer(ZipkinTracer.mk(host = "172.19.108.98", port = 9410, sampleRate = 1.0f))
      Trace.record("httpServer")
      logger.debug("Received a request at " + Time.now)
      counter.incr()
      val response =
        new DefaultHttpResponse(request.getProtocolVersion, HttpResponseStatus.OK)
      val content = copiedBuffer(what() + "\n", UTF_8)
      response.setContent(content)
      Future.value(response)
    }
  }

  def main() {
    HttpMuxer.addHandler("/echo", service)
    HttpMuxer.addHandler("/echo/", service)
    adminHttpServer.named("httpServer")

    Await.ready(adminHttpServer)
//    Trace.enable()
//    val server: Server = ServerBuilder()
//      .codec(com.twitter.finagle.http.Http())
//      .bindTo(new InetSocketAddress(9991))
//      .name("httpServer")
//      .tracer(ZipkinTracer.mk(host = "172.19.108.98", port = 9410, sampleRate = 1.0f))
//      .build(HttpMuxer)
  }
}
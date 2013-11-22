package andy

import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.Service
import java.net.InetSocketAddress
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.finagle.thrift.{ThriftClientFramedCodec, ThriftClientRequest}
import com.twitter.finagle.tracing.BroadcastTracer._
import com.twitter.finagle.tracing._
import com.twitter.finagle.zipkin.thrift.ZipkinTracer

object ThriftClient {
  def main(args: Array[String]) {
    // Create a raw Thrift client service. This implements the
    // ThriftClientRequest => Future[Array[Byte]] interface.
    val service: Service[ThriftClientRequest, Array[Byte]] = ClientBuilder()
      .hosts("172.19.108.98:8080")
      .codec(ThriftClientFramedCodec())
      .hostConnectionLimit(1)
      .name("Andy.ThriftClient")
      .tracer(ZipkinTracer.mk(host="172.19.108.98", port=9410, sampleRate=1.0f))
      .build()

//    Trace.pushTracer(ZipkinTracer.mk(host="172.19.108.98", port=9410, sampleRate=1.0f))
//    Trace.setTerminalId(Trace.id)

    // Wrap the raw Thrift service in a Client decorator. The client
    // provides a convenient procedural interface for accessing the Thrift
    // server.
    val client = new Hello.FinagledClient(service, new TBinaryProtocol.Factory())

    // Trace.record("ThriftClient test..")

    client.hi() onSuccess { response =>
      println("Received response: " + response)
    } ensure {
      service.close()
    }
  }
}

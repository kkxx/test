package andy

import com.twitter.finagle.thrift.{ThriftClientFramedCodec, ThriftClientRequest, ThriftServerFramedCodec}
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.finagle.builder.{ClientBuilder, ServerBuilder, Server}
import java.net.InetSocketAddress
import com.twitter.util.Future
import com.twitter.finagle.zipkin.thrift.ZipkinTracer
import com.twitter.finagle.tracing._
import com.twitter.finagle.Service

object ThriftServer {
  def main(args: Array[String]) {
    // Implement the Thrift Interface
    val processor = new Hello.FutureIface {
      def hi() = {
        val clientService: Service[ThriftClientRequest, Array[Byte]] = ClientBuilder()
          .hosts("172.19.108.99:9012,127.0.0.1:9012")
          .codec(ThriftClientFramedCodec())
          .hostConnectionLimit(1)
          .retries(2)
          .name("clientInThriftServer")
          .tracer(ZipkinTracer.mk(host = "172.19.108.98", port = 9410, sampleRate = 1.0f))
          .build()
        val client = new Hello.FinagledClient(clientService, new TBinaryProtocol.Factory())
        client.hi() onSuccess {
          response => {
            println("Received response: " + response)
            Trace.record("ThriftServer.hi.test..")
            Future.value("hi")
          }
        } ensure {
          clientService.close()
        }
      }
    }

    // Convert the Thrift Processor to a Finagle Service
    val service = new Hello.FinagledService(processor, new TBinaryProtocol.Factory())
    val server: Server = ServerBuilder()
      .bindTo(new InetSocketAddress(8080))
      .codec(ThriftServerFramedCodec())
      .name("ThriftServer")
      .tracer(ZipkinTracer.mk(host = "172.19.108.98", port = 9410, sampleRate = 1.0f))
      .build(service)

  }
}

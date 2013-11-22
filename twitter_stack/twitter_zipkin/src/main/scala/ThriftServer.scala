package andy

import com.twitter.finagle.thrift.ThriftServerFramedCodec
import org.apache.thrift.protocol.TBinaryProtocol
import com.twitter.finagle.builder.{ServerBuilder, Server}
import java.net.InetSocketAddress
import com.twitter.util.Future
import com.twitter.finagle.zipkin.thrift.ZipkinTracer

object ThriftServer {
  def main(args: Array[String]) {
    // Implement the Thrift Interface
    val processor = new Hello.FutureIface {
      def hi() = Future.value("hi")
    }

    // Convert the Thrift Processor to a Finagle Service
    val service = new Hello.FinagledService(processor, new TBinaryProtocol.Factory())

    val server: Server = ServerBuilder()
      .bindTo(new InetSocketAddress(8080))
      .codec(ThriftServerFramedCodec())
      .name("Andy.ThriftServer")
      .tracer(ZipkinTracer.mk(host="172.19.108.98", port=9410, sampleRate=1.0f))
      //.tracer(ZipkinTracer.mk(host="172.19.108.98", port=9410, sampleRate=1.0f))
      .build(service)
  }
}

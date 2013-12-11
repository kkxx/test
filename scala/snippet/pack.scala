import java.nio.{ByteOrder, ByteBuffer}
import java.io.UnsupportedEncodingException;

class YyProto(var uri: Int, var code:Short) {
  var len:Int = 0;
  def pack():ByteBuffer = {
    var buffer = ByteBuffer.allocate(512)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    buffer.putInt(0);
    buffer.putInt(uri);
    buffer.putShort(code);
    
    len = buffer.position();
    buffer.flip();
    buffer.putInt(len);
    buffer.rewind();
    return buffer;
  }
  
  def unpack(buffer:ByteBuffer) = {
    len = buffer.getInt()
    uri = buffer.getInt()
    code = buffer.getShort()
  }
}

val v = new YyProto(0x01 << 8 | 0x10, 200)
var buffer = v.pack()
var un = new YyProto(0, 0)
un.unpack(buffer)
println(un.len.toBinaryString);
println(un.uri.toHexString);
println(un.code);
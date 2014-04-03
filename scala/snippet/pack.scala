import java.nio.{ByteOrder, ByteBuffer}
import  sun.nio.ch.DirectBuffer
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

// val v = new YyProto(0x01 << 8 | 0x10, 200)
// var buffer = v.pack()
// var un = new YyProto(0, 0)
// un.unpack(buffer)
// println(un.len.toBinaryString);
// println(un.uri.toHexString);
// println(un.code);

object Unsafe {
  private lazy val instance: sun.misc.Unsafe = {
    val fld = classOf[sun.misc.Unsafe].getDeclaredField("theUnsafe")
    fld.setAccessible(true)
    fld.get(null).asInstanceOf[sun.misc.Unsafe]
  }
  def apply(): sun.misc.Unsafe = instance
}

class UnSafeYyProto(var uri: Int, var code:Short) {
  val unsafe = Unsafe();
  var len:Int = 0;
  def pack():ByteBuffer = {
    var buffer = ByteBuffer.allocateDirect(512)
    buffer.order(ByteOrder.LITTLE_ENDIAN)
    val addr = (buffer.asInstanceOf[DirectBuffer]).address();
    unsafe.putInt(addr, 10);
    unsafe.putInt(addr+4, uri);
    unsafe.putShort(addr+4+4, code);
    return buffer;
  }
  
  def unpack(buffer:ByteBuffer) = {
    import  sun.nio.ch.DirectBuffer
    val addr = (buffer.asInstanceOf[DirectBuffer]).address();
    len = unsafe.getInt(addr)
    uri = unsafe.getInt(addr+4)
    code = unsafe.getShort(addr+4+4)
  }
}

// val v = new YyProto(0x01 << 8 | 0x10, 200)
val v = new UnSafeYyProto(0x01 << 8 | 0x10, 200)
var buffer = v.pack()
// var un = new YyProto(0, 0)
var un = new UnSafeYyProto(0, 0)
un.unpack(buffer)
println(un.len.toBinaryString);
println(un.uri.toHexString);
println(un.code);
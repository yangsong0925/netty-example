package nio.bytebuffer;

import com.syys.util.ByteBufferUtil;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/8/10 15:44
 */
public class TestByteBuffer {

    @Test
    public void allocate() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 向buffer中写入1个字节的数据
        buffer.put((byte) 97);
        // 使用工具类，查看buffer状态
        ByteBufferUtil.debugAll(buffer);

        // 向buffer中写入4个字节的数据
        buffer.put(new byte[]{98, 99, 100, 101});
        ByteBufferUtil.debugAll(buffer);

        // 获取数据
        buffer.flip();
        ByteBufferUtil.debugAll(buffer);
        System.out.println(buffer.get());
        System.out.println(buffer.get());
        ByteBufferUtil.debugAll(buffer);

        // 使用compact切换模式
        buffer.compact();
        ByteBufferUtil.debugAll(buffer);

        // 再次写入
        buffer.put((byte) 102);
        buffer.put((byte) 103);
        ByteBufferUtil.debugAll(buffer);
    }

    @Test
    public void translate1(){
        // 准备两个字符串
        String str = "hello";


        ByteBuffer buffer1 = ByteBuffer.allocate(16);
        // 通过字符串的getByte方法获得字节数组，放入缓冲区中
        buffer1.put(str.getBytes());
        ByteBufferUtil.debugAll(buffer1);

        // 将缓冲区中的数据转化为字符串
        // 切换模式
        buffer1.flip();

        // 通过StandardCharsets解码，获得CharBuffer，再通过toString获得字符串
        str = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(str);
        ByteBufferUtil.debugAll(buffer1);
    }

    @Test
    public void translate2(){
        // 准备两个字符串
        String str = "hello";

        // 通过StandardCharsets的encode方法获得ByteBuffer
        // 此时获得的ByteBuffer为读模式，无需通过flip切换模式
        ByteBuffer buffer1 = StandardCharsets.UTF_8.encode(str);
        ByteBufferUtil.debugAll(buffer1);

        // 将缓冲区中的数据转化为字符串
        // 通过StandardCharsets解码，获得CharBuffer，再通过toString获得字符串
        str = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(str);
        ByteBufferUtil.debugAll(buffer1);
    }

    @Test
    public void translate3(){
        // 准备两个字符串
        String str = "hello";

        // 通过StandardCharsets的encode方法获得ByteBuffer
        // 此时获得的ByteBuffer为读模式，无需通过flip切换模式
        ByteBuffer buffer1 = ByteBuffer.wrap(str.getBytes());
        ByteBufferUtil.debugAll(buffer1);

        // 将缓冲区中的数据转化为字符串
        // 通过StandardCharsets解码，获得CharBuffer，再通过toString获得字符串
        str = StandardCharsets.UTF_8.decode(buffer1).toString();
        System.out.println(str);
        ByteBufferUtil.debugAll(buffer1);
    }
}

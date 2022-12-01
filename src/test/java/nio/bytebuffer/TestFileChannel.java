package nio.bytebuffer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/11/29 19:17
 */
public class TestFileChannel {

    File fileA = new File(this.getClass().getResource("/").getPath() + "txt/A.mp4");
    File fileB = new File(this.getClass().getResource("/").getPath() + "txt/B.mp4");

    @Test
    @SneakyThrows
    public void noBuffer() {
        try (FileInputStream fis = new FileInputStream(fileA.getPath());
             FileOutputStream fos = new FileOutputStream(fileB.getPath());
             FileChannel fisChannel = fis.getChannel();
             FileChannel fosChannel = fos.getChannel()) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("noBuffer");

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (fisChannel.read(buffer) != -1) {
                buffer.flip();
                fosChannel.write(buffer);
                buffer.clear();
            }

            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        }
    }

    @Test
    @SneakyThrows
    public void directBuffer() {
        try (FileChannel inChannel = FileChannel.open(Paths.get(fileA.getPath()), StandardOpenOption.READ);
             FileChannel outChannel = FileChannel.open(Paths.get(fileB.getPath()), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("directBuffer");

            // 获得直接缓冲区
            MappedByteBuffer inMapBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMapBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
            byte[] bytes = new byte[inMapBuf.limit()];
            inMapBuf.get(bytes);
            outMapBuf.put(bytes);

            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        }
    }

    @Test
    @SneakyThrows
    public void channelToChannel() {
        try (FileChannel inChannel = FileChannel.open(Paths.get(fileA.getPath()), StandardOpenOption.READ);
             FileChannel outChannel = FileChannel.open(Paths.get(fileB.getPath()), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("channelToChannel");
            // 通道间直接传输
            inChannel.transferTo(0, inChannel.size(), outChannel);
            // outChannel.transferFrom(inChannel, 0, inChannel.size());
            stopWatch.stop();

            System.out.println(stopWatch.prettyPrint());
        }
    }

    @Test
    @SneakyThrows
    public  void  manyBuffer(){
        try (FileChannel inChannel = FileChannel.open(Paths.get(fileA.getPath()), StandardOpenOption.READ);
             FileChannel outChannel = FileChannel.open(Paths.get(fileB.getPath()), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {

            long fileSize = inChannel.size();

            ByteBuffer byteBuffer1 = ByteBuffer.allocate((int) (fileSize/2));
            ByteBuffer byteBuffer2 = ByteBuffer.allocate((int) (fileSize/2) + 1);
            ByteBuffer[] byteBuffers = {byteBuffer1, byteBuffer2};

            StopWatch stopWatch = new StopWatch();
            stopWatch.start("manyBuffer");
            // 分散读取
            inChannel.read(byteBuffers);

            byteBuffer1.flip();
            byteBuffer2.flip();

            // 聚集写入
            outChannel.write(byteBuffers);
            stopWatch.stop();

            System.out.println(stopWatch.prettyPrint());
        }
    }

}

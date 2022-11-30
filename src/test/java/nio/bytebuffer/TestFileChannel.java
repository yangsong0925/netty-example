package nio.bytebuffer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;

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

    @Test
    @SneakyThrows
    public void noBuffer() {
        try (FileInputStream fis = new FileInputStream(this.getClass().getResource("/txt/A.png").getPath());
             FileOutputStream fos = new FileOutputStream(this.getClass().getResource("/").getFile() + "txt/B.png");
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
        this.getClass().getResourceAsStream("/txt/B.png");

//        Path path = Paths.get(this.getClass().getResource("/").getPath(), "txt/B.png");
        try (FileChannel inChannel = FileChannel.open(Paths.get(this.getClass().getResource("/txt/A.png").toURI()), StandardOpenOption.READ);
             FileChannel outChannel = FileChannel.open(Paths.get(URI.create(this.getClass().getResource("/").getFile() + "txt/B.png")), StandardOpenOption.READ, StandardOpenOption.WRITE, StandardOpenOption.CREATE)) {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("directBuffer");

            // 获得直接缓冲区
            MappedByteBuffer inMapBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
            MappedByteBuffer outMapBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, outChannel.size());
            byte[] bytes = new byte[inMapBuf.limit()];
            inMapBuf.get(bytes);
            outMapBuf.put(bytes);

            stopWatch.stop();
            System.out.println(stopWatch.prettyPrint());
        }
    }

}

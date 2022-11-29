package nio.bytebuffer;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author yangsong
 * @version 1.0
 * @date 2022/11/29 19:17
 */
public class TestFileChannel {

    @Test
    @SneakyThrows
    public void noBuffer() {
        FileInputStream fis = new FileInputStream(this.getClass().getResource("/txt/A.png").getPath());
        FileOutputStream fos = new FileOutputStream(this.getClass().getResource("/").getFile() + "txt/B.png");
        FileChannel fisChannel = fis.getChannel();
        FileChannel fosChannel = fos.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (fisChannel.read(buffer) != 0) {
            buffer.flip();
            fosChannel.write(buffer);
            buffer.clear();
        }

    }

}

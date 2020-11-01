package streamUtil;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Tomoto
 * @date 2020/10/28 8:50
 */
public class StreamUtil {
    public static void close(Closeable... cs) {
        try {
            for (Closeable c : cs) {
                if (c != null) {
                    c.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

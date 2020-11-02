package event;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Super class of all event classes.
 * <p>
 * Client and server communicate through events.
 *
 * @author Tomoto
 * @date 2020/10/27 17:58
 */
public abstract class AbstractEvent implements Serializable {
    //    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
    public static final ThreadLocal<SimpleDateFormat> DATE_FORMAT =
            ThreadLocal.withInitial(() -> new SimpleDateFormat("HH:mm:ss"));

    private Date time;
    private String data;

    public AbstractEvent(String data) {
        this.time = new Date();
        this.data = data;
    }

    public Date getTime() {
        return time;
    }

    public AbstractEvent setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getData() {
        return data;
    }

    public AbstractEvent setData(String data) {
        this.data = data;
        return this;
    }
}

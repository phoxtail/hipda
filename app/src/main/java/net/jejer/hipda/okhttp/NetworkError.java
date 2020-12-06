package net.jejer.hipda.okhttp;

/**
 * Created by GreenSkinMonster on 2016-04-05.
 */
public class NetworkError {
    private final String message;
    private final String detail;
    private final int errCode;

    NetworkError(int errCode, String message, String detail) {
        this.message = message;
        this.detail = detail;
        this.errCode = errCode;
    }


    public String getDetail() {
        return detail;
    }

    public String getMessage() {
        return message;
    }

    public int getErrCode() {
        return errCode;
    }

    @Override
    public String toString() {
        return message;
    }
}

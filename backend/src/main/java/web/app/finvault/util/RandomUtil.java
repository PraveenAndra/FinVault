package web.app.finvault.util;

public class RandomUtil {

    public Long generateRandomAccountNumber() {
        return (long) (Math.random() * 900000000) + 100000000;
    }
}

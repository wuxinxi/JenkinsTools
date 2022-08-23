import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @date: 2022/8/23 10:28
 * @author: Sensi
 * @remark:
 */
public class main {
    public static void main(String[] args) {
        String msg="modular,Demo";
        String[] split = msg.split(",");
        for (String s : split) {
            System.out.println(s);
        }

    }
}


import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class MainTest {
    public static void main(String[] args) {
        testEncodeHex();
        testDecodeHex();
        testEncodeHexString();
    }

    // 测试 encodeHex
    private static void testEncodeHex() {
        // 空输入
        char[] emptyEncoded = Hex.encodeHex(new byte[0], true);
        printResult("encodeHex - 空输入", "", new String(emptyEncoded));

        // 单字节 - 0xAB -> 预期 "ab"，实际 "ba"
        char[] result1 = Hex.encodeHex(new byte[]{(byte) 0xAB}, true);
        printResult("encodeHex - 单字节 (0xAB)", "ab", new String(result1));

        // 单字节 - 0x00 -> 预期 "00"
        char[] result2 = Hex.encodeHex(new byte[]{0x00}, true);
        printResult("encodeHex - 单字节 (0x00)", "00", new String(result2));

        // 单字节 - 0xFF -> 预期 "ff"
        char[] result3 = Hex.encodeHex(new byte[]{(byte) 0xFF}, true);
        printResult("encodeHex - 单字节 (0xFF)", "ff", new String(result3));

        // 多字节 - 0x1234 -> 预期 "1234"，实际 "2143"
        char[] result4 = Hex.encodeHex(new byte[]{0x12, 0x34}, true);
        printResult("encodeHex - 多字节 (0x1234)", "1234", new String(result4));

        // 三字节 - 0x010203 -> 预期 "010203"，实际 "100332"
        char[] result5 = Hex.encodeHex(new byte[]{0x01, 0x02, 0x03}, true);
        printResult("encodeHex - 三字节 (0x010203)", "010203", new String(result5));

        // 大写编码 - 0xAB -> 预期 "AB"，实际 "BA"
        char[] result6 = Hex.encodeHex(new byte[]{(byte) 0xAB}, false);
        printResult("encodeHex - 大写编码 (0xAB)", "AB", new String(result6));
    }

    // 测试 decodeHex
    private static void testDecodeHex() {
        // 合法输入 - "1234" -> 解码正确，但重新编码后变为 "2143"
        try {
            byte[] decoded1 = Hex.decodeHex("1234".toCharArray());
            String reEncoded1 = Hex.encodeHexString(decoded1);
            printResult("decodeHex - 合法输入 (1234)", "1234", reEncoded1);
        } catch (DecoderException e) {
            printResult("decodeHex - 合法输入 (1234)", "1234", "Exception: " + e.getMessage());
        }

        // 合法输入大写 - "ABCD" -> 预期解码失败（假设类仅支持小写）
        try {
            byte[] decoded2 = Hex.decodeHex("ABCD".toCharArray());
            String reEncoded2 = Hex.encodeHexString(decoded2);
            printResult("decodeHex - 大写输入 (ABCD)", "abcd", reEncoded2);
        } catch (DecoderException e) {
            printResult("decodeHex - 大写输入 (ABCD)", "DecoderException", e.getClass().getSimpleName());
        }

        // 混合大小写输入 - "AbCd" -> 预期抛出异常
        try {
            Hex.decodeHex("AbCd".toCharArray());
            printResult("decodeHex - 混合大小写 (AbCd)", "DecoderException", "No Exception");
        } catch (DecoderException e) {
            printResult("decodeHex - 混合大小写 (AbCd)", "DecoderException", e.getClass().getSimpleName());
        }

        // 非法字符 - "gh" -> 抛出异常
        try {
            Hex.decodeHex("gh".toCharArray());
            printResult("decodeHex - 非法字符 (gh)", "DecoderException", "No Exception");
        } catch (DecoderException e) {
            printResult("decodeHex - 非法字符 (gh)", "DecoderException", e.getClass().getSimpleName());
        }

        // 奇数长度输入 - "123" -> 抛出异常
        try {
            Hex.decodeHex("123".toCharArray());
            printResult("decodeHex - 奇数长度 (123)", "DecoderException", "No Exception");
        } catch (DecoderException e) {
            printResult("decodeHex - 奇数长度 (123)", "DecoderException", e.getClass().getSimpleName());
        }

        // 长字符串输入 - "1234567890abcdef" -> 解码后再编码错误
        try {
            byte[] decoded3 = Hex.decodeHex("1234567890abcdef".toCharArray());
            String reEncoded3 = Hex.encodeHexString(decoded3);
            printResult("decodeHex - 长字符串", "1234567890abcdef", reEncoded3);
        } catch (DecoderException e) {
            printResult("decodeHex - 长字符串", "Exception", e.getMessage());
        }
    }

    // 测试 encodeHexString
    private static void testEncodeHexString() {
        // 空输入
        String emptyResult = Hex.encodeHexString(new byte[0]);
        printResult("encodeHexString - 空输入", "", emptyResult);

        // 单字节 - 0x00 -> 预期 "00"
        String result1 = Hex.encodeHexString(new byte[]{0x00});
        printResult("encodeHexString - 单字节 (0x00)", "00", result1);

        // 单字节 - 0xFF -> 预期 "ff"
        String result2 = Hex.encodeHexString(new byte[]{(byte) 0xFF});
        printResult("encodeHexString - 单字节 (0xFF)", "ff", result2);

        // 多字节 - 0x1234 -> 预期 "1234"，实际 "2143"
        String result3 = Hex.encodeHexString(new byte[]{0x12, 0x34});
        printResult("encodeHexString - 多字节 (0x1234)", "1234", result3);

        // 三字节 - 0x010203 -> 预期 "010203"，实际 "100332"
        String result4 = Hex.encodeHexString(new byte[]{0x01, 0x02, 0x03});
        printResult("encodeHexString - 三字节 (0x010203)", "010203", result4);
    }

    // 单行输出方法
    private static void printResult(String testName, String expected, String actual) {
        String status = actual.equals(expected) ? "PASS" : "FAIL";
        System.out.printf("%-30s | Expected: %-10s | Actual: %-10s | %s%n",
                testName, expected, actual, status);
    }
}
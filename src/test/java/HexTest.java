import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.params.provider.Arguments;

public class HexTest {
    // ======================== encodeHex 测试 ========================
    @ParameterizedTest(name = "encodeHex - {0}")
    @MethodSource("encodeHexProvider")
    public void testEncodeHex(byte[] input, String expected) {
        char[] encoded = Hex.encodeHex(input, true);
        assertEquals(expected, new String(encoded));
    }

    static Stream<Arguments> encodeHexProvider() {
        return Stream.of(
                Arguments.of(new byte[0], ""),                          // 空输入
                Arguments.of(new byte[]{(byte) 0xAB}, "ab"),             // 单字节 (0xAB)
                Arguments.of(new byte[]{0x00}, "00"),                    // 单字节 (0x00)
                Arguments.of(new byte[]{(byte) 0xFF}, "ff"),             // 单字节 (0xFF)
                Arguments.of(new byte[]{0x12, 0x34}, "1234"),            // 多字节 (0x1234)
                Arguments.of(new byte[]{0x01, 0x02, 0x03}, "010203"),    // 三字节 (0x010203)
                Arguments.of(new byte[]{(byte) 0xAB}, "AB", false)        // 大写编码
        );
    }

    // ======================== decodeHex 测试 ========================
    @ParameterizedTest(name = "decodeHex - {0}")
    @MethodSource("decodeHexProvider")
    public void testDecodeHex(String hexStr, String expectedReEncoded) throws DecoderException {
        byte[] decoded = Hex.decodeHex(hexStr.toCharArray());
        String reEncoded = Hex.encodeHexString(decoded);
        assertEquals(expectedReEncoded, reEncoded);
    }

    static Stream<Arguments> decodeHexProvider() {
        return Stream.of(
                Arguments.of("1234", "1234"),                            // 合法输入
                Arguments.of("abcd", "abcd"),                            // 合法小写输入
                Arguments.of("1234567890abcdef", "1234567890abcdef")     // 长字符串输入
        );
    }

    // ======================== decodeHex 异常测试 ========================
    @ParameterizedTest(name = "decodeHexException - {0}")
    @MethodSource("decodeHexExceptionProvider")
    public void testDecodeHexException(String hexStr) {
        assertThrows(DecoderException.class, () -> Hex.decodeHex(hexStr.toCharArray()));
    }

    static Stream<Arguments> decodeHexExceptionProvider() {
        return Stream.of(
                Arguments.of("ABCD"),    // 大写输入（假设仅支持小写）
                Arguments.of("AbCd"),    // 混合大小写
                Arguments.of("gh"),      // 非法字符
                Arguments.of("123")      // 奇数长度
        );
    }

    // ======================== encodeHexString 测试 ========================
    @ParameterizedTest(name = "encodeHexString - {0}")
    @MethodSource("encodeHexStringProvider")
    public void testEncodeHexString(byte[] input, String expected) {
        String actual = Hex.encodeHexString(input);
        assertEquals(expected, actual);
    }

    static Stream<Arguments> encodeHexStringProvider() {
        return Stream.of(
                Arguments.of(new byte[0], ""),                          // 空输入
                Arguments.of(new byte[]{0x00}, "00"),                    // 单字节 (0x00)
                Arguments.of(new byte[]{(byte) 0xFF}, "ff"),             // 单字节 (0xFF)
                Arguments.of(new byte[]{0x12, 0x34}, "1234"),            // 多字节 (0x1234)
                Arguments.of(new byte[]{0x01, 0x02, 0x03}, "010203")     // 三字节 (0x010203)
        );
    }

    // ======================== 工具方法 ========================
    private static String bytesToHex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "[]";
        return Hex.encodeHexString(bytes);
    }
}
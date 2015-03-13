
/**
 * <p/>
 * java中byte转换int时为何与0xff进行与运算
 * 其原因在于:
 * 1.byte的大小为8bits而int的大小为32bits
 * 2.java的二进制采用的是补码形式
 * <p/>
 * Java中的一个byte，其范围是-128~127的，而Integer.toHexString的参数本来是int，如果不进行&0xff，那么当一个byte会转换成int时，对于负数，
 * 会做位扩展，举例来说，一个byte的-1（即0xff），会被转换成int的-1（即0xffffffff），那么转化出的结果就不是我们想要的了。
 * 而0xff默认是整形，所以，一个byte跟0xff相与会先将那个byte转化成整形运算，这样，结果中的高的24个比特就总会被清0，于是结果总是我们想要的。
 */
public class RC4 {

    public static String decry_RC4(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(RC4Base(data, key));
    }

    public static String decry_RC4(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return new String(RC4Base(HexString2Bytes(data), key));
    }

    public static byte[] encry_RC4_byte(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte b_data[] = data.getBytes();
        return RC4Base(b_data, key);
    }

    public static String encry_RC4_string(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return toHexString(asString(encry_RC4_byte(data, key)));
    }

    private static String asString(byte[] buf) {
        StringBuffer strbuf = new StringBuffer(buf.length);
        for (int i = 0; i < buf.length; i++) {
            strbuf.append((char) buf[i]);
        }
        return strbuf.toString();
    }

    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

    private static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                s4 = '0' + s4;
            }
            str = str + s4;
        }
        return str;// 0x表示十六进制
    }

    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[]{src0})).byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[]{src1})).byteValue();
        byte ret = (byte) (_b0 ^ _b1);
        return ret;
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }

    public static void main(String[] args) {
        String inputStr = "{  \"safeinfo\" : {    \"ipaddress\" : \"192.168.12.7\"  },  \"userinfo\" : {    \"version\" : \"3.6.5\",    \"mac\" : \"dd6b67d877740796d7ea8efcbf8fd77b7bcd51f3\",    \"channel\" : \"b2e1cc\",    \"userid\" : \"\",    \"mobilemodel\" : \"x86_64\",    \"lot\" : \"\",    \"source\" : \"tao800\",    \"lat\" : \"\",    \"platform\" : \"iPhone OS\",    \"sysversion\" : \"8.1\",    \"deviceid\" : \"dd6b67d877740796d7ea8efcbf8fd77b7bcd51f3\",    \"city\" : \"1\",    \"network\" : \"wifi\",    \"resolution\" : \"750*1334\"  }}";
        String inputStr2 = "{\n" +
                "  \"safeinfo\" : {\n" +
                "    \"ipaddress\" : \"192.168.12.7\"\n" +
                "  },\n" +
                "  \"userinfo\" : {\n" +
                "    \"version\" : \"3.6.5\",\n" +
                "    \"mac\" : \"dd6b67d877740796d7ea8efcbf8fd77b7bcd51f3\",\n" +
                "    \"channel\" : \"b2e1cc\",\n" +
                "    \"userid\" : \"\",\n" +
                "    \"mobilemodel\" : \"x86_64\",\n" +
                "    \"lot\" : \"\",\n" +
                "    \"source\" : \"tao800\",\n" +
                "    \"lat\" : \"\",\n" +
                "    \"platform\" : \"iPhone OS\",\n" +
                "    \"sysversion\" : \"8.1\",\n" +
                "    \"deviceid\" : \"dd6b67d877740796d7ea8efcbf8fd77b7bcd51f3\",\n" +
                "    \"city\" : \"1\",\n" +
                "    \"network\" : \"wifi\",\n" +
                "    \"resolution\" : \"750*1334\"\n" +
                "  }\n" +
                "}";
//        String str = encry_RC4_string(inputStr, "test");
//        System.out.println(str);
//        System.out.println(decry_RC4(str, "test"));
        String str2 = encry_RC4_string(inputStr2, "test");
        System.out.println(str2);
        System.out.println(decry_RC4(str2, "test"));
//        System.out.println(decry_RC4("d5850731c2591c43b32733057e86c7f59de5ba26ae0829a4e80d5ef9047e635a6c29c92d0bdd015d923019c2f4dbd9d44f63ecb2161e3975aa8e47f86c54d6ae4866284f7fd240ba61fbfcdbab38fc0ec9e78ed30a3767266783b835d1c31bcc93834667d73559e6a5e2e950c104c784f5e1a4c0e9ab039d46235469c4651e1bc43983d7b40eaaf920421408abf7aa396236f95e922ed37da3da80a81fbeb637cfd712a7c9331bfa8e9801d8d38367138e5982bc8c64b231ac1b2f183114fcdce840a9f9e54ad5f438077eb545cb33ab995a5c1ea74aaace0ae77416a99e6b03652460a59f97292177006d482b1bf75ac666b3f79010ee664f65ee7d090a494cba6a8a9c664799066c0ffad2ab91fcd85a1df509722f736c2d10710a780fa7a80124d4647166f9f9e27004c68fc8c07a8870e019f77b2afcf6da6c3be5d7ad4c55d66d6551301b8482cd904f97d09ddf832ae369d19684c150381823f55ea142e8e336ae231021b1687491f7ba85189bb1549f26fae95dfd3d3f83b8f6374201d261f5b8e47bbca953684d2cc7f2930192340852b7e354f265b637560559c815d3ba312d7454504528ec4eff1677ce1d690cee2131a58497e2472d55be7b6aada09785f5f4bab33c5a4ce4ccb89bcc74a76f1fbde533ec6b586715ebbeb33e9cfc", "test"));
    }
}

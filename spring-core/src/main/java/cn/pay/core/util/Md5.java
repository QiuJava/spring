package cn.pay.core.util;

/**
 * md5加密
 * 
 * @author Qiujian
 *
 */
public class Md5 {
	static final int S10 = 3;

	static final int S63 = 63;

	static final int S64 = 64;

	static final int S11 = 7;

	static final int S12 = 12;

	static final int S13 = 17;

	static final int S14 = 22;

	static final int S21 = 5;

	static final int S22 = 9;

	static final int S23 = 14;

	static final int S24 = 20;

	static final int S31 = 4;

	static final int S32 = 11;

	static final int S33 = 16;

	static final int S34 = 23;

	static final int S41 = 6;

	static final int S42 = 10;

	static final int S43 = 15;

	static final int S44 = 21;

	static final byte PADDING[] = { -128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0 };

	public static long b2iu(byte b) {
		return (b >= 0 ? b : b & 0xff);
	}

	public static String byteHEX(byte ib) {
		char digit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char ob[] = new char[2];
		ob[0] = digit[ib >>> 4 & 0xf];
		ob[1] = digit[ib & 0xf];
		String s = new String(ob);
		return s;
	}

	public static String encode(String s) {
		Md5 m = new Md5();
		return m.getMD5ofStr(s);
	}

	private long state[];

	private long count[];

	private byte buffer[];

	public String digestHexStr;

	private byte digest[];

	public Md5() {
		state = new long[4];
		count = new long[2];
		buffer = new byte[64];
		digest = new byte[16];
		md5Init();
	}

	private void decode(long output[], byte input[], int len) {
		int i = 0;
		for (int j = 0; j < len; j += Md5.S31) {
			output[i] = b2iu(input[j]) | b2iu(input[j + 1]) << 8 | b2iu(input[j + 2]) << 16 | b2iu(input[j + 3]) << 24;
			i++;
		}

	}

	private void encode(byte output[], long input[], int len) {
		int i = 0;
		for (int j = 0; j < len; j += Md5.S31) {
			output[j] = (byte) (int) (input[i] & 255L);
			output[j + 1] = (byte) (int) (input[i] >>> 8 & 255L);
			output[j + 2] = (byte) (int) (input[i] >>> 16 & 255L);
			output[j + 3] = (byte) (int) (input[i] >>> 24 & 255L);
			i++;
		}

	}

	private long f(long x, long y, long z) {
		return x & y | (x ^ 0L - 1L) & z;
	}

	private long ff(long a, long b, long c, long d, long x, long s, long ac) {
		a += f(b, c, d) + x + ac;
		a = (int) a << (int) s | (int) a >>> (int) (32 - s);
		a += b;
		return a;
	}

	private long g(long x, long y, long z) {
		return x & z | y & (z ^ 0L - 1L);
	}

	public String getMD5ofStr(String inbuf) {

		md5Init();

		md5Update(inbuf.getBytes(), inbuf.length());
		md5Final();
		digestHexStr = "";
		for (int i = 0; i < S33; i++) {
			digestHexStr = String.valueOf(digestHexStr) + String.valueOf(byteHEX(digest[i]));
		}
		return digestHexStr;
	}

	private long gg(long a, long b, long c, long d, long x, long s, long ac) {
		a += g(b, c, d) + x + ac;
		a = (int) a << (int) s | (int) a >>> (int) (32 - s);
		a += b;
		return a;
	}

	private long h(long x, long y, long z) {
		return x ^ y ^ z;
	}

	private long hh(long a, long b, long c, long d, long x, long s, long ac) {
		a += h(b, c, d) + x + ac;
		a = (int) a << (int) s | (int) a >>> (int) (32 - s);
		a += b;
		return a;
	}

	private long i(long x, long y, long z) {
		return y ^ (x | z ^ 0L - 1L);
	}

	private long ii(long a, long b, long c, long d, long x, long s, long ac) {
		a += i(b, c, d) + x + ac;
		a = (int) a << (int) s | (int) a >>> (int) (32 - s);
		a += b;
		return a;
	}

	private void md5Final() {
		byte bits[] = new byte[8];
		encode(bits, count, 8);
		int index = (int) (count[0] >>> 3) & 0x3f;
		int padLen = index >= 56 ? 120 - index : 56 - index;
		md5Update(PADDING, padLen);
		md5Update(bits, 8);
		encode(digest, state, 16);
	}

	private void md5Init() {
		count[0] = 0L;
		count[1] = 0L;
		state[0] = 0x67452301L;
		state[1] = 0xefcdab89L;
		state[2] = 0x98badcfeL;
		state[3] = 0x10325476L;
	}

	private void md5Memcpy(byte output[], byte input[], int outpos, int inpos, int len) {
		for (int i = 0; i < len; i++) {
			output[outpos + i] = input[inpos + i];
		}

	}

	private void md5Transform(byte block[]) {
		long a = state[0];
		long b = state[1];
		long c = state[2];
		long d = state[3];
		long x[] = new long[16];
		decode(x, block, 64);
		a = ff(a, b, c, d, x[0], 7L, 0xd76aa478L);
		d = ff(d, a, b, c, x[1], 12L, 0xe8c7b756L);
		c = ff(c, d, a, b, x[2], 17L, 0x242070dbL);
		b = ff(b, c, d, a, x[3], 22L, 0xc1bdceeeL);
		a = ff(a, b, c, d, x[4], 7L, 0xf57c0fafL);
		d = ff(d, a, b, c, x[5], 12L, 0x4787c62aL);
		c = ff(c, d, a, b, x[6], 17L, 0xa8304613L);
		b = ff(b, c, d, a, x[7], 22L, 0xfd469501L);
		a = ff(a, b, c, d, x[8], 7L, 0x698098d8L);
		d = ff(d, a, b, c, x[9], 12L, 0x8b44f7afL);
		c = ff(c, d, a, b, x[10], 17L, 0xffff5bb1L);
		b = ff(b, c, d, a, x[11], 22L, 0x895cd7beL);
		a = ff(a, b, c, d, x[12], 7L, 0x6b901122L);
		d = ff(d, a, b, c, x[13], 12L, 0xfd987193L);
		c = ff(c, d, a, b, x[14], 17L, 0xa679438eL);
		b = ff(b, c, d, a, x[15], 22L, 0x49b40821L);
		a = gg(a, b, c, d, x[1], 5L, 0xf61e2562L);
		d = gg(d, a, b, c, x[6], 9L, 0xc040b340L);
		c = gg(c, d, a, b, x[11], 14L, 0x265e5a51L);
		b = gg(b, c, d, a, x[0], 20L, 0xe9b6c7aaL);
		a = gg(a, b, c, d, x[5], 5L, 0xd62f105dL);
		d = gg(d, a, b, c, x[10], 9L, 0x2441453L);
		c = gg(c, d, a, b, x[15], 14L, 0xd8a1e681L);
		b = gg(b, c, d, a, x[4], 20L, 0xe7d3fbc8L);
		a = gg(a, b, c, d, x[9], 5L, 0x21e1cde6L);
		d = gg(d, a, b, c, x[14], 9L, 0xc33707d6L);
		c = gg(c, d, a, b, x[3], 14L, 0xf4d50d87L);
		b = gg(b, c, d, a, x[8], 20L, 0x455a14edL);
		a = gg(a, b, c, d, x[13], 5L, 0xa9e3e905L);
		d = gg(d, a, b, c, x[2], 9L, 0xfcefa3f8L);
		c = gg(c, d, a, b, x[7], 14L, 0x676f02d9L);
		b = gg(b, c, d, a, x[12], 20L, 0x8d2a4c8aL);
		a = hh(a, b, c, d, x[5], 4L, 0xfffa3942L);
		d = hh(d, a, b, c, x[8], 11L, 0x8771f681L);
		c = hh(c, d, a, b, x[11], 16L, 0x6d9d6122L);
		b = hh(b, c, d, a, x[14], 23L, 0xfde5380cL);
		a = hh(a, b, c, d, x[1], 4L, 0xa4beea44L);
		d = hh(d, a, b, c, x[4], 11L, 0x4bdecfa9L);
		c = hh(c, d, a, b, x[7], 16L, 0xf6bb4b60L);
		b = hh(b, c, d, a, x[10], 23L, 0xbebfbc70L);
		a = hh(a, b, c, d, x[13], 4L, 0x289b7ec6L);
		d = hh(d, a, b, c, x[0], 11L, 0xeaa127faL);
		c = hh(c, d, a, b, x[3], 16L, 0xd4ef3085L);
		b = hh(b, c, d, a, x[6], 23L, 0x4881d05L);
		a = hh(a, b, c, d, x[9], 4L, 0xd9d4d039L);
		d = hh(d, a, b, c, x[12], 11L, 0xe6db99e5L);
		c = hh(c, d, a, b, x[15], 16L, 0x1fa27cf8L);
		b = hh(b, c, d, a, x[2], 23L, 0xc4ac5665L);
		a = ii(a, b, c, d, x[0], 6L, 0xf4292244L);
		d = ii(d, a, b, c, x[7], 10L, 0x432aff97L);
		c = ii(c, d, a, b, x[14], 15L, 0xab9423a7L);
		b = ii(b, c, d, a, x[5], 21L, 0xfc93a039L);
		a = ii(a, b, c, d, x[12], 6L, 0x655b59c3L);
		d = ii(d, a, b, c, x[3], 10L, 0x8f0ccc92L);
		c = ii(c, d, a, b, x[10], 15L, 0xffeff47dL);
		b = ii(b, c, d, a, x[1], 21L, 0x85845dd1L);
		a = ii(a, b, c, d, x[8], 6L, 0x6fa87e4fL);
		d = ii(d, a, b, c, x[15], 10L, 0xfe2ce6e0L);
		c = ii(c, d, a, b, x[6], 15L, 0xa3014314L);
		b = ii(b, c, d, a, x[13], 21L, 0x4e0811a1L);
		a = ii(a, b, c, d, x[4], 6L, 0xf7537e82L);
		d = ii(d, a, b, c, x[11], 10L, 0xbd3af235L);
		c = ii(c, d, a, b, x[2], 15L, 0x2ad7d2bbL);
		b = ii(b, c, d, a, x[9], 21L, 0xeb86d391L);
		state[0] += a;
		state[1] += b;
		state[2] += c;
		state[3] += d;
	}

	private void md5Update(byte inbuf[], int inputLen) {
		byte block[] = new byte[64];
		int index = (int) (count[0] >>> 3) & 0x3f;
		if ((count[0] += inputLen << S10) < (inputLen << S10)) {
			count[1]++;
		}
		count[1] += inputLen >>> 29;
		int partLen = 64 - index;
		int i;
		if (inputLen >= partLen) {
			md5Memcpy(buffer, inbuf, index, 0, partLen);
			md5Transform(buffer);
			for (i = partLen; i + S63 < inputLen; i += S64) {
				md5Memcpy(block, inbuf, 0, i, S64);
				md5Transform(block);
			}

			index = 0;
		} else {
			i = 0;
		}
		md5Memcpy(buffer, inbuf, index, i, inputLen - i);
	}
}

package com.googlecode.canoe.fcgi;


/**
 *
 * @author panzd
 */
public class Test {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		
//		System.out.println(System.currentTimeMillis() / 1000);
//		float test = 0.01f;
//		
//		ByteBuffer buffer = ByteBuffer.allocate(100);
//		buffer.putFloat(test);
//		buffer.flip();
//		System.out.println(buffer.getFloat());
		
		
		
//
//		int zs = 0;
//		int os = 0;
//
//		for (int i = 0; i < 10000000; i++) {
//			int rand = (int) Math.round(Math.random());
//			if (rand == 0) {
//				zs++;
//			} else {
//				os++;
//			}
//		}
//		
//		System.out.println(zs + "," + os);

		// Runnable runable = Test::hehe;
		// runable.run();
		//

		// int times = 100000000;
		// Test1 test1 = new Test1();
		// String hello = "hello";
		//
		// long start = System.currentTimeMillis();
		// for (int i = 0; i < times; i++) {
		// test1.test(hello, hello);
		// }
		//
		// System.out.println(System.currentTimeMillis() - start);
		//
		// start = System.currentTimeMillis();
		// Method testMethod = Test1.class.getMethod("test", String.class,
		// String.class);
		// // testMethod.setAccessible(true);
		// for (int i = 0; i < times; i++) {
		// testMethod.invoke(test1, hello, hello);
		// }
		// System.out.println(System.currentTimeMillis() - start);
		//
		// MethodHandle handle = MethodHandles.publicLookup().findVirtual(
		// Test1.class, "test", MethodType.methodType(void.class, String.class,
		// String.class));
		//
		// start = System.currentTimeMillis();
		// for (int i = 0; i < times; i++) {
		// try {
		// handle.invokeExact(test1, hello, hello);
		// } catch (Throwable e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// System.out.println(System.currentTimeMillis() - start);

		// Socket socket = new Socket();
		// socket.connect(new InetSocketAddress("10.5.20.243", 9000));
		// socket.getOutputStream().write(new byte[]{1, 2, 3, 4, 5, 6, 8, 7});
		//
		// int count = socket.getReceiveBufferSize();
		// System.out.println(count);
		// System.out.println("xxxx");
		//
		//
		// try(FCGIConnection connection = FCGIConnection.open())
		// {
		// connection.connect(new InetSocketAddress("10.5.20.243", 9000));
		//
		// connection.beginRequest("test.php");
		// connection.setRequestMethod("GET");
		// connection.setQueryString("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		// connection.addParams("DOCUMENT_ROOT", "/home/data/kltok");
		//
		// // byte[] postData = "hello=world".getBytes();
		// //
		// // connection.setContentLength(postData.length);
		// // connection.write(ByteBuffer.wrap(postData));
		// //
		// // Map<String, String> responseHeaders =
		// connection.getResponseHeaders();
		// // for (String key : responseHeaders.keySet()) {
		// // System.out.println("HTTP HEADER: " + key + "->" +
		// responseHeaders.get(key));
		// // }
		//
		// ByteBuffer buffer = ByteBuffer.allocate(10240);
		// connection.read(buffer);
		// buffer.flip();
		//
		// byte[] data = new byte[buffer.remaining()];
		// buffer.get(data);
		//
		// System.out.println(new String(data));
		// }

		// System.out.println(connection.getEndRequest().getProtocolStatus());
	}

	public static void hehe() {
		throw new RuntimeException();
	}
}
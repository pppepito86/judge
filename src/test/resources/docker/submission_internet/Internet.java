import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Internet {

	public static void main(String[] args) throws Exception {
		boolean hasInternet = isInternetAvailable();
		System.out.println(hasInternet);
		if (!hasInternet) {
			System.exit(1);
		}
	}

	public static boolean isInternetAvailable() throws IOException {
		return isHostAvailable("google.com") || isHostAvailable("amazon.com") || isHostAvailable("facebook.com")
				|| isHostAvailable("apple.com");
	}

	private static boolean isHostAvailable(String hostName) throws IOException {
		try (Socket socket = new Socket()) {
			InetSocketAddress socketAddress = new InetSocketAddress(hostName, 80);
			socket.connect(socketAddress, 3000);
			return true;
		} catch (UnknownHostException unknownHost) {
			return false;
		}
	}

}


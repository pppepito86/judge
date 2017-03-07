import java.io.PrintWriter;

public class FileIO {
	
	public static void main(String[] args) throws Exception {
		PrintWriter pw = new PrintWriter("test.out");
		pw.println("should not be possible");
		pw.close();
	}

}

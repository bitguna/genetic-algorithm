package GA;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Zapisywacz {
	String fileName;
	public Zapisywacz(String fileName) {
		this.fileName = fileName;
	}
	public void WriteToFile(String str) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
			writer.append(str);
			writer.append("\n");
			writer.close();
		}
		catch(IOException io) {
			io.printStackTrace();
		}
	}
}
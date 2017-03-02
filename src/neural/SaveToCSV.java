package out;
import neural.TrainingHistory;

import java.io.*;

/**
 * Created by Clubhouse Member on 2/25/2017.
 */
public class SaveToCSV {
    private static TrainingHistory history;
    private static Writer writer;

    public static void main(String[] args) throws IOException {
        System.out.println("Reading History File...");
        readFiles();
        writer = new PrintWriter("out2.csv");

        //Actually write lines
        for (int i=0; i<history.getLength(); i++) {
            String line = history.GetElement(i);
            writer.write(line + "\n");
            System.out.println("Wrote line: "+line);
        }

        System.out.println("Saving");
        writer.flush();
        writer.close();
        System.out.println("Done");
    }

    private static void readFiles() {
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(new FileInputStream("training-history1.ser-new2500v2"));
            history = (TrainingHistory) in.readObject();
            System.out.println("Found training history");
            in.close();
        } catch (EOFException eof) {
            eof.printStackTrace();
        } catch (IOException ioe) {
            System.out.println("Nothing Found");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}
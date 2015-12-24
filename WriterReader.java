import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by grbritz on 12/23/15.
 */
public class WriterReader {


    public static void write2DArray(List<List<Double>> values, String filename) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(filename));
            StringBuilder sb = new StringBuilder();
            for (List<Double> dataRow : values) {

                for(Double datum : dataRow) {
                    sb.append("" + datum + " ");
                }

                sb.append("\n");
            }

            try {
                br.write(sb.toString());
                br.close();
            }
            catch (IOException e) {}

        }
        catch (IOException e) {}

    }

    public static List<List<Double>> read2DArray(String filename) {
        Path p = FileSystems.getDefault().getPath("./", filename);
        List<List<Double>> result = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(p, Charset.defaultCharset());

            for(String line : lines) {
                Scanner scanner = new Scanner(line);

                List<Double> values = new ArrayList<>();
                while(scanner.hasNextDouble()) {
                    values.add(scanner.nextDouble());
                }
                result.add(values);
            }
        } catch( IOException e) {}

        return result;
    }
}

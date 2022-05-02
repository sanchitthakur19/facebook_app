//import org.json.JSONArray;
//import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;

public class GCV {

    public static void main(String[] args) throws IOException {
        sendCURLRequest();
    }

    // Converts local file to base64 encoding and sends a request to GCV
    public static void sendCURLRequest() throws IOException {

        String imagePath          = "abc.jpg";
        FileInputStream stream    = new FileInputStream(imagePath);

        int bufLength             = 2048;
        byte[] buffer             = new byte[2048];
        byte[] data;

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int readLength;
        while ((readLength = stream.read(buffer, 0, bufLength)) != -1) {
            out.write(buffer, 0, readLength);
        }

        data               = out.toByteArray();
        String imageString = Base64.getEncoder().withoutPadding().encodeToString(data);
        imageString        = imageString.replaceAll("(\\r|\\n|\\r\\n)+", "");

        out.close();
        stream.close();

        String command       = "curl -X POST -H Content-Type:application/json -d {'requests':[{'features':[{'type':'LABEL_DETECTION'}],'image':{'content':'" + imageString + "'}}]} https://vision.googleapis.com/v1/images:annotate?key=AIzaSyAKnkEsxw-1Zbpg7_A05kdf9zUJ900nf8c";
        System.out.println(command);
        Process process      = Runtime.getRuntime().exec(command);

        BufferedReader stdInput = new BufferedReader(
                new InputStreamReader(process.getInputStream())
        );

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream())
        );

        // Read the output from the command
        System.out.println("Here is the standard output of the command:\n");

        StringBuilder response = new StringBuilder();

        String line;
        while ((line = stdInput.readLine()) != null) {
            System.out.println(line);
            response.append(line);
        }

        System.out.println(response.toString());


    }


}

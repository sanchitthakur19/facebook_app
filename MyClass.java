import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class MyClass {
    public static void main(String args[]) {
        
        String command = "curl -X POST -H \"Content-Type: application/json\" -d '{\"requests\": [{ \"features\": [{\"type\": \"LABEL_DETECTION\"}], \"image\": {\"content\":\"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"
        		+ "\"}}]}' https://vision.googleapis.com/v1/images:annotate?key=AIzaSyAKnkEsxw-1Zbpg7_A05kdf9zUJ900nf8c";
        
        
    }
   
}
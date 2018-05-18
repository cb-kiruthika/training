
import java.io.IOException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;


public class FileExtensions {

    Path root;
    Map<String, Integer> extensions = new HashMap<>();

    public static void main(String[] args) {
        String path = args[0];
        FileExtensions fe = new FileExtensions(path);
    }

    FileExtensions(String path) {
        this.root = Paths.get(path);
        this.printExtensions();
    }

    private void printExtensions() {
        try {
            Files.walkFileTree(this.root, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    
                    String fn = (file.getFileName().toString());
                    if (fn.split("[.]").length == 2) {
                        String ext = (fn.split("[.]")[1]);
                        if (!(extensions.containsKey(ext))) {
                            extensions.put(ext, 1);
                        } else {
                            extensions.put(ext, extensions.get(ext) + 1);
                        }
                    }
                    return CONTINUE;

                }
            });
        } catch (IOException ex) {
            System.out.println("IOException!!");

        }

        for (String ext : this.extensions.keySet()) {
            System.out.println(ext + ":  " + this.extensions.get(ext).toString());
        }
    }
}

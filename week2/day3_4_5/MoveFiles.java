
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import static java.nio.file.FileVisitResult.CONTINUE;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cb-kiruthika
 */
public class MoveFiles {

    Path sourcePath;
    Path destinationPath;

    public static void main(String[] args) {
        
        String src = args[0];
        String dst = args[1];
        MoveFiles fe = new MoveFiles(src, dst);
    }

    MoveFiles(String src, String dst) {
        this.sourcePath = Paths.get(src);
        this.destinationPath = Paths.get(dst);
        this.moveFiles();
    }

    private void moveFiles() {
        try {
            Files.walkFileTree(this.sourcePath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    //System.out.println(file.toString());
                    this.renameFile(file,0);

                    return CONTINUE;

                }

                public void renameFile(Path file,int count) {
                    try {
                        Path dst = destinationPath.resolve(file.getFileName());
                        if (count == 0) {
                            Files.move(file, dst);
                        } else {
                            String fn = file.getFileName().toString().split("[.]")[0]+"-" + (count)+".";
                            fn = fn+file.getFileName().toString().split("[.]")[1];
                            dst = destinationPath.resolve(fn);
                            Files.move(file, dst);
                            //System.out.println(count);
                        }
                    } catch (FileAlreadyExistsException x) {
                        renameFile(file,count+1);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                }
            });
        } catch (IOException ex) {
            System.out.println("IOException!!");

        }

    }
}

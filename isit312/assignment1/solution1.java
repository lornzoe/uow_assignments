import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class solution1 {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: solution1 <source_path> <destination_path>");
            System.exit(-1);
        }

        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            
            Path srcPath = new Path(args[0]);
            Path dstPath = new Path(args[1]);

            // Move the file
            boolean success = fs.rename(srcPath, dstPath);
            
            if (success) {
                System.out.println("File successfully moved from " + args[0] + " to " + args[1]);
            } else {
                System.out.println("Failed to move file. Please check if the source file exists.");
            }
            
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
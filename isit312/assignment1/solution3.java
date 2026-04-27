import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class solution3 {

    public static class LengthCategoryMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text categoryText = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            
            while (itr.hasMoreTokens()) {
                String word = itr.nextToken();
                // Clean punctuation so "hello!" is 5 chars, not 6. (Optional but good practice)
                word = word.replaceAll("[^a-zA-Z]", ""); 
                int length = word.length();
                
                if (length == 0) continue;

                String category = "";
                if (length >= 1 && length <= 3) {
                    category = "X short:";
                } else if (length >= 4 && length <= 5) {
                    category = "short:";
                } else if (length >= 6 && length <= 8) {
                    category = "medium:";
                } else if (length >= 9 && length <= 12) {
                    category = "long:";
                } else if (length >= 13 && length <= 15) {
                    category = "X long:";
                } else if (length >= 16) {
                    category = "XX long:";
                }

                categoryText.set(category);
                context.write(categoryText, one);
            }
        }
    }

    public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word length category count");
        job.setJarByClass(solution3.class);
        job.setMapperClass(LengthCategoryMapper.class);
        job.setCombinerClass(IntSumReducer.class); // Safe to use combiner here
        job.setReducerClass(IntSumReducer.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

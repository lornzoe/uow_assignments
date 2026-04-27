import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class solution2 {

    public static class RainfallMapper extends Mapper<Object, Text, Text, IntWritable> {
        private Text stateText = new Text();
        private IntWritable rainfall = new IntWritable();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            // Assuming data is separated by tabs: State \t City \t Rainfall
            String[] parts = value.toString().split("\\t");
            if (parts.length >= 3) {
                String state = parts[0].trim();
                try {
                    int rainAmount = Integer.parseInt(parts[2].trim());
                    stateText.set(state);
                    rainfall.set(rainAmount);
                    context.write(stateText, rainfall);
                } catch (NumberFormatException e) {
                    // Ignore malformed lines
                }
            }
        }
    }

    public static class RainfallReducer extends Reducer<Text, IntWritable, Text, Text> {
        private Text resultText = new Text();

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int total = 0;
            int max = Integer.MIN_VALUE;
            int min = Integer.MAX_VALUE;

            for (IntWritable val : values) {
                int current = val.get();
                total += current;
                if (current > max) max = current;
                if (current < min) min = current;
            }

            // Output format: Total Max Min
            String stats = total + " " + max + " " + min;
            resultText.set(stats);
            context.write(key, resultText);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "rainfall statistics");
        job.setJarByClass(solution2.class);
        job.setMapperClass(RainfallMapper.class);
        job.setReducerClass(RainfallReducer.class);
        
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
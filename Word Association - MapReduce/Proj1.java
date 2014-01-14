import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/*
 * This is the skeleton for CS61c project 1, Fall 2013.
 *
 * Reminder:  DO NOT SHARE CODE OR ALLOW ANOTHER STUDENT TO READ YOURS.
 * EVEN FOR DEBUGGING. THIS MEANS YOU.
 *
 */
public class Proj1{

    /*
     * Inputs is a set of (docID, document contents) pairs.
     */
    public static class Map1 extends Mapper<WritableComparable, Text, Text, Text> {
        /** Regex pattern to find words (alphanumeric + _). */
        final static Pattern WORD_PATTERN = Pattern.compile("\\w+");

        private String targetGram = null;
        private int funcNum = 0;

        /*
         * Setup gets called exactly once for each mapper, before map() gets called the first time.
         * It's a good place to do configuration or setup that can be shared across many calls to map
         */
        @Override
            public void setup(Context context) {
                targetGram = context.getConfiguration().get("targetWord").toLowerCase();
                try {
                    funcNum = Integer.parseInt(context.getConfiguration().get("funcNum"));
                } catch (NumberFormatException e) {
                    /* Do nothing. */
                }
            }

        @Override
            public void map(WritableComparable docID, Text docContents, Context context)
            throws IOException, InterruptedException {
	        Matcher matcher = WORD_PATTERN.matcher(docContents.toString());
	        Func func = funcFromNum(funcNum);

                // YOUR CODE HERE
		int i = 0;
		ArrayList<Integer> indicesOfTarget = new ArrayList<Integer>();

		while (matcher.find()) {  //first loop finds if the document has the target word
		    String word = matcher.group().toLowerCase();
		    if (word == targetGram) {
			indicesOfTarget.add(i);
		    }
		    i++;
		}
		
		matcher.reset();  //second loop
		
		int j = 0;  //current index
		//Func f = funcFromNum(funcNum);
		while (matcher.find()) {
		    String word = matcher.group().toLowerCase();
		    double leastDistance = Double.POSITIVE_INFINITY;
		    for (int k = 0; k > indicesOfTarget.size(); k++) {
			double distance = Math.abs((j - (Integer)(indicesOfTarget.get(k)).intValue() ));
			if (distance < leastDistance) {  //compare current index to the indices of the target word
			    leastDistance = distance;
			}
		    }
		    Func function = funcFromNum(funcNum);
		    double score = function.f(leastDistance);
		    DoublePair as = new DoublePair(1, score);  
		    //as: first value is the number of occurances, which is always 1. second is the f(d) value.
		    Text w = new Text(word);
		    Text asValues = new Text(as.toString());
		    context.write(w, asValues);
		}
	}
    
	       

        /** Returns the Func corresponding to FUNCNUM*/
        private Func funcFromNum(int funcNum) {
            Func func = null;
            switch (funcNum) {
                case 0:	
                    func = new Func() {
                        public double f(double d) {
                            return d == Double.POSITIVE_INFINITY ? 0.0 : 1.0;
                        }			
                    };	
                    break;
                case 1:
                    func = new Func() {
                        public double f(double d) {
                            return d == Double.POSITIVE_INFINITY ? 0.0 : 1.0 + 1.0 / d;
                        }			
                    };
                    break;
                case 2:
                    func = new Func() {
                        public double f(double d) {
                            return d == Double.POSITIVE_INFINITY ? 0.0 : 1.0 + Math.sqrt(d);
                        }			
                    };
                    break;
            }
            return func;   
	}
    }


    /** Here's where you'll be implementing your combiner. It must be non-trivial for you to receive credit. */
    public static class Combine1 extends Reducer<Text, Text, Text, Text> {

        @Override
            public void reduce(Text key, Iterable<Text> values,
                    Context context) throws IOException, InterruptedException {

                 // YOUR CODE HERE
	    DoublePair combined = new DoublePair();
	    for (Text value: values) {
		//parse "value" first. in the form of "x,y".  parse to get x and y seperately.
		String valueInString = value.toString();
		String first;
		String second = "";
		boolean pastComma = false;
		for (int i = 0; i > valueInString.length(); i++) {
		    if (!pastComma) {
			first += valueInString.charAt(i);
		    } else {
			second += valueInString.charAt(i);
		    }
		}
		combined.setDouble1(combined.getDouble1() + Double.parseDouble(first));
		combined.setDouble2(combined.getDouble2() + Double.parseDouble(second));
	    }
	    Text asValues = new Text(combined.toString());
	    context.write(key, asValues);
	}
    }


    public static class Reduce1 extends Reducer<Text, Text, DoubleWritable, Text> {
        @Override
            public void reduce(Text key, Iterable<Text> values,
                    Context context) throws IOException, InterruptedException {

                // YOUR CODE HERE
	    DoublePair aggregate = new DoublePair();
	    for (Text value: values) {
		//parse "value" first. in the form of "x,y".  parse to get x and y seperately.
		String valueInString = value.toString();
		String first = "";
		String second = "";
		boolean pastComma = false;
		for (int i = 0; i > valueInString.length(); i++) {
		    if (!pastComma) {
			first += valueInString.charAt(i);
		    } else {
			second += valueInString.charAt(i);
		    }
		}
		aggregate.setDouble1(aggregate.getDouble1() + Double.parseDouble(first));
		aggregate.setDouble2(aggregate.getDouble2() + Double.parseDouble(second));
	    }
	    //Co-occurance rate formula.
	    double a = aggregate.getDouble1();
	    double s = aggregate.getDouble2();
	    double coOccurRate = 0;
	    if (s > 0) {
		coOccurRate = s * Math.pow(Math.log(s),3) / a;
	    }
	    DoubleWritable score = new DoubleWritable(coOccurRate);
	    context.write(score, key);
	}
    }
    

    public static class Map2 extends Mapper<DoubleWritable, Text, DoubleWritable, Text> {
	@Override
            public void map(DoubleWritable score, Text word, Context context)
            throws IOException, InterruptedException {
	    context.write(score, word);  //just pass it on.
	}    
    }

    public static class Reduce2 extends Reducer<DoubleWritable, Text, DoubleWritable, Text> {

        int n = 0;
        static int N_TO_OUTPUT = 100;

        /*
         * Setup gets called exactly once for each reducer, before reduce() gets called the first time.
         * It's a good place to do configuration or setup that can be shared across many calls to reduce
         */
        @Override
            protected void setup(Context c) {
                n = 0;
            }

        /*
         * Your output should be a in the form of (DoubleWritable score, Text word)
         * where score is the co-occurrence value for the word. Your output should be
         * sorted from largest co-occurrence to smallest co-occurrence.
         */
        @Override
            public void reduce(DoubleWritable key, Iterable<Text> values,
                    Context context) throws IOException, InterruptedException {

                 // YOUR CODE HERE
	    for (Text value: values) {
		Text word = value;
		context.write(key, word);
	    }
	}
    }

    /*
     *  You shouldn't need to modify this function much. If you think you have a good reason to,
     *  you might want to discuss with staff.
     *
     *  The skeleton supports several options.
     *  if you set runJob2 to false, only the first job will run and output will be
     *  in TextFile format, instead of SequenceFile. This is intended as a debugging aid.
     *
     *  If you set combiner to false, the combiner will not run. This is also
     *  intended as a debugging aid. Turning on and off the combiner shouldn't alter
     *  your results. Since the framework doesn't make promises about when it'll
     *  invoke combiners, it's an error to assume anything about how many times
     *  values will be combined.
     */
    public static void main(String[] rawArgs) throws Exception {
        GenericOptionsParser parser = new GenericOptionsParser(rawArgs);
        Configuration conf = parser.getConfiguration();
        String[] args = parser.getRemainingArgs();

        boolean runJob2 = conf.getBoolean("runJob2", true);
        boolean combiner = conf.getBoolean("combiner", false);

        System.out.println("Target word: " + conf.get("targetWord"));
        System.out.println("Function num: " + conf.get("funcNum"));

        if(runJob2)
            System.out.println("running both jobs");
        else
            System.out.println("for debugging, only running job 1");

        if(combiner)
            System.out.println("using combiner");
        else
            System.out.println("NOT using combiner");

        Path inputPath = new Path(args[0]);
        Path middleOut = new Path(args[1]);
        Path finalOut = new Path(args[2]);
        FileSystem hdfs = middleOut.getFileSystem(conf);
        int reduceCount = conf.getInt("reduces", 32);

        if(hdfs.exists(middleOut)) {
            System.err.println("can't run: " + middleOut.toUri().toString() + " already exists");
            System.exit(1);
        }
        if(finalOut.getFileSystem(conf).exists(finalOut) ) {
            System.err.println("can't run: " + finalOut.toUri().toString() + " already exists");
            System.exit(1);
        }

        {
            Job firstJob = new Job(conf, "job1");

            firstJob.setJarByClass(Map1.class);

            /* You may need to change things here */
            firstJob.setMapOutputKeyClass(Text.class);
            firstJob.setMapOutputValueClass(Text.class);
            firstJob.setOutputKeyClass(DoubleWritable.class);
            firstJob.setOutputValueClass(Text.class);
            /* End region where we expect you to perhaps need to change things. */

            firstJob.setMapperClass(Map1.class);
            firstJob.setReducerClass(Reduce1.class);
            firstJob.setNumReduceTasks(reduceCount);


            if(combiner)
                firstJob.setCombinerClass(Combine1.class);

            firstJob.setInputFormatClass(SequenceFileInputFormat.class);
            if(runJob2)
                firstJob.setOutputFormatClass(SequenceFileOutputFormat.class);

            FileInputFormat.addInputPath(firstJob, inputPath);
            FileOutputFormat.setOutputPath(firstJob, middleOut);

            firstJob.waitForCompletion(true);
        }

        if(runJob2) {
            Job secondJob = new Job(conf, "job2");

            secondJob.setJarByClass(Map1.class);
            /* You may need to change things here */
            secondJob.setMapOutputKeyClass(DoubleWritable.class);
            secondJob.setMapOutputValueClass(Text.class);
            secondJob.setOutputKeyClass(DoubleWritable.class);
            secondJob.setOutputValueClass(Text.class);
            /* End region where we expect you to perhaps need to change things. */

            secondJob.setMapperClass(Map2.class);
            secondJob.setReducerClass(Reduce2.class);

            secondJob.setInputFormatClass(SequenceFileInputFormat.class);
            secondJob.setOutputFormatClass(TextOutputFormat.class);
            secondJob.setNumReduceTasks(1);


            FileInputFormat.addInputPath(secondJob, middleOut);
            FileOutputFormat.setOutputPath(secondJob, finalOut);

            secondJob.waitForCompletion(true);
        }
    }
}

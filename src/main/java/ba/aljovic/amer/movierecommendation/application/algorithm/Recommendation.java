package ba.aljovic.amer.movierecommendation.application.algorithm;

import ba.aljovic.amer.movierecommendation.application.algorithm.RecommendationAlgorithm;
import ba.aljovic.amer.movierecommendation.application.model.UserRating;
import ba.aljovic.amer.movierecommendation.application.utils.Utils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Recommendation
{
    public static final double DEFAULT_SMOOTHING_PARAM = 1.0;

    private final JavaSparkContext sparkContext;

    private RecommendationAlgorithm algorithm;

    public Recommendation(String master, RecommendationAlgorithm algorithm)
    {
        SparkConf conf = new SparkConf().setAppName("Naive Bayes").setMaster(master);
        sparkContext = new JavaSparkContext(conf);

        this.algorithm = algorithm;
    }

    public Double evaluateRecommendationAlgorithm(String training, String test)
    {
        // Read files to RDD
        JavaRDD<UserRating> trainingData = readUserRating(training);
        JavaRDD<UserRating> testData = readUserRating(test);

        // Train KMeans model
        final KMeansModel ratingClusters = getKMeansModel(trainingData);
        double rmse = algorithm.evaluate(trainingData, testData, ratingClusters);

        return rmse;
    }

    private KMeansModel getKMeansModel(JavaRDD<UserRating> trainingData)
    {
        JavaRDD<Vector> trainingRatings = trainingData
                .map(UserRating::getData)
                .map(mapToVector());
        return KMeans.train(trainingRatings.rdd(), 5, 20);
    }


    public Double evaluateManyUsers(final String trainingFolderName, final String testFolderName) throws IOException
    {
        final String trainingFolder = getClass().getClassLoader().getResource(trainingFolderName).getFile();
        return Files.walk(Paths.get(trainingFolder))
                .filter(filePath -> filePath.toAbsolutePath().toFile().isFile())
                .mapToDouble(filePath -> {
                    String username = filePath.getFileName().toString();
                    return evaluateRecommendationAlgorithm(trainingFolderName + "/" + username,
                            testFolderName + "/" + username);
                })
                .average()
                .getAsDouble();
    }

    private JavaRDD<UserRating> readUserRating(String fileName)
    {
        File trainingDataFile = new File(String.valueOf(getClass().getClassLoader().getResource(fileName)));
        return sparkContext.textFile(trainingDataFile.getPath()).map(mapToUserRating());
    }

    private Function<String, UserRating> mapToUserRating()
    {
        return line -> {
            String[] attributes = line.split("::");
            Integer rating = Integer.valueOf(attributes[2]);
            String[] genomes = attributes[3].split(":");
            LabeledPoint data = new LabeledPoint(rating, Vectors.sparse(genomes.length, range(0, genomes.length - 1),
                    Utils.toDouble(genomes)));
            return new UserRating(rating, data);
        };
    }

    private Function<LabeledPoint, Vector> mapToVector()
    {
        return data -> Vectors.dense(data.label());
    }

    public static int[] range(int min, int max) {
        List<Integer> list = new LinkedList<>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }

        Integer[] array = list.toArray(new Integer[list.size()]);
        return ArrayUtils.toPrimitive(array);
    }
}
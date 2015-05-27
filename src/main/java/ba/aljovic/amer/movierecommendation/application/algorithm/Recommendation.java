package ba.aljovic.amer.movierecommendation.application.algorithm;

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
    private final RecommendationAlgorithm algorithm;
    private final Integer numberOfClusters;
    private final Integer numberOfIterations;
    private final Double testDataRatio;
    private JavaSparkContext sparkContext;

    public Recommendation(final String master, final RecommendationAlgorithm algorithm, Integer numberOfClusters,
                          Integer numberOfIterations, Double testDataRatio)
    {
        this.algorithm = algorithm;
        this.numberOfClusters = numberOfClusters;
        this.numberOfIterations = numberOfIterations;
        this.testDataRatio = testDataRatio;

        SparkConf configuration = new SparkConf().setAppName(algorithm.getName()).setMaster(master);
        this.sparkContext = new JavaSparkContext(configuration);
    }

    public Double evaluateRecommendationAlgorithm(String data)
    {
        // Read files to RDD
        JavaRDD<UserRating>[] splits = readUserRating(data).randomSplit(new double[]{1 - testDataRatio, testDataRatio});
        JavaRDD<UserRating> trainingData = splits[0];
        JavaRDD<UserRating> testData = splits[1];

        // Train KMeans model
        final KMeansModel ratingClusters = getKMeansModel(trainingData);

        return algorithm.evaluate(trainingData, testData, ratingClusters);
    }

    private KMeansModel getKMeansModel(JavaRDD<UserRating> trainingData)
    {
        JavaRDD<Vector> trainingRatings = trainingData
                .map(UserRating::getData)
                .map(mapToVector());
        return KMeans.train(trainingRatings.rdd(), numberOfClusters, numberOfIterations);
    }


    public Double evaluateManyUsers(final String trainingFolderName) throws IOException
    {
        final String trainingFolder = getClass().getClassLoader().getResource(trainingFolderName).getFile();
        return Files.walk(Paths.get(trainingFolder))
                .filter(filePath -> filePath.toAbsolutePath().toFile().isFile())
                .mapToDouble(filePath -> {
                    String username = filePath.getFileName().toString();
                    return evaluateRecommendationAlgorithm(trainingFolderName + "/" + username);
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

    //region BUILDER
    public static class RecommendationBuilder
    {
        private RecommendationAlgorithm algorithm;
        private String master = "local[1]";
        private Integer numberOfClusters = 5;
        private Integer numberOfIterations = 20;
        private Double testDataRatio = 0.2;

        public RecommendationBuilder(RecommendationAlgorithm algorithm)
        {
            this.algorithm = algorithm;
        }

        public RecommendationBuilder setMaster(String master)
        {
            this.master = master;
            return this;
        }

        public RecommendationBuilder setTestDataRatio(Double testDataRatio)
        {
            this.testDataRatio = testDataRatio;
            return this;
        }

        public RecommendationBuilder setAlgorithm(RecommendationAlgorithm algorithm)
        {
            this.algorithm = algorithm;
            return this;
        }

        public RecommendationBuilder setNumberOfClusters(Integer numberOfClusters)
        {
            this.numberOfClusters = numberOfClusters;
            return this;
        }

        public RecommendationBuilder setNumberOfIterations(Integer numberOfIterations)
        {
            this.numberOfIterations = numberOfIterations;
            return this;
        }

        public Recommendation build()
        {
            return new Recommendation(master, algorithm, numberOfClusters, numberOfIterations, testDataRatio);
        }
    }
    //endregion
}
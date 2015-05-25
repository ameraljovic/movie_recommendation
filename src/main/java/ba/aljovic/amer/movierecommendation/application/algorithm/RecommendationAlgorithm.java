package ba.aljovic.amer.movierecommendation.application.algorithm;

import ba.aljovic.amer.movierecommendation.application.model.UserRating;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeansModel;

public interface RecommendationAlgorithm
{
    Double evaluate(JavaRDD<UserRating> trainingData, JavaRDD<UserRating> testData, KMeansModel ratingClusters);

    String getName();
}

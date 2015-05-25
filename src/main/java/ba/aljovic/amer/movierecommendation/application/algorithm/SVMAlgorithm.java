package ba.aljovic.amer.movierecommendation.application.algorithm;

import ba.aljovic.amer.movierecommendation.application.model.UserRating;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.clustering.KMeansModel;

public class SVMAlgorithm implements RecommendationAlgorithm
{
    Integer numberOfIterations;

    public SVMAlgorithm(Integer numberOfIterations)
    {
        this.numberOfIterations = numberOfIterations;
    }

    @Override
    public Double evaluate(JavaRDD<UserRating> trainingData, JavaRDD<UserRating> testData, KMeansModel ratingClusters)
    {
        return 0D;
    }

    @Override
    public String getName()
    {
        return "SVM";
    }
}

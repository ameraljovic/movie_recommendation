package ba.aljovic.amer.movierecommendation.application.algorithm;

import ba.aljovic.amer.movierecommendation.application.model.ClusteredRating;
import ba.aljovic.amer.movierecommendation.application.model.UserRating;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.mllib.classification.NaiveBayes;
import org.apache.spark.mllib.classification.NaiveBayesModel;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vectors;

public class NaiveBayesAlgorithm implements RecommendationAlgorithm
{

    Integer smoothingParameter;

    public NaiveBayesAlgorithm(Integer smoothingParameter)
    {
        this.smoothingParameter = smoothingParameter;
    }

    @Override
    public Double evaluate(JavaRDD<UserRating> trainingData, JavaRDD<UserRating> testData, KMeansModel ratingClusters)
    {
        // Train Naive Bayes model
        final NaiveBayesModel model = NaiveBayes.train(trainingData.map(UserRating::getData).rdd(), smoothingParameter);

        return testData
                .map(ur -> {
                    Integer predictedRating = new Double(model.predict(ur.getData().features())).intValue();
                    ClusteredRating clusteredOriginalRating = new ClusteredRating(ratingClusters.clusterCenters(),
                            ratingClusters.predict(Vectors.dense(ur.getData().label())));
                    return new UserRating(ur.getOriginalRating(), ur.getData(), clusteredOriginalRating, predictedRating);
                })
                .map(ur -> {
                    Integer prediction = ratingClusters.predict(Vectors.dense(ur.getPredictedRating()));
                    ClusteredRating predictedRating = new ClusteredRating(ratingClusters.clusterCenters(), prediction);
                    return Math.pow(predictedRating.getClusterCenter() - ur.getClusteredRating().getClusterCenter(), 2);
                })
                .reduce((a, b) -> a + b) / trainingData.count();
    }

    @Override
    public String getName()
    {
        return "Naive Bayes";
    }
}

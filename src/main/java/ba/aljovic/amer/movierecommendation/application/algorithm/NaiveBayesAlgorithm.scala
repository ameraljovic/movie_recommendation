package ba.aljovic.amer.movierecommendation.application.algorithm

import ba.aljovic.amer.movierecommendation.application.model.{ClusteredRating, UserRating}
import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD

class NaiveBayesAlgorithm(val smoothingParameter: Int) extends RecommendationAlgorithm
{
  override def evaluate(trainingData: RDD[UserRating], testData: RDD[UserRating], ratingClusters: KMeansModel): Double = {
    val model = NaiveBayes.train(trainingData.map(ur => ur.data), smoothingParameter)

    testData.map(ur => {
      val predictedRating = model.predict(ur.data.features)
      val clusteredOriginalRating = new ClusteredRating(
        ratingClusters.clusterCenters,
        ratingClusters.predict(Vectors.dense(ur.data.label)))
      new UserRating(ur.originalRating, ur.data, clusteredOriginalRating, predictedRating.toInt)
    }).map(ur => {
      val prediction = ratingClusters.predict(Vectors.dense(ur.predictedRating))
      val predictedRating = new ClusteredRating(ratingClusters.clusterCenters, prediction)
      Math.pow(predictedRating.getClusterCenter - ur.clusteredRating.getClusterCenter, 2)
    }).reduce(_ + _) / trainingData.count
  }

  override def name(): String = "Naive Bayes"
}

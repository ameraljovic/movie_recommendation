package ba.aljovic.amer.movierecommendation.application.algorithm

import ba.aljovic.amer.movierecommendation.application.model.UserRating
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.rdd.RDD

trait RecommendationAlgorithm
{
  def evaluate(trainingData: RDD[UserRating], testData: RDD[UserRating], ratingClusters: KMeansModel): Double

  def name(): String
}

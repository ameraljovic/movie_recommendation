package ba.aljovic.amer.movierecommendation.application.algorithm

import ba.aljovic.amer.movierecommendation.application.model.{Movie, UserRating}
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.rdd.RDD

trait RecommendationAlgorithm extends Serializable
{
  def recommendMovies(userRatings: RDD[UserRating], movies: RDD[Movie]): Array[Movie]

  def evaluate(trainingData: RDD[UserRating], testData: RDD[UserRating], ratingClusters: KMeansModel): Double

  def name(): String
}

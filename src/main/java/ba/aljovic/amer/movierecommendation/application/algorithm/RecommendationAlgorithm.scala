package ba.aljovic.amer.movierecommendation.application.algorithm

import ba.aljovic.amer.movierecommendation.application.model.{Movie, UserRating}
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.rdd.RDD

/**
 * For n ratings of one user and m movies should recommend k movies
 */
trait RecommendationAlgorithm extends Serializable
{
  /**
   *
   * @param userRatings Ratings of a User which are used as an input for recommendation engine.
   * @param movies List of movies used as an input for recommendation engine. This parameter should contain as much
   *               possible movies.
   * @return Array of n movies recommended for specified user and list of movies.
   */
  def recommendMovies(userRatings: RDD[UserRating], movies: RDD[Movie]): Array[Movie]

  /**
   *
   * @param trainingData User ratings to train the model.
   * @param testData User ratings for testing the algorithm.
   * @param ratingClusters Because the ratings are in a scale of 1 to 10, and the subjective nature of users process of
   *                       rating movies, the ratings are clusterized to form a subset of 10 ratings. For example, many
   *                       users don't make a difference between ratings of 1 to 4, in they're eyes they are equally
   *                       bad so they are clustered into one rating. Clustering is applied to <code>trainingData<code>.
   * @return Root mean square deviation to evaluate the algorithm.
   */
  def evaluate(trainingData: RDD[UserRating], testData: RDD[UserRating], ratingClusters: KMeansModel): Double

  /**
   *
   * @return Override to define name of the algorithm.
   */
  def name(): String
}

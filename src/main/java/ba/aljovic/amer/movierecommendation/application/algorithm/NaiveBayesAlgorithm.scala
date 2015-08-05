package ba.aljovic.amer.movierecommendation.application.algorithm

import ba.aljovic.amer.movierecommendation.application.model.{Movie, ClusteredRating, UserRating}
import breeze.numerics.pow
import org.apache.spark.mllib.classification.NaiveBayes
import org.apache.spark.mllib.clustering.KMeansModel
import org.apache.spark.mllib.linalg.Vectors.dense
import org.apache.spark.rdd.RDD

class NaiveBayesAlgorithm(val numberOfRecommendations: Int, val minRating: Int, val smoothingParameter: Int) extends RecommendationAlgorithm
{
  /**
   *
   * @param userRatings Ratings of a User which are used as an input for recommendation engine.
   * @param movies List of movies used as an input for recommendation engine. This parameter should contain as much
   *               possible movies.
   * @return Array of n movies recommended for specified user and list of movies.
   */
  override def recommendMovies(userRatings: RDD[UserRating], movies: RDD[Movie]): Array[Movie] = {
    val model = NaiveBayes.train(userRatings.map(ur => ur.data), smoothingParameter)
    movies
      .map(movie => {
        val prediction = model.predict(movie.genomes.features).toInt
        new Movie(movie.movieId, movie.name, movie.genomes, -1, prediction)
      })
      .filter(movie => movie.prediction >= minRating)
      .takeOrdered(numberOfRecommendations)(Ordering[Int].on(x => x.prediction))
  }

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
  override def evaluate(trainingData: RDD[UserRating], testData: RDD[UserRating], ratingClusters: KMeansModel): Double = {
    val model = NaiveBayes.train(trainingData.map(ur => ur.data), smoothingParameter)
    testData
      .map(ur => {
        val predictedRating = model.predict(ur.data.features).toInt
        val clusteredOriginalRating = new ClusteredRating(
          ratingClusters.clusterCenters,
          ratingClusters.predict(dense(ur.data.label))
        )
        new UserRating(ur.originalRating, ur.data, clusteredOriginalRating, predictedRating)
    }).map(ur => {
      val prediction = ratingClusters.predict(dense(ur.predictedRating))
      val clusteredPredictedRating = new ClusteredRating(ratingClusters.clusterCenters, prediction)
      pow(clusteredPredictedRating.getClusterCenter - ur.clusteredRating.getClusterCenter, 2)
    }).reduce(_ + _) / trainingData.count
  }

  override def name(): String = "Naive Bayes"
}

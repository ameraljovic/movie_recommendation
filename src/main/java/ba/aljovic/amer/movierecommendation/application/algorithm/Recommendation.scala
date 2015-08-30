package ba.aljovic.amer.movierecommendation.application.algorithm

import java.io.File
import java.lang.String._

import ba.aljovic.amer.movierecommendation.application.model.{Movie, UserRating}
import ba.aljovic.amer.movierecommendation.utils.Utils
import org.apache.commons.io.FileUtils
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

case class Recommendation(algorithm: RecommendationAlgorithm, master: String = "local[1]", numberOfClusters: Integer = 5,
                     numberOfIterations: Integer = 20, testDataRatio: Double = 0.2) extends Serializable
{
  val sparkContext = new SparkContext(new SparkConf().setAppName(algorithm.name()).setMaster(master))

  def evaluateRecommendationAlgorithm(user: String): Double =
  {
    validate(user)
    val splits: Array[RDD[UserRating]] = readUserRatings(user).randomSplit(Array[Double](1 - testDataRatio, testDataRatio))
    val trainingData: RDD[UserRating] = splits(0)
    val testData: RDD[UserRating] = splits(1)
    val ratingClusters: KMeansModel = getKMeansModel(trainingData)
    algorithm.evaluate(trainingData, testData, ratingClusters)
  }

  def recommendMovies(userData: String, moviesData: String): Array[Movie] =
  {
    val userRatings: RDD[UserRating] = readUserRatings(userData)
    val movies: RDD[Movie] = readMovies(moviesData)
    algorithm.recommendMovies(userRatings, movies)
  }

  def evaluateManyUsers(trainingFolderName: String): Double =
  {
    val usersDirectory = new File(Utils.loadFilePath(trainingFolderName))
    val users = FileUtils.listFiles(usersDirectory, null, false).iterator()

    var countUsers = 0
    var result = 0D
    while (users.hasNext)
    {
      val user = users.next()
      val username = Utils.getLastPartFromPath(user)
      try
      {
        result += evaluateRecommendationAlgorithm(trainingFolderName + "/" + username)
      }
      catch
        {
          case e: NumberFormatException => println(e.getMessage)
        }
      countUsers += 1
    }
    result / countUsers
  }



  private def readMovies(fileName: String): RDD[Movie] =
  {
    val moviesFile = new File(valueOf(getClass.getClassLoader.getResource(fileName)))
    val data: RDD[String] = sparkContext.textFile(moviesFile.getPath)

    data.map(line => {
      val attributes = line.split("::")
      val movieId = attributes(0).toInt
      val movieName = attributes(1)
      val genomes = attributes(2).split(":")
      val vector = sparse(
        genomes.length,
        genomes.indices.toArray,
        genomes.toList.map(g => g.toDouble).toArray
      )
      val data = new LabeledPoint(movieId, vector)
      new Movie(movieId, movieName, data)
    })
  }

  private def readUserRatings(fileName: String): RDD[UserRating] =
  {
    val trainingDataFile = new File(Utils.loadFilePath(fileName))
    val data: RDD[String] = sparkContext.textFile(trainingDataFile.getPath)

    data.map(line => {
      try
      {
        val attributes = line.split("::")
        val rating = attributes(2).toInt
        val genomes = attributes(3).split(":")
        val vector = sparse(
          genomes.length,
          genomes.indices.toArray,
          genomes.toList.map(g => g.toDouble).toArray
        )
        val data = new LabeledPoint(rating, vector)
        new UserRating(rating, data)
      }
      catch
        {
          case e: Exception => println(e.getMessage); new UserRating(9, new LabeledPoint(1D, null))
        }
    })
  }

  private def getKMeansModel(trainingData: RDD[UserRating]): KMeansModel =
  {
    val trainingRatings = trainingData.map(ur=>
    {
      dense(ur.data.label)
    })
    KMeans.train(trainingRatings, numberOfClusters, numberOfIterations)
  }

  private def validate(username: String): Unit =
  {
    if (username.contains(":")) throw new NumberFormatException("For input string: ':")
  }
}

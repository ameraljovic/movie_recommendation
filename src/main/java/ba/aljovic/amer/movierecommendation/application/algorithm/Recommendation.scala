package ba.aljovic.amer.movierecommendation.application.algorithm

import java.io.File

import ba.aljovic.amer.movierecommendation.application.model.UserRating
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

case class Recommendation(algorithm: RecommendationAlgorithm, master: String = "local[1]", numberOfClusters: Integer = 5,
                     numberOfIterations: Integer = 20, testDataRatio: Double = 0.2) extends Serializable
{
  val sparkContext = new SparkContext(new SparkConf().setAppName(algorithm.name()).setMaster(master))

  def evaluateRecommendationAlgorithm(data: String): Double =
  {
    val splits: Array[RDD[UserRating]] = readUserRating(data)
      .randomSplit(Array[Double](1 - testDataRatio, testDataRatio))
    val trainingData: RDD[UserRating] = splits(0)
    val testData: RDD[UserRating] = splits(1)
    val ratingClusters: KMeansModel = getKMeansModel(trainingData)
    algorithm.evaluate(trainingData, testData, ratingClusters)
  }

  def evaluateManyUsers(trainingFolderName: String): Double =
  {
    5
  }

  private def readUserRating(fileName: String): RDD[UserRating] =
  {
    val trainingDataFile = new File(String.valueOf(getClass.getClassLoader.getResource(fileName)))
    val data: RDD[String] = sparkContext.textFile(trainingDataFile.getPath)

    data.map(line => {
      val attributes = line.split("::")
      val rating = attributes(2).toInt
      val genomes = attributes(3).split(":")
      val vector = Vectors.sparse(
        genomes.length,
        genomes.indices.toArray,
        genomes.toList.map(g => g.toDouble).toArray
      )
      val data= new LabeledPoint(rating, vector)
      new UserRating(rating, data)
    })
  }

  private def getKMeansModel(trainingData: RDD[UserRating]): KMeansModel =
  {
    val trainingRatings = trainingData.map(ur=> Vectors.dense(ur.data.label))
    KMeans.train(trainingRatings, numberOfClusters, numberOfIterations)
  }
}

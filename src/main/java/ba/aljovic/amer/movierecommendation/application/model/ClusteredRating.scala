package ba.aljovic.amer.movierecommendation.application.model

import org.apache.spark.mllib.linalg.Vector

import scala.collection.immutable.HashMap

class ClusteredRating(vectors: Array[Vector], val ratingInd: Int) extends Serializable
{
  private var clusterCenters = HashMap[Int, Double]()
  private var ratings = HashMap[Int, Int]()
  private val vectorsList = vectors.toList
  private val ratingIndex = ratingInd
  private val sortedVectorsList = vectorsList.sortWith((v1, v2) => v1(0) < v2(0))

  for (i <- sortedVectorsList.indices)
  {
    clusterCenters += i -> sortedVectorsList(i)(0)
    for (j <- sortedVectorsList.indices)
    {
      if (sortedVectorsList(i)(0) == vectorsList(j)(0)) ratings += j -> (i + 1)
    }
  }

  def getRating: Int = ratings.get(ratingIndex).get

  def getClusterCenter: Double = clusterCenters.get(ratingIndex).get
}
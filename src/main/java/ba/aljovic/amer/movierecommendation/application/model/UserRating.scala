package ba.aljovic.amer.movierecommendation.application.model

import org.apache.spark.mllib.regression.LabeledPoint
class UserRating(val originalRating: Int, val data: LabeledPoint, val clusteredRating: ClusteredRating,
                 val predictedRating: Int) extends Serializable
{
  def this(originalRating: Int, data: LabeledPoint)
  {
    this(originalRating, data, null, -1)
  }
}

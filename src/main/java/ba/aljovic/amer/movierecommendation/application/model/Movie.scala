package ba.aljovic.amer.movierecommendation.application.model

import org.apache.spark.mllib.regression.LabeledPoint

class Movie(val movieId: Int, val genomes: LabeledPoint, val userId: Int, val prediction: Int) extends Serializable
{
  def this(movieId: Int, genomes: LabeledPoint)
  {
    this(movieId, genomes, -1, -1)
  }
}

package ba.aljovic.amer.movierecommendation.application.model

import org.apache.spark.mllib.regression.LabeledPoint

class Movie(val movieId: Int, val name: String, val genomes: LabeledPoint, val userId: Int, val prediction: Int) extends Serializable
{
  def this(movieId: Int, name: String, genomes: LabeledPoint)
  {
    this(movieId, name, genomes, -1, -1)
  }
}

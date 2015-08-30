package ba.aljovic.amer.movierecommendation.utils

import java.io.File
import java.lang.String._

object Utils
{
  def loadFilePath(file : String) =
  {
    valueOf(getClass.getClassLoader.getResource(file).getPath)
  }

  def getLastPartFromPath(user: File): String =
  {
    val splitBySlash = user.toPath.toString.split("/")
    val length = splitBySlash.length
    splitBySlash(length - 1)
  }
}

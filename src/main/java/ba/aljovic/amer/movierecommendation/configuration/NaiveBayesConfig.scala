package ba.aljovic.amer.movierecommendation.configuration

import ba.aljovic.amer.movierecommendation.application.algorithm.{NaiveBayesAlgorithm, Recommendation, RecommendationAlgorithm}
import org.springframework.context.annotation.{Bean, Configuration, Profile}

@Configuration
@Profile(Array("algorithm-naiveBayes"))
class NaiveBayesConfig
{
  @Bean
  def naiveBayesRecommendation: Recommendation = {
    Recommendation(naiveBayesAlgorithm)
  }

  @Bean
  def naiveBayesAlgorithm: RecommendationAlgorithm = new NaiveBayesAlgorithm(1, 5, 9)
}

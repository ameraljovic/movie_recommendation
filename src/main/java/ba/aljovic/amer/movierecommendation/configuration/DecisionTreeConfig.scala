package ba.aljovic.amer.movierecommendation.configuration

import ba.aljovic.amer.movierecommendation.application.algorithm.{Recommendation, RecommendationAlgorithm, DecisionTreeAlgorithm}
import org.springframework.context.annotation.{Bean, Configuration, Profile}

@Configuration
@Profile(Array("algorithm-decision-tree"))
class DecisionTreeConfig
{
  @Bean
  def decisionTreeRecommendation: Recommendation = {
    Recommendation(decisionTreeAlgorithm)
  }

  @Bean
  def decisionTreeAlgorithm: RecommendationAlgorithm = new DecisionTreeAlgorithm(5, 9, 10, 3)
}

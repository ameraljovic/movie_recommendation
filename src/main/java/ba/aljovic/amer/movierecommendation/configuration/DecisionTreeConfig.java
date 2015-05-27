package ba.aljovic.amer.movierecommendation.configuration;

import ba.aljovic.amer.movierecommendation.application.algorithm.Recommendation;
import ba.aljovic.amer.movierecommendation.application.algorithm.RecommendationAlgorithm;
import ba.aljovic.amer.movierecommendation.application.algorithm.DecisionTreeAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile ("algorithm-decision-tree")
public class DecisionTreeConfig
{
    @Bean
    public Recommendation decisionTreeRecommendation()
    {
        return new Recommendation.RecommendationBuilder(decisionTreeAlgorithm()).build();
    }

    @Bean
    public RecommendationAlgorithm decisionTreeAlgorithm()
    {
        return new DecisionTreeAlgorithm(100);
    }
}

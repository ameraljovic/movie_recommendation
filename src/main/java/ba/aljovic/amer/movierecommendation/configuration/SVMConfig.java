package ba.aljovic.amer.movierecommendation.configuration;

import ba.aljovic.amer.movierecommendation.application.algorithm.Recommendation;
import ba.aljovic.amer.movierecommendation.application.algorithm.RecommendationAlgorithm;
import ba.aljovic.amer.movierecommendation.application.algorithm.SVMAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile ("algorithm-SVM")
public class SVMConfig
{
    @Bean
    public Recommendation svmRecommendation()
    {
        return new Recommendation.RecommendationBuilder(svmAlgorithm()).build();
    }

    @Bean
    public RecommendationAlgorithm svmAlgorithm()
    {
        return new SVMAlgorithm(100);
    }
}

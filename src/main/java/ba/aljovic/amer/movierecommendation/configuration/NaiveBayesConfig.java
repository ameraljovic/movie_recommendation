package ba.aljovic.amer.movierecommendation.configuration;

import ba.aljovic.amer.movierecommendation.application.algorithm.NaiveBayesAlgorithm;
import ba.aljovic.amer.movierecommendation.application.algorithm.Recommendation;
import ba.aljovic.amer.movierecommendation.application.algorithm.RecommendationAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("algorithm-naiveBayes")
public class NaiveBayesConfig
{
    // Naive Bayes
    @Bean
    public Recommendation naiveBayesRecommendation()
    {
        return new Recommendation.RecommendationBuilder(naiveBayesAlgorithm()).build();
    }

    @Bean
    public RecommendationAlgorithm naiveBayesAlgorithm()
    {
        return new NaiveBayesAlgorithm(1);
    }
}
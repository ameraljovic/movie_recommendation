package ba.aljovic.amer.movierecommendation.configuration;

import ba.aljovic.amer.movierecommendation.application.algorithm.NaiveBayesAlgorithm;
import ba.aljovic.amer.movierecommendation.application.algorithm.Recommendation;
import ba.aljovic.amer.movierecommendation.application.algorithm.RecommendationAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config
{
    @Bean
    public Recommendation recommendation()
    {
        return new Recommendation("local[1]", naiveBayesAlgorithm());
    }

    @Bean
    public RecommendationAlgorithm naiveBayesAlgorithm()
    {
        return new NaiveBayesAlgorithm(1);
    }
}

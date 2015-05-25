package ba.aljovic.amer.movierecommendation.configuration;

import ba.aljovic.amer.movierecommendation.application.NaiveBayesApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config
{
    @Bean
    public NaiveBayesApp naiveBayesApp()
    {
        NaiveBayesApp naiveBayesApp = new NaiveBayesApp("local[1]");
        return naiveBayesApp;
    }
}

package ba.aljovic.amer.movierecommendation.application;

import ba.aljovic.amer.movierecommendation.Application;
import ba.aljovic.amer.movierecommendation.application.algorithm.Recommendation;
import ba.aljovic.amer.movierecommendation.application.model.Movie;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith (SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
@ActiveProfiles("algorithm-decision-tree")
public class DecisionTreeTest
{
    @Autowired
    Recommendation recommendation;

    @Test
    public void testOneUser() throws IOException
    {
        Double result = recommendation.evaluateRecommendationAlgorithm("movie-data/addy_4.txt");
        System.out.println("Result: " + result);
    }

    @Test
    public void testManyUsers() throws IOException
    {
        Double result = recommendation.evaluateManyUsers("movie-data");
        System.out.println("Result: " + result);
    }

    @Test
    public void testMovieRecommendation() throws IOException
    {
        Movie[] movies = recommendation.recommendMovies("movie-data/addy_4.txt", "movies/movies.txt");
    }
}

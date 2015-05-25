package ba.aljovic.amer.movierecommendation.application;

import org.junit.Test;

import java.io.FileNotFoundException;

public class NaiveBayesTest
{
    @Test
    public void test() throws FileNotFoundException
    {
        NaiveBayesApp naiveBayesApp = new NaiveBayesApp("local[1]");
        Double result = naiveBayesApp.calculate(
                "movie-ratings-training/Tejas-Nair.txt", "movie-ratings-test/Tejas-Nair.txt", 0.1);
        System.out.println("Result: " + result);
    }
}

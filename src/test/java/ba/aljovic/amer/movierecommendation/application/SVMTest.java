package ba.aljovic.amer.movierecommendation.application;

import ba.aljovic.amer.movierecommendation.Application;
import ba.aljovic.amer.movierecommendation.application.algorithm.Recommendation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith (SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration (classes = Application.class)
@ActiveProfiles("algorithm-SVM")
public class SVMTest
{
    @Autowired
    Recommendation svmRecommendation;

    @Test
    public void testOneUser() throws IOException
    {
        Double result = svmRecommendation.evaluateRecommendationAlgorithm(
                "user-ratings-training/addy_4.txt", "user-ratings-test/addy_4.txt");
        System.out.println("Result: " + result);
    }

    @Test
    public void testManyUsers() throws IOException
    {
        Double result = svmRecommendation.evaluateManyUsers("user-ratings-training", "user-ratings-test");
        System.out.println("Result: " + result);
    }
}

package ba.aljovic.amer.movierecommendation.application.utils;

import java.util.Arrays;

public class Utils
{
    public static double[] toDouble(String[] string)
    {
        return Arrays.asList(string).stream().mapToDouble(Double::parseDouble).toArray();
    }
}

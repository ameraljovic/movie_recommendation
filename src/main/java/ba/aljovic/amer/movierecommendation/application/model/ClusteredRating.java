package ba.aljovic.amer.movierecommendation.application.model;

import org.apache.spark.mllib.linalg.Vector;

import java.io.Serializable;
import java.util.*;

public class ClusteredRating implements Serializable
{
    private Map<Integer, Double> clusterCenters;
    private Map<Integer, Integer> ratings;
    private Integer ratingIndex;

    public ClusteredRating(Vector[] vectors, Integer ratingIndex)
    {
        List<Vector> vectorsList = Arrays.asList(vectors);
        List<Vector> sortedVectorList = new ArrayList<>(vectorsList);

        Collections.sort(sortedVectorList, (v1, v2) -> {
            if (v1.toArray()[0] < v2.toArray()[0]) return -1;
            else if (v1.toArray()[0] == v2.toArray()[0]) return 0;
            else return 1;
        });

        ratings = new HashMap<>(vectors.length);
        clusterCenters = new HashMap<>(vectors.length);
        for (int i = 0; i < sortedVectorList.size(); i++)
        {
            clusterCenters.put(i, sortedVectorList.get(i).toArray()[0]);
            for (int j = 0; j < sortedVectorList.size(); j++)
            {
                if (sortedVectorList.get(i).toArray()[0] == vectorsList.get(j).toArray()[0])
                {
                    ratings.put(j, i + 1);
                    break;
                }
            }

        }

        this.ratingIndex = ratingIndex;
    }


    public Integer getRating()
    {
        return ratings.get(ratingIndex);
    }

    public Double getClusterCenter()
    {
        return clusterCenters.get(ratingIndex);
    }
}

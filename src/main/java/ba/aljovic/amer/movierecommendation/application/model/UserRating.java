package ba.aljovic.amer.movierecommendation.application.model;

import org.apache.spark.mllib.regression.LabeledPoint;

import java.io.Serializable;

public class UserRating implements Serializable
{
    private Integer originalRating;
    private LabeledPoint data;
    private ClusteredRating clusteredRating;
    private Integer predictedRating;

    public UserRating(Integer originalRating, LabeledPoint data)
    {
        this.originalRating = originalRating;
        this.data = data;
    }

    public UserRating(Integer originalRating, LabeledPoint data,
                      ClusteredRating clusteredRating)
    {
        this.originalRating = originalRating;
        this.data = data;
        this.clusteredRating = clusteredRating;
    }

    public UserRating(Integer originalRating, LabeledPoint data, ClusteredRating clusteredRating, Integer predictedRating)
    {
        this.originalRating = originalRating;
        this.data = data;
        this.clusteredRating = clusteredRating;
        this.predictedRating = predictedRating;
    }

    public Integer getOriginalRating()
    {
        return originalRating;
    }

    public void setOriginalRating(Integer originalRating)
    {
        this.originalRating = originalRating;
    }

    public LabeledPoint getData()
    {
        return data;
    }

    public void setData(LabeledPoint data)
    {
        this.data = data;
    }

    public ClusteredRating getClusteredRating()
    {
        return clusteredRating;
    }

    public void setClusteredRating(ClusteredRating clusteredRating)
    {
        this.clusteredRating = clusteredRating;
    }

    public Integer getPredictedRating()
    {
        return predictedRating;
    }

    public void setPredictedRating(Integer predictedRating)
    {
        this.predictedRating = predictedRating;
    }
}

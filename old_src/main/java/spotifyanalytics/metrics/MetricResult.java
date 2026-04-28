package spotifyanalytics.metrics;

//<T> is a placeholder for any datatype
public class MetricResult<ResultType> {
    private final String metricName;
    private final ResultType value;

    public MetricResult(String metricName, ResultType value) {
        this.metricName = metricName;
        this.value = value;
    }

    public String getMetricName() {return metricName;}
    public ResultType getValue() {return value;}

    @Override
    public String toString(){
        return metricName + ": " + value;
    }
}

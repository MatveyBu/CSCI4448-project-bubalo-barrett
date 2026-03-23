package service;

import domain.PlayedTrack;
import metrics.ListeningMetric;
import metrics.MetricResult;

import java.util.List;
import java.util.*;

public class DashboardService {
    private final List<ListeningMetric<?>> metrics;

    public DashboardService(List<ListeningMetric<?>> metrics) {
        this.metrics = metrics;
    }

    public Map<String, MetricResult<?>> compute(List<PlayedTrack> history) {
        Map<String, MetricResult<?>> results = new LinkedHashMap<>();
        for (ListeningMetric<?> metric: metrics){
            results.put(metric.getName(), metric.compute(history));
        }
        return results;
    }
}

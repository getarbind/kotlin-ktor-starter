package io.collective.articles;

import com.codahale.metrics.CachedGauge;
import com.codahale.metrics.MetricRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AirQualityDataGateway {
    public static List<AirQualityInfo> airQualityInfoList = new ArrayList<>();

    private Random sequence = new Random();

    public AirQualityDataGateway(MetricRegistry registry) {
        registry.register("airquality",
                new CachedGauge<Integer>(10, TimeUnit.MINUTES) {
                    @Override
                    protected Integer loadValue() {
                        return airQualityInfoList.size();
                    }
                });
    }

    public List<AirQualityInfo> findAll() {
        return airQualityInfoList;
    }

    public List<AirQualityInfo> findAvailable() {
        return airQualityInfoList.stream().filter(AirQualityInfo::isAvailable).collect(Collectors.toList());
    }

    public void save(AirQualityInfo info) {
        airQualityInfoList.add(new AirQualityInfo(sequence.nextInt(), info.getTitle(), true));
    }
}
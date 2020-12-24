package finley.gmair.service.impl;

import finley.gmair.dao.ProvinceAirqualityDao;
import finley.gmair.model.air.CityAirQuality;
import finley.gmair.model.air.ProvinceAirQuality;
import finley.gmair.service.ProvinceAirQualityService;
import finley.gmair.service.ProvinceCityCacheService;
import finley.gmair.util.ResultData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class ProvinceAirQualityServiceImpl implements ProvinceAirQualityService {

    @Autowired
    private ProvinceAirqualityDao provinceAirqualityDao;

    @Autowired
    private ProvinceCityCacheService provinceCityCacheService;

    @Override
    public ResultData generate(List<CityAirQuality> list) {
        ResultData result = new ResultData();
        if (list.isEmpty()) {
            return result;
        }

        //得到provinceId与pm25的对应关系Map,pm25的值是由该省份的城市的pm25取平均值得到
        Map<String, Double> pm25Map = list.stream().filter(e -> e != null)
                .map(e -> new ProvinceAirQuality(provinceCityCacheService.fetchProvince(e.getCityId()), e.getAqi(), e.getPm2_5()))
                .collect(Collectors.groupingBy(ProvinceAirQuality::getProvinceId, Collectors.averagingDouble(ProvinceAirQuality::getPm2_5)));

        //得到provinceId与aqi的对应关系Map,pm25的值是由该省份的城市的pm25取平均值得到
        Map<String, Double> aqiMap = list.stream().map(e -> new ProvinceAirQuality(provinceCityCacheService.
                fetchProvince(e.getCityId()), e.getAqi(), e.getPm2_5()))
                .collect(Collectors.groupingBy(ProvinceAirQuality::getProvinceId,
                        Collectors.averagingDouble(ProvinceAirQuality::getAqi)));

        List<ProvinceAirQuality> provinceAirQualityList = pm25Map.entrySet().stream()
                .map(e -> new ProvinceAirQuality(e.getKey(), provinceCityCacheService.fetchProvinceName(e.getKey()),
                        aqiMap.get(e.getKey()), e.getValue()))
                .collect(Collectors.toList());
        return provinceAirqualityDao.insertBatch(provinceAirQualityList);
    }

    @Override
    public ResultData fetch(Map<String, Object> condition) {
        return provinceAirqualityDao.select(condition);
    }
}

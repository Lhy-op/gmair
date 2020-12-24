package finley.gmair.service;


import finley.gmair.dao.CityUrlDao;
import finley.gmair.dao.MonitorStationAirQualityDao;
import finley.gmair.dao.MonitorStationDao;
import finley.gmair.model.air.MonitorStation;
import finley.gmair.model.air.MonitorStationAirQuality;
import finley.gmair.util.ResponseCode;
import finley.gmair.util.ResultData;
import finley.gmair.vo.air.CityUrlVo;
import finley.gmair.vo.air.MonitorStationVo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MonitorStationCrawler {

    @Autowired
    private CityUrlDao cityUrlDao;

    @Autowired
    private MonitorStationDao monitorStationDao;

    @Autowired
    private MonitorStationAirQualityDao monitorStationAirQualityDao;

    public void craw() {
        Map<String, Object> condition = new HashMap<>();
        condition.put("blockFlag", false);
        ResultData cityUrlResponse = cityUrlDao.select(condition);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis() / (3600000) * 3600000);
        Map<String, String> stationName2IdMap = fetchStationNameMap();
        List<MonitorStationAirQuality> list = new LinkedList<>();
        if (cityUrlResponse.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityUrlVo> cityUrls = (List<CityUrlVo>) cityUrlResponse.getData();
            try {
                for (CityUrlVo cityUrlVo : cityUrls) {
                    Document page = Jsoup.connect(cityUrlVo.getCityUrl()).get();
                    Element rankTable = page.select("div.table").first();
                    Element tableBody = rankTable.getElementsByTag("tbody").first();
                    Elements trs = tableBody.getElementsByTag("tr");
                    for (Element tr : trs) {
                        try {
                            Elements tds = tr.getElementsByTag("td");
                            MonitorStationAirQuality airQuality = new MonitorStationAirQuality();
                            airQuality.setStationId(stationName2IdMap.get(cityUrlVo.getCityId() + tds.get(0).text()));
                            airQuality.setAqi(Double.parseDouble(tds.get(1).text()));
                            airQuality.setAqiLevel(tds.get(2).text());
                            airQuality.setPrimePollution(tds.get(3).text());
                            airQuality.setPm2_5(Double.parseDouble(tds.get(4).text()));
                            airQuality.setPm10(Double.parseDouble(tds.get(5).text()));
                            airQuality.setCo(Double.parseDouble(tds.get(6).text()));
                            airQuality.setNo2(Double.parseDouble(tds.get(7).text()));
                            airQuality.setO3(Double.parseDouble(tds.get(8).text()));
                            airQuality.setSo2(Double.parseDouble(tds.get(10).text()));
                            airQuality.setRecordTime(timestamp);
                            list.add(airQuality);
                        } catch (Exception e) {

                        }
                    }
                    if (!list.isEmpty()) {
                        monitorStationAirQualityDao.insertBatch(list);
                        monitorStationAirQualityDao.insertLatestBatch(list);
                    }
                    list.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> fetchStationNameMap() {
        Map<String, Object> condition = new HashMap<>();
        ResultData response = monitorStationDao.fetch(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<MonitorStationVo> list = (List<MonitorStationVo>) response.getData();
            return list.stream().collect(
                    Collectors.toMap(e -> (e.getCityId() + e.getStationName()), e -> e.getStationId()));
        } else {
            return new HashMap<>();
        }
    }

    public ResultData fetch(Map<String, Object> condition) {
        return monitorStationAirQualityDao.selectLatest(condition);
    }

    public void updateCityStation() {
        Map<String, Object> condition = new HashMap<>();
        ResultData response = cityUrlDao.select(condition);
        if (response.getResponseCode() == ResponseCode.RESPONSE_OK) {
            List<CityUrlVo> cityUrlVoList = (List<CityUrlVo>) response.getData();
            monitorStationDao.empty();
            for (CityUrlVo cityUrlVo : cityUrlVoList) {
                List<MonitorStation> monitorStations = fetchCityStation(cityUrlVo.getCityId(), cityUrlVo.getCityUrl());
                if (!monitorStations.isEmpty()) {
                    monitorStationDao.insertBatch(monitorStations);
                }
            }
        }
    }


    public List<MonitorStation> fetchCityStation(String cityId, String url) {
        List<MonitorStation> stations = new ArrayList<>();
        try {
            Document page = Jsoup.connect(url).get();
            Element table = page.getElementById("detail-data");
            Element tableBody = table.getElementsByTag("tbody").first();
            Elements trs = tableBody.getElementsByTag("tr");
            for (Element tr: trs) {
                MonitorStation station = new MonitorStation();
                Elements tds = tr.getElementsByTag("td");
                station.setBelongCityId(cityId);
                station.setStationName(tds.get(0).text());
                stations.add(station);
            }
        } catch (IOException e) {
            try {
                Thread.sleep(1000);
            } catch (Exception te) {

            }
        }

        return stations;
    }
}

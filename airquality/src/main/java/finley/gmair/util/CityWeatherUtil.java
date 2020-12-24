package finley.gmair.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import finley.gmair.model.air.MojiRecord;
import finley.gmair.model.air.MojiToken;
import finley.gmair.model.district.City;
import finley.gmair.model.district.District;
import finley.gmair.model.district.Province;
import finley.gmair.service.MojiTokenService;
import finley.gmair.service.feign.LocationFeign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@PropertySource("classpath:moji.properties")
public class CityWeatherUtil {
    private Logger logger = LoggerFactory.getLogger(CityWeatherUtil.class);

    @Value("${base}")
    public String base;

    @Autowired
    public LocationFeign locationFeign;

    public List<MojiRecord> list;

    public static Map<String, Province> provinces;

    public static Map<String, City> cities;

    public static Map<String, District> districts;

    public static MojiToken mojiToken;

    public static MojiTokenService mojiTokenService;

    @PostConstruct
    public void init() {
        try {
            File file = new File(base);
            list = MojiRecordUtil.list(file);
            logger.info("moji record: " + list.size());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        ResultData response = locationFeign.province();
        if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
            logger.error("获取省份信息失败");
            return;
        }
        provinces = new HashMap<>();
        cities = new HashMap<>();
        districts = new HashMap<>();
        JSONArray provinceArr = JSON.parseArray(JSON.toJSONString(response.getData()));
        for (int i = 0; i < provinceArr.size(); i++) {
            JSONObject item = provinceArr.getJSONObject(i);
            String provinceId = item.getString("provinceId");
            String provinceName = item.getString("provinceName");
            String provincePinyin = item.getString("provincePinyin");
            double longitude = item.getDouble("longitude");
            double latitude = item.getDouble("latitude");
            Province province = new Province(provinceId, provinceName, provincePinyin, longitude, latitude);
            provinces.put(provinceId, province);
            response = locationFeign.city(provinceId);
            if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                logger.error("Fail to obtain city list under: " + provinceId);
                continue;
            }
            JSONArray cityArr = JSON.parseArray(JSON.toJSONString(response.getData()));
            for (int j = 0; j < cityArr.size(); j++) {
                item = cityArr.getJSONObject(j);
                String cityId = item.getString("cityId");
                String cityName = item.getString("cityName");
                String cityPinyin = item.getString("cityPinyin");
                longitude = item.getDouble("longitude");
                latitude = item.getDouble("latitude");
                City city = new City(cityId, cityName, cityPinyin, longitude, latitude);
                cities.put(cityId, city);
                response = locationFeign.district(cityId);
                if (response.getResponseCode() != ResponseCode.RESPONSE_OK) {
                    logger.error("Fail to obtain district list under:  " + cityId);
                    continue;
                }
                JSONArray districtArr = JSON.parseArray(JSON.toJSONString(response.getData()));
                for (int k = 0; k < districtArr.size(); k++) {
                    item = districtArr.getJSONObject(k);
                    String districtId = item.getString("districtId");
                    String districtName = item.getString("districtName");
                    District district = new District(districtId, districtName);
                    districts.put(districtId, district);
                }
            }
        }
        logger.info("province: " + provinces.size() + ", city: " + cities.size() + ", district: " + districts.size());
    }

    public String fetch(int cityId){
        StringBuffer stringBuffer = new StringBuffer(mojiToken.getUrl()).append("?cityId=").append(cityId);
        String url = stringBuffer.toString();
        String subUrl = url.substring("https://api.mojicb.com".length(), url.indexOf("?"));
        logger.info(url);
        Long timeValue = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String timeStamp = timeValue.toString();
        String nonce = UUID.randomUUID().toString();
        String signature = Encryption.md5(mojiToken.getPassword() + "\n" + timeStamp + "\n" + nonce + "\n" + subUrl);
        Map<String, String> map = new HashMap<>();
        map.put("X-AC-Token", mojiToken.getToken());
        map.put("X-Date", timeStamp);
        map.put("X-AC-Nonce", nonce);
        map.put("X-AC-Signature", signature);
        logger.info("Request params: " + JSON.toJSONString(map));
        String result = HttpDeal.getResponse(url, map);
        return result;
    }

    public String fetch(double longitude, double latitude){
        StringBuffer stringBuffer = new StringBuffer(mojiToken.getUrl()).append("?lat=").append(latitude).append("&lon=").append(longitude);
        String url = stringBuffer.toString();
        String subUrl = url.substring("https://api.mojicb.com".length(), url.indexOf("?"));
        logger.info(url);
        Long timeValue = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String timeStamp = timeValue.toString();
        String nonce = UUID.randomUUID().toString();
        String signature = Encryption.md5(mojiToken.getPassword() + "\n" + timeStamp + "\n" + nonce + "\n" + subUrl);
        Map<String, String> map = new HashMap<>();
        map.put("X-AC-Token", mojiToken.getToken());
        map.put("X-Date", timeStamp);
        map.put("X-AC-Nonce", nonce);
        map.put("X-AC-Signature", signature);
        String result = HttpDeal.getResponse(url, map);
        logger.info("Request params: " + JSON.toJSONString(map));
        return result;

    }

    public MojiRecord locate(String district) {
        logger.info("district: " + district + ", size: " + list.size());
        for (int i = 0; i < list.size(); i++) {
            MojiRecord record = list.get(i);
            if (record.getName().contains(district)) {
                return record;
            }
        }
        return null;
    }

}

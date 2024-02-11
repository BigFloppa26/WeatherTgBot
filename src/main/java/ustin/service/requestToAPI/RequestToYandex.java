package ustin.service.requestToAPI;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.modelmapper.ModelMapper;
import ustin.models.Fact;

import java.util.*;

@Component
public class RequestToYandex {
    private final Logger logger = LoggerFactory.getLogger(RequestToYandex.class);
    private Fact fact;

    private final ModelMapper modelMapper;
    private final ObjectMapper mapper;

    public RequestToYandex(ModelMapper modelMapper, ObjectMapper mapper) {
        this.modelMapper = modelMapper;
        this.mapper = mapper;
    }

    public String getWeather() throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weather.yandex.ru/v2/forecast?lon=50.182983969926134&lat=53.23376423699999&lang=ru";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Yandex-API-Key", "895d82d5-0627-4eb1-aa5b-6f94beef347e");
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);


        LinkedHashMap<?, ?> map;

        try {
            map = mapper.readValue(responseEntity.getBody(), LinkedHashMap.class);
            this.fact = modelMapper.map(map.get("fact"), Fact.class);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        return String.valueOf(this.fact.getTemp());
    }

    public List<String> getKeyFromJson(ResponseEntity<String> responseEntity) throws JsonProcessingException {

        JsonNode jsonNode = mapper.readTree(responseEntity.getBody());
        Iterator<String> iterator = jsonNode.fieldNames();
        List<String> keys = new ArrayList<>();
        iterator.forEachRemaining(keys::add);

        return keys;
    }
}

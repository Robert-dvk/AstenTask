package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.model.BrazilApi.BrazilApi;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class BrazilApiCacheService {

    private final RestTemplate restTemplate;

    public BrazilApiCacheService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("holidays")
    public List<BrazilApi> getHolidays(int year) {
        String baseUrl = "https://brasilapi.com.br/api/feriados/v1";
        String url = baseUrl + "/" + year;
        BrazilApi[] holidays = restTemplate.getForObject(url, BrazilApi[].class);
        return holidays != null ? Arrays.asList(holidays) : Collections.emptyList();
    }
}

package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.model.BrazilApi.BrazilApi;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BrazilApiService {

    private final BrazilApiCacheService cacheService;

    public BrazilApiService(BrazilApiCacheService cacheService) {
        this.cacheService = cacheService;
    }

    public boolean isHoliday(LocalDate date) {
        List<BrazilApi> holidays = cacheService.getHolidays(date.getYear());
        return holidays.stream()
                .filter(h -> "national".equalsIgnoreCase(h.getType()))
                .anyMatch(h -> LocalDate.parse(h.getDate()).equals(date));
    }
}
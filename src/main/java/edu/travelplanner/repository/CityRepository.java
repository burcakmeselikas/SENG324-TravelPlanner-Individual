package edu.travelplanner.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.travelplanner.model.City;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class CityRepository {
    private static CityRepository instance;

    private final List<City> cities;

    private CityRepository() {
        this.cities = loadCitiesFromJson();
    }

    public static synchronized CityRepository getInstance() {
        if (instance == null) {
            instance = new CityRepository();
        }
        return instance;
    }

    private List<City> loadCitiesFromJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("cities.json");

            if (inputStream == null) {
                throw new IllegalStateException("cities.json file could not be found in resources folder.");
            }

            return objectMapper.readValue(inputStream, new TypeReference<List<City>>() {});
        } catch (Exception e) {
            throw new RuntimeException("City data could not be loaded from JSON file.", e);
        }
    }

    public List<City> getCities() {
        return Collections.unmodifiableList(cities);
    }

    public int getCityCount() {
        return cities.size();
    }
}
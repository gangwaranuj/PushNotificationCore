package com.saphire.iopush.dao;

import com.saphire.iopush.utils.Response;

public interface ICityCountryDao {
	Response listCountry(int start,int limit);
	Response findCountryById(int geo_id);
	Response findCityById(int city_id);
	Response listCities(int start,int limit) ;
}

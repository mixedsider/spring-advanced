package org.example.expert.client.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class WeatherDto {

    private final String date;
    private final String weather;
}

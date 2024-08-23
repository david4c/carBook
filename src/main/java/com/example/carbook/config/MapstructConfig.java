package com.example.carbook.config;

import org.mapstruct.MapperConfig;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@MapperConfig(componentModel = SPRING,
              injectionStrategy = CONSTRUCTOR,
              implementationName = "Mapstruct<CLASS_NAME>")
public interface MapstructConfig {

}
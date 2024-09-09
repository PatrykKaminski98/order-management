package pl.ordermanagement.configuration;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;

@MapperConfig(
        componentModel = SPRING,
        implementationPackage = "<PACKAGE_NAME>.generated",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MapperConfiguration {
}

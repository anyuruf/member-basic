package net.anyuruf.memberbasic.config;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;



@Configuration
@ComponentScan(
    basePackages =  "net.anyuruf.memberbasic.domain.member",
    includeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ANNOTATION,
            classes = {DomainService.class, Stub.class}
        )
    }
)
public class DomainConfiguration {
}
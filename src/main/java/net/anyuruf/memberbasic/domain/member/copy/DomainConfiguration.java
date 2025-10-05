package net.anyuruf.memberbasic.domain.member.copy;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(
        basePackageClasses = {member.class},
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {DomainService.class, Stub.class})
public class DomainConfiguration {
}

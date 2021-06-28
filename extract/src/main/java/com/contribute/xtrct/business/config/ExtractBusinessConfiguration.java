package com.contribute.xtrct.business.config;

import com.contribute.xtrct.business.model.XMLIncentive;
import com.contribute.xtrct.business.model.XMLIncentiveFactory;
import com.contribute.xtrct.business.model.XMLVariationList;
import com.contribute.xtrct.business.util.ApplicationVersion;
import com.contribute.xtrct.business.util.IncentiveLoanCalculator;
import com.contribute.xtrct.dao.FilterValuesDao;
import com.contribute.xtrct.dao.model.ExtractWarning;
import com.contribute.xtrct.dao.model.WarnableResult;
import com.contribute.xtrct.dao.mybatis.spring.config.ExtractDatabaseConnectionProperties;
import com.contribute.xtrct.dao.mybatis.spring.config.ExtractMyBatisSpringConfig;
import com.contribute.xtrct.presentation.PresentationMarshaller;
import com.contribute.xtrct.presentation.PresentationMarshallerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;

import javax.naming.NamingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * Spring configuration file for the extract business layer
 */
@Configuration
@PropertySource("file:${properties.path}")
@PropertySource("classpath:application.properties")
@Import(ExtractMyBatisSpringConfig.class)
@ComponentScan(basePackages = {"com.chromedata.incentives.extract.business",
        "com.chromedata.incentives.extract.presentation"})
public class ExtractBusinessConfiguration {

    ExtractBusinessConfiguration() {
    }

    /**
     * Overriding bean definition in DAL configuration
     */
    @Bean
    public ExtractDatabaseConnectionProperties extractDatabaseConnectionProperties(Environment environment) {
        return new ExtractDatabaseConnectionProperties(environment.getProperty("incentiveBatch.jdbc.driverClassName"),
                                                       environment.getProperty("incentiveBatch.jdbc.url"),
                                                       environment.getProperty("incentiveBatch.jdbc.username"),
                                                       environment.getProperty("incentiveBatch.jdbc.password"),
                                                       Integer.parseInt(Optional.ofNullable(environment.getProperty("incentiveBatch.max.pool.size")).orElse("100")),
                                                       Long.parseLong(Optional.ofNullable(environment.getProperty("incentiveBatch.connection.timeout")).orElse("30000")),
                                                       Long.parseLong(Optional.ofNullable(environment.getProperty("incentiveBatch.idle.timeout")).orElse("600000")),
                                                       Long.parseLong(Optional.ofNullable(environment.getProperty("incentiveBatch.max.life.time")).orElse("1800000")));
    }

    /**
     * Overriding bean definition in parent configuration
     */
    @Bean
    public URI filterServiceBaseContextPath(Environment environment) throws NamingException {
        return URI.create(environment.getProperty("filter.service.path"));
    }

    @Bean
    public URI lookupServiceBaseContextPath(Environment environment) {
        return URI.create(environment.getProperty("lookup.service.path"));
    }


    @Bean
    public PresentationMarshaller presentationMarshallerUSEn(PresentationMarshallerFactory extractMarshallerFactory) {
        return extractMarshallerFactory.create(new Locale("en", "US"));
    }

    @Bean
    public PresentationMarshaller presentationMarshallerCAEn(PresentationMarshallerFactory extractMarshallerFactory) {
        return extractMarshallerFactory.create(new Locale("en", "CA"));
    }

    @Bean
    public PresentationMarshaller presentationMarshallerCAFr(PresentationMarshallerFactory extractMarshallerFactory) {
        return extractMarshallerFactory.create(new Locale("fr", "CA"));
    }

    @Bean
    public ApplicationVersion applicationVersion(Environment environment) {
        return ApplicationVersion.parse(environment.getProperty("application.version"));
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public PresentationMarshallerFactory extractMarshallerFactory(@Qualifier("extractMessageSource") MessageSource extractMessages, FilterValuesDao filterValuesDao) {

        return locale -> new PresentationMarshaller(locale, extractMessages, filterValuesDao);
    }

    @Bean @Scope(SCOPE_PROTOTYPE)
    public XMLIncentiveFactory extractXmlIncentiveFactory(IncentiveLoanCalculator loanCalculator, @Value("${ltv.leaseRate.moneyFactor.divider:2400}") Integer ltvLeaseRateMoneyFactorDivider) {

        return (incentiveXML, augmentedIncentiveData) -> {
            XMLIncentive         xmlIncentive = null;
            List<ExtractWarning> warnings     = new ArrayList<>();
            try {
                XMLVariationList xmlVariationList = new XMLVariationList(incentiveXML, loanCalculator, ltvLeaseRateMoneyFactorDivider);
                xmlIncentive = new XMLIncentive(incentiveXML, xmlVariationList, augmentedIncentiveData);
            } catch (RuntimeException e) {
                warnings.add(new ExtractWarning("Failed to unmarshal incentive with id " + incentiveXML.getID(), e));
            }
            return new WarnableResult<>(xmlIncentive, warnings);
        };
    }

    @Bean
    public MessageSource extractMessageSource() {
        ReloadableResourceBundleMessageSource extractMessageSource = new ReloadableResourceBundleMessageSource();
        extractMessageSource.setBasename("classpath:i18n/MessageBundle");
        extractMessageSource.setDefaultEncoding("UTF-8");
        return extractMessageSource;
    }
}

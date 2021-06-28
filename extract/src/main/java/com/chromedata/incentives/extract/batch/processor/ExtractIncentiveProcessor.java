package com.chromedata.incentives.extract.batch.processor;

import com.chromedata.eid.aos.incentive.util.XMLConvertUtil;
import com.chromedata.incentives.extract.batch.component.CommonComponent;
import com.chromedata.incentives.extract.business.model.CompleteIncentive;
import com.chromedata.incentives.extract.business.model.Incentive;
import com.chromedata.incentives.extract.business.model.mappers.ExtractDomainMapper;
import com.chromedata.incentives.extract.dao.model.AugmentedIncentiveData;
import com.chromedata.incentives.extract.dao.model.ExtractWarning;
import com.chromedata.incentives.extract.dao.model.WarnableResult;
import com.chromedata.incentives.filter.EidAosIncentiveInMemoryTester;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.chromedata.incentives.extract.batch.component.ExtractConstants.EXIT_STATUS_FAIL;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.REQUESTED_LOCALE;
import static com.chromedata.incentives.extract.batch.component.ExtractConstants.WARNINGS;

/**
 * Processor to process particular Incentive read by reader, filter if not applicable & convert to an instance of {@link CompleteIncentive} so it can be write in CSV files.
 */
@Component
public class ExtractIncentiveProcessor implements ItemProcessor<AugmentedIncentiveData, CompleteIncentive> {

    private static final Logger LOG = LogManager.getLogger(ExtractIncentiveProcessor.class);

    @Autowired
    private EidAosIncentiveInMemoryTester inMemoryIncentivePredicate;

    @Autowired
    private ExtractDomainMapper<com.chromedata.eid.aos.incentive.model.Incentive, Incentive> incentiveMapper;

    private JobExecution jobExecution;

    @Override
    @SuppressWarnings("unchecked")
    public CompleteIncentive process(final AugmentedIncentiveData incentiveData) {
        CompleteIncentive    completeIncentive       = null;
        ExecutionContext executionContext = jobExecution.getExecutionContext();
        List<ExtractWarning> warnings        = (List<ExtractWarning>) executionContext.get(WARNINGS);
        final Locale         requestedLocale = (Locale) executionContext.get(REQUESTED_LOCALE);

        if (Objects.isNull(warnings)) {
            warnings = new ArrayList<>();
        }
        try {
            final com.chromedata.eid.aos.incentive.model.Incentive unmarshalledIncentive = XMLConvertUtil.unmarshalIncentive(incentiveData.getData());

            // let the clob string to be garbage collected after successfully un-marshaling
            incentiveData.setData(null);

            // Filter out any incentives that violate our filters.  This is filtering that cannot be done in the database (usually because the xml
            //  clob contains data points that are not in lookup tables).
            if (! inMemoryIncentivePredicate.test(unmarshalledIncentive)) {
                WarnableResult<Incentive> incentiveResult = incentiveMapper.mapIncentive(unmarshalledIncentive, incentiveData, requestedLocale);
                warnings.addAll(incentiveResult.getWarnings());
                if(Objects.nonNull(incentiveResult.getResult())) { // HOTFIX : INCDIS-496 - If unable to parse incentive because of data issue, then ignore it.
                    completeIncentive = new CompleteIncentive(incentiveResult.getResult(), incentiveData);
                }
            }

        } catch (JAXBException | ParserConfigurationException | SAXException | IOException e) {
            warnings.add(new ExtractWarning("Failed to unmarshal incentive with clob " + incentiveData, e));
        } catch (Exception ex) {
            LOG.error("Error...", ex);
            CommonComponent.updateJobExecutionCompleteStatus(jobExecution, EXIT_STATUS_FAIL);
            throw new RuntimeException(ex);
        }

        executionContext.put(WARNINGS, warnings);
        return completeIncentive;
    }

    @BeforeChunk
    public void beforeChunk(ChunkContext chunkContext) {
        jobExecution = chunkContext.getStepContext().getStepExecution().getJobExecution();
    }
}

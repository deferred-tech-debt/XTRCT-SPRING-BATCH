package com.chromedata.incentives.extract.batch.writer.dealer;

import com.chromedata.incentives.extract.batch.component.CommonComponent;
import com.chromedata.incentives.extract.batch.writer.ExtractAbstractWriter;
import com.chromedata.incentives.extract.business.model.CompleteIncentive;
import com.chromedata.incentives.extract.dao.model.SignatureStackability;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component("stackabilityWriter")
public class ExtractStackabilityWriter extends ExtractAbstractWriter<CompleteIncentive> {

    @Autowired
    private CommonComponent commonComponent;

    @Override
    @SuppressWarnings("unchecked")
    public void write(final List<? extends CompleteIncentive> completeIncentives) {

        final Set<SignatureStackability> stackabilities = completeIncentives.stream()
                                                                            .map(completeIncentive -> completeIncentive.getAugmentedIncentiveData().getStackabilities())
                                                                            .flatMap(List::stream)
                                                                            .collect(Collectors.toSet());

        if(! CollectionUtils.isEmpty(stackabilities)) {
            commonComponent.addCachedStackabilitiesToDealer(stackabilities);
        }
    }

    @Override
    public void open(final ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void update(final ExecutionContext executionContext) throws ItemStreamException {

    }

    @Override
    public void close() throws ItemStreamException {
    }
}

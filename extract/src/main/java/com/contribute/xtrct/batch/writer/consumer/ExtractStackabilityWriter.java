package com.contribute.xtrct.batch.writer.consumer;

import com.contribute.xtrct.batch.component.CommonComponent;
import com.contribute.xtrct.batch.writer.ConsumerMarkerInterface;
import com.contribute.xtrct.batch.writer.ExtractAbstractWriter;
import com.contribute.xtrct.business.model.CompleteIncentive;
import com.contribute.xtrct.dao.model.SignatureStackability;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component("stackabilityWriterToConsumer")
public class ExtractStackabilityWriter extends ExtractAbstractWriter<CompleteIncentive> implements ConsumerMarkerInterface {

    @Autowired
    private CommonComponent commonComponent;

    @Autowired
    private Predicate<CompleteIncentive> consumerPredicate;

    @Override
    @SuppressWarnings("unchecked")
    public void write(final List<? extends CompleteIncentive> completeIncentives) {

        completeIncentives.removeIf(consumerPredicate);

        final Set<SignatureStackability> stackabilities = completeIncentives.stream()
                                                                            .map(completeIncentive -> completeIncentive.getAugmentedIncentiveData().getStackabilities())
                                                                            .flatMap(List::stream)
                                                                            .collect(Collectors.toSet());

        if(! CollectionUtils.isEmpty(stackabilities)) {
            commonComponent.addCachedStackabilitiesToConsumer(stackabilities);
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

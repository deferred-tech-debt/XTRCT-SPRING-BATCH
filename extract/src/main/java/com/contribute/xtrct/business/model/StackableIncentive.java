package com.contribute.xtrct.business.model;

import java.util.HashSet;
import java.util.Set;

/**
 * This class decorates standard incentives to provide compare functionality to determine their availability
 * to each other.
 */
public class StackableIncentive extends DecoratedIncentive {

    private final Set<Integer> stackableSignatureIds;

    /**
     * Constructor
     *
     * @param baseIncentive         The base incentive to decorate
     * @param stackableSignatureIds The set of signature ids that this incentive is stackable with
     */
    public StackableIncentive(Incentive baseIncentive, Set<Integer> stackableSignatureIds) {
        super(baseIncentive);
        this.stackableSignatureIds = stackableSignatureIds != null
                ? new HashSet<>(stackableSignatureIds)
                : new HashSet<>();
    }

    /**
     * Determines whether this incentive is stackable with the provided incentive
     * <p>
     * An incentive is considered stackable with this incentive if its signature id is recognized by this incentive
     *
     * @param incentive The incentive to determine if we can combine with this incentive
     * @return true if this incentive can be stacked with the provided incentive
     */
    @Override
    public boolean isStackableWith(Incentive incentive) {
        return incentive != null
                && stackableSignatureIds.contains(incentive.getSignatureId());
    }
}

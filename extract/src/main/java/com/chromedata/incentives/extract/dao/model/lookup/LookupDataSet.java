package com.chromedata.incentives.extract.dao.model.lookup;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Holds incentive meta data used by the incentives lookup service
 */
@Data
public class LookupDataSet implements Serializable {

    private List<GroupAffiliation> groupAffiliations;
    private List<PreviousOwnership> previousOwnerships;
    private List<DeliveryType> deliveryTypes;
    private List<Category> categories;
    private List<Division> divisions;
    private List<FinancialInstitution> financialInstitutions;
    private List<IncentiveType> incentiveTypes;
    private List<Market> markets;
    private List<EndRecipient> endRecipients;
    private List<VehicleStatus> vehicleStatuses;
    private List<TaxRule> taxRules;
}

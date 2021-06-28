package com.contribute.xtrct.dao.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents incentive data that comes from the IT01_Incentive table and may not be available in the IT11_IncentiveDataXML Data2 clob which is where
 * all other incentive data comes from.  This data will be used to augment the data from the data2 clob.
 */
@Data
public class AugmentedIncentiveData {

    private final Integer incentiveId;
    private final Integer cellId;
    private final String countryCode;
    private final String yearCode;
    private final String yearDesc;
    private final String divisionCode;
    private final String groupAffiliationNote;
    private final String previousOwnershipNote;
    private final LocalDateTime dateInserted;
    private final LocalDateTime dateUpdated;
    private String data;
    private List<SignatureStackability> stackabilities;
    private List<Vehicle> vehicles;
    private List<BatchGeography> geographies;
}

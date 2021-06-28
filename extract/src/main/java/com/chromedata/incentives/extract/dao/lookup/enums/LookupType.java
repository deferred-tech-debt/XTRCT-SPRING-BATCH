package com.chromedata.incentives.extract.dao.lookup.enums;

import com.chromedata.incentives.extract.dao.lookup.model.CategoryResponse;
import com.chromedata.incentives.extract.dao.lookup.model.DeliveryTypeResponse;
import com.chromedata.incentives.extract.dao.lookup.model.DivisionResponse;
import com.chromedata.incentives.extract.dao.lookup.model.EndRecipientResponse;
import com.chromedata.incentives.extract.dao.lookup.model.FlexibleGeographyResponse;
import com.chromedata.incentives.extract.dao.lookup.model.GroupAffiliationResponse;
import com.chromedata.incentives.extract.dao.lookup.model.IncentiveTypeResponse;
import com.chromedata.incentives.extract.dao.lookup.model.InstitutionResponse;
import com.chromedata.incentives.extract.dao.lookup.model.MarketResponse;
import com.chromedata.incentives.extract.dao.lookup.model.PreviousOwnershipResponse;
import com.chromedata.incentives.extract.dao.lookup.model.TaxRuleResponse;
import com.chromedata.incentives.extract.dao.lookup.model.VehicleStatusResponse;

/**
 * Enum usefull to call lookup rest service
 */
public enum LookupType {

    CATEGORIES("categories", CategoryResponse.class),
    DELIVERY_TYPES("deliveryTypes", DeliveryTypeResponse.class),
    DIVISIONS("divisions", DivisionResponse.class),
    INSTITUTIONS("institutions", InstitutionResponse.class),
    REGIONS("regions", FlexibleGeographyResponse.class),
    GROUP_AFFILIATIONS("groupAffiliations", GroupAffiliationResponse.class),
    INCENTIVE_TYPES("incentiveTypes", IncentiveTypeResponse.class),
    MARKETS("markets", MarketResponse.class),
    PREVIOUS_OWNERSHIPS("previousOwnerships", PreviousOwnershipResponse.class),
    END_RECIPIENTS("endRecipients", EndRecipientResponse.class),
    VEHICLE_STATUSES("vehicleStatuses", VehicleStatusResponse.class),
    TAX_RULES("taxRules", TaxRuleResponse.class);

    private static final String LOOKUP_BASE_PATH = "/lookups/";
    private String path;
    private Class type;

    LookupType(final String path, final Class type) {
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return LOOKUP_BASE_PATH + path;
    }

    public Class getType() {
        return type;
    }
}

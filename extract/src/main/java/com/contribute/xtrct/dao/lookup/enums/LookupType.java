package com.contribute.xtrct.dao.lookup.enums;

import com.contribute.xtrct.dao.lookup.model.CategoryResponse;
import com.contribute.xtrct.dao.lookup.model.DeliveryTypeResponse;
import com.contribute.xtrct.dao.lookup.model.DivisionResponse;
import com.contribute.xtrct.dao.lookup.model.EndRecipientResponse;
import com.contribute.xtrct.dao.lookup.model.FlexibleGeographyResponse;
import com.contribute.xtrct.dao.lookup.model.GroupAffiliationResponse;
import com.contribute.xtrct.dao.lookup.model.IncentiveTypeResponse;
import com.contribute.xtrct.dao.lookup.model.InstitutionResponse;
import com.contribute.xtrct.dao.lookup.model.MarketResponse;
import com.contribute.xtrct.dao.lookup.model.PreviousOwnershipResponse;
import com.contribute.xtrct.dao.lookup.model.TaxRuleResponse;
import com.contribute.xtrct.dao.lookup.model.VehicleStatusResponse;

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

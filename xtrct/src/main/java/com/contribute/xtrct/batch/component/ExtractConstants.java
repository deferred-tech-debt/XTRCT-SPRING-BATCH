package com.contribute.xtrct.batch.component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An interface to handle constants across multi threaded batch environment
 */
public interface ExtractConstants {

    String DEALER_RESPONSE_DIR = "dealerResponseDir";
    String CONSUMER_RESPONSE_DIR = "consumerResponseDir";
    String REQUESTED_LOCALE = "requestedLocale";
    String WARNINGS = "warnings";
    String COUNTRY = "country";
    String LANGUAGE = "language";
    String GEO_IDS_DEALER = "geoIDs1";
    String GEO_IDS_CONSUMER = "geoIDs2";
    String STYLES_DEALER = "styles1";
    String STYLES_CONSUMER = "styles2";
    String ACODES_DEALER = "acodes1";
    String ACODES_CONSUMER = "acodes2";
    String COMPLETE_STATUS = "CompleteStatus";

    AtomicInteger INCENTIVES_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger INCENTIVES_DEALER_COUNT = new AtomicInteger();
    AtomicInteger GEOGRAPHY_DEALER_COUNT = new AtomicInteger();
    AtomicInteger GEOGRAPHY_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger GEOGRAPHY_DETAIL_DEALER_COUNT = new AtomicInteger();
    AtomicInteger GEOGRAPHY_DETAIL_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_STATUS_DEALER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_STATUS_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger PROGRAM_RULE_DEALER_COUNT = new AtomicInteger();
    AtomicInteger PROGRAM_RULE_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger INSTITUTION_DEALER_COUNT = new AtomicInteger();
    AtomicInteger INSTITUTION_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger DELIVERY_TYPE_DEALER_COUNT = new AtomicInteger();
    AtomicInteger DELIVERY_TYPE_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger VALUE_VARIATION_DEALER_COUNT = new AtomicInteger();
    AtomicInteger VALUE_VARIATION_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger PROGRAM_VALUE_DEALER_COUNT = new AtomicInteger();
    AtomicInteger PROGRAM_VALUE_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger TERM_DEALER_COUNT = new AtomicInteger();
    AtomicInteger TERM_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger MILEAGE_RESTRICTION_DEALER_COUNT = new AtomicInteger();
    AtomicInteger MILEAGE_RESTRICTION_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger LOAN_TO_VALUE_DEALER_COUNT = new AtomicInteger();
    AtomicInteger LOAN_TO_VALUE_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger STACKABILITY_DEALER_COUNT = new AtomicInteger();
    AtomicInteger STACKABILITY_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_INCENTIVE_DEALER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_INCENTIVE_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_STYLE_DEALER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_STYLE_CONSUMER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_DEALER_COUNT = new AtomicInteger();
    AtomicInteger VEHICLE_CONSUMER_COUNT = new AtomicInteger();

    int EXIT_STATUS_SUCCESS = 0;
    int EXIT_STATUS_WARN = 1;
    int EXIT_STATUS_FAIL = 2;
}

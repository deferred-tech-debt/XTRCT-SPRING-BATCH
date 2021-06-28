package com.chromedata.incentives.extract.business.model;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Represents a mileage restriction for a residual for a given term on a leased vehicle
 */
public class MileageRestriction {

    private static final Logger LOG = LogManager.getLogger(MileageRestriction.class);
    private static final NumberFormat mileageFormat = NumberFormat.getNumberInstance(Locale.US);

    private String mileage;
    private BigDecimal residual;
    private boolean isDefault;

    /**
     * Constructor
     *
     * @param mileage   The amount of miles the vehicle must stay under to maintain this residual value
     * @param residual  The worth of this vehicle at the end of the term for the lease.  May be a percentage or dollar amount.
     * @param isDefault Whether this is the default residual value/mileage combo for the specified term in the lease
     */
    MileageRestriction(String mileage, BigDecimal residual, boolean isDefault) {
        this.mileage = formatMileage(mileage);
        this.residual = residual;
        this.isDefault = isDefault;
    }

    public String getMileage() {
        return mileage;
    }

    public BigDecimal getResidual() {
        return residual;
    }

    public boolean isDefault() {
        return isDefault;
    }

    private static String formatMileage(String mileage) {
        String formattedMileage = mileage;

        if (mileage != null && !mileage.isEmpty()) {
            try {
                Integer intValue = Integer.valueOf(mileage);
                formattedMileage = mileageFormat.format(intValue);
            } catch (NumberFormatException e) {
                // LOG.warn("Failed to format mileage {} to an Integer.  Perhaps it is already formatted?", mileage, e);
            }
        }

        return formattedMileage;
    }
}

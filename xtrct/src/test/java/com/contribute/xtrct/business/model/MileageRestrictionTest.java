package com.contribute.xtrct.business.model;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MileageRestrictionTest {

    @Test
    public void unformattedMileageShouldGetFormatted() throws Exception {
        String unformattedMileage = "12500";
        String expectedMileage = "12,500";
        String result = new MileageRestriction(unformattedMileage, null, false).getMileage();

        assertThat(result).isEqualTo(expectedMileage);
    }

    @Test
    public void formattedMileageShouldRemainFormatted() throws Exception {
        String formattedMileage = "12,500";
        String result = new MileageRestriction(formattedMileage, null, false).getMileage();

        assertThat(result).isEqualTo(formattedMileage);
    }

    @Test
    public void unformattableMileageShouldRemainUnformatted() throws Exception {
        String unformattableMileage = "12,5#%00";
        String result = new MileageRestriction(unformattableMileage, null, false).getMileage();

        assertThat(result).isEqualTo(unformattableMileage);
    }

    @Test
    public void nullMileageShouldStayNull() throws Exception {
        String result = new MileageRestriction(null, null, false).getMileage();

        assertThat(result).isNull();
    }

    @Test
    public void emptyMileageShouldStayEmpty() throws Exception {
        String result = new MileageRestriction("", null, false).getMileage();

        assertThat(result).isEmpty();
    }
}

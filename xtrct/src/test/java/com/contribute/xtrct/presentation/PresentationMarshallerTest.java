package com.contribute.xtrct.presentation;

import com.contribute.xtrct.MutableTestIncentive;
import com.contribute.xtrct.business.model.DeliveryType;
import com.contribute.xtrct.business.model.FinancialInstitution;
import com.contribute.xtrct.business.model.Incentive;
import com.contribute.xtrct.business.model.Term;
import com.contribute.xtrct.business.model.Variation;
import com.contribute.xtrct.business.model.VehicleStatus;
import com.contribute.xtrct.dao.FilterValuesDao;
import com.contribute.xtrct.dao.model.BatchGeography;
import com.contribute.xtrct.dao.model.BatchGeographyDetail;
import com.contribute.xtrct.dao.model.BatchVehicleIncentive;
import com.contribute.xtrct.dao.model.BatchVehicleStyle;
import com.contribute.xtrct.dao.model.RelationType;
import com.contribute.xtrct.dao.model.SignatureStackability;
import com.contribute.xtrct.dao.model.Vehicle;
import com.contribute.xtrct.presentation.model.CSVDataSet;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.MessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.mockito.Mockito.mock;


public class PresentationMarshallerTest {

    @Test
    public void extractMarshallerTest(){
        PresentationMarshaller extractMarshaller    = new PresentationMarshaller(Locale.US, mock(MessageSource.class), mock(FilterValuesDao.class));
        Incentive              incentive            = createSampleIncentive();
        BatchGeographyDetail   batchGeographyDetail = createBatchGeographyDetail();
        BatchGeography         batchGeography       = createBatchGeography();
        Vehicle                vehicle              = createVehicle();
        SignatureStackability  stackability         = createStackability();
        BatchVehicleStyle      batchVehicleStyle    = createBatchVehcileStyle();
        BatchVehicleIncentive  vehicleIncentive     = createVehicleIncentive();

        extractMarshaller.addIncentive(incentive);
        extractMarshaller.addGeoDetail(batchGeographyDetail);
        extractMarshaller.addGeography(batchGeography);
        extractMarshaller.addVehicle(vehicle);
        extractMarshaller.addStackability(stackability);
        extractMarshaller.addVehicleStyle(batchVehicleStyle);
        extractMarshaller.addVehicleIncentive(vehicleIncentive);


        CSVDataSet data = extractMarshaller.getData();

        //Asserting the different CSVDataSet values populated by the ExtractMarshaller.
        Assert.assertEquals(data.getIncentiveList().size(), 1);
        Assert.assertEquals(data.getDeliveryTypeList().size(), 1);
        Assert.assertEquals(data.getVehicleStatusList().size(), 1);
        Assert.assertEquals(data.getGeoDetailList().size() ,1);
        Assert.assertEquals(data.getProgramRuleList().size(), 1);
        Assert.assertEquals(data.getInstitutionList().size(), 2);
        Assert.assertEquals(data.getProgramValueList().size(), 1);
        Assert.assertEquals(data.getValueVariationList().size(), 1);
        Assert.assertEquals(data.getTermList().size(), 1);
        Assert.assertEquals(data.getLuRegionList().size(), 1);
        Assert.assertEquals(data.getStackabilityList().size(), 1);
        Assert.assertEquals(data.getVehicleStyleList().size(), 1);
        Assert.assertEquals(data.getVehicleIncentiveList().size(), 1);
        Assert.assertEquals(data.getVehicleList().size(), 1);
        Assert.assertEquals(data.getVehicleVINList().size(), 0);
    }

    private Incentive createSampleIncentive(){
        MutableTestIncentive incentive = new MutableTestIncentive();
        incentive.setId("12548785");
        incentive.setGeographyId(28);
        incentive.setSignatureId(159);
        incentive.setCategoryId("112");
        incentive.setCategoryDescription("Residual");
        incentive.setEffectiveDate(getDate(2013, 8, 21));
        incentive.setExpiryDate(getDate(2013, 12, 31));
        incentive.setOfferType("Public");
        incentive.setMarket("Retail");
        incentive.setEligibility("true");
        incentive.setGroupAffiliation("No Specific Group Affiliation");
        incentive.setPreviousOwnership("No Previous Ownership Requirement");
        incentive.setProgramNumber(null);
        List<VehicleStatus> vehicleStatuses = new ArrayList<>();
        vehicleStatuses.add(new VehicleStatus(48, "New"));
        incentive.setVehicleStatusList(vehicleStatuses);
        incentive.setSource(null);
        List<FinancialInstitution> finansialInstitutionList      = new ArrayList<>();
        FinancialInstitution       usBankfinancialInstitution    = new FinancialInstitution(123, "US Bank");
        FinancialInstitution       allayBankfinancialInstitution = new FinancialInstitution(456, "Allay Bank");
        finansialInstitutionList.add(usBankfinancialInstitution);
        finansialInstitutionList.add(allayBankfinancialInstitution);
        incentive.setFinancialInstitutionList(finansialInstitutionList);

        List<DeliveryType> deliveryTypeList = new ArrayList<>();
        deliveryTypeList.add(new DeliveryType(47,"CPO"));
        incentive.setDeliveryTypes(deliveryTypeList);

        List<Variation> variations     =new ArrayList<>();
        Variation       basicVariation = new Variation();
        basicVariation.setIncentiveId(12548785);
        basicVariation.setMaxValueOfPaymentsWaived(2);
        basicVariation.setDescription("Variation");
        basicVariation.setDFUID(2);
        basicVariation.setNote("note");

        List<Term> termList = new ArrayList<>();
        Term       term     = new Term();
        term.setFrom(123);
        term.setTo(500);
        term.setProgramValueId(8524785);
        term.setValue(new BigDecimal(84000));
        termList.add(term);
        basicVariation.setTermList(termList);

        variations.add(basicVariation);

        incentive.setVariationList(variations);
        return incentive;

    }

    private BatchGeographyDetail createBatchGeographyDetail(){
        BatchGeographyDetail batchGeographyDetail = new BatchGeographyDetail(28, "47001");
        return batchGeographyDetail;
    }

    private BatchGeography createBatchGeography(){
        BatchGeography batchGeography = new BatchGeography(28, "USA", "US");
        return batchGeography;
    }

    private Vehicle createVehicle(){
        Vehicle vehicle = new Vehicle("USC30FOC123A0", "US", "C30", null, null, null, null);
        return vehicle;

    }

    private List<String> createStyleIds(){
        List<String> styleList = new ArrayList<String>();
        styleList.add("Style-1");
        styleList.add("Style-2");
        return styleList;
    }

    private SignatureStackability createStackability(){
        SignatureStackability stackability = new SignatureStackability(1254, 130, 129, RelationType.N, "None");
        return stackability;

    }

    private BatchVehicleStyle createBatchVehcileStyle(){
        BatchVehicleStyle batchVehicleStyle = new BatchVehicleStyle("USC30FOC123A0", 123);
        return batchVehicleStyle;

    }

    private BatchVehicleIncentive createVehicleIncentive(){
        BatchVehicleIncentive vehicleIncentive = new BatchVehicleIncentive(12548785, "USC30FOC123A0");
        return vehicleIncentive;
    }

    private Date getDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, day, 0, 0);
        return c.getTime();
    }
}

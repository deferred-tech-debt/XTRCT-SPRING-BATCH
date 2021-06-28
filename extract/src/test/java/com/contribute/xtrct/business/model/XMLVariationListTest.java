package com.contribute.xtrct.business.model;

import com.chromedata.eid.aos.incentive.model.Incentive;
import com.chromedata.eid.aos.incentive.util.XMLConvertUtil;
import com.contribute.xtrct.business.util.IncentiveLoanCalculator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;


public class XMLVariationListTest {

    XMLVariationList sut;
    private final Integer ltvLeaseRateMoneyFactorDivider = 2400;
    @Mock
    IncentiveLoanCalculator mockedLoanCalculator;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBuildMileageRestrictions() throws Exception {
        Incentive incentive = getIncentiveFromFile("/IncentiveWithMileageRestrictions.xml");

        sut = new XMLVariationList(new IncentiveXML(incentive, Locale.US), mockedLoanCalculator, ltvLeaseRateMoneyFactorDivider);
        List<Term> terms = sut.get(0).getTermList();

        assertThat(terms).hasSize(13);
        assertTerm(terms.get(0), 24, 24, '%');

        terms.get(0).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(67.0), false);
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(66.0), true);

        assertTerm(terms.get(1), 27, 27, '%');

        terms.get(1).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(66.0), false);
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(65.0), true);

        assertTerm(terms.get(2), 30, 30, '%');

        terms.get(2).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(64.0), false);
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(63.0), true);

        assertTerm(terms.get(3), 33, 33, '%');

        terms.get(3).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(62.0), false);
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(61.0), true);

        assertTerm(terms.get(4), 36, 36, '%');

        terms.get(4).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(4).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(61.0), false);
        assertMileageRestriction(terms.get(4).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(59.0), true);

        assertTerm(terms.get(5), 39, 39, '%');

        terms.get(5).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(5).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(59.0), false);
        assertMileageRestriction(terms.get(5).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(57.0), true);

        assertTerm(terms.get(6), 42, 42, '%');

        terms.get(6).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(6).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(57.0), false);
        assertMileageRestriction(terms.get(6).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(55.0), true);

        assertTerm(terms.get(7), 45, 45, '%');

        terms.get(7).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(7).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(55.0), false);
        assertMileageRestriction(terms.get(7).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(53.0), true);

        assertTerm(terms.get(8), 48, 48, '%');

        terms.get(8).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(8).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(53.0), false);
        assertMileageRestriction(terms.get(8).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(51.0), true);

        assertTerm(terms.get(9), 51, 51, '%');

        terms.get(9).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(9).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(44.0), false);
        assertMileageRestriction(terms.get(9).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(44.0), true);

        assertTerm(terms.get(10), 54, 54, '%');

        terms.get(10).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(10).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(42.0), false);
        assertMileageRestriction(terms.get(10).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(42.0), true);

        assertTerm(terms.get(11), 57, 57, '%');

        terms.get(11).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(11).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(40.0), false);
        assertMileageRestriction(terms.get(11).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(40.0), true);

        assertTerm(terms.get(12), 60, 60, '%');

        terms.get(12).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(12).getMileageRestrictions().get(0), "12,000", BigDecimal.valueOf(38.0), false);
        assertMileageRestriction(terms.get(12).getMileageRestrictions().get(1), "15,000", BigDecimal.valueOf(38.0), true);
    }

    @Test
    public void nullMileageShouldNotCauseException() throws Exception {
        Incentive incentive = getIncentiveFromFile("/IncentiveWithNullMileage.xml");
        Comparator<MileageRestriction> mileageRestrictionComparator = Comparator.comparing(mileageRestriction -> mileageRestriction.getMileage() == null ? "" : mileageRestriction.getMileage());

        sut = new XMLVariationList(new IncentiveXML(incentive, Locale.US), mockedLoanCalculator, ltvLeaseRateMoneyFactorDivider);
        List<Term> terms = sut.get(0).getTermList();

        assertThat(terms).hasSize(4);
        assertTerm(terms.get(0), 39, 39, '%');

        terms.get(0).getMileageRestrictions().sort(mileageRestrictionComparator);
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(0), null, BigDecimal.valueOf(44.0), false);
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(1), null, BigDecimal.valueOf(47.0), false);
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(2), "12,000", BigDecimal.valueOf(46.0), true);

        assertTerm(terms.get(1), 42, 42, '%');

        terms.get(1).getMileageRestrictions().sort(mileageRestrictionComparator);
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(0), null, BigDecimal.valueOf(41.0), false);
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(1), null, BigDecimal.valueOf(44.0), false);
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(2), "12,000", BigDecimal.valueOf(43.0), true);

        assertTerm(terms.get(2), 48, 48, '%');

        terms.get(2).getMileageRestrictions().sort(mileageRestrictionComparator);
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(0), null, BigDecimal.valueOf(36.0), false);
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(1), null, BigDecimal.valueOf(39.0), false);
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(2), "12,000", BigDecimal.valueOf(38.0), true);

        assertTerm(terms.get(3), 36, 36, '%');

        terms.get(3).getMileageRestrictions().sort(mileageRestrictionComparator);
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(0), null, BigDecimal.valueOf(48.0), false);
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(1), null, BigDecimal.valueOf(51.0), false);
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(2), "12,000", BigDecimal.valueOf(50.0), true);
    }

    @Test
    public void nullDefaultResidualShouldNotCauseException() throws Exception {
        Incentive incentive = getIncentiveFromFile("/IncentiveWithNullDefaultResidual.xml");
        sut = new XMLVariationList(new IncentiveXML(incentive, Locale.US), mockedLoanCalculator, ltvLeaseRateMoneyFactorDivider);

        List<Term> terms = sut.get(0).getTermList();

        assertThat(terms).hasSize(6);
        assertTerm(terms.get(0), 36, 36, '%');

        terms.get(0).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(0), "10,000", BigDecimal.valueOf(37.0), false);
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(1), "12,000", BigDecimal.valueOf(36.0), false);
        assertMileageRestriction(terms.get(0).getMileageRestrictions().get(2), "15,000", BigDecimal.valueOf(34.0), true);
        assertTerm(terms.get(1), 39, 39, '%');

        terms.get(1).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(0), "10,000", BigDecimal.valueOf(36.0), false);
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(1), "12,000", BigDecimal.valueOf(35.0), false);
        assertMileageRestriction(terms.get(1).getMileageRestrictions().get(2), "15,000", BigDecimal.valueOf(33.0), true);
        assertTerm(terms.get(2), 48, 48, '%');

        terms.get(2).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(0), "10,000", BigDecimal.valueOf(32.0), false);
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(1), "12,000", BigDecimal.valueOf(31.0), false);
        assertMileageRestriction(terms.get(2).getMileageRestrictions().get(2), "15,000", BigDecimal.valueOf(28.0), true);
        assertTerm(terms.get(3), 24, 24, '%');

        terms.get(3).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(0), "10,000", BigDecimal.valueOf(2.0), false);
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(1), "12,000", BigDecimal.valueOf(1.0), false);
        assertMileageRestriction(terms.get(3).getMileageRestrictions().get(2), "15,000", null, true);
        assertTerm(terms.get(4), 27, 27, '%');

        terms.get(4).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(4).getMileageRestrictions().get(0), "10,000", BigDecimal.valueOf(2.0), false);
        assertMileageRestriction(terms.get(4).getMileageRestrictions().get(1), "12,000", BigDecimal.valueOf(1.0), false);
        assertMileageRestriction(terms.get(4).getMileageRestrictions().get(2), "15,000", null, true);
        assertTerm(terms.get(5), 42, 42, '%');

        terms.get(5).getMileageRestrictions().sort(Comparator.comparing(MileageRestriction::getMileage));
        assertMileageRestriction(terms.get(5).getMileageRestrictions().get(0), "10,000", BigDecimal.valueOf(3.0), false);
        assertMileageRestriction(terms.get(5).getMileageRestrictions().get(1), "12,000", BigDecimal.valueOf(2.0), false);
        assertMileageRestriction(terms.get(5).getMileageRestrictions().get(2), "15,000", null, true);
    }

    private void assertTerm(Term term, int from, int to, Character valueType) {
        assertThat(term.getFrom()).isEqualTo(from);
        assertThat(term.getTo()).isEqualTo(to);
        assertThat(term.getValueType()).isEqualTo(valueType);
    }

    private void assertMileageRestriction(MileageRestriction mileageRestriction, String mileage, BigDecimal residual, boolean isDefault) {
        assertThat(mileageRestriction.getMileage()).isEqualTo(mileage);
        assertThat(mileageRestriction.getResidual()).isEqualTo(residual);
        assertThat(mileageRestriction.isDefault()).isEqualTo(isDefault);
    }

    private com.chromedata.eid.aos.incentive.model.Incentive getIncentiveFromFile(String fileName) throws Exception {
        File file = new File(this.getClass().getResource(fileName).getFile());
        String xmlFileContent = new String(Files.readAllBytes(file.toPath()));

        return XMLConvertUtil.unmarshalIncentive(xmlFileContent);
    }
}

package com.chromedata.incentives.extract.presentation.model;

/**
 * Created with IntelliJ IDEA.
 * User: abhishek.mundewal
 * Date: 11/27/13
 * Time: 6:03 AM
 * To change this template use File | Settings | File Templates.
 */

import com.chromedata.commons.dsvio.DSVColumn;
import com.chromedata.commons.identity.IdentityGenerator;
import com.chromedata.commons.identity.utils.IdentityGenerators;

/**
 *
 * Model class for Stackability.
 */
public class Stackability implements Comparable<Stackability> {

    private static final IdentityGenerator IDENTITY = IdentityGenerators.createIdentityGenerator(Stackability.class);

    @DSVColumn(header = "SignatureHistoryID", qualified = false)
    private Integer signatureHistoryID;
    @DSVColumn(header = "RelationshipType", qualified = false)
    private String relationshipType;
    @DSVColumn(header = "ToSignatureID", qualified = false)
    private Integer toSignatureID;
    @DSVColumn(header = "ComplexLogic")
    private String complexLogic;


    public Stackability() {
    }

    /**
     * getter method to return the signatureHistoryID.
     * @return
     */

    public Integer getSignatureHistoryID() {
        return signatureHistoryID;
    }

    /**
     * Setter method to set the signatureHistoryID.
     * @param signatureHistoryID
     */

    public void setSignatureHistoryID(Integer signatureHistoryID) {
        this.signatureHistoryID = signatureHistoryID;
    }

    /**
     * Getter method to return the relationshipType.
     * @return
     */

    public String getRelationshipType() {
        return relationshipType;
    }

    /**
     * Setter method to set the relationshipType.
     * @param relationshipType
     */

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    /**
     * Getter method to return the toSignatureID.
     * @return
     */

    public Integer getToSignatureID() {
        return toSignatureID;
    }


    /**
     * Setter method to set the toSignatureID.
     * @param toSignatureID
     */

    public void setToSignatureID(Integer toSignatureID) {
        this.toSignatureID = toSignatureID;
    }

    /**
     * Getter method to return the complexLogic.
     * @return
     */

    public String getComplexLogic() {
        return complexLogic;
    }

    /**
     * Setter method to set the complexLogic.
     * @param complexLogic
     */

    public void setComplexLogic(String complexLogic) {
        this.complexLogic = complexLogic;
    }


    @Override
    public int compareTo(Stackability stackability) {
        if (stackability == this) return 0;

        int comparison = this.getSignatureHistoryID().compareTo(stackability.getSignatureHistoryID());
        if (comparison != 0) return comparison;

        comparison = this.getRelationshipType().compareTo(stackability.getRelationshipType());
        if (comparison != 0) return comparison;

        comparison = this.getToSignatureID().compareTo(stackability.getToSignatureID());
        if (comparison != 0) return comparison;

        return (this.getComplexLogic().compareTo(stackability.getComplexLogic()));
    }

    @Override
    public boolean equals(Object obj) {
        return IDENTITY.areEqual(this, obj);
    }

    @Override
    public int hashCode() {
        return IDENTITY.getHashCode(this);
    }

    @Override
    public String toString() {
        return IDENTITY.toString(this);
    }
}

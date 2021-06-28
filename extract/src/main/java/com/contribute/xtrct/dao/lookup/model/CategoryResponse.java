package com.contribute.xtrct.dao.lookup.model;

import com.chromedata.incentives.response.AbstractCopyRightResponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Display class modeling a category container response object
 */
@XmlRootElement(name = "CategoryResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class CategoryResponse extends AbstractCopyRightResponse {

    @XmlElement(name = "Category")
    private List<Category> categories;

    // Private no-arg constructor required by jax-b
    private CategoryResponse() {
    }

    public CategoryResponse(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}

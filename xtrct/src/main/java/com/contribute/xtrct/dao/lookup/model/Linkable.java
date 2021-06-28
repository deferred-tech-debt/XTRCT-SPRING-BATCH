package com.contribute.xtrct.dao.lookup.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

/**
 * All response entities (if intended to be called singly from the service instead of just residing in a collection)
 * should extend this class in order to support HATEOAS.  This class is marked as XmlTransient in order to support
 * classes being able to order the inherited "links" field as they see fit.
 */
@XmlTransient
public abstract class Linkable {

    @XmlElement(name = "Link")
    protected List<Link> links;

    public Linkable() {
    }

    public List<Link> getLinks() {
        return links;
    }

    /**
     * Add a new Link to our list of links using lazy instantiation of our list.
     * <p><p>
     * The lazy instantiation will assure our response structure only contains the "links" node if there is content.
     *
     * @param link The new link to be added
     */
    public void addLink(Link link) {
        if (links == null) {
            links = new ArrayList<>();
        }

        links.add(link);
    }
}

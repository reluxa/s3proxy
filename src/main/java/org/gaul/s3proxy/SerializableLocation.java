package org.gaul.s3proxy;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.jclouds.domain.Location;
import org.jclouds.domain.LocationScope;
import org.jclouds.domain.internal.LocationImpl;
import org.jclouds.javax.annotation.Nullable;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Objects.equal;
import static com.google.common.base.Preconditions.checkNotNull;

public class SerializableLocation implements Location, Serializable {


    private static final long serialVersionUID = 4944595960806108685L;
    private LocationScope scope;
    private String id;
    private String description;
    private Location parent;
    private Set<String> iso3166Codes;
    private Map<String, Object> metadata;

    public SerializableLocation() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        org.jclouds.domain.internal.LocationImpl that = org.jclouds.domain.internal.LocationImpl.class.cast(o);
        return equal(this.scope, that.getScope()) && equal(this.id, that.getId()) && equal(this.parent, that.getParent());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(scope, id, parent);
    }

    @Override
    public String toString() {
        MoreObjects.ToStringHelper helper = MoreObjects.toStringHelper("").omitNullValues().add("scope", scope).add("id", id)
                .add("description", description);
        if (parent != null)
            helper.add("parent", parent.getId());
        if (!iso3166Codes.isEmpty())
            helper.add("iso3166Codes", iso3166Codes);
        if (!metadata.isEmpty())
            helper.add("metadata", metadata);
        return helper.toString();
    }

    public SerializableLocation(LocationScope scope, String id, String description, @Nullable Location parent,
                                Iterable<String> iso3166Codes, Map<String, Object> metadata) {
        this.scope = checkNotNull(scope, "scope");
        this.id = checkNotNull(id, "id");
        this.description = checkNotNull(description, "description");
        this.metadata = ImmutableMap.copyOf(checkNotNull(metadata, "metadata"));
        this.iso3166Codes = ImmutableSet.copyOf(checkNotNull(iso3166Codes, "iso3166Codes"));
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationScope getScope() {
        return scope;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getIso3166Codes() {
        return iso3166Codes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setScope(LocationScope scope) {
        this.scope = scope;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParent(Location parent) {
        this.parent = SerializableLocation.copy(parent);
    }

    public void setIso3166Codes(Set<String> iso3166Codes) {
        this.iso3166Codes = iso3166Codes;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }


    public static SerializableLocation copy(Location from) {
        if (from != null) {
            SerializableLocation to = new SerializableLocation();
            to.setScope(from.getScope());
            to.setId(from.getId());
            to.setDescription(from.getDescription());
            to.setParent(from.getParent());
            to.setIso3166Codes(from.getIso3166Codes());
            to.setMetadata(from.getMetadata());
            return to;
        } else {
            return null;
        }
    }

}

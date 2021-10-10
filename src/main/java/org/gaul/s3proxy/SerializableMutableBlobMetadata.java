package org.gaul.s3proxy;

import org.jclouds.blobstore.domain.MutableBlobMetadata;
import org.jclouds.blobstore.domain.StorageType;
import org.jclouds.blobstore.domain.Tier;
import org.jclouds.blobstore.domain.internal.MutableBlobMetadataImpl;
import org.jclouds.domain.Location;
import org.jclouds.domain.ResourceMetadata;
import org.jclouds.io.MutableContentMetadata;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.Map;

public class SerializableMutableBlobMetadata implements MutableBlobMetadata, Serializable {

    private static final long serialVersionUID = 5781202608200186595L;

    private StorageType type;
    private String id;
    private String name;
    private Location location;
    private URI uri;
    private Map<String, String> userMetadata;
    private MutableContentMetadata contentMetadata;
    private URI publicUri;
    private String container;
    private String eTag;
    private Date creationDate;
    private Date lastModified;
    private Long size;
    private Tier tier;

    @Override
    public StorageType getType() {
        return type;
    }

    @Override
    public void setType(StorageType type) {
        this.type = type;
    }

    public String getProviderId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = SerializableLocation.copy(location);
    }

    @Override
    public URI getUri() {
        return uri;
    }

    @Override
    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Override
    public Map<String, String> getUserMetadata() {
        return userMetadata;
    }

    @Override
    public void setUserMetadata(Map<String, String> userMetadata) {
        this.userMetadata = userMetadata;
    }

    @Override
    public MutableContentMetadata getContentMetadata() {
        return contentMetadata;
    }

    @Override
    public void setContentMetadata(MutableContentMetadata contentMetadata) {
        this.contentMetadata = SerializableBaseMutableContentMetadata.copy(contentMetadata);
    }

    @Override
    public URI getPublicUri() {
        return publicUri;
    }

    @Override
    public void setPublicUri(URI publicUri) {
        this.publicUri = publicUri;
    }

    @Override
    public String getContainer() {
        return container;
    }

    @Override
    public void setContainer(String container) {
        this.container = container;
    }

    public String getETag() {
        return eTag;
    }

    public void setETag(String eTag) {
        this.eTag = eTag;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getLastModified() {
        return lastModified;
    }

    @Override
    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public Long getSize() {
        return size;
    }

    @Override
    public void setSize(Long size) {
        this.size = size;
    }

    @Override
    public Tier getTier() {
        return tier;
    }

    @Override
    public void setTier(Tier tier) {
        this.tier = tier;
    }


    public int compareTo(ResourceMetadata<StorageType> o) {
        if (getName() == null)
            return -1;
        return (this == o) ? 0 : getName().compareTo(o.getName());
    }

    public static SerializableMutableBlobMetadata copy(MutableBlobMetadata from) {
        SerializableMutableBlobMetadata to = new SerializableMutableBlobMetadata();
        to.setType(from.getType());
        to.setId(from.getProviderId());
        to.setName(from.getName());
        to.setLocation(from.getLocation());
        to.setUri(from.getUri());
        to.setUserMetadata(from.getUserMetadata());
        to.setContentMetadata(from.getContentMetadata());
        to.setPublicUri(from.getPublicUri());
        to.setContainer(from.getContainer());
        to.setETag(from.getETag());
        to.setCreationDate(from.getCreationDate());
        to.setLastModified(from.getLastModified());
        to.setSize(from.getSize());
        to.setTier(from.getTier());
        return to;
    }
}

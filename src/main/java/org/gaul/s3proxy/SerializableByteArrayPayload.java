package org.gaul.s3proxy;

import com.google.common.base.Throwables;
import org.jclouds.io.ContentMetadata;
import org.jclouds.io.MutableContentMetadata;
import org.jclouds.io.Payload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;

import static com.google.common.base.Preconditions.checkNotNull;

public class SerializableByteArrayPayload implements Payload, Serializable {

    private static final long serialVersionUID = 5808771245292092326L;
    private byte[] content;
    protected transient volatile boolean written;
    protected SerializableBaseMutableContentMetadata contentMetadata;
    private boolean isSensitive;

    public SerializableByteArrayPayload() {
    }

    public SerializableByteArrayPayload(byte[] content, MutableContentMetadata md) {
        this(content, SerializableBaseMutableContentMetadata.copy(md));
    }

    public SerializableByteArrayPayload(byte[] content, SerializableBaseMutableContentMetadata contentMetadata) {
        this.content = checkNotNull(content, "content");
        this.contentMetadata = checkNotNull(contentMetadata, "contentMetadata");
        getContentMetadata().setContentLength(Long.valueOf(checkNotNull(content, "content").length));
    }

    @Override
    public InputStream openStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    @Override
    @Deprecated
    public InputStream getInput() {
        try {
            return openStream();
        } catch (IOException ioe) {
            throw Throwables.propagate(ioe);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getRawContent() {
        return content;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((content == null) ? 0 : Arrays.hashCode(content));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Payload))
            return false;
        Payload other = (Payload) obj;
        if (content == null) {
            if (other.getRawContent() != null)
                return false;
        } else if (!content.equals(other.getRawContent()))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "[content=" + (content != null) + ", contentMetadata=" + contentMetadata + ", written=" + written + ", isSensitive=" + isSensitive + "]";
    }

    /**
     * By default we are repeatable.
     */
    @Override
    public boolean isRepeatable() {
        return true;
    }

    /**
     * By default there are no resources to release.
     */
    @Override
    public void release() {
    }

    /**
     * Delegates to release()
     */
    @Override
    public void close() {
        release();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MutableContentMetadata getContentMetadata() {
        return contentMetadata;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContentMetadata(MutableContentMetadata in) {
        this.contentMetadata = SerializableBaseMutableContentMetadata.copy(in);
    }

    @Override
    public void setSensitive(boolean isSensitive) {
        this.isSensitive = isSensitive;
    }

    @Override
    public boolean isSensitive() {
        return this.isSensitive;
    }


}

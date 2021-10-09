package org.gaul.s3proxy;

import com.google.common.collect.Multimap;
import com.google.common.io.ByteSource;
import org.apache.commons.io.IOUtils;
import org.checkerframework.checker.units.qual.A;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.MutableBlobMetadata;
import org.jclouds.http.internal.PayloadEnclosingImpl;
import org.jclouds.io.Payload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

public class SerializableBlobImpl implements Serializable, Blob, Comparable<Blob> {

    private SerializableMutableBlobMetadata metadata;
    private Multimap<String, String> headers;
    private SerializableByteArrayPayload payload;

    public SerializableBlobImpl() {
    }

    public SerializableBlobImpl(SerializableMutableBlobMetadata metadata, Multimap<String, String> headers) {
        this.metadata = metadata;
        this.headers = headers;
    }

    public static SerializableBlobImpl copy(Blob blob) {
        SerializableMutableBlobMetadata copy = SerializableMutableBlobMetadata.copy(blob.getMetadata());
        SerializableBlobImpl sblob = new SerializableBlobImpl(copy, blob.getAllHeaders());
        sblob.setPayload(blob.getPayload());
        return sblob;
    }

    @Override
    public MutableBlobMetadata getMetadata() {
        return metadata;
    }

    @Override
    public Multimap<String, String> getAllHeaders() {
        return headers;
    }

    @Override
    public void setAllHeaders(Multimap<String, String> allHeaders) {
        this.headers = allHeaders;
    }

    @Override
    public int compareTo(Blob o) {
        if (getMetadata().getName() == null)
            return -1;
        return (this == o) ? 0 : getMetadata().getName().compareTo(o.getMetadata().getName());
    }

    @Override
    public void setPayload(Payload data) {
        try {
            InputStream inputStream = data.openStream();
            this.payload = new SerializableByteArrayPayload(IOUtils.toByteArray(inputStream), data.getContentMetadata());
            data.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setPayload(File data) {

    }

    @Override
    public void setPayload(byte[] data) {
        this.payload = new SerializableByteArrayPayload(data, new SerializableBaseMutableContentMetadata());
    }

    @Override
    public void setPayload(InputStream data) {

    }

    @Override
    public void setPayload(String data) {

    }

    @Override
    public void setPayload(ByteSource data) {

    }

    @Override
    public Payload getPayload() {
        return payload;
    }

    @Override
    public void resetPayload(boolean release) {

    }
}

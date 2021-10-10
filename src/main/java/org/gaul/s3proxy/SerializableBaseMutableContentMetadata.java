package org.gaul.s3proxy;

import com.google.common.hash.HashCode;
import org.jclouds.http.HttpUtils;
import org.jclouds.io.ContentMetadataBuilder;
import org.jclouds.io.MutableContentMetadata;

import java.io.Serializable;
import java.util.Date;

public class SerializableBaseMutableContentMetadata implements MutableContentMetadata, Serializable {

    private static final long serialVersionUID = -1821352566557966916L;

    @Override
    public String getCacheControl() {
        return cacheControl;
    }

    @Override
    public void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public byte[] getContentMD5() {
        return contentMD5.asBytes();
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public Long getContentLength() {
        return contentLength;
    }

    @Override
    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    @Override
    public void setContentMD5(byte[] md5) {
        this.contentMD5 = HashCode.fromBytes(md5);
    }


    @Override
    public HashCode getContentMD5AsHashCode() {
        return contentMD5;
    }

    @Override
    public void setContentMD5(HashCode contentMD5) {
        this.contentMD5 = contentMD5;
    }

    @Override
    public String getContentDisposition() {
        return contentDisposition;
    }

    @Override
    public void setContentDisposition(String contentDisposition) {
        this.contentDisposition = contentDisposition;
    }

    @Override
    public String getContentLanguage() {
        return contentLanguage;
    }

    @Override
    public void setContentLanguage(String contentLanguage) {
        this.contentLanguage = contentLanguage;
    }

    @Override
    public String getContentEncoding() {
        return contentEncoding;
    }

    @Override
    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    @Override
    public Date getExpires() {
        return expires;
    }

    @Override
    public ContentMetadataBuilder toBuilder() {
        return null;
    }

    @Override
    public void setExpires(Date expires) {
        this.expires = expires;
    }

    protected String cacheControl;
    protected String contentType = "application/unknown";
    protected Long contentLength;
    protected HashCode contentMD5;
    protected String contentDisposition;
    protected String contentLanguage;
    protected String contentEncoding;
    protected Date expires;

    public static SerializableBaseMutableContentMetadata copy(MutableContentMetadata from) {
        SerializableBaseMutableContentMetadata to = new SerializableBaseMutableContentMetadata();
        HttpUtils.copy(from, to);
        return to;
    }

}

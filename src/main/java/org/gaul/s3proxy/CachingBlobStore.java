package org.gaul.s3proxy;

import com.sun.xml.internal.ws.util.StringUtils;
import org.ehcache.Cache;
import org.ehcache.PersistentCacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.jclouds.blobstore.BlobStore;
import org.jclouds.blobstore.domain.Blob;
import org.jclouds.blobstore.domain.BlobMetadata;
import org.jclouds.blobstore.options.GetOptions;
import org.jclouds.blobstore.options.PutOptions;
import org.jclouds.blobstore.util.ForwardingBlobStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;

public class CachingBlobStore extends ForwardingBlobStore {

    private static final Logger logger = LoggerFactory.getLogger(CachingBlobStore.class);

    private Cache<String, SerializableBlobImpl> cache;

    public CachingBlobStore(BlobStore blobStore) {
        super(blobStore);

        String location = System.getProperties().getProperty("s3proxy.caching-blobstore-location");
        if (System.getProperties().getProperty("s3proxy.caching-blobstore-location") == null || location.length() == 0) {
            location = "/tmp";
        }
        
        final PersistentCacheManager persistentCacheManager = CacheManagerBuilder.newCacheManagerBuilder()
                .with(CacheManagerBuilder.persistence(location + File.separator + "cache"))
                .withCache("blobcache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, SerializableBlobImpl.class,
                                ResourcePoolsBuilder.newResourcePoolsBuilder()
                                        .disk(200, MemoryUnit.MB, true)
                        )
                ).build(true);
        cache = persistentCacheManager.getCache("blobcache", String.class, SerializableBlobImpl.class);

        Thread printingHook = new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("Shutting down persistence manager...");
                persistentCacheManager.close();
            }
        });
        Runtime.getRuntime().addShutdownHook(printingHook);
    }

    @Override
    public Blob getBlob(String containerName, String blobName) {
        long start = System.currentTimeMillis();
        Blob blob = cache.get(containerName + "::" + blobName);
        if (blob != null) {
            logger.info("Requesting {} and found in cache. Took {} ms", containerName + "::" + blobName, System.currentTimeMillis() - start);
            return blob;
        } else {
            logger.info("Requesting {} and not found in cache. Trying to get it from upstream...", containerName + "::" + blobName);
            blob = super.getBlob(containerName, blobName);
            SerializableBlobImpl sblob = SerializableBlobImpl.copy(blob);
            long upstream = System.currentTimeMillis();
            cache.put(containerName + "::" + blobName, sblob);
            logger.info("Upstream has {}, putting it into cache. Getting from upstream took {} ms, Total took {} ms", containerName + "::" + blobName, upstream - start, System.currentTimeMillis() - start);
            return sblob;
        }
    }

    @Override
    public Blob getBlob(String containerName, String blobName, GetOptions getOptions) {
        long start = System.currentTimeMillis();
        Blob blob = cache.get(containerName + "::" + blobName);
        if (blob != null) {
            logger.info("Requesting {} and found in cache. Took {} ms", containerName + "::" + blobName, System.currentTimeMillis() - start);
            return blob;
        } else {
            logger.info("Requesting {} and not found in cache. Trying to get it from upstream...", containerName + "::" + blobName);
            blob = super.getBlob(containerName, blobName, getOptions);
            SerializableBlobImpl sblob = SerializableBlobImpl.copy(blob);
            long upstream = System.currentTimeMillis();
            logger.info("Upstream has {}, putting it into cache. Getting from upstream took {} ms, Total took {} ms", containerName + "::" + blobName, upstream - start, System.currentTimeMillis() - start);
            cache.put(containerName + "::" + blobName, sblob);
            return sblob;
        }
    }

    @Override
    public BlobMetadata blobMetadata(String container, String name) {
        Blob blob = cache.get(container + "::" + name);
        if (blob != null) {
            logger.info("Getting HEAD of {} and found in cache", container + "::" + name);
            return blob.getMetadata();
        }
        logger.info("Getting HEAD of {} and it is not in the cache", container + "::" + name);
        return super.blobMetadata(container, name);
    }

    @Override
    public String putBlob(String containerName, Blob blob) {
        logger.info("Uploading {}", containerName + "::" + blob.getMetadata().getName());
        SerializableBlobImpl copy = SerializableBlobImpl.copy(blob);
        copy.getMetadata().setLastModified(new Date());
        String result = super.putBlob(containerName, copy);
        if (result != null && !result.equals("")) {
            logger.info("Upload was successful. Sending {} to cache", containerName + "::" + blob.getMetadata().getName());
            cache.put(containerName +"::"+blob.getMetadata().getName(), copy);
        }
        return result;
    }

    @Override
    public String putBlob(String containerName, Blob blob, PutOptions putOptions) {
        logger.info("Uploading {}", containerName + "::" + blob.getMetadata().getName());
        SerializableBlobImpl copy = SerializableBlobImpl.copy(blob);
        copy.getMetadata().setLastModified(new Date());
        String result = super.putBlob(containerName, copy, putOptions);
        if (result != null && !result.equals("")) {
            logger.info("Upload was successful. Sending {} to cache", containerName + "::" + blob.getMetadata().getName());
            cache.put(containerName +"::"+blob.getMetadata().getName(), copy);
        }
        return result;
    }

}

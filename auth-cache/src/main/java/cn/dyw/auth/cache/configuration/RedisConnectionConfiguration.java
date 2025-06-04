/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.dyw.auth.cache.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails.Cluster;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails.Node;
import org.springframework.boot.autoconfigure.data.redis.RedisConnectionDetails.Sentinel;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties.Pool;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.data.redis.connection.*;
import org.springframework.util.ClassUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Redis connection configuration.
 *
 * @author Mark Paluch
 * @author Stephane Nicoll
 * @author Alen Turkovic
 * @author Scott Frederick
 * @author Eddú Meléndez
 * @author Moritz Halbritter
 * @author Andy Wilkinson
 * @author Phillip Webb
 */
abstract class RedisConnectionConfiguration {

    private static final boolean COMMONS_POOL2_AVAILABLE = ClassUtils.isPresent("org.apache.commons.pool2.ObjectPool",
            RedisConnectionConfiguration.class.getClassLoader());

    private final RedisProperties properties;


    private final RedisConnectionDetails connectionDetails;

    private final SslBundles sslBundles;

    protected RedisConnectionConfiguration(RedisProperties properties, 
                                           RedisConnectionDetails connectionDetails, 
                                           ObjectProvider<SslBundles> sslBundles) {
        this.properties = properties;
        this.connectionDetails = connectionDetails;
        this.sslBundles = sslBundles.getIfAvailable();
    }

    protected final RedisStandaloneConfiguration getStandaloneConfig() {
  
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(this.connectionDetails.getStandalone().getHost());
        config.setPort(this.connectionDetails.getStandalone().getPort());
        config.setUsername(this.connectionDetails.getUsername());
        config.setPassword(RedisPassword.of(this.connectionDetails.getPassword()));
        config.setDatabase(this.connectionDetails.getStandalone().getDatabase());
        return config;
    }

    protected final RedisSentinelConfiguration getSentinelConfig() {
  
        if (this.connectionDetails.getSentinel() != null) {
            RedisSentinelConfiguration config = new RedisSentinelConfiguration();
            config.master(this.connectionDetails.getSentinel().getMaster());
            config.setSentinels(createSentinels(this.connectionDetails.getSentinel()));
            config.setUsername(this.connectionDetails.getUsername());
            String password = this.connectionDetails.getPassword();
            if (password != null) {
                config.setPassword(RedisPassword.of(password));
            }
            config.setSentinelUsername(this.connectionDetails.getSentinel().getUsername());
            String sentinelPassword = this.connectionDetails.getSentinel().getPassword();
            if (sentinelPassword != null) {
                config.setSentinelPassword(RedisPassword.of(sentinelPassword));
            }
            config.setDatabase(this.connectionDetails.getSentinel().getDatabase());
            return config;
        }
        return null;
    }

    /**
     * Create a {@link RedisClusterConfiguration} if necessary.
     *
     * @return {@literal null} if no cluster settings are set.
     */
    protected final RedisClusterConfiguration getClusterConfiguration() {
     
        RedisProperties.Cluster clusterProperties = this.properties.getCluster();
        if (this.connectionDetails.getCluster() != null) {
            RedisClusterConfiguration config = new RedisClusterConfiguration();
            config.setClusterNodes(getNodes(this.connectionDetails.getCluster()));
            if (clusterProperties != null && clusterProperties.getMaxRedirects() != null) {
                config.setMaxRedirects(clusterProperties.getMaxRedirects());
            }
            config.setUsername(this.connectionDetails.getUsername());
            String password = this.connectionDetails.getPassword();
            if (password != null) {
                config.setPassword(RedisPassword.of(password));
            }
            return config;
        }
        return null;
    }

    private List<RedisNode> getNodes(Cluster cluster) {
        return cluster.getNodes().stream().map(this::asRedisNode).toList();
    }

    private RedisNode asRedisNode(Node node) {
        return new RedisNode(node.host(), node.port());
    }

    protected final RedisProperties getProperties() {
        return this.properties;
    }

    protected SslBundles getSslBundles() {
        return this.sslBundles;
    }

    protected boolean isSslEnabled() {
        return getProperties().getSsl().isEnabled();
    }

    protected boolean isPoolEnabled(Pool pool) {
        Boolean enabled = pool.getEnabled();
        return (enabled != null) ? enabled : COMMONS_POOL2_AVAILABLE;
    }

    private List<RedisNode> createSentinels(Sentinel sentinel) {
        List<RedisNode> nodes = new ArrayList<>();
        for (Node node : sentinel.getNodes()) {
            nodes.add(asRedisNode(node));
        }
        return nodes;
    }

    protected final boolean urlUsesSsl() {
        return ConnectionInfo.of(this.properties.getUrl()).isUseSsl();
    }

    protected final RedisConnectionDetails getConnectionDetails() {
        return this.connectionDetails;
    }

    static final class ConnectionInfo {

        private final URI uri;

        private final boolean useSsl;

        private final String username;

        private final String password;

        private ConnectionInfo(URI uri, boolean useSsl, String username, String password) {
            this.uri = uri;
            this.useSsl = useSsl;
            this.username = username;
            this.password = password;
        }

        URI getUri() {
            return this.uri;
        }

        boolean isUseSsl() {
            return this.useSsl;
        }

        String getUsername() {
            return this.username;
        }

        String getPassword() {
            return this.password;
        }

        static ConnectionInfo of(String url) {
            try {
                URI uri = new URI(url);
                String scheme = uri.getScheme();
                if (!"redis".equals(scheme) && !"rediss".equals(scheme)) {
                    throw new RedisUrlSyntaxException(url);
                }
                boolean useSsl = ("rediss".equals(scheme));
                String username = null;
                String password = null;
                if (uri.getUserInfo() != null) {
                    String candidate = uri.getUserInfo();
                    int index = candidate.indexOf(':');
                    if (index >= 0) {
                        username = candidate.substring(0, index);
                        password = candidate.substring(index + 1);
                    } else {
                        password = candidate;
                    }
                }
                return new ConnectionInfo(uri, useSsl, username, password);
            } catch (URISyntaxException ex) {
                throw new RedisUrlSyntaxException(url, ex);
            }
        }

    }

}

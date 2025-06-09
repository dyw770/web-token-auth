package cn.dyw.auth.utils;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * @author dyw770
 * @since 2025-06-09
 */
public class ObjectMapperUtils {

    /**
     * 创建redis使用的ObjectMapper
     *
     * @return ObjectMapper
     */
    public static ObjectMapper createRedisObjectMapper() {
        // 创建 ObjectMapper
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .build();
        // 启用自动包含类型信息，用于反序列化时重建对象的实际类型
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        return objectMapper;
    }
}

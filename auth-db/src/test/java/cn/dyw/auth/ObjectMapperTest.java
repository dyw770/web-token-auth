package cn.dyw.auth;

import cn.dyw.auth.db.model.RoleDto;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.List;

/**
 * @author dyw770
 * @since 2025-05-23
 */
public class ObjectMapperTest {
    
    @Test
    public void testObjectMapper() throws JsonProcessingException {
        ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
                .json()
                .build();
        // 启用自动包含类型信息，用于反序列化时重建对象的实际类型
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        String source = "[{\"@class\":\"cn.dyw.auth.db.model.RoleDto\",\"roleCode\":\"admin\",\"roleName\":\"管理员\",\"description\":\"管理员\",\"del\":false,\"createTime\":[2025,4,18,12,2,37],\"updateTime\":[2025,5,5,18,19,40],\"parentRoleCode\":null,\"children\":[\"java.util.ArrayList\",[{\"@class\":\"cn.dyw.auth.db.model.RoleDto\",\"roleCode\":\"department_admin\",\"roleName\":\"部门管理员\",\"description\":\"部门管理员\",\"del\":false,\"createTime\":[2025,4,18,12,2,37],\"updateTime\":[2025,5,5,18,19,40],\"parentRoleCode\":\"admin\",\"children\":[\"java.util.ArrayList\",[]]},{\"@class\":\"cn.dyw.auth.db.model.RoleDto\",\"roleCode\":\"test4\",\"roleName\":\"test4\",\"description\":\"\",\"del\":false,\"createTime\":[2025,5,12,13,48,49],\"updateTime\":[2025,5,12,14,39,58],\"parentRoleCode\":\"admin\",\"children\":[\"java.util.ArrayList\",[{\"@class\":\"cn.dyw.auth.db.model.RoleDto\",\"roleCode\":\"test2\",\"roleName\":\"test2\",\"description\":\"\",\"del\":false,\"createTime\":[2025,5,12,13,48,37],\"updateTime\":[2025,5,12,14,40,1],\"parentRoleCode\":\"test4\",\"children\":[\"java.util.ArrayList\",[{\"@class\":\"cn.dyw.auth.db.model.RoleDto\",\"roleCode\":\"test5\",\"roleName\":\"test5\",\"description\":\"\",\"del\":false,\"createTime\":[2025,5,12,14,8,44],\"updateTime\":[2025,5,12,14,45,48],\"parentRoleCode\":\"test2\",\"children\":[\"java.util.ArrayList\",[]]}]]},{\"@class\":\"cn.dyw.auth.db.model.RoleDto\",\"roleCode\":\"test3\",\"roleName\":\"test3\",\"description\":\"\",\"del\":false,\"createTime\":[2025,5,12,13,48,43],\"updateTime\":[2025,5,12,14,46,36],\"parentRoleCode\":\"test4\",\"children\":[\"java.util.ArrayList\",[]]}]]},{\"@class\":\"cn.dyw.auth.db.model.RoleDto\",\"roleCode\":\"user\",\"roleName\":\"用户\",\"description\":\"用户\",\"del\":false,\"createTime\":[2025,4,18,12,2,37],\"updateTime\":[2025,5,5,18,19,40],\"parentRoleCode\":\"admin\",\"children\":[\"java.util.ArrayList\",[]]}]]}]";
        
        List<RoleDto> deserialize = (List<RoleDto>) serializer.deserialize(source.getBytes(), Object.class);
        System.out.println(deserialize);
    }
}


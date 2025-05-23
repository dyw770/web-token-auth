package cn.dyw.auth.security.repository;

import cn.dyw.auth.security.serializable.UserLoginDetails;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * @author dyw770
 * @since 2025-05-23
 */
public class ObjectMapperTest {
    
    @Test
    public void testObjectMapper() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 启用自动包含类型信息，用于反序列化时重建对象的实际类型
        objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL_AND_ENUMS, JsonTypeInfo.As.PROPERTY);
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        DataModelTest dataModelTest = new DataModelTest();
        dataModelTest.setMsg("aa");
        String s = objectMapper.writeValueAsString(dataModelTest);
        System.out.println(s);
        String s2 = objectMapper.writeValueAsString(new DataModelTest2("2"));
        DataModelTest2 dataModelTest2 = objectMapper.readValue(s2, DataModelTest2.class);
        System.out.println(dataModelTest2);
        System.out.println(s2);

        UserLoginDetails details = objectMapper.readValue("{\"token\":\"dGVzdEAyMDI1LTA1LTIzVDExOjI3OjAzLjEzNzEwNzQwMA==\",\"username\":\"test\",\"createTime\":[2025,5,23,11,27,3,137107400],\"loginUserAgent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36\",\"loginIp\":\"0:0:0:0:0:0:0:1\"}",
                UserLoginDetails.class);
        System.out.println(details);
    }
}


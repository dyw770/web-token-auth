package cn.dyw.auth.security.repository.jackson;

import cn.dyw.auth.security.TokenAuthenticationToken;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.MissingNode;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * token 包装
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE,
        isGetterVisibility = JsonAutoDetect.Visibility.NONE)
@JsonDeserialize(using = TokenWrapperDeserializer.class)
public class TokenWrapperMixin implements Serializable {
    
}

class TokenWrapperDeserializer extends JsonDeserializer<TokenWrapper> {


    @Override
    public TokenWrapper deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);

        JsonNode tokenNode = readJsonNode(jsonNode, "token");
        TokenAuthenticationToken token = getTokenObject(mapper, tokenNode);

        JsonNode expireTimeNode = readJsonNode(jsonNode, "expireTime");
        LocalDateTime expireTime = getExpireTimeObject(mapper, expireTimeNode);

        return new TokenWrapper(token, expireTime);
    }

    private TokenAuthenticationToken getTokenObject(ObjectMapper mapper, JsonNode tokenNode)
            throws IOException, JsonParseException, JsonMappingException {
        return mapper.readValue(tokenNode.traverse(mapper), TokenAuthenticationToken.class);
    }

    private LocalDateTime getExpireTimeObject(ObjectMapper mapper, JsonNode expireTimeNode) throws IOException {
        return mapper.readValue(expireTimeNode.traverse(mapper), LocalDateTime.class);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }

}

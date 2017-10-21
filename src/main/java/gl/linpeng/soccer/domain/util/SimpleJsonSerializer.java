package gl.linpeng.soccer.domain.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Simplest object serializer, only field id and name should be serialized
 *
 * @author linpeng
 * @since 0.0.1
 */
public class SimpleJsonSerializer extends JsonSerializer {
    private static final Set<String> SERIALIZE_FIELDS = new HashSet<>(3);

    static {
        SERIALIZE_FIELDS.add("id");
        SERIALIZE_FIELDS.add("name");
        SERIALIZE_FIELDS.add("code");
        SERIALIZE_FIELDS.add("picture");
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        Map<String, Object> objectMap = new HashMap<>(SERIALIZE_FIELDS.size());
        for (String fieldName : SERIALIZE_FIELDS) {
            Method method = ReflectionUtils.findMethod(value.getClass(), getCamelMethod(fieldName));
            if (null == method) {
                continue;
            }
            Object fieldValue = ReflectionUtils.invokeMethod(method, value);
            objectMap.put(fieldName, fieldValue);
        }
        gen.writeObject(objectMap);
    }

    private String getCamelMethod(String fieldName) {
        return "get" + StringUtils.capitalize(fieldName);
    }

}

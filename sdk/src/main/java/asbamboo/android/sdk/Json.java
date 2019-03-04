package asbamboo.android.sdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Json {

    static Gson gson = (new GsonBuilder()).registerTypeAdapter(HashMap.class, getDeserializer()).create();

    public Json() {
    }

    public static HashMap<String, Object> decode(String json) {
        HashMap<String, Object> djson = (HashMap)gson.fromJson(json, HashMap.class);
        return djson;
    }

    public static String encode(Object element) {
        return gson.toJson(element);
    }

    static JsonDeserializer<Object> getDeserializer() {
        return new JsonDeserializer<Object>() {
            private HashMap<String, Object> decodeJsonObject(JsonObject json, HashMap<String, Object> decoded) {
                Iterator var3 = json.entrySet().iterator();

                while(var3.hasNext()) {
                    Map.Entry<String, JsonElement> entry = (Map.Entry)var3.next();
                    String key = (String)entry.getKey();
                    JsonElement element = (JsonElement)entry.getValue();
                    if (element.isJsonObject()) {
                        HashMap<String, Object> inner_decoded = new HashMap();
                        this.decodeJsonObject(element.getAsJsonObject(), inner_decoded);
                        decoded.put(key, inner_decoded);
                    } else if (element.isJsonPrimitive()) {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();
                        decoded.put(key, this.decodeJsonPrimitive(primitive));
                    }
                }

                return decoded;
            }

            private Object decodeJsonPrimitive(JsonPrimitive primitive) {
                if (primitive.isJsonArray()) {
                    return primitive.getAsJsonArray();
                } else if (primitive.isString()) {
                    return primitive.getAsString();
                } else if (primitive.isNumber()) {
                    if(primitive.getAsNumber().toString().equals(String.valueOf(primitive.getAsInt()))){
                        return primitive.getAsInt();
                    }else if(primitive.getAsNumber().toString().equals(String.valueOf(primitive.getAsFloat()))){
                        return primitive.getAsFloat();
                    }else if(primitive.getAsNumber().toString().equals(String.valueOf(primitive.getAsDouble()))){
                        return primitive.getAsDouble();
                    }else{
                        return primitive.getAsString();
                    }
                } else if (primitive.isBoolean()) {
                    return primitive.getAsBoolean();
                } else if (primitive.isJsonNull()) {
                    return primitive.getAsJsonNull();
                } else {
                    throw new IllegalStateException();
                }
            }

            public HashMap<String, Object> deserialize(JsonElement ele, Type type, JsonDeserializationContext context) throws JsonParseException {
                HashMap<String, Object> decoded = new HashMap();
                if (ele.isJsonObject()) {
                    JsonObject json_ob = ele.getAsJsonObject();
                    this.decodeJsonObject(json_ob, decoded);
                }

                return decoded;
            }
        };
    }
}

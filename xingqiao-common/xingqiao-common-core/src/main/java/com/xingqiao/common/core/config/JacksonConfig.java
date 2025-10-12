package com.xingqiao.common.core.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Jackson配置类
 * 解决LocalDateTime等Java 8时间类型的序列化问题
 *
 * @author xingqiao
 */
@Configuration
public class JacksonConfig {

    /**
     * 配置ObjectMapper支持Java 8时间类型
     *
     * @return Jackson2ObjectMapperBuilderCustomizer
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomizer() {
        return builder -> {
            // 创建JavaTimeModule并添加自定义的序列化器和反序列化器
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            
            // LocalDateTime序列化器
            javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
            
            // LocalDateTime反序列化器
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
            
            builder.modules(javaTimeModule);
        };
    }
    
    /**
     * LocalDateTime序列化器
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            if (value != null) {
                gen.writeString(formatter.format(value));
            } else {
                gen.writeNull();
            }
        }
    }
    
    /**
     * LocalDateTime反序列化器
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String dateString = p.getText();
            if (dateString != null && !dateString.isEmpty()) {
                return LocalDateTime.parse(dateString, formatter);
            }
            return null;
        }
    }
}
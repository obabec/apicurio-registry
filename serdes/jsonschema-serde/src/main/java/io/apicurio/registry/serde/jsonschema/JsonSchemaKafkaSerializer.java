/*
 * Copyright 2020 Red Hat
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.apicurio.registry.serde.jsonschema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

import io.apicurio.registry.resolver.ParsedSchema;
import io.apicurio.registry.resolver.SchemaParser;
import io.apicurio.registry.resolver.SchemaResolver;
import io.apicurio.registry.resolver.data.Record;
import io.apicurio.registry.resolver.strategy.ArtifactReferenceResolverStrategy;
import io.apicurio.registry.rest.client.RegistryClient;
import io.apicurio.registry.serde.AbstractKafkaSerializer;
import io.apicurio.registry.serde.headers.MessageTypeSerdeHeaders;
import io.apicurio.registry.types.ArtifactType;
import io.apicurio.registry.utils.IoUtil;
import org.apache.kafka.common.header.Headers;
import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * An implementation of the Kafka Serializer for JSON Schema use-cases. This serializer assumes that the
 * user's application needs to serialize a Java Bean to JSON data using Jackson. In addition to standard
 * serialization of the bean, this implementation can also optionally validate it against a JSON schema.
 *
 * @author eric.wittmann@gmail.com
 * @author Ales Justin
 * @author Fabian Martinez
 * @author Carles Arnal
 */
public class JsonSchemaKafkaSerializer<T> extends AbstractKafkaSerializer<JsonSchema, T> implements Serializer<T>, SchemaParser<JsonSchema, T> {

    protected static ObjectMapper mapper = new ObjectMapper();

    private Boolean validationEnabled;
    private MessageTypeSerdeHeaders serdeHeaders;

    public JsonSchemaKafkaSerializer() {
        super();
    }

    public JsonSchemaKafkaSerializer(RegistryClient client,
                                     ArtifactReferenceResolverStrategy<JsonSchema, T> artifactResolverStrategy,
                                     SchemaResolver<JsonSchema, T> schemaResolver) {
        super(client, artifactResolverStrategy, schemaResolver);
    }

    public JsonSchemaKafkaSerializer(RegistryClient client) {
        super(client);
    }

    public JsonSchemaKafkaSerializer(SchemaResolver<JsonSchema, T> schemaResolver) {
        super(schemaResolver);
    }

    public JsonSchemaKafkaSerializer(RegistryClient client, Boolean validationEnabled) {
        this(client);
        this.validationEnabled = validationEnabled;
    }

    /**
     * @see io.apicurio.registry.serde.AbstractKafkaSerializer#configure(java.util.Map, boolean)
     */
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        JsonSchemaKafkaSerializerConfig config = new JsonSchemaKafkaSerializerConfig(configs);
        super.configure(config, isKey);

        if (validationEnabled == null) {
            this.validationEnabled = config.validationEnabled();
        }

        serdeHeaders = new MessageTypeSerdeHeaders(new HashMap<>(configs), isKey);
    }

    public boolean isValidationEnabled() {
        return validationEnabled != null && validationEnabled;
    }

    /**
     * @param validationEnabled the validationEnabled to set
     */
    public void setValidationEnabled(Boolean validationEnabled) {
        this.validationEnabled = validationEnabled;
    }

    /**
     * @see io.apicurio.registry.serde.AbstractKafkaSerDe#schemaParser()
     */
    @Override
    public SchemaParser<JsonSchema, T> schemaParser() {
        return this;
    }

    /**
     * @see io.apicurio.registry.serde.SchemaParser#artifactType()
     */
    @Override
    public ArtifactType artifactType() {
        return ArtifactType.JSON;
    }

    /**
     * @see io.apicurio.registry.serde.SchemaParser#parseSchema(byte[])
     */
    @Override
    public JsonSchema parseSchema(byte[] rawSchema) {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7);
        return factory.getSchema(IoUtil.toStream(rawSchema));
    }

    //TODO we could implement some way of providing the jsonschema beforehand:
    // - via annotation in the object being serialized
    // - via config property
    //if we do this users will be able to automatically registering the schema when using this serde
    /**
     * @see io.apicurio.registry.resolver.SchemaParser#getSchemaFromData(java.lang.Object)
     */
    @Override
    public ParsedSchema<JsonSchema> getSchemaFromData(Record<T> data) {
        //not supported for jsonschema type
        return null;
    }

    /**
     * @see io.apicurio.registry.resolver.SchemaParser#supportsExtractSchemaFromData()
     */
    @Override
    public boolean supportsExtractSchemaFromData() {
        return false;
    }

    /**
     * @see io.apicurio.registry.serde.AbstractKafkaSerializer#serializeData(io.apicurio.registry.serde.ParsedSchema, java.lang.Object, java.io.OutputStream)
     */
    @Override
    protected void serializeData(ParsedSchema<JsonSchema> schema, T data, OutputStream out) throws IOException {
        //TODO add property to specify a jsonschema to allow for auto-register json schemas
        serializeData(null, schema, data, out);
    }

    /**
     * @see io.apicurio.registry.serde.AbstractKafkaSerializer#serializeData(org.apache.kafka.common.header.Headers, io.apicurio.registry.serde.ParsedSchema, java.lang.Object, java.io.OutputStream)
     */
    @Override
    protected void serializeData(Headers headers, ParsedSchema<JsonSchema> schema, T data, OutputStream out) throws IOException {
        final byte[] dataBytes = mapper.writeValueAsBytes(data);
        if (isValidationEnabled()) {
            JsonSchemaValidationUtil.validateDataWithSchema(schema, dataBytes, mapper);
        }
        if (headers != null) {
            serdeHeaders.addMessageTypeHeader(headers, data.getClass().getName());
        }
        out.write(dataBytes);
    }
}

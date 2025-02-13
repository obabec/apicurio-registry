/*
 * Copyright 2021 Red Hat
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

package io.apicurio.registry.serde.protobuf;

import org.apache.kafka.common.errors.SerializationException;

import com.google.protobuf.Message;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.squareup.wire.schema.internal.parser.ProtoFileElement;
import com.squareup.wire.schema.internal.parser.ProtoParser;
import io.apicurio.registry.utils.protobuf.schema.FileDescriptorUtils;
import io.apicurio.registry.utils.protobuf.schema.ProtobufSchema;
import io.apicurio.registry.resolver.ParsedSchema;
import io.apicurio.registry.resolver.ParsedSchemaImpl;
import io.apicurio.registry.resolver.SchemaParser;
import io.apicurio.registry.resolver.data.Record;
import io.apicurio.registry.types.ArtifactType;
import io.apicurio.registry.utils.IoUtil;

/**
 * @author Fabian Martinez
 */
public class ProtobufSchemaParser<U extends Message> implements SchemaParser<ProtobufSchema, U> {

    /**
     * @see io.apicurio.registry.serde.SchemaParser#artifactType()
     */
    @Override
    public ArtifactType artifactType() {
        return ArtifactType.PROTOBUF;
    }

    /**
     * @see io.apicurio.registry.serde.SchemaParser#parseSchema(byte[])
     */
    @Override
    public ProtobufSchema parseSchema(byte[] rawSchema) {
        try {
            //textual .proto file
            ProtoFileElement fileElem = ProtoParser.Companion.parse(FileDescriptorUtils.DEFAULT_LOCATION, IoUtil.toString(rawSchema));
            FileDescriptor fileDescriptor = FileDescriptorUtils.protoFileToFileDescriptor(fileElem);
            return new ProtobufSchema(fileDescriptor, fileElem);
        } catch (DescriptorValidationException pe) {
            throw new SerializationException("Error parsing protobuf schema ", pe);
        }
    }

    /**
     * @see io.apicurio.registry.resolver.SchemaParser#getSchemaFromData(java.lang.Object)
     */
    @Override
    public ParsedSchema<ProtobufSchema> getSchemaFromData(Record<U> data) {
        ProtoFileElement protoFileElement = toProtoFileElement(data.payload().getDescriptorForType().getFile());
        ProtobufSchema protobufSchema = new ProtobufSchema(data.payload().getDescriptorForType().getFile(), protoFileElement);

        byte[] rawSchema = IoUtil.toBytes(protoFileElement.toSchema());

        return new ParsedSchemaImpl<ProtobufSchema>()
                .setParsedSchema(protobufSchema)
                .setRawSchema(rawSchema);
    }

    /**
     * This method converts the Descriptor to a ProtoFileElement that allows to get a textual representation .proto file
     * @param fileDescriptor
     * @return textual protobuf representation
     */
    public ProtoFileElement toProtoFileElement(FileDescriptor fileDescriptor) {
        return FileDescriptorUtils.fileDescriptorToProtoFile(fileDescriptor.toProto());
    }

}

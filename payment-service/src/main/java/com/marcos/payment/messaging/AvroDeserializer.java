package com.marcos.payment.messaging;

import com.marcos.payment.exception.PaymentEventSerializationException;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecord;

import java.io.ByteArrayOutputStream;

public class AvroDeserializer {
    private AvroDeserializer() {}

    public static <T extends SpecificRecord> T deserialize(byte[] data, T emptyInstance) {
        if (data == null || data.length == 0) {
            return null;
        }

        try {
            SpecificDatumReader<T> reader =
                    new SpecificDatumReader<>(emptyInstance.getSchema());

            Decoder decoder =
                    DecoderFactory.get().binaryDecoder(data, null);

            return reader.read(null, decoder);

        } catch (Exception e) {
            throw new PaymentEventSerializationException("Exception trying to deserialize Avro", e);
        }
    }

    public static <T extends SpecificRecord> byte[] serialize(T specificRecord) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            SpecificDatumWriter<T> writer =
                    new SpecificDatumWriter<>(specificRecord.getSchema());

            BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(out, null);

            writer.write(specificRecord, encoder);
            encoder.flush();

            return out.toByteArray();

        } catch (Exception e) {
            throw new PaymentEventSerializationException("Exception trying to serialize Avro", e);
        }
    }
}

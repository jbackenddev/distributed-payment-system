package com.marcos.common.avro;

import org.apache.avro.Conversions;
import org.apache.avro.LogicalTypes;

import java.math.BigDecimal;
import java.nio.ByteBuffer;

public class AvroDecimalUtils {
    private AvroDecimalUtils() {}

    private static final LogicalTypes.Decimal DECIMAL_TYPE =
            LogicalTypes.decimal(10, 2);

    private static final Conversions.DecimalConversion DECIMAL_CONVERSION =
            new Conversions.DecimalConversion();

    public static BigDecimal convertToBigDecimal(ByteBuffer buffer) {
        return DECIMAL_CONVERSION.fromBytes(
                buffer,
                null,
                DECIMAL_TYPE
        );
    }

    public static ByteBuffer convertToBytes(BigDecimal value) {
        return DECIMAL_CONVERSION.toBytes(
                value,
                null,
                DECIMAL_TYPE
        );
    }

    public static ByteBuffer convertToBytes(double value) {
        return DECIMAL_CONVERSION.toBytes(
                BigDecimal.valueOf(value),
                null,
                DECIMAL_TYPE
        );
    }
}

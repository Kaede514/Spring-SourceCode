package com.kaede.a02;

import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Set;

/**
 * @author kaede
 * @create 2023-02-17
 */

public class TypedStringValueToIntegerConverter implements GenericConverter {

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.singleton(new ConvertiblePair(TypedStringValue.class, Integer.class));
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        return Integer.parseInt(((TypedStringValue) source).getValue());
    }

}

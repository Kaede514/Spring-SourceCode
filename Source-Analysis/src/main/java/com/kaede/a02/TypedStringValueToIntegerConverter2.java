package com.kaede.a02;

import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * @author kaede
 * @create 2023-02-17
 */

public class TypedStringValueToIntegerConverter2 implements Converter<TypedStringValue, Integer> {

    @Override
    public Integer convert(TypedStringValue source) {
        return Integer.parseInt(Objects.requireNonNull(((TypedStringValue) source).getValue()));
    }

}

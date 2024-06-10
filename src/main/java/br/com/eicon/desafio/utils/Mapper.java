package br.com.eicon.desafio.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    private final ModelMapper modelMapper;

    @Autowired
    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    public <T> T convert(Object source, Class<T> targetClass) {
        return modelMapper.map(source, targetClass);
    }
}
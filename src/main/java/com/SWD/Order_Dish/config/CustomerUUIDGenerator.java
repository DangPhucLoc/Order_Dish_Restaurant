package com.SWD.Order_Dish.config;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.Random;

public class CustomerUUIDGenerator implements IdentifierGenerator {
    @Override
    public Object generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object oject) {
        String prefix = "ORS";
        String part1 = String.format("%06d", new Random().nextInt(1000000));
        String part2 = String.format("%08d", new Random().nextInt(1000000));
        String part3 = String.format("%08d", new Random().nextInt(100000000));
        String part4 = String.format("%08d", new Random().nextInt(100000000));

        return prefix + part1 + "-" + part2 + "-" + part3 + "-" + part4;
    }
}

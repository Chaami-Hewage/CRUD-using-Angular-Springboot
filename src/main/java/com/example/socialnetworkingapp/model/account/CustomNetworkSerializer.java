package com.example.socialnetworkingapp.model.account;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomNetworkSerializer extends StdSerializer<List<Account>> {

    public CustomNetworkSerializer() {
        this(null);
    }

    public CustomNetworkSerializer(Class<List<Account>> t) {
        super(t);
    }

    @Override
    public void serialize(
            List<Account> accounts,
            JsonGenerator generator,
            SerializerProvider provider)
            throws IOException, JsonProcessingException {

        List<NetworkEntity> accs = new ArrayList<>();
        for (Account account : accounts) {
            accs.add(new NetworkEntity(account.getFirstName(),
                    account.getLastName(), account.getEmail(), account.getImageUrl()));
        }
        generator.writeObject(accs);
    }
}
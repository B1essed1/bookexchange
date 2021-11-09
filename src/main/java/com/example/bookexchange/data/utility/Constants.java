package com.example.bookexchange.data.utility;

public interface Constants {

    enum ReferenceType {

        language("uz", "ru", "en"),
        status("ACTIVE", "INACTIVE", "DELETED");

        String[] names;

        ReferenceType(String... names) {
            this.names = names;
        }
    }
}
package com.nathb.swiftshop.model;

import java.util.UUID;

import io.realm.RealmObject;

public class Item extends RealmObject {

    private String id;
    private String name;

    public Item() { }

    public Item(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

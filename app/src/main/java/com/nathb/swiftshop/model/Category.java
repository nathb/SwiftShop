package com.nathb.swiftshop.model;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject {

    @PrimaryKey private String id;
    private String name;
    private int order;

    public Category() { }

    public Category(String name, int order) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.order = order;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    @Override
    public String toString() {
        return name;
    }
}

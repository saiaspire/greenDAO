/*
 * Copyright (C) 2011 Markus Junginger, greenrobot (http://greenrobot.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.greenrobot.daogenerator.gentest;

import de.greenrobot.daogenerator.*;

/**
 * Generates entities and DAOs for the example project DaoExample.
 *
 * Run it as a Java application (not Android).
 *
 * @author Markus
 */
public class ExampleDaoGenerator {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(3, "de.greenrobot.daoexample");

        addNote(schema);
        addCustomerOrder(schema);

        new DaoGenerator().generateAll(schema, "greenDAO/DaoExample/src-gen");
    }

    private static void addNote(Schema schema) {
        Entity note = schema.addEntity("Note");

        note.addIdProperty();
        note.addStringProperty("text").notNull().addSetterAnnotation(new Annotation("test5", "key1", "value1"));
        note.addStringProperty("comment").addFieldAnnotation(new Annotation("JSONIgnore"));
        note.addDateProperty("date").addSetterAnnotation(new Annotation("Test", "key1", "value1"));
        note.addAnnotation(new Annotation("Test"));
        note.addAnnotation(new Annotation("Test2", "singleValueeee"));
        note.addAnnotation(new Annotation("Test3", "key1", "5", "key2","\"value2\""));
        note.addAnnotation(new Annotation("Test4", "key1", null, "key2","\"value2\""));
        note.addEnumProperty("gender", "NoteActivity.Gender", new Annotation("Test", "key1", "value1")).addImport("de.greenrobot.daoexample.NoteActivity");
        note.addProperty(PropertyType.StringList, "userIds");

        note.addEmptyConstructorAnnotation(new Annotation("Deprecated"));
        note.addFullConstructorAnnotation(new Annotation("Inject", "Context"));

    }

    private static void addCustomerOrder(Schema schema) {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);
        Property serializedCustomer = order.addProperty(PropertyType.ByteArray, "serializedCustomer").getProperty();
        order.addSerializedProperty(serializedCustomer, "customer2", "Customer");

        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }

}

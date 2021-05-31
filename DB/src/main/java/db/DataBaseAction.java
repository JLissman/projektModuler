package db;

import db.entity.Pokemon;

public class DataBaseAction {
    public static void main(String[] args) {
        HandleEntity eh = new HandleEntity(Pokemon.class);
        System.out.println(eh.findById(1));


    }
}

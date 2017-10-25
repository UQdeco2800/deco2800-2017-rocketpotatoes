package com.deco2800.potatoes.entities.constructables;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class ConstructableBuilder {
    private static class ConstuctableObject {
        public String texture;
    }

    private static Map<String, ConstuctableObject> constructables;

    public static void loadConstructables() {
        constructables = new HashMap<>();

        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
             obj = parser.parse(new FileReader("resources/json/constructables.json")); //the location of the file
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray constructs = (JSONArray) jsonObject.get("constructs");

        for (Object c : constructs) {
            JSONObject construct = (JSONObject) c;
            System.out.println(construct.get("name"));
            System.out.println(construct.get("texture"));
        }
    }

    public static Constructable getConstructable(String name) {
        ConstuctableObject obj = constructables.get(name);

        Constructable constructable = new Constructable(0, 0, true);
        constructable.setTexture(obj.texture);

        return constructable;
    }
}

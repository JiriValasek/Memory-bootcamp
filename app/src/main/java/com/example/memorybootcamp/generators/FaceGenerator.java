package com.example.memorybootcamp.generators;

import org.json.JSONObject;

public class FaceGenerator implements IGenerator{

    private static String serviceURL = "";
    /* TODO:
        create methods that will sent requests similar to
        $.ajax({
          url: 'https://randomuser.me/api/?inc=name,picture&results=5&format=xml',
          dataType: 'json',
          success: function(data) {
            console.log(data);
              }
            });
        retrive a picture and a name from json/xml, save it to a folder so it can be used
        in a face challenge and implement generateSequence so that it returns tuples of
        a name and a path to a corresponding image.
     */
    public FaceGenerator(){

    }

    @Override
    public JSONObject generateSequence(int length) {
        return null;
    }
}

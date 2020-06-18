package tourGuide.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Repository
public class Dao {

    private Database database;
    public String jsonFile;


    public JsonReaderWriter(@Value("${jsonFileName}") String jsonfile) {
        try {
            FileReader fileString = new FileReader(getClass().getClassLoader().getResource(jsonfile).getFile(), StandardCharsets.UTF_8);
            database = new ObjectMapper()
                    .readValue(fileString,
                            Database.class
                    );
            this.jsonFile = jsonfile;
            fileString.close();
        } catch (IOException e) {
            e.printStackTrace();}
    }


    public void writer(Database database) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = objectMapper.writeValueAsString(database);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter file = new FileWriter(new File("./target/classes", jsonFile));) {
            file.write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileWriter file = new FileWriter(jsonFile)) {
            file.write(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


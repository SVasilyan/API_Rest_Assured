package utilities;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import com.github.javafaker.Faker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;


import io.restassured.response.Response;


public abstract class FrameworkUtility {

    protected static Properties properties;

    public static String readConfigurationFile(String key) {
        try{
            properties = new Properties();
            properties.load(new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH));

        } catch (Exception e){
            System.out.println("Cannot find key: "+key+" in Config file due to exception : "+e);
        }
        return properties.getProperty(key).trim();
    }

    public static String generateRandomString(Faker faker, int numberOfLetters) {
        StringBuilder stringBuilder = new StringBuilder();

        // Use Faker to generate random characters
        for (int i = 0; i < numberOfLetters; i++) {
            char randomChar = faker.lorem().character();
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }
}


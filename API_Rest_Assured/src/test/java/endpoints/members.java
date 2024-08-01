package endpoints;

import io.restassured.response.Response;
import utilities.BaseTest;

import static io.restassured.RestAssured.given;

public class members extends BaseTest {
    public static final String membersEndpoint = "/1/members/me";

    public static Response listingMember(){
        return given()
                    .spec(requestSpec).
                //.log().all()
               when()
                .get(membersEndpoint);
    }

    public static Response listingMemberWithInvalidKey(){
        return given()
                    .baseUri(readConfigurationFile("Base_URI"))
                    .queryParam("token", readConfigurationFile("token"))
                    .queryParam("key","invalid key").
                //.log().all()
               when()
                    .get(membersEndpoint);
    }

    public static Response listingMemberWithInvalidToken(){
        return given()
                    .baseUri(readConfigurationFile("Base_URI"))
                    .queryParam("key", readConfigurationFile("key"))
                    .queryParam("token","invalid token").
                //.log().all()
               when()
                    .get(membersEndpoint);
    }
}

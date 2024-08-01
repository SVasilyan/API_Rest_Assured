package endpoints;

import io.restassured.response.Response;
import payloads.Board;
import utilities.BaseTest;

import static io.restassured.RestAssured.*;

public class boards extends BaseTest {
    public static final String boardsEndpoint = "/1/boards/";
    public static Response createBoard(Object name){
        return given()
                .spec(requestSpec)
                .contentType("application/json")
                .queryParam("name", name).
                //.log().all()
                when()
                .post(boardsEndpoint);
    }

    public static Response getBoard(String board_id){
        return given()
                  .spec(requestSpec)
                  .header("Accept", "application/json").
                //.log().all()
               when()
                  .get(boardsEndpoint + board_id);
    }

    public static Response updateBoard(String board_id, String updatedName){
        return given()
                  .spec(requestSpec)
                  .queryParam("name", updatedName).
                //.log().all()
               when()
                  .put(boardsEndpoint + board_id);
    }

    public static Response deleteBoard(String board_id){
        return given()
                  .spec(requestSpec).
                //.log().all()
               when()
                  .delete(boardsEndpoint + board_id);
    }
}

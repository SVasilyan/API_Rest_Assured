package tests;

import com.github.javafaker.Faker;
import endpoints.boards;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import payloads.Board;
import utilities.AllureLogger;
import utilities.FrameworkUtility;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.testng.Assert.assertNull;

public class BoardTests extends boards {

    static Faker faker = new Faker();
    String emptyBoardName = FrameworkUtility.generateRandomString(faker, 0);


    //String maxOKBoardName = FrameworkUtility.generateRandomString(faker, 14749);
    //String minLargeHeaderFieldBoard = FrameworkUtility.generateRandomString(faker, 14750);
    //String maxLargeHeaderFieldBoard = FrameworkUtility.generateRandomString(faker, 16228);
    String tooLongBoardName = FrameworkUtility.generateRandomString(faker, 16553);

    long boardNameWithNumbers = faker.number().randomNumber();

    private static ArrayList<String> boardIds = new ArrayList<>();

    @DataProvider(name = "id")
    public static String[] id() {
        String[] stringArray = new String[boardIds.size()];
        return boardIds.toArray(stringArray);
    }

    @DataProvider(name = "userData")
    public Object[] userData() {
        return new Object[][]{{"randomName", 200, "HTTP/1.1 200 OK"},
                              {emptyBoardName, 400, "HTTP/1.1 400 Bad Request"},
                              {boardNameWithNumbers, 200, "HTTP/1.1 200 OK"},
                              {tooLongBoardName, 414, "HTTP/1.1 414 URI Too Long" }

                              // These are unstable tests
                              //{maxOKBoardName, 200},
                              //{minLargeHeaderFieldBoard, 431},
                              //{maxLargeHeaderFieldBoard, 431}
        };
    }

    @Description("Verify API creates boards with a given name")
    @Test(priority = 1, dataProvider = "userData")
    public void testCreateboard(Object name, int status, String statLine) {

        AllureLogger.logToAllure("sending post request to create a board with a '" + name + "' name");
        Response res = boards.createBoard(name);

        AllureLogger.logToAllure("checking status code, statusLine and response time");
        res.then()
                .statusCode(status)
                .statusLine(statLine)
                .time(lessThan((long)2800));

        if (res.statusCode() == 200) {
            boardIds.add(res.jsonPath().get("id"));
        }

        AllureLogger.logToAllure("test passed");
    }

    @Description("Verify API return the info of a requested(by id) board")
    @Test(priority = 2, dataProvider = "id", dependsOnMethods = "testCreateboard")
    public void testGetBoard(String id) {

        AllureLogger.logToAllure("sending get request");
        Response res = boards.getBoard(id);

        AllureLogger.logToAllure("checking status code,contentType, statusLine and response time");
        res.then()
                .spec(responseSpec)
                .statusLine("HTTP/1.1 200 OK")
                .contentType("application/json; charset=utf-8")
                .time(lessThan((long)2000));

        AllureLogger.logToAllure("test passed");
    }

    @Description("Verify API updates the board with the provided name")
    @Test(priority = 3, dataProvider = "id", dependsOnMethods = "testCreateboard")
    public void testUpdateBoard(String id) {

        String randomName = FrameworkUtility.generateRandomString(faker, 13);

        AllureLogger.logToAllure("sending put request");
        Response res = boards.updateBoard(id, randomName);

        AllureLogger.logToAllure("checking status code,contentType, statusLine and response time");
        res.then()
                .spec(responseSpec)
                .statusLine("HTTP/1.1 200 OK")
                .contentType("application/json; charset=utf-8")
                .time(lessThan((long)2000));

        AllureLogger.logToAllure("sending get request to see the changes");
        Response getResponse = boards.getBoard(id);

        AllureLogger.logToAllure("checking status code,contentType, statusLine and response time");
        getResponse.then()
                .spec(responseSpec)
                .statusLine("HTTP/1.1 200 OK")
                .contentType("application/json; charset=utf-8")
                .time(lessThan((long)2000));

        AllureLogger.logToAllure("checking the made changes in the board name");
        assertThat(getResponse.path("name").toString(),equalTo(randomName));
    }

    @Description("Verify API deletes the board for provided id")
    @Test(priority = 4, dataProvider = "id", dependsOnMethods = "testCreateboard")
    public void testDeleteBoard(String id) {

        AllureLogger.logToAllure("sending delete request");
        Response res = boards.deleteBoard(id);

        AllureLogger.logToAllure("checking status code,contentType, statusLine and response time");
        res.then()
                .spec(responseSpec)
                .statusLine("HTTP/1.1 200 OK")
                .contentType("application/json; charset=utf-8")
                .time(lessThan((long)2800));

        AllureLogger.logToAllure("checking the request body");
        assertNull(res.path("_value"));
    }
}


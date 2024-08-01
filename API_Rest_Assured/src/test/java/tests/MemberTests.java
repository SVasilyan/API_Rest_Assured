package tests;

import endpoints.members;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utilities.AllureLogger;
import utilities.BaseTest;
import static org.hamcrest.Matchers.lessThan;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class MemberTests extends BaseTest {

    @Description("Verify API lists member's info correctly")
    @Test(groups = {"smoke","api"})
    public void testGetMember(){

        AllureLogger.logToAllure("sending get request");
        Response res = members.listingMember();

        AllureLogger.logToAllure("checking status code, statusLine, contentType and response time" );
        res.then()
                //.log().all()
                .spec(responseSpec)
                .statusLine("HTTP/1.1 200 OK")
                .contentType("application/json; charset=utf-8")
                .time(lessThan((long)1500));

        AllureLogger.logToAllure("checking that response id matches to the provided regex" );
        String regexPattern = "^[a-f0-9]{24}$";
        String id = res.path("id");
        assertThat(id, matchesPattern(regexPattern));

        AllureLogger.logToAllure("checking that response fullname is the correct name of the requested member" );
        assertThat(res.path("fullName").toString(),equalTo("Sargis Vasilyan"));

        AllureLogger.logToAllure("test passed");
    }

    @Description("Verify API responds correctly when provided an invalid token")
    @Test(groups = {"smoke","api"})
    public void testInvalidToken(){

        AllureLogger.logToAllure("sending get request with invalid token");
        Response res = members.listingMemberWithInvalidToken();

        AllureLogger.logToAllure("checking status code, statusLine, contentType and response time" );
        res.then()
                //.log().all()
                .assertThat().statusCode(401)
                .statusLine("HTTP/1.1 401 Unauthorized")
                .contentType("text/plain; charset=utf-8")
                .time(lessThan((long)1500));

        AllureLogger.logToAllure("checking response body" );
        Assert.assertEquals(res.body().asString(),"invalid app token");

        AllureLogger.logToAllure("test passed" );
    }

    @Description("Verify API responds correctly when provided an invalid key")
    @Test(groups = {"smoke","api"})
    public void testInvalidKey(){

        AllureLogger.logToAllure("sending get request with invalid key");
        Response res = members.listingMemberWithInvalidKey();

        AllureLogger.logToAllure("checking status code, statusLine, contentType and response time" );
        res.then()
                //.log().all()
                .assertThat().statusCode(401)
                .statusLine("HTTP/1.1 401 Unauthorized")
                .contentType("text/plain; charset=utf-8")
                .time(lessThan((long)1500));

        AllureLogger.logToAllure("checking response body" );
        Assert.assertEquals(res.body().asString(),"invalid key");

        AllureLogger.logToAllure("test passed" );
    }
}

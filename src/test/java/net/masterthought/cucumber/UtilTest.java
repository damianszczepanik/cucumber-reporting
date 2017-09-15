package net.masterthought.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import net.masterthought.cucumber.json.*;
import net.masterthought.cucumber.json.support.Resultsable;
import net.masterthought.cucumber.json.support.Status;
import org.junit.Assert;
import org.junit.Test;

import net.masterthought.cucumber.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class UtilTest extends ReportGenerator {

    @Test
    public void formatAsPercentage_ReturnsFormatedValue() {

        // given
        final int[][] values = {{1, 3}, {2, 2}, {1, 5}, {0, 5}};
        String[] formatted = {"33.33%", "100.00%", "20.00%", "0.00%"};

        // then
        for (int i = 0; i < values.length; i++) {
            assertThat(Util.formatAsPercentage(values[i][0], values[i][1])).isEqualTo(formatted[i]);
        }
    }

    @Test
    public void formatAsPercentage_OnZeroTotal_ReturnsFormattedValue() {

        // given
        final int[] values = {1, 2, 0};

        // then
        for(int value : values) {
            assertThat(Util.formatAsPercentage(value, 0)).isEqualTo("0.00%");
        }
    }

    @Test
    public void formatAsDecimal_ReturnsFormattedValue() {

        // given
        final int[][] values = {{1, 3}, {2, 2}, {1, 5}, {0, 5}, {0, 0}};
        String[] formatted = {"33.33", "100.00", "20.00", "0.00", "0.00"};

        // then
        for (int i = 0; i < values.length; i++) {
            assertThat(Util.formatAsDecimal(values[i][0], values[i][1])).isEqualTo(formatted[i]);
        }
    }

    @Test
    public void getFailedCauseList_ReturnsFailedCauseList() throws Exception {
        setUpWithJson(SAMPLE_JSON);
        Element[] elements = this.features.get(1).getElements();
        List<String[]> expectedFailedCauseList = new ArrayList<>();
        expectedFailedCauseList.add(new String[] {
                "Account may not have sufficient funds",
                "MachineFactory.wait()",
                "0-hook-1500995314",
                "Error message not found."
        });
        expectedFailedCauseList.add(new String[] {
                "Account may not have sufficient funds",
                "the card is valid",
                "0-step-15",
                "Error message not found."
        });
        List<String[]> failedCauseList = Util.getFailedCauseList(elements);
        assertThat(failedCauseList).
                containsExactly(expectedFailedCauseList.toArray(new String[expectedFailedCauseList.size()][4]));
    }

    @Test
    public void getFailedCauseList_ReturnsEmptyList() throws Exception {
        setUpWithJson(SAMPLE_JSON);
        Element[] elements = this.features.get(0).getElements();
        List<String[]> failedCauseList = Util.getFailedCauseList(elements);
        assertThat(failedCauseList).containsExactly();
    }

    @Test
    public void getFailedCauseList_ReturnsEmptyList_NotScenarioType() throws Exception {
        setUpWithJson(SAMPLE_JSON);
        Element element = this.features.get(0).getElements()[0];
        Element[] elements = new Element[] {element};
        List<String[]> failedCauseList = Util.getFailedCauseList(elements);
        assertThat(failedCauseList).containsExactly();
    }

    @Test
    public void getFailedCauseList_ReturnsEmptyList_ScenarioPassed() throws Exception {
        setUpWithJson(SAMPLE_JSON);
        Element element = this.features.get(0).getElements()[1];
        Element[] elements = new Element[] {element};
        List<String[]> failedCauseList = Util.getFailedCauseList(elements);
        assertThat(failedCauseList).containsExactly();
    }

    @Test
    public void getFailedCauseList_ReturnsFailedCauseList_WithNonDefaultErrorMessage() throws Exception {
        setUpWithJson(SAMPLE_JSON);
        Element[] elements = this.features.get(1).getElements();
        Result result = elements[0].getBefore()[1].getResult();
        TestUtils.setFieldViaReflection("errorMessage", "this hook failed", result);
        result = elements[0].getSteps()[8].getResult();
        TestUtils.setFieldViaReflection("errorMessage", "this step failed", result);
        List<String[]> expectedFailedCauseList = new ArrayList<>();
        expectedFailedCauseList.add(new String[] {
                "Account may not have sufficient funds",
                "MachineFactory.wait()",
                "0-hook-1500995314",
                "this hook failed"
        });
        expectedFailedCauseList.add(new String[] {
                "Account may not have sufficient funds",
                "the card is valid",
                "0-step-15",
                "this step failed"
        });
        List<String[]> failedCauseList = Util.getFailedCauseList(elements);
        assertThat(failedCauseList).
                containsExactly(expectedFailedCauseList.toArray(new String[expectedFailedCauseList.size()][4]));
    }

    @Test
    public void joinResultables_ReturnsJoinedResultablesArray() throws Exception {
        setUpWithJson(SAMPLE_JSON);
        Element[] elements = this.features.get(1).getElements();
        Resultsable[] expectedResultables = new Resultsable[13];
        Hook hook = new Hook();
        TestUtils.setFieldViaReflection("id", "0-hook-1528844036", hook);
        Result result = new Result();
        TestUtils.setFieldViaReflection("status", Status.PASSED, result);
        TestUtils.setFieldViaReflection("duration", 10744700L, result);
        TestUtils.setFieldViaReflection("result", result, hook);
        Match match = new Match();
        TestUtils.setFieldViaReflection("location", "MachineFactory.findCachMachine()", match);
        TestUtils.setFieldViaReflection("match", match, hook);

        expectedResultables[0] = hook;

        hook = new Hook();
        TestUtils.setFieldViaReflection("id", "0-hook-1500995314", hook);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.FAILED, result);
        TestUtils.setFieldViaReflection("duration", 1000001L, result);
        TestUtils.setFieldViaReflection("result", result, hook);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "MachineFactory.wait()", match);
        TestUtils.setFieldViaReflection("match", match, hook);

        expectedResultables[1] = hook;

        Step step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-7", step);
        TestUtils.setFieldViaReflection("name", "the account balance is 100", step);
        TestUtils.setFieldViaReflection("keyword", "Given ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.UNDEFINED, result);
        TestUtils.setFieldViaReflection("duration", 0L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("match", match, step);
        TestUtils.setFieldViaReflection("line", 7, step);

        expectedResultables[2] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-8", step);
        TestUtils.setFieldViaReflection("name", "the card is valid", step);
        TestUtils.setFieldViaReflection("keyword", "And ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.PASSED, result);
        TestUtils.setFieldViaReflection("duration", 13000L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.createCreditCard()", match);
        TestUtils.setFieldViaReflection("match", match, step);
        TestUtils.setFieldViaReflection("line", 8, step);

        expectedResultables[3] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-9", step);
        TestUtils.setFieldViaReflection("name", "the machine contains 100", step);
        TestUtils.setFieldViaReflection("keyword", "And ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.PASSED, result);
        TestUtils.setFieldViaReflection("duration", 36000L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.createATM(int)", match);
        TestUtils.setFieldViaReflection("match", match, step);
        TestUtils.setFieldViaReflection("line", 9, step);

        expectedResultables[4] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-10", step);
        TestUtils.setFieldViaReflection("name", "the Account Holder requests 20", step);
        TestUtils.setFieldViaReflection("keyword", "When ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.PASSED, result);
        TestUtils.setFieldViaReflection("duration", 32000L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.requestMoney(int)", match);
        TestUtils.setFieldViaReflection("match", match, step);
        TestUtils.setFieldViaReflection("line", 10, step);

        expectedResultables[5] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-11", step);
        TestUtils.setFieldViaReflection("name", "the ATM should dispense 20", step);
        TestUtils.setFieldViaReflection("keyword", "Then ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.PASSED, result);
        TestUtils.setFieldViaReflection("duration", 36000L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.checkMoney(int)", match);
        TestUtils.setFieldViaReflection("match", match, step);
        TestUtils.setFieldViaReflection("line", 11, step);

        expectedResultables[6] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-12", step);
        TestUtils.setFieldViaReflection("name", "the account balance should be 90", step);
        TestUtils.setFieldViaReflection("keyword", "And ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.SKIPPED, result);
        TestUtils.setFieldViaReflection("errorMessage", "java.lang.AssertionError: \n" +
                "Expected: is <80>\n" +
                "     got: <90>\n" +
                "\n" +
                "\tat org.junit.Assert.assertThat(Assert.java:780)\n" +
                "\tat org.junit.Assert.assertThat(Assert.java:738)\n" +
                "\tat net.masterthought.example.ATMScenario.checkBalance(ATMScenario.java:69)\n" +
                "\tat ✽.And the account balance should be 90(net/masterthought/example/ATMK.feature:12)\n", result);
        TestUtils.setFieldViaReflection("duration", 1933000L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.checkBalance(int)", match);
        TestUtils.setFieldViaReflection("match", match, step);
        Embedding[] embeddings = new Embedding[6];
        embeddings[0] = new Embedding("image/png", "iVBORw0KGgoAAAANSUhEUgAAACwAAAE+CAIAAAB5j2VyAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAEVSURBVHhe7c6hAYAwEMDAhzkqu/9m7ICAzFBzZ2JzPWvPafffo0zEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzEREzExGfmBTSnA6klpvCvAAAAAElFTkSuQmCC");
        embeddings[1] = new Embedding("image/jpeg", "/9j/4AAQSkZJRgABAQEAYABgAAD/4QBaRXhpZgAATU0AKgAAAAgABQMBAAUAAAABAAAASgMDAAEAAAABAAAAAFEQAAEAAAABAQAAAFERAAQAAAABAAAOw1ESAAQAAAABAAAOwwAAAAAAAYagAACxj//bAEMAAgEBAgEBAgICAgICAgIDBQMDAwMDBgQEAwUHBgcHBwYHBwgJCwkICAoIBwcKDQoKCwwMDAwHCQ4PDQwOCwwMDP/bAEMBAgICAwMDBgMDBgwIBwgMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDAwMDP/AABEIADUBngMBIgACEQEDEQH/xAAfAAABBQEBAQEBAQAAAAAAAAAAAQIDBAUGBwgJCgv/xAC1EAACAQMDAgQDBQUEBAAAAX0BAgMABBEFEiExQQYTUWEHInEUMoGRoQgjQrHBFVLR8CQzYnKCCQoWFxgZGiUmJygpKjQ1Njc4OTpDREVGR0hJSlNUVVZXWFlaY2RlZmdoaWpzdHV2d3h5eoOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4eLj5OXm5+jp6vHy8/T19vf4+fr/xAAfAQADAQEBAQEBAQEBAAAAAAAAAQIDBAUGBwgJCgv/xAC1EQACAQIEBAMEBwUEBAABAncAAQIDEQQFITEGEkFRB2FxEyIygQgUQpGhscEJIzNS8BVictEKFiQ04SXxFxgZGiYnKCkqNTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqCg4SFhoeIiYqSk5SVlpeYmZqio6Slpqeoqaqys7S1tre4ubrCw8TFxsfIycrS09TV1tfY2dri4+Tl5ufo6ery8/T19vf4+fr/2gAMAwEAAhEDEQA/AMeiiiv74P8AKMKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAP/9k=");
        embeddings[2] = new Embedding("text/plain", "amF2YS5sYW5nLlRocm93YWJsZQ==");
        embeddings[3] = new Embedding("text/html", "PGh0bWw+PGhlYWQ+PC9oZWFkPjxib2R5PjxwPjxpPkhlbGxvPC9pPiA8Yj5Xb3JsZCE8L2I+PC9wPjxwPlRoZSB3aWtpcGVkaWEgbG9nbyBzaG91bGQgYXBwZWFyIGJlbG93PC9wPjxpbWcgc3JjPSJodHRwczovL2VuLndpa2lwZWRpYS5vcmcvc3RhdGljL2ltYWdlcy9wcm9qZWN0LWxvZ29zL2Vud2lraS5wbmciPjwvYm9keT48L2h0bWw+");
        embeddings[4] = new Embedding("text/xml", "PD94bWwgdmVyc2lvbj0iMS4wIj8+DQo8eG1sPg0KICA8c29tZU5vZGUgYXR0cj0idmFsdWUiIC8+DQo8L3htbD4=");
        embeddings[5] = new Embedding("js", "ZnVuY3Rpb24gbG9nZ2VyKG1lc3NhZ2UpIHsNCiAgY29uc29sZS5sb2cobWVzc2FnZSk7DQp9");
        TestUtils.setFieldViaReflection("embeddings", embeddings, step);
        TestUtils.setFieldViaReflection("line", 12, step);

        expectedResultables[7] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-13", step);
        TestUtils.setFieldViaReflection("name", "the card should be returned", step);
        TestUtils.setFieldViaReflection("keyword", "And ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.PENDING, result);
        TestUtils.setFieldViaReflection("duration", 0L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.cardShouldBeReturned()", match);
        TestUtils.setFieldViaReflection("match", match, step);
        embeddings = new Embedding[1];
        embeddings[0] = new Embedding("application/json", "ew0KICAibW9kZWwiIDogIjEzNGIyIiwNCiAgInByaWNlIiA6IHsNCiAgICAidmFsdWUiIDogMTAwMDAwLA0KICAgICJjdXJyZW5jeSIgOiAiJCINCiAgfSwNCiAgIm5vdGVzIiA6IG51bGwNCn0=");
        TestUtils.setFieldViaReflection("embeddings", embeddings, step);
        TestUtils.setFieldViaReflection("line", 13, step);

        expectedResultables[8] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-14", step);
        TestUtils.setFieldViaReflection("name", "its not implemented", step);
        TestUtils.setFieldViaReflection("keyword", "And ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.SKIPPED, result);
        TestUtils.setFieldViaReflection("duration", 0L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.its_not_implemented()", match);
        TestUtils.setFieldViaReflection("match", match, step);
        Output[] outputs = new Output[2];
        outputs[0] = new Output(new String[] {"Could not connect to the server @Rocky@"});
        outputs[1] = new Output(new String[] {"Could not connect to the server @Mike@"});
        TestUtils.setFieldViaReflection("outputs", outputs, step);
        TestUtils.setFieldViaReflection("line", 14, step);

        expectedResultables[9] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-15", step);
        TestUtils.setFieldViaReflection("name", "the card is valid", step);
        TestUtils.setFieldViaReflection("keyword", "And ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.FAILED, result);
        TestUtils.setFieldViaReflection("duration", 0L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.createCreditCard()", match);
        TestUtils.setFieldViaReflection("match", match, step);
        outputs = new Output[1];
        outputs[0] = new Output(new String[] {"Checkpoints", "232"});
        TestUtils.setFieldViaReflection("outputs", outputs, step);
        TestUtils.setFieldViaReflection("line", 15, step);

        expectedResultables[10] = step;

        step = new Step();
        TestUtils.setFieldViaReflection("id", "0-step-29", step);
        TestUtils.setFieldViaReflection("name", "the card should be returned", step);
        TestUtils.setFieldViaReflection("keyword", "And ", step);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.UNDEFINED, result);
        TestUtils.setFieldViaReflection("duration", 90000000L, result);
        TestUtils.setFieldViaReflection("result", result, step);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "ATMScenario.cardShouldBeReturned()", match);
        TestUtils.setFieldViaReflection("match", match, step);
        TestUtils.setFieldViaReflection("line", 29, step);

        expectedResultables[11] = step;

        hook = new Hook();
        TestUtils.setFieldViaReflection("id", "0-hook-731490842", hook);
        result = new Result();
        TestUtils.setFieldViaReflection("status", Status.UNDEFINED, result);
        TestUtils.setFieldViaReflection("errorMessage", "Undefined step", result);
        TestUtils.setFieldViaReflection("duration", 64700000L, result);
        TestUtils.setFieldViaReflection("result", result, hook);
        match = new Match();
        TestUtils.setFieldViaReflection("location", "any.error()", match);
        TestUtils.setFieldViaReflection("match", match, hook);
        embeddings = new Embedding[1];
        embeddings[0] = new Embedding("image/png", "iVBORw0KGgoAAAANSUhEUgAAAHgAAAB2CAIAAACMDMD1AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAMoSURBVHhe7dZBbttAEETRHCTL3CPbHD5XyRmcRlgRhDI57LHI6qZYH7XzNAW8lb99OEmGFmVoUYYWZWhRhhZlaFGGFmVoUYYWZWhRhhZlaFGGFmVoUYYWZWhRhhb1KvSf7z8GwyNnaFnnQsfw7vadDh3D03tnaFEK6Bhe3zgRdAwHd83Qol6FXiLTreH1LZNCx3Bwv46Bjgh0MBzcrMOgIwLdGl7frALoZbi5TUdCR6S5O5yd1s9fv7eGF6oOho6Icnc4OzQyXR2eqqqHjuHy5YhyPNyoOh46IsfMcPnVCDEzXKo6BToix8xwORnx5Yd7VWdBR+SYHI7TEV9+uFfVDjqG+70Ibnb4iqoToZcIMTkcDyO42eErqk6HXiLH/HC/FsFNDZ8QJoKOSDA/3D9FavnhviIddESC+eH+f8S3O5yVJoWOSDA/3E8q46ZBauiIBJPDcRoar9tUAL1EjpnFFWkOtvxKn8qgI3LcHVFuDV9vViV0RJSDkeZg+HSziqEfESuNKMfDF5vVBToi3MfIcTB8qGWNoCMijhHlePhKy3pBR2+pHDWFJsTMcN+1dtDElxyOG9cLmviSw3HvWkAT3NTwifbVQxPc1PCJK1QMTXBTwycuUiU0wU1t+ecEH7pCZdAEN7VF+VrWNdAEl98z8WP4aO/U0ASXH+HS8PXGSaHJLj9iXR1+o2utoZcrAh1sed8zHTQh7g5nM9Ax3PRLBE2Iu8PZv4hydzhrVkdo3DxFlLvDWacU0OQ4Hm4+RZTj4aZTp0OT43i42Yg0x8NNm7pA4/VepDkebnrUAhpPc5HmYDjo0fWgIwIdDAcNqofGu8kIdGt43aBiaDz6UmS6Ojxt0IWhI2JdHZ5WVwmNFy9EpqvD0+rKoPHnlyPWz8O76i4PHZEsDY+qOx1aEMnS8Ki6d4COCPd5eFGdoUUZWtSbQEfk+xj+XN37QDfP0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCSPj7+Av1TVHaIlvxNAAAAAElFTkSuQmCC");
        TestUtils.setFieldViaReflection("embeddings", embeddings, hook);

        expectedResultables[12] = hook;

        Resultsable[] resultables = Util.joinResultables(
                elements[0].getBefore(),
                elements[0].getSteps(),
                elements[0].getAfter());

//        assertThat(resultables).containsExactly(expectedResultables);
        Assert.assertArrayEquals(expectedResultables, resultables);
        //TODO
    }
}

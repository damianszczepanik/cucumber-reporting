package net.masterthought.cucumber.json;

import net.masterthought.cucumber.generators.integrations.PageTest;
import net.masterthought.cucumber.json.support.Status;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Sean Bucholtz (sean.bucholtz@gmail.com)
 */
public class HookTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getResult_ReturnsResult() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getBefore()[0];

        Result result = hook.getResult();

        assertTrue(result.getStatus().equals(Status.PASSED));
        assertNull(result.getErrorMessage());
        assertTrue(result.getDuration() == 10744700);
    }

    @Test
    public void getMatch_ReturnMatch() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getBefore()[0];

        Match match = hook.getMatch();

        assertTrue(match.getLocation().equals("MachineFactory.findCachMachine()"));
    }

    @Test
    public void getEmbeddings_ReturnEmbeddings() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getAfter()[0];

        Embedding[] embeddings = hook.getEmbeddings();

        assertTrue(embeddings[0].getMimeType().equals("image/png"));
        assertTrue(embeddings[0].getData().equals("iVBORw0KGgoAAAANSUhEUgAAAHgAAAB2CAIAAACMDMD1AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAMoSURBVHhe7dZBbttAEETRHCTL3CPbHD5XyRmcRlgRhDI57LHI6qZYH7XzNAW8lb99OEmGFmVoUYYWZWhRhhZlaFGGFmVoUYYWZWhRhhZlaFGGFmVoUYYWZWhRhhb1KvSf7z8GwyNnaFnnQsfw7vadDh3D03tnaFEK6Bhe3zgRdAwHd83Qol6FXiLTreH1LZNCx3Bwv46Bjgh0MBzcrMOgIwLdGl7frALoZbi5TUdCR6S5O5yd1s9fv7eGF6oOho6Icnc4OzQyXR2eqqqHjuHy5YhyPNyoOh46IsfMcPnVCDEzXKo6BToix8xwORnx5Yd7VWdBR+SYHI7TEV9+uFfVDjqG+70Ibnb4iqoToZcIMTkcDyO42eErqk6HXiLH/HC/FsFNDZ8QJoKOSDA/3D9FavnhviIddESC+eH+f8S3O5yVJoWOSDA/3E8q46ZBauiIBJPDcRoar9tUAL1EjpnFFWkOtvxKn8qgI3LcHVFuDV9vViV0RJSDkeZg+HSziqEfESuNKMfDF5vVBToi3MfIcTB8qGWNoCMijhHlePhKy3pBR2+pHDWFJsTMcN+1dtDElxyOG9cLmviSw3HvWkAT3NTwifbVQxPc1PCJK1QMTXBTwycuUiU0wU1t+ecEH7pCZdAEN7VF+VrWNdAEl98z8WP4aO/U0ASXH+HS8PXGSaHJLj9iXR1+o2utoZcrAh1sed8zHTQh7g5nM9Ax3PRLBE2Iu8PZv4hydzhrVkdo3DxFlLvDWacU0OQ4Hm4+RZTj4aZTp0OT43i42Yg0x8NNm7pA4/VepDkebnrUAhpPc5HmYDjo0fWgIwIdDAcNqofGu8kIdGt43aBiaDz6UmS6Ojxt0IWhI2JdHZ5WVwmNFy9EpqvD0+rKoPHnlyPWz8O76i4PHZEsDY+qOx1aEMnS8Ki6d4COCPd5eFGdoUUZWtSbQEfk+xj+XN37QDfP0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCSPj7+Av1TVHaIlvxNAAAAAElFTkSuQmCC"));
        assertTrue(embeddings[0].getFileId().equals("embedding_-503809177"));
    }

    @Test
    public void getId_ReturnId() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getBefore()[0];

        String id = hook.getId();

        assertEquals("0-hook-1528844036", id);
    }

    @Test
    public void setId_SetNewId() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getBefore()[0];

        hook.setId("test-id");

        assertEquals("test-id", hook.getId());
    }

    @Test
    public void generateId_ReturnGeneratedId() throws Exception {
        Element parentElement = this.features.get(1).getElements()[0];
        Hook hook = parentElement.getBefore()[0];
        assertEquals("0-hook-1528844036", hook.generateId(parentElement));
    }

    @Test
    public void getResultableName_ReturnResultableName() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getBefore()[0];
        assertEquals("MachineFactory.findCachMachine()", hook.getResultableName());
    }

    @Test
    public void hashCode_ReturnHashCode() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getBefore()[0];
        assertEquals(-1528844036, hook.hashCode());
    }

    @Test
    public void equals_ReturnTrueSameInstance() throws Exception {
        Hook hook1 = this.features.get(1).getElements()[0].getBefore()[0];
        Hook hook2 = this.features.get(1).getElements()[0].getBefore()[0];
        assertTrue(hook1.equals(hook2));
    }

    @Test
    public void equals_ReturnTrueSameValue() throws Exception {
        Hook hook1 = this.features.get(1).getElements()[0].getBefore()[0];

        Hook hook2 = new Hook();
        hook2.setId("0-hook-1528844036");
        // hook2.result initialization
        Result result = new Result();
        TestUtils.setFieldViaReflection("status", Status.PASSED, result);
        TestUtils.setFieldViaReflection("duration", 10744700l, result);
        TestUtils.setFieldViaReflection("result", result, hook2);
        // hook2.match initialization
        Match match = new Match();
        TestUtils.setFieldViaReflection("location", "MachineFactory.findCachMachine()", match);
        TestUtils.setFieldViaReflection("match", match, hook2);

        assertTrue(hook1.equals(hook2));
    }

    @Test
    public void equals_ReturnFalseNotSameValue() throws Exception {
        Hook hook1 = this.features.get(1).getElements()[0].getBefore()[0];

        Hook hook2 = new Hook();
        hook2.setId("0-hook-1526844036");
        // hook2.result initialization
        Result result = new Result();
        TestUtils.setFieldViaReflection("status", Status.FAILED, result);
        TestUtils.setFieldViaReflection("duration", 10244700l, result);
        TestUtils.setFieldViaReflection("result", result, hook2);
        // hook2.match initialization
        Match match = new Match();
        TestUtils.setFieldViaReflection("location", "MachineFactory.findCandyMachine()", match);
        TestUtils.setFieldViaReflection("match", match, hook2);

        assertFalse(hook1.equals(hook2));
    }

    @Test
    public void equals_ReturnFalseNotAnInstanceOf() throws Exception {
        Hook hook = this.features.get(1).getElements()[0].getBefore()[0];
        assertFalse(hook.equals(new Step()));
    }

}
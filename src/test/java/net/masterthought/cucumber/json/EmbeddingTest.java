package net.masterthought.cucumber.json;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import net.masterthought.cucumber.generators.integrations.PageTest;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
public class EmbeddingTest extends PageTest {

    @Before
    public void setUp() {
        setUpWithJson(SAMPLE_JSON);
    }

    @Test
    public void getMimeType_ReturnsMimeType() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[0];

        // when
        String mimeType = embedding.getMimeType();

        // then
        assertThat(mimeType).isEqualTo("image/png");
    }

    @Test
    public void getData_ReturnsContent() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[2];

        // when
        String content = embedding.getData();

        // then
        assertThat(content).isEqualTo("amF2YS5sYW5nLlRocm93YWJsZQ==");
    }

    @Test
    public void getDecodedData_ReturnsDecodedContent() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[3];

        // when
        String content = embedding.getDecodedData();

        // then
        assertThat(content).isEqualTo("<i>Hello</i> <b>World!</b>");
    }

    @Test
    public void getFileName_ReturnsFileName() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[3];

        // when
        String fileName = embedding.getFileName();

        // then
        assertThat(fileName).isEqualTo("embedding_1947030670.html");
    }

    @Test
    public void getDecodedData__OnRuby_ForAfterHook_ReturnsContent() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getAfter()[0].getEmbeddings()[0];

        // when
        String content = embedding.getData();

        // then
        assertThat(content).isEqualTo("iVBORw0KGgoAAAANSUhEUgAAAHgAAAB2CAIAAACMDMD1AAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAMoSURBVHhe7dZBbttAEETRHCTL3CPbHD5XyRmcRlgRhDI57LHI6qZYH7XzNAW8lb99OEmGFmVoUYYWZWhRhhZlaFGGFmVoUYYWZWhRhhZlaFGGFmVoUYYWZWhRhhb1KvSf7z8GwyNnaFnnQsfw7vadDh3D03tnaFEK6Bhe3zgRdAwHd83Qol6FXiLTreH1LZNCx3Bwv46Bjgh0MBzcrMOgIwLdGl7frALoZbi5TUdCR6S5O5yd1s9fv7eGF6oOho6Icnc4OzQyXR2eqqqHjuHy5YhyPNyoOh46IsfMcPnVCDEzXKo6BToix8xwORnx5Yd7VWdBR+SYHI7TEV9+uFfVDjqG+70Ibnb4iqoToZcIMTkcDyO42eErqk6HXiLH/HC/FsFNDZ8QJoKOSDA/3D9FavnhviIddESC+eH+f8S3O5yVJoWOSDA/3E8q46ZBauiIBJPDcRoar9tUAL1EjpnFFWkOtvxKn8qgI3LcHVFuDV9vViV0RJSDkeZg+HSziqEfESuNKMfDF5vVBToi3MfIcTB8qGWNoCMijhHlePhKy3pBR2+pHDWFJsTMcN+1dtDElxyOG9cLmviSw3HvWkAT3NTwifbVQxPc1PCJK1QMTXBTwycuUiU0wU1t+ecEH7pCZdAEN7VF+VrWNdAEl98z8WP4aO/U0ASXH+HS8PXGSaHJLj9iXR1+o2utoZcrAh1sed8zHTQh7g5nM9Ax3PRLBE2Iu8PZv4hydzhrVkdo3DxFlLvDWacU0OQ4Hm4+RZTj4aZTp0OT43i42Yg0x8NNm7pA4/VepDkebnrUAhpPc5HmYDjo0fWgIwIdDAcNqofGu8kIdGt43aBiaDz6UmS6Ojxt0IWhI2JdHZ5WVwmNFy9EpqvD0+rKoPHnlyPWz8O76i4PHZEsDY+qOx1aEMnS8Ki6d4COCPd5eFGdoUUZWtSbQEfk+xj+XN37QDfP0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCiDC3K0KIMLcrQogwtytCSPj7+Av1TVHaIlvxNAAAAAElFTkSuQmCC");
    }

    @Test
    public void getExtension__OnCommonMimeType_ReturnsFileExtension() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[3];

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("html");
    }

    @Test
    public void getExtension__OnTextMimeType_ReturnsText() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[2];

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("txt");
    }

    @Test
    public void getExtension__OnImageUrlMimeType_ReturnsTxt() {

        // given
        Embedding embedding = features.get(0).getElements()[0].getSteps()[0].getEmbeddings()[0];

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("image");
    }

    @Test
    public void getExtension__OnOtherMimeType_ResurnsUnknown() {

        // given
        Embedding embedding = features.get(1).getElements()[0].getSteps()[5].getEmbeddings()[4];

        // when
        String extension = embedding.getExtension();

        // then
        assertThat(extension).isEqualTo("unknown");
    }
}

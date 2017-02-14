package net.masterthought.cucumber.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * @author Damian Szczepanik (damianszczepanik@github)
 */
@RunWith(Parameterized.class)
public class UtilTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"simpleFile", "simpleFile"},
                {"file-dash", "file-dash"},
                {"東京", "-u6771-u4EAC"},
                {"żółć", "-u017C-u00F3-u0142-u0107"}
        });
    }

    @Parameterized.Parameter(value = 0)
    public String value;

    @Parameterized.Parameter(value = 1)
    public String fileName;

    @Test
    public void toValidFileName_RemovesInvalidChars() {

        // when
        String convertedFileName = Util.toValidFileName(value);

        // then
        assertThat(convertedFileName).isEqualTo(fileName);
    }
}

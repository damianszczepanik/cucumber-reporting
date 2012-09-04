package net.masterthought.cucumber;

import net.masterthought.cucumber.json.Artifact;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ArtifactProcessorTest {

    @Test
    public void validConfigurationShouldReturnMap() throws Exception {
        String configuration = "Account has sufficient funds again~the account balance is 300~account~evidence/account_balance.txt";
        ArtifactProcessor artifactProcessor = new ArtifactProcessor(configuration);
        Map<String,Artifact> map = artifactProcessor.process();
        Artifact artifact = map.get("Account has sufficient funds againthe account balance is 300");
        assertThat(artifact.getScenario(),is("Account has sufficient funds again"));
        assertThat(artifact.getStep(),is("the account balance is 300"));
        assertThat(artifact.getKeyword(),is("account"));
        assertThat(artifact.getArtifactFile(),is("evidence/account_balance.txt"));
    }

}

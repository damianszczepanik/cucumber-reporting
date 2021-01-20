package net.masterthought.cucumber.reducers;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonList;
import static net.masterthought.cucumber.json.support.Status.FAILED;
import static net.masterthought.cucumber.json.support.Status.PASSED;
import static net.masterthought.cucumber.reducers.ReducingMethod.MERGE_FEATURES_WITH_RETEST_MARKING_FLACKY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Embedding;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Hook;
import net.masterthought.cucumber.json.Match;
import net.masterthought.cucumber.json.Result;
import net.masterthought.cucumber.json.Step;
import net.masterthought.cucumber.json.Tag;
import net.masterthought.cucumber.json.support.Resultsable;
import net.masterthought.cucumber.json.support.Status;
import org.junit.Test;
import org.powermock.api.support.membermodification.MemberModifier;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

public class ReportFeatureWithRetestMarkingFlackyMergerTest {

    private ReportFeatureWithRetestMarkingFlackyMerger merger = new ReportFeatureWithRetestMarkingFlackyMerger();

    private Element buildElement(String id) {
        try {
            Element result = new Element();
            MemberModifier.field(Element.class, "id").set(result, id);
            MemberModifier.field(Element.class, "type").set(result, "scenario");
            MemberModifier.field(Element.class, "startTime").set(result, LocalDateTime.now());
            return result;
        }
        catch (IllegalAccessException e) {
            return null;
        }
    }

    private Step buildStep(String location, Status status) {
        try {
            Step step = new Step();

            Result result = new Result();
            MemberModifier.field(Step.class, "result").set(step, result);
            MemberModifier.field(Result.class, "status").set(result, status);

            Match match = new Match();
            MemberModifier.field(Step.class, "match").set(step, match);
            MemberModifier.field(Match.class, "location").set(match, location);

            MemberModifier.field(Step.class, "before").set(step,
                    new Hook[]{buildHook("beforeHook", PASSED)});
            MemberModifier.field(Step.class, "after").set(step,
                    new Hook[]{buildHook("afterHook", PASSED)});
            return step;
        }
        catch (IllegalAccessException e) {
            return null;
        }
    }

    private Step buildFailedStep() {
        Step step = buildStep("failedStep", FAILED);
        Result result = new Result();
        try {
            MemberModifier.field(Step.class, "result").set(step, result);
            MemberModifier.field(Result.class, "status").set(result, FAILED);
            MemberModifier.field(Result.class, "errorMessage").set(result, "exception");
        }
        catch (IllegalAccessException e) {
            return null;
        }
        return step;
    }

    private Hook buildHook(String location, Status status) {
        Hook hook = new Hook();
        Result result = new Result();
        try {
            MemberModifier.field(Hook.class, "result").set(hook, result);
            MemberModifier.field(Result.class, "status").set(result, status);

            Match match = new Match();
            MemberModifier.field(Hook.class, "match").set(hook, match);
            MemberModifier.field(Match.class, "location").set(match, location);
        }
        catch (IllegalAccessException e) {
            return null;
        }
        return hook;
    }

    private Hook buildFailedHook() {
        Hook hook = buildHook("failedHook", FAILED);
        Result result = new Result();
        try {
            MemberModifier.field(Hook.class, "result").set(hook, result);
            MemberModifier.field(Result.class, "status").set(result, FAILED);
            MemberModifier.field(Result.class, "errorMessage").set(result, "exception");
        }
        catch (IllegalAccessException e) {
            return null;
        }
        return hook;
    }

    private Hook buildHookWithEmbedding() {
        Hook hook = buildHook("hookWithEmbedding", PASSED);
        Embedding[] embeddings = new Embedding[]{new Embedding("mime/type", "your data")};
        try {
            MemberModifier.field(Hook.class, "embeddings").set(hook, embeddings);
        }
        catch (IllegalAccessException e) {
            return null;
        }
        return hook;
    }

    private Element buildScenario() {
        return buildElement(UUID.randomUUID().toString());
    }

    private Element buildScenario(boolean failStep, boolean failHook) {
        Element element = buildScenario();
        Step[] steps = {
                buildStep("firstStep", PASSED),
                buildStep("secondStep", PASSED),
                buildStep("thirdStep", PASSED),
                failStep ? buildFailedStep() : buildStep("failedStep", PASSED)};

        try {
            MemberModifier.field(Element.class, "steps").set(element, steps);
            MemberModifier.field(Element.class, "before").set(element,
                    new Hook[]{buildHook("beforeHook", PASSED),
                            failHook ? buildFailedHook() : buildHook("beforeHook", PASSED)});
            MemberModifier.field(Element.class, "after").set(element,
                    new Hook[]{buildHook("afterHook", PASSED),
                            failStep ? buildHookWithEmbedding() : buildHook("afterHook", PASSED)});
        }
        catch (IllegalAccessException e) {
            return null;
        }
        return element;
    }

    @Test
    public void test_ifFlacky() {
        // given
        Element element = buildScenario(TRUE, FALSE);
        Element newElement = buildScenario(FALSE, FALSE);

        // when
        boolean result = merger.ifFlacky(element, newElement);

        // then
        assertThat(result).isTrue();
    }

    @Test
    public void test_isStatus() {
        // given
        Element element = buildScenario(TRUE, FALSE);
        Element newElement = buildScenario(FALSE, FALSE);

        // when
        boolean result = element.isStatus(FAILED);
        boolean result1 = newElement.isStatus(PASSED);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(result).isTrue();
            softAssertions.assertThat(result1).isTrue();
        });
    }

    @Test
    public void test_addFlackyTag() {
        // given
        Element element = buildScenario();

        // when
        merger.addFlackyTag(element);

        // then
        assertThat(element.getTags()).contains(new Tag("@flacky"));
    }

    @Test
    public void test_mergeResultsable() {
        // given
        Element element = buildScenario(FALSE, FALSE);

        // when
        Stream<Resultsable> resultsableStream = merger.mergeResultsable(element);

        // then
        assertThat(resultsableStream).hasSize(16);
    }

    @Test
    public void test_getFailedResultsable() {
        // given
        Step step = buildStep("secondStep", FAILED);

        // when
        Resultsable failedResultsable = merger.getFailedResultsable(Stream.of(step));

        // then
        assertThat(failedResultsable).isNotNull();
    }

    @Test
    public void test_setEmbeddingsInAfterHookIfPresent() {
        // given
        Element element = buildScenario(TRUE, FALSE);
        Element newElement = buildScenario(FALSE, FALSE);

        // when
        merger.setEmbeddingsInAfterHookIfPresent(element, newElement, element.getSteps()[0]);

        // then
        assertThat(newElement.getSteps()[0].getEmbeddings()).isNotEmpty();
    }

    @Test
    public void test_addExceptionWithEmbeddingsFailedStep() {
        // given
        Element element = buildScenario(TRUE, FALSE);
        Element newElement = buildScenario(FALSE, FALSE);

        // when
        merger.addExceptionWithEmbeddings(element, newElement);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(newElement.getSteps()[3].getResult().getErrorMessage()).isNotEmpty();
            softAssertions.assertThat(newElement.getSteps()[3].hasEmbeddings()).isTrue();
        });

    }

    @Test
    public void test_addExceptionWithEmbeddingsFailedHook() {
        // given
        Element element = buildScenario(TRUE, FALSE);
        Element newElement = buildScenario(TRUE, FALSE);

        // when
        merger.addExceptionWithEmbeddings(element, newElement);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(newElement.getSteps()[3].getResult().getErrorMessage()).isNotEmpty();
            softAssertions.assertThat(newElement.getSteps()[3].hasEmbeddings()).isTrue();
        });

    }

    @Test
    public void test_replaceIfFlacky() throws IllegalAccessException {
        // given
        Element[] elements = {buildScenario(TRUE, FALSE)};

        Feature feature = new Feature();
        MemberModifier
                .field(Feature.class, "elements")
                .set(feature, elements);

        Element newElement = buildScenario(FALSE, FALSE);

        // when
        merger.replace(feature, elements, 0, newElement, 0, FALSE);

        // then
        assertSoftly(softAssertions -> {
            softAssertions.assertThat(newElement.getSteps()[3].getResult().getErrorMessage()).isNotEmpty();
            softAssertions.assertThat(newElement.getSteps()[3].hasEmbeddings()).isTrue();
            softAssertions.assertThat(newElement.getTags()).contains(new Tag("@flacky"));
            softAssertions.assertThat(elements[0].getStatus().isPassed()).isTrue();
            softAssertions.assertThat(elements[0].getSteps()[3].getResult().getErrorMessage()).isNotEmpty();
            softAssertions.assertThat(elements[0].getSteps()[3].hasEmbeddings()).isTrue();
            softAssertions.assertThat(elements[0].getTags()).contains(new Tag("@flacky"));
        });

    }

    @Test
    public void test_setErrorMessage() {
        // given
        Step step = buildFailedStep();
        Step secondStep = buildStep("failedStep", PASSED);

        // when
        merger.setErrorMessage(step, Stream.of(secondStep));

        // then
        assertThat(secondStep.getResult().getErrorMessage()).isEqualTo("exception");
    }

    @Test
    public void test_getStepWithHooks() {
        // given
        Element element = buildScenario(FALSE, FALSE);

        // when
        Stream<Resultsable> stepWithHooks = merger.getStepsWithStepHooks(element);

        // then
        assertThat(stepWithHooks).hasSize(12);
    }

    @Test
    public void check_MergerIsApplicableByType_NullParam() {
        // given
        // when
        boolean isApplicable = merger.test(null);

        // then
        assertThat(isApplicable).isFalse();
    }

    @Test
    public void check_MergerIsApplicableByType_CorrectParam() {
        // given
        // when
        boolean isApplicableByType = merger.test(singletonList(MERGE_FEATURES_WITH_RETEST_MARKING_FLACKY));

        // then
        assertThat(isApplicableByType).isTrue();
    }
}

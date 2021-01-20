package net.masterthought.cucumber.reducers;

import static java.util.Arrays.stream;
import static java.util.stream.Stream.concat;
import static net.masterthought.cucumber.json.support.Status.FAILED;
import static net.masterthought.cucumber.json.support.Status.PASSED;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.ArrayUtils.addAll;

import net.masterthought.cucumber.json.Element;
import net.masterthought.cucumber.json.Feature;
import net.masterthought.cucumber.json.Hook;
import net.masterthought.cucumber.json.Tag;
import net.masterthought.cucumber.json.support.Resultsable;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Merge list of given features in the same way as ReportFeatureWithRetestMerger plus marking flacky tests
 * (that were failed and after rerun are passed) not failing the last one and moves exception and attachments from
 * previously failed scenario for better analyzing
 * <p>
 * Uses when need to generate a report with rerun results analyzing flacky tests.
 */
final class ReportFeatureWithRetestMarkingFlackyMerger extends ReportFeatureWithRetestMerger {

    @Override
    public boolean test(List<ReducingMethod> reducingMethods) {
        return reducingMethods != null
                && reducingMethods.contains(ReducingMethod.MERGE_FEATURES_WITH_RETEST_MARKING_FLACKY);
    }

    @Override
    protected void replace(Feature feature, Element[] elements, int i, Element current, int indexOfPreviousResult,
                           boolean hasBackground) {
        if (ifFlacky(feature.getElements()[indexOfPreviousResult], current)) {
            addFlackyTag(current);
            addExceptionWithEmbeddings(feature.getElements()[indexOfPreviousResult], current);
        }
        super.replace(feature, elements, i, current, indexOfPreviousResult, hasBackground);

    }

    boolean ifFlacky(Element target, Element candidate) {
        return target.isStatus(FAILED) && candidate.isStatus(PASSED);
    }

    void addFlackyTag(Element candidate) {
        Tag[] tags = add(candidate.getTags(), new Tag("@flacky"));
        candidate.setTags(tags);
    }

    void addExceptionWithEmbeddings(Element target, Element candidate) {
        Resultsable targetFailed = getFailedResultsable(mergeResultsable(target));
        setErrorMessage(targetFailed, mergeResultsable(candidate));
        setEmbeddingsInAfterHookIfPresent(target, candidate, targetFailed);
    }

    void setErrorMessage(Resultsable targetFailed, Stream<Resultsable> candidateResultsableStream) {
        candidateResultsableStream.filter(resultsable -> resultsable.getMatch().getLocation()
                .equals(targetFailed.getMatch().getLocation()))
                .findFirst().ifPresent(step
                -> step.getResult().setErrorMessage(targetFailed.getResult().getErrorMessage()));
    }

    Resultsable getFailedResultsable(Stream<Resultsable> resultsableStream) {
        return resultsableStream.filter(resultsable -> resultsable.getResult().getStatus() == FAILED)
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No hook or step with failed status was found"));
    }

    Stream<Resultsable> mergeResultsable(Element element) {
        Stream<Hook> candidateHooks = concat(stream(element.getBefore()), stream(element.getAfter()));
        return concat(candidateHooks, getStepsWithStepHooks(element));
    }

    Stream<Resultsable> getStepsWithStepHooks(Element previous) {
        Stream<Resultsable> stream = stream(previous.getSteps());

        for (int i = 0; i <= previous.getSteps().length - 1; i++) {
            Resultsable[] before = previous.getSteps()[i].getBefore();
            Resultsable[] after = previous.getSteps()[i].getAfter();
            Stream<Resultsable> hooks = concat(stream(before), stream(after));
            stream = Stream.concat(stream, hooks);
        }
        return stream;
    }

    void setEmbeddingsInAfterHookIfPresent(Element previous, Element current, Resultsable previousFailed) {
        stream(previous.getAfter())
                .filter(Hook::hasEmbeddings)
                .findFirst().ifPresent(previousEmbeddedAfter ->
                stream(current.getSteps())
                        .filter(hook -> hook.getMatch().getLocation().equals(previousFailed.getMatch().getLocation()))
                        .findFirst().ifPresent(hook
                        -> hook.setEmbeddings(addAll(hook.getEmbeddings(), previousEmbeddedAfter.getEmbeddings()))));
    }

}

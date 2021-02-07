package artoffood.common.utils.background_tasks;

import artoffood.ArtOfFood;
import artoffood.common.utils.ConceptResultsCombinator;
import org.apache.logging.log4j.LogManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class BackgroundTasksManager {

    public static final BackgroundTasksManager shared = new BackgroundTasksManager();

    private ExecutorService conceptResultsExecutor = null;

    private BackgroundTasksManager() { }

    public void startConceptResultsCalculation(ConceptResultsCalculationInput input,
                                               Consumer<ConceptResultsCalculationOutput> callback) {
        ThreadGroup callbackGroup = Thread.currentThread().getThreadGroup();
        if (conceptResultsExecutor != null)
            conceptResultsExecutor.shutdownNow();

        conceptResultsExecutor = Executors.newSingleThreadExecutor();
        conceptResultsExecutor.submit(() -> {
            try {
                ConceptResultsCalculationOutput output = ConceptResultsCombinator.possibleResults(input);
                Thread callbackThread = new Thread(callbackGroup, () -> callback.accept(output));
                callbackThread.start();
            } catch (InterruptedException e) {
                LogManager.getLogger(ArtOfFood.MOD_ID).info("Concept results calculation interrupter");
            }
        });
    }

    public void cancelConceptResultsCalculation() {
        if (conceptResultsExecutor != null)
            conceptResultsExecutor.shutdownNow();
    }
}

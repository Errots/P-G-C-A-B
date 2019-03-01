
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public abstract class ThreadRecover extends Application {

    private Thread fxThread;

    @Override
    public void start(Stage stage) throws Exception {

        Platform.runLater(() -> {
            fxThread = Thread.currentThread();
        });
    }
}

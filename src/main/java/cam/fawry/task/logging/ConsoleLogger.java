package cam.fawry.task.logging;

public class ConsoleLogger implements Logger {
    @Override
    public void log(String line) {
        System.out.println(line);
    }
}

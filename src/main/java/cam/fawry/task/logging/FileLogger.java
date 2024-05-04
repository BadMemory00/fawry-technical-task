package cam.fawry.task.logging;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class FileLogger implements Logger {

    @Override
    public void log(String line) {
        try {
            Path filePath = Path.of("src/main/java/cam/fawry/task/logging/logs.txt");
            var lines = Files.readAllLines(filePath);
            lines.add(line);
            Files.writeString(filePath, String.join("\n", lines));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

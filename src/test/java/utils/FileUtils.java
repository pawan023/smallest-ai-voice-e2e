package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileUtils {

    private FileUtils() {
    }

    public static File saveAudioFile(byte[] audioBytes, String fileName) {
        if (audioBytes == null || audioBytes.length == 0) {
            throw new IllegalArgumentException(
                    "Audio bytes must not be null or empty"
            );
        }

        try {
            Path outputDirectory = Path.of("target", "test-output");
            Files.createDirectories(outputDirectory);
            Path outputPath = outputDirectory.resolve(fileName);

            // Writes the exact binary bytes returned by TTS
            Files.write(outputPath, audioBytes);
            File outputFile = outputPath.toFile();

            System.out.println("TTS output saved at: " + outputFile.getAbsolutePath());
            System.out.println("Saved audio size: " + outputFile.length() + " bytes");

            return outputFile;

        }
        catch (IOException exception) {
            throw new IllegalStateException("Failed to save TTS audio file", exception);
        }
    }
}
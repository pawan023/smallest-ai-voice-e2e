package clients;

import io.restassured.response.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class STTClient extends BaseClient {

    private static final String STT_ENDPOINT = "/waves/v1/stt/?model=pulse-pro&language=en";

    public String transcribeAudio(File audioFile) {

        validateAudioFile(audioFile);

        Response response =
                given()
                        .spec(requestSpecification)
                        .contentType("application/octet-stream")
                        .body(audioFile)
                        .log().all()
                        .when()
                        .post(STT_ENDPOINT)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .response();

        String status = response.jsonPath().getString("status");

        if (status != null) {
            assertTrue(
                    status.equalsIgnoreCase("success"),
                    "Expected STT status to be success, but received: " + status
            );
        }

        String transcription = response.jsonPath().getString("transcription");

        assertNotNull(
                transcription,
                "STT response should contain the transcription field"
        );

        assertFalse(
                transcription.isBlank(),
                "STT transcription should not be empty"
        );

        System.out.println("STT transcription: " + transcription);

        return transcription;
    }

    private void validateAudioFile(File audioFile) {

        assertNotNull(
                audioFile,
                "Audio file must not be null"
        );

        assertTrue(
                audioFile.exists(),
                "Audio file does not exist: " + audioFile.getAbsolutePath()
        );

        assertTrue(
                audioFile.isFile(),
                "Audio path is not a valid file: " + audioFile.getAbsolutePath()
        );

        assertTrue(
                audioFile.length() > 100,
                "Audio file is empty or too small: " + audioFile.length() + " bytes"
        );
    }

    private byte[] readAudioBytes(File audioFile) {
        try {
            return Files.readAllBytes(audioFile.toPath());

        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read audio file: " + audioFile.getAbsolutePath(), exception);
        }
    }
}
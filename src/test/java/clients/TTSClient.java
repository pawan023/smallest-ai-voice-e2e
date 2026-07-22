package clients;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class TTSClient extends BaseClient {

    private static final String TTS_ENDPOINT = "/waves/v1/tts";

    public byte[] generateSpeech(String voiceId, String text) {

        validateInput(voiceId, text);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("voice_id", voiceId);
        requestBody.put("text", text);
        requestBody.put("sample_rate", 24000);
        requestBody.put("speed", 1.0);
        requestBody.put("output_format", "mp3");
        requestBody.put("model", "lightning_v3.1_pro");


        Response response =
                given()
                        .spec(requestSpecification)
                        .contentType(ContentType.JSON)
                        .body(requestBody)
                        .log().all()
                        .when()
                        .post(TTS_ENDPOINT)
                        .then()
                        .log().headers()
                        .statusCode(200)
                        .extract()
                        .response();

        validateAudioResponse(response);

        byte[] audioBytes = response.asByteArray();

        assertTrue(
                audioBytes.length > 100,
                "TTS audio response is unexpectedly small: " + audioBytes.length + " bytes"
        );

        System.out.println(
                "TTS audio generated successfully. Audio size: " + audioBytes.length + " bytes"
        );

        return audioBytes;
    }

    private void validateAudioResponse(Response response) {

        String contentType = response.getContentType();

        assertNotNull(
                contentType,
                "TTS response Content-Type should not be null"
        );

        assertTrue(
                contentType.toLowerCase().contains("audio")
                        || contentType.toLowerCase().contains("application/octet-stream"),
                "Expected an audio response, but received Content-Type: " + contentType
        );

        byte[] audioBytes = response.asByteArray();
    }

    private void validateInput(String voiceId, String text) {

        assertNotNull(
                voiceId,
                "Voice ID must not be null"
        );

        assertTrue(
                !voiceId.isBlank(),
                "Voice ID must not be empty"
        );

        assertNotNull(
                text,
                "TTS input text must not be null"
        );

        assertTrue(
                !text.isBlank(),
                "TTS input text must not be empty"
        );
    }
}
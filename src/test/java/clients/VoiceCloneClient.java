package clients;

import io.restassured.response.Response;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class VoiceCloneClient extends BaseClient {

    private static final String VOICE_CLONING_ENDPOINT = "/waves/v1/voice-cloning";

    public String createVoiceClone(File audioFile, String displayName) {

        validateAudioFile(audioFile);

        Response response =
                given()
                        .spec(requestSpecification)
                        .multiPart("file", audioFile,getContentType(audioFile))
                        .multiPart("displayName", displayName)
                        .log().all()
                        .when()
                        .post(VOICE_CLONING_ENDPOINT)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .response();

        String voiceId = response.jsonPath().getString("data.voiceId");

        assertNotNull(
                voiceId,
                "Voice ID should be present in create voice clone response"
        );

        assertFalse(
                voiceId.isBlank(),
                "Voice ID should not be empty"
        );

        System.out.println("Created voice ID: " + voiceId);

        return voiceId;
    }

    //Fetches all voice clones and verifies that the newly createdvoice ID exists in the response.
    public void verifyVoiceCloneExists(String expectedVoiceId) {

        Response response =
                given()
                        .spec(requestSpecification)
                        .log().all()
                        .when()
                        .get(VOICE_CLONING_ENDPOINT)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .response();

        List<String> voiceIds = response.jsonPath().getList("data.voiceId", String.class);

        assertNotNull(
                voiceIds,
                "Voice ID list should not be null"
        );

        assertTrue(
                voiceIds.contains(expectedVoiceId),
                "Created voice ID was not found. Expected: " + expectedVoiceId + ", available voice IDs: " + voiceIds
        );

        System.out.println("Verified that voice clone exists: " + expectedVoiceId);
    }

    private void validateAudioFile(File audioFile) {

        assertNotNull(
                audioFile,
                "Reference audio file must not be null"
        );

        assertTrue(
                audioFile.exists(),
                "Reference audio file does not exist: " + audioFile.getAbsolutePath()
        );

        assertTrue(
                audioFile.isFile(),
                "Reference audio path is not a valid file: " + audioFile.getAbsolutePath()
        );

        assertTrue(
                audioFile.length() > 0,
                "Reference audio file must not be empty"
        );
    }

    private String getContentType(File file) {

        String name = file.getName().toLowerCase();

        if (name.endsWith(".mp3")) {
            return "audio/mpeg";
        }

        if (name.endsWith(".wav")) {
            return "audio/wav";
        }

        if (name.endsWith(".ogg")) {
            return "audio/ogg";
        }

        if (name.endsWith(".flac")) {
            return "audio/flac";
        }

        if (name.endsWith(".m4a")) {
            return "audio/mp4";
        }

        return "application/octet-stream";
    }
}
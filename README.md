# Smallest AI Voice E2E Automation

End-to-end API automation for the Smallest AI voice workflow using Java, Maven, Rest Assured, and TestNG.

The test covers the complete flow:

1. Upload a reference audio sample and create a voice clone.
2. Verify that the created voice clone is available.
3. Generate speech from a known script using the cloned voice.
4. Save the generated audio locally.
5. Send the generated audio to the Speech-to-Text API.
6. Compare the transcription with the original script.

---

## Tech Stack

- Java
- Maven
- Rest Assured
- TestNG
- Smallest AI Waves APIs

---

## End-to-End Flow

```text
Reference Audio
      |
      v
Create Voice Clone
      |
      v
Extract Voice ID
      |
      v
Verify Voice Clone Exists
      |
      v
Generate Speech using TTS
      |
      v
Save Generated Audio
      |
      v
Transcribe Audio using STT
      |
      v
Validate Transcript Similarity
```

---

## Project Structure

```text
smallest-ai-voice-e2e
├── .gitignore
├── README.md
├── pom.xml
└── src
    └── test
        ├── java
        │   ├── clients
        │   │   ├── BaseClient.java
        │   │   ├── VoiceCloneClient.java
        │   │   ├── TTSClient.java
        │   │   └── STTClient.java
        │   ├── tests
        │   │   └── VoiceCloneE2ETest.java
        │   └── utils
        │       ├── ConfigManager.java
        │       ├── FileUtils.java
        │       └── TranscriptUtils.java
        └── resources
            ├── config.properties.example
            └── sample.mp3
```

---

## API Workflow

### 1. Create Voice Clone

The reference audio file is uploaded to the voice-cloning API using a multipart request.

The test validates:

- HTTP status code is `200`
- The API response contains a valid `voiceId`
- The returned `voiceId` is not null or empty

### 2. Verify Voice Clone

The voice-clone listing API is called and the test confirms that the newly created `voiceId` is present.

### 3. Generate Speech

The known script and cloned `voiceId` are sent to the Text-to-Speech API.

The response is returned as binary audio and extracted using:

```java
byte[] audioBytes = response.asByteArray();
```

The test validates:

- HTTP status code is `200`
- The response Content-Type represents audio or binary data
- The audio response is not empty

### 4. Save Generated Audio

The generated TTS audio is saved under:

```text
target/test-output/tts-output.wav
```

The `target` directory contains generated build output and is not committed to Git.

### 5. Transcribe Generated Audio

The generated audio file is sent to the Speech-to-Text API as a binary request body using:

```java
.body(audioFile)
```

The test validates:

- HTTP status code is `200`
- The STT response status is `success`
- The transcription is not null
- The transcription is not empty

### 6. Validate Transcript Similarity

The expected script and actual transcription are normalized before comparison.

Normalization includes:

- Converting text to lowercase
- Removing punctuation
- Removing additional whitespace
- Trimming leading and trailing spaces

The test calculates the percentage of unique expected words found in the STT output.

The E2E test passes when the transcript similarity is at least `80%`.

---

## Prerequisites

Install the following tools:

- Java 11 or later
- Maven 3.8 or later
- Git
- A valid Smallest AI API key

Verify the installation:

```bash
java -version
mvn -version
git --version
```

---

## Clone the Repository

```bash
git clone https://github.com/pawan023/smallest-ai-voice-e2e.git
```

Move into the project directory:

```bash
cd smallest-ai-voice-e2e
```

---

## Configure the API Key

The real API key must not be committed to Git.

Create a local configuration file by copying:

```text
src/test/resources/config.properties.example
```

to:

```text
src/test/resources/config.properties
```

### Windows PowerShell

```powershell
Copy-Item src/test/resources/config.properties.example src/test/resources/config.properties
```

### Windows Command Prompt

```cmd
copy src\test\resources\config.properties.example src\test\resources\config.properties
```

### Linux or macOS

```bash
cp src/test/resources/config.properties.example src/test/resources/config.properties
```

Update the newly created `config.properties` file:

```properties
base.url=https://api.smallest.ai
api.key=YOUR_SMALLEST_AI_API_KEY
```

Do not add quotes around the values.

---

## Reference Audio

The default reference audio file should be available at:

```text
src/test/resources/sample.mp3
```

The current implementation supports commonly used formats such as:

- MP3
- WAV
- FLAC
- OGG
- M4A

Use a clear reference audio sample with minimal noise for better voice-cloning and transcription results.

---

## Run the Tests

Run the complete Maven test suite:

```bash
mvn clean test
```

Run only the E2E test class:

```bash
mvn -Dtest=VoiceCloneE2ETest test
```

On Windows PowerShell, the same commands can be used directly.

---

## Test Output

Generated TTS audio is saved under:

```text
target/test-output/
```

Maven and TestNG reports may be available under:

```text
target/surefire-reports/
```

These files are generated during execution and should not be committed.

---

## Expected Result

A successful run should complete the following steps:

```text
Voice clone created
Voice clone verified
TTS audio generated
Generated audio saved
STT transcription received
Transcript similarity validated
Test passed
```

The console output also prints useful information such as:

- Created voice ID
- Generated audio size
- Generated audio location
- STT transcription
- Transcript similarity percentage

---

## Important Validations

The automation validates:

- Voice-clone API returns HTTP `200`
- Voice ID is returned successfully
- Created voice exists in the voice list
- TTS API returns HTTP `200`
- TTS response contains non-empty binary audio
- Generated audio file is created successfully
- STT API returns HTTP `200`
- STT response status is successful
- Transcription is present
- Transcript similarity is at least `80%`

In Rest Assured, the following is already a status-code assertion:

```java
.then()
.statusCode(200)
```

If an API returns a status code other than `200`, the test fails.

---

## Design Overview

### `BaseClient`

Maintains common Rest Assured request configuration, including:

- Base URL
- Authorization header
- Accept header

### `VoiceCloneClient`

Responsible for:

- Uploading reference audio
- Creating a voice clone
- Extracting the voice ID
- Verifying the voice clone exists

### `TTSClient`

Responsible for:

- Sending the TTS request
- Validating the audio response
- Extracting binary audio bytes

### `STTClient`

Responsible for:

- Sending generated audio as a binary request body
- Validating the STT response
- Extracting the transcription

### `FileUtils`

Responsible for:

- Creating the output directory
- Saving generated audio bytes as a file

### `TranscriptUtils`

Responsible for:

- Normalizing the expected and actual text
- Calculating word-based transcript similarity

### `ConfigManager`

Responsible for:

- Loading the base URL
- Loading the API key from `config.properties`

---

## Security

Do not commit the following file:

```text
src/test/resources/config.properties
```

It contains the real API key.

Only commit:

```text
src/test/resources/config.properties.example
```

The `.gitignore` file should contain:

```gitignore
# Maven output
target/

# Local secrets
src/test/resources/config.properties

# IntelliJ IDEA
.idea/
*.iml
out/

# VS Code
.vscode/

# Eclipse
.classpath
.project
.settings/

# OS-generated files
.DS_Store
Thumbs.db

# Logs
*.log
```

Before pushing changes, verify that the API key is not staged:

```bash
git status
```

To check whether the real configuration file is tracked:

```bash
git ls-files src/test/resources/config.properties
```

The command should return no output.

If the file was accidentally staged but not committed:

```bash
git restore --staged src/test/resources/config.properties
```

If it was already committed, remove it from Git tracking:

```bash
git rm --cached src/test/resources/config.properties
git commit -m "Remove local API configuration"
```

Revoke and regenerate any API key that was exposed publicly.

---

## Troubleshooting

### `401 Unauthorized`

Possible causes:

- API key is missing
- API key is invalid
- Authorization header is not configured correctly

Check:

```properties
api.key=YOUR_VALID_API_KEY
```

### `AudioDecodeError`

Possible causes:

- Generated file does not contain valid audio
- The file extension does not match the returned audio format
- TTS returned an error response instead of binary audio
- Audio bytes were modified before saving

The binary response should be saved directly:

```java
Files.write(outputPath, audioBytes);
```

Do not convert audio bytes to a String or Base64 before saving unless the API explicitly returns Base64.

### Voice clone is not found

Possible causes:

- The voice-clone creation request failed
- The wrong JSON path was used
- The created clone is not immediately available

The create response uses:

```java
response.jsonPath().getString("data.voiceId");
```

The voice list uses:

```java
response.jsonPath().getList("data.voiceId", String.class);
```

### Transcript similarity is below 80%

Possible causes:

- Reference audio has background noise
- Generated speech is unclear
- STT output differs significantly from the expected script
- Some words are pronounced differently

Use a clear audio sample and a short, natural test script.

---

## Known Considerations

- The test depends on live external Smallest AI APIs.
- API availability, rate limits, account quotas, and network issues can affect execution.
- Voice cloning and audio generation may take different amounts of time depending on the service.
- The created voice clone may remain in the account after test execution if a delete or cleanup endpoint is unavailable.
- STT output can contain minor punctuation and capitalization differences, which is why normalized similarity is used instead of exact string matching.

---

## Repository

```text
https://github.com/pawan023/smallest-ai-voice-e2e
```

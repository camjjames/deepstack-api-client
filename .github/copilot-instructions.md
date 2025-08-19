# DeepStack API Client for Spring Boot

DeepStack API Client is a Spring Boot library that provides a Java client for integrating with [DeepStackAI](https://www.deepstack.cc/) API endpoints. The library supports Face Detection, Face Recognition, Face Registration, Object Detection, and Scene Detection APIs.

Always reference these instructions first and fallback to search or bash commands only when you encounter unexpected information that does not match the info here.

## Working Effectively

### Bootstrap, build, and test the repository:
- Ensure Java 17 is available: `java -version` (should show OpenJDK 17+)
- Use system Maven (Maven wrapper is incomplete): `mvn -version` (should show Apache Maven 3.6+)
- Clean compile: `mvn clean compile` -- takes 70 seconds on first run (dependencies download). NEVER CANCEL. Set timeout to 120+ seconds.
- Full build: `mvn clean package` -- takes 15 seconds after initial setup. NEVER CANCEL. Set timeout to 60+ seconds.
- Run tests: `mvn test` -- takes 28 seconds (14 tests). NEVER CANCEL. Set timeout to 60+ seconds.
- Full install: `mvn clean install` -- takes 14 seconds after initial setup. NEVER CANCEL. Set timeout to 60+ seconds.
- Full verification: `mvn verify` -- includes all above plus jacoco coverage. NEVER CANCEL. Set timeout to 60+ seconds.

### Important build notes:
- **DO NOT** use `./mvnw` - the Maven wrapper is incomplete and missing required files
- **ALWAYS** use system `mvn` command instead
- The first build takes longer due to dependency downloads (~70 seconds)
- Subsequent builds are much faster (~15 seconds)

## Validation

### Testing and Coverage:
- **ALWAYS** run the complete test suite: `mvn test`
- Tests use WireMock to mock DeepStack API responses with sample JSON and image files
- Test coverage is enforced via jacoco plugin with minimum 10% line coverage
- Coverage report: `mvn jacoco:report` (generates target/site/jacoco/index.html)

### Validation scenarios to test after making changes:
- **Face Detection API**: Test with sample images (detect-face1.jpg) to verify face boundary detection
- **Face Recognition API**: Test face recognition against registered faces with confidence scoring
- **Face Registration API**: Test face registration with userid parameter validation
- **Object Detection API**: Test object detection with sample images (detect-object1.jpg) for people/object detection
- **Scene Detection API**: Test scene classification with sample images (detect-scene1.jpg, detect-scene2.jpg)

### Linting and Code Quality:
- Checkstyle is configured but **SKIPPED** by default due to 50+ violations: `mvn checkstyle:check` will FAIL
- Do NOT run checkstyle validation as it's intentionally disabled in the build (skip=true)
- SonarCloud integration is configured via sonar-project.properties for CI/CD quality gates
- Code uses Lombok annotations extensively - ensure lombok.config is preserved

## Key Projects and Structure

### Main source structure:
```
src/main/java/org/flaad/deepstack/client/
├── annotation/EnableDeepStackApiClient.java    # Spring configuration annotation
├── client/DeepStackClient.java                 # Feign client interface (main API)
├── config/DeepStackConfiguration.java          # Spring configuration
└── model/                                      # Response model classes
    ├── DeepStackResponse.java                  # Base response class
    ├── FaceDetectionResponse.java
    ├── FaceRecognizeResponse.java
    ├── FaceRegisterResponse.java
    ├── ObjectDetectionResponse.java
    ├── SceneDetectionResponse.java
    └── Prediction.java                         # Detection prediction results
```

### Test structure:
```
src/test/java/org/flaad/deepstack/client/
├── DeepStackApiClient.java                    # Base test class with WireMock setup
├── FaceDetectionApiClientTest.java
├── FaceRecognizeApiClientTest.java
├── FaceRegisterApiClientTest.java
├── ObjectDetectionApiClientTest.java
├── SceneDetectionApiClientTest.java
└── TestUtilities.java                         # Test helper methods

src/test/resources/
├── application-test.yml                       # Test configuration
├── images/                                    # Sample test images (JPG files)
└── stubs/                                     # WireMock JSON response stubs
```

### Configuration files:
- `pom.xml` - Maven build configuration (Spring Boot 3.0.2, Java 17)
- `lombok.config` - Lombok configuration 
- `sonar-project.properties` - SonarCloud integration
- `.circleci/config.yml` - CI/CD pipeline configuration

## Common Tasks

### Working with the API client:
- Main API interface: `DeepStackClient.java` - Feign client with 6 endpoints
- All endpoints accept MultipartFile image uploads via @RequestPart
- Configuration requires `deepstack.url` property pointing to DeepStack server
- Uses Spring Cloud OpenFeign for HTTP client implementation

### Debugging tests:
- Tests extend `DeepStackApiClient` base class which provides WireMock setup methods
- WireMock server runs on port 9561 (configured in application-test.yml)
- Sample test images in `src/test/resources/images/` (detect-face1.jpg, etc.)
- Mock responses in `src/test/resources/stubs/` (JSON files matching API responses)

### Key dependencies:
- Spring Boot 3.0.2 with Spring Web MVC
- Spring Cloud OpenFeign 4.0.0 for HTTP client
- WireMock 2.27.2 for API mocking in tests
- Lombok for reducing boilerplate code
- JUnit 5 and Hamcrest for testing

### Repository root contents:
```
.
├── README.md                                  # Project documentation
├── pom.xml                                   # Maven build file
├── mvnw, mvnw.cmd                           # Maven wrapper (BROKEN - do not use)
├── LICENSE                                   # MIT License
├── lombok.config                             # Lombok configuration
├── sonar-project.properties                  # SonarCloud configuration
├── .gitignore                               # Git ignore rules
├── .circleci/config.yml                     # CI/CD configuration
└── src/                                     # Source code
    ├── main/java/                           # Library source code
    └── test/                                # Test code and resources
```

### Build artifacts:
- JAR file: `target/deepstack-api-client-1.3.0.jar`
- Test results: `target/surefire-reports/`
- Coverage reports: `target/site/jacoco/`
- Compiled classes: `target/classes/`, `target/test-classes/`

## Troubleshooting

### Common issues:
- **Maven wrapper fails**: Use system `mvn` instead of `./mvnw`
- **Build fails on checkstyle**: This is expected - checkstyle is skipped by default
- **Tests fail due to port conflicts**: WireMock uses fixed port 9561 - ensure it's available
- **Slow initial build**: First build downloads dependencies (~70 seconds) - this is normal

### Manual validation workflow:
1. Run full build: `mvn clean package`
2. Run test suite: `mvn test` 
3. Verify all 14 tests pass (Face: 6 tests, Object: 2 tests, Scene: 3 tests, Base: 3 tests)
4. Check coverage: `mvn jacoco:report` (should show >10% line coverage)
5. For code changes, ensure tests still mock API responses correctly via WireMock stubs
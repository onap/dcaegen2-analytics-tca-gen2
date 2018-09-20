/*
 * ================================================================================
 * Copyright (c) 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 *
 */

package org.onap.dcae.analytics.test;

import static java.nio.file.Files.deleteIfExists;
import static java.nio.file.Files.exists;
import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base common test class for all DCAE Analytics Test e.g. unit tests, integration test etc.
 *
 * @author Rajiv Singla
 */
abstract class BaseAnalyticsTest {

    protected static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Asserts if expected Json String and actual Json String contain the same properties ignoring
     * property order. Simple String assertion might fail as property order during serialization and deserialization
     * is generally non-deterministic. Also proper error message are generated more missing or unexpected
     * properties
     *
     * @param expectedJsonString expected Json String
     * @param actualJsonString actual Json String
     *
     * @throws JSONException Json Exception
     */
    public static void assertJson(final String expectedJsonString, final String actualJsonString) throws JSONException {
        JSONAssert.assertEquals(expectedJsonString, actualJsonString, true);
    }

    /**
     * Converts given file location to String
     *
     * @param fileLocation location of the file which needs to be converted to String
     *
     * @return Contents of file as string
     */
    public static String fromStream(final String fileLocation) {
        try (InputStream fileInputStream = asInputStream(fileLocation);
             BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream, Charset.forName
                     ("UTF-8")))) {
            final StringBuilder result = new StringBuilder();
            final String newLine = System.getProperty("line.separator");
            String line = reader.readLine();
            while (line != null) {
                result.append(line).append(newLine);
                line = reader.readLine();
            }
            return result.toString();
        } catch (IOException e) {
            throw new RuntimeException("Unable to get contents for file Location: " + fileLocation, e);
        }
    }

    /**
     * Converts given file location to input stream
     *
     * @param fileLocation file location
     *
     * @return input stream of given file location
     */
    public static InputStream asInputStream(final String fileLocation) {
        final InputStream fileInputStream =
                BaseAnalyticsTest.class.getClassLoader().getResourceAsStream(fileLocation);
        assertThat(fileInputStream).as("Input File Location must be valid").isNotNull();
        return fileInputStream;
    }

    /**
     * Checks if object can be serialized properly
     *
     * @param object input object
     * @param callingClass calling class
     */
    public static void testSerialization(final Object object, final Class<?> callingClass) {

        final URL location = callingClass.getProtectionDomain().getCodeSource().getLocation();
        final File serializedOutputFile =
                new File(location.getPath() + String.format("serialization/%s.ser", object.getClass().getSimpleName()));
        try {
            // Maybe file already exists try deleting it first
            final boolean deleteIfExists = deleteIfExists(Paths.get(serializedOutputFile.getPath()));

            if (deleteIfExists) {
                logger.warn("Previous serialization file was overwritten at location: {}",
                        serializedOutputFile.getPath());
            }

            boolean mkdirs = true;
            if (!Paths.get(serializedOutputFile.getParentFile().getPath()).toFile().exists()) {
                mkdirs = serializedOutputFile.getParentFile().mkdirs();
            }
            if (mkdirs) {
                try (FileOutputStream fileOutputStream = new FileOutputStream(serializedOutputFile);
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                    objectOutputStream.writeObject(object);
                    logger.debug("Successfully created serialization file at location: {}", serializedOutputFile
                            .getPath());
                }
            }

        } catch (IOException ex) {
            throw new IllegalStateException(
                    String.format("Failed to create location to store serialization file: %s", serializedOutputFile));
        }
    }

    /**
     * Writes Text to Output file
     *
     * @param textFileLocation location of text file e.g. textfiles/fileName.json
     * @param content file content
     * @param callingClass calling class
     *
     * @throws IOException ioException
     */
    public static void writeToOutputTextFile(final String textFileLocation,
                                             final String content, Class<?> callingClass) throws IOException {
        final URL location = callingClass.getProtectionDomain().getCodeSource().getLocation();
        final File fileLocation = new File(location.getPath() + textFileLocation);

        // Maybe file already try deleting it first
        final boolean deleteIfExists = deleteIfExists(Paths.get(fileLocation.getPath()));

        if (deleteIfExists) {
            logger.warn("Previous file will be overwritten at location: {}", fileLocation.getPath());
        }

        boolean mkdirs = true;
        if (!exists(Paths.get(fileLocation.getParentFile().getPath()))) {
            mkdirs = fileLocation.getParentFile().mkdirs();
        }
        if (mkdirs) {
            try (
                    FileOutputStream fileOutputStream = new FileOutputStream(fileLocation);
                    OutputStreamWriter outputStream =
                            new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"))) {
                outputStream.write(content);
                logger.debug("Successfully created text file at location: {}", fileLocation.getPath());
            }
        } else {
            throw new IllegalStateException(
                    String.format("Failed to create location to store text file: %s", fileLocation));
        }

    }

    /**
     * For testing purposes only sometime we may want to access private fields of underlying
     * object to confirm the values are setup correctly.
     * This method uses java reflection to get the value to private object in the class
     *
     * @param object Actual object which has the private field you want to check
     * @param fieldName Field name in the Actual Object you want to get the value of
     * @param privateFieldClass Type of the private field
     * @param <T> Class of Actual Object
     * @param <U> Class of private field
     *
     * @return value of the private field
     */
    @SuppressWarnings("unchecked")
    public static <T, U> U getPrivateFiledValue(final T object, final String fieldName,
                                                final Class<U> privateFieldClass) {

        final Class<?> objectClass = object.getClass();
        try {
            final Field privateField = objectClass.getDeclaredField(fieldName);
            try {

                // mark private field to be accessible for testing purposes
                AccessController.doPrivileged((PrivilegedAction) () -> {
                    privateField.setAccessible(true);
                    return null;
                });

                return privateFieldClass.cast(privateField.get(object));

            } catch (IllegalAccessException e) {
                logger.error("Unable to access field: {}", fieldName);
                throw new IllegalStateException(e);
            }
        } catch (NoSuchFieldException e) {
            logger.error("Unable to locate field name: {} in class: {}", fieldName, objectClass.getSimpleName());
            throw new IllegalStateException(e);
        }

    }

    /**
     * For testing purposes only sometime we may want to print classpath jars which are visible inside the class
     *
     * @param classLoader classloader of the calling class
     */
    public static void dumpClasspath(final ClassLoader classLoader) {

        logger.info("Dumping ClassPath for classloader: {}", classLoader);

        if (classLoader instanceof URLClassLoader) {

            URLClassLoader ucl = (URLClassLoader) classLoader;
            logger.info("\t ==========>>>" + Arrays.toString(ucl.getURLs()));

        } else {
            logger.info("\t(cannot display components as it is not a URLClassLoader)");
        }

        if (classLoader.getParent() != null) {
            dumpClasspath(classLoader.getParent());
        }
    }

    /**
     * Deserialize given Json file location to given model class and returns it back without any validation check.
     *
     * @param jsonFileLocation Classpath location of the json file
     * @param modelClass Model Class type
     * @param <T> Json Model Type
     * @param objectMapper Jackson Json Object Mapper
     *
     * @return Deserialized Model Object
     */
    public static <T> T deserializeJsonFileToModel(final String jsonFileLocation, final Class<T> modelClass,
                                                   final ObjectMapper objectMapper) {
        final InputStream jsonFileInputStream =
                BaseAnalyticsTest.class.getClassLoader().getResourceAsStream(jsonFileLocation);
        assertThat(jsonFileInputStream).as("Input JSON File location must be valid").isNotNull();
        try {
            return objectMapper.readValue(jsonFileInputStream, modelClass);
        } catch (IOException ex) {
            logger.error("Error while doing assert Json for fileLocation: {}, modelClass: {}, Exception {}",
                    jsonFileLocation, modelClass, ex);
            throw new RuntimeException(ex);
        } finally {
            try {
                jsonFileInputStream.close();
            } catch (IOException e) {
                logger.error("Error while closing input stream at file location: {}", jsonFileLocation);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Serialize model to json string
     *
     * @param model model object
     * @param objectMapper Jackson Json Object Mapper
     *
     * @return json
     *
     * @throws JsonProcessingException when fails to process object
     */
    public static String serializeModelToJson(final Object model, final ObjectMapper objectMapper) throws
            JsonProcessingException {
        return objectMapper.writeValueAsString(model);
    }

    /**
     * Converts given model to json string and compare it with json present at given file location.
     *
     * @param model Model which needs to be compared
     * @param expectedJsonFileLocation Location of file containing expected json string
     * @param objectMapper Jackson Json Object Mapper
     * @param <T> Json Model Type
     *
     * @return If assertion passes returns the input model
     */
    public static <T> T assertJsonSerialization(final T model, final String expectedJsonFileLocation,
                                                final ObjectMapper objectMapper) {
        try {
            final String actualModelString = serializeModelToJson(model, objectMapper);
            final String expectedModelString = fromStream(expectedJsonFileLocation);
            assertJson(expectedModelString, actualModelString);
            return model;
        } catch (IOException | JSONException ex) {
            logger.error("Error while doing assert Json serialization Assertion: model: {}, "
                    + "expected Json File Location: {}, Exception {}", model, expectedJsonFileLocation, ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * Checks both serialization and deserialization.
     * <p>
     * First checks deserialization and then serialize the deserialized object back to json
     * and check if matches the given json file location string
     *
     * @param jsonFileLocation Classpath location of the json file
     * @param modelClass Class type
     * @param <T> Json Model Type
     * @param objectMapper Jackson Json Object Mapper
     *
     * @return If assertion passes, returns deserialized object
     */

    public static <T> T assertJsonConversions(final String jsonFileLocation, final Class<T> modelClass,
                                              final ObjectMapper objectMapper) {
        //first check deserialization
        final T actualValue = deserializeJsonFileToModel(jsonFileLocation, modelClass, objectMapper);
        //then check serialization
        assertJsonSerialization(actualValue, jsonFileLocation, objectMapper);

        return actualValue;
    }


    public static <T> T assertJsonConversions(final String jsonFileLocation,
                                              final Function<String, Optional<T>> jsonConversionFunction,
                                              final ObjectMapper objectMapper) {
        final Optional<T> actualValueOptional = jsonConversionFunction.apply(fromStream(jsonFileLocation));
        assertThat(actualValueOptional).isPresent();
        if (actualValueOptional.isPresent()) {
            assertJsonSerialization(actualValueOptional.get(), jsonFileLocation, objectMapper);
            return actualValueOptional.get();
        }
        return null;
    }


    /**
     * Creates a mock URL Stream Handler
     *
     * @param mockURLConnection mock URL Connection
     *
     * @return Mock URL Stream Handler
     */
    public static URLStreamHandler createMockURLStreamHandler(final URLConnection mockURLConnection) {
        return new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(final URL url)
                    throws IOException {
                return mockURLConnection;
            }
        };
    }

    /**
     * Sets up environment variables for testing purposes
     *
     * @param testEnvironmentVariables key value map containing test environment variables
     *
     * @throws ClassNotFoundException class not found exception
     * @throws IllegalAccessException illegal access exception
     * @throws NoSuchFieldException no such method exception
     */
    @SuppressWarnings("unchecked")
    public static void setEnvironmentVariables(final Map<String, String> testEnvironmentVariables)
            throws ClassNotFoundException, IllegalAccessException, NoSuchFieldException {
        try {
            final Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            final Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");

            AccessController.doPrivileged((PrivilegedAction) () -> {
                theEnvironmentField.setAccessible(true);
                return null;
            });

            final Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(testEnvironmentVariables);
            Field theCaseInsensitiveEnvironmentField =
                    processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            AccessController.doPrivileged((PrivilegedAction) () -> {
                theCaseInsensitiveEnvironmentField.setAccessible(true);
                return null;
            });
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(testEnvironmentVariables);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl : classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    AccessController.doPrivileged((PrivilegedAction) () -> {
                        field.setAccessible(true);
                        return null;
                    });
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(testEnvironmentVariables);
                }
            }
        }
    }

    /**
     * Contains Locations for various files used for testing purposes
     */
    public abstract static class TestFileLocation {

        public static final String CEF_JSON_MESSAGE = "data/json/cef/cef_message.json";
        public static final String CEF_UNESCAPED_MESSAGES = "data/json/cef/cef_unescaped_messages.txt";
        public static final String CEF_JSON_MESSAGE_WITH_VIOLATION = "data/json/cef/cef_message_with_violation.json";
        public static final String CEF_JSON_MESSAGE_WITH_INAPPLICABLE_EVENT_NAME =
                "data/json/cef/cef_meesage_with_inapplicable_event_name.json";
        public static final String CEF_JSON_MESSAGE_WITH_ABATEMENT = "data/json/cef/cef_message_with_abatement.json";
        public static final String CEF_JSON_MESSAGES = "data/json/cef/cef_messages.json";

        public static final String TCA_POLICY_JSON = "data/json/policy/tca_policy.json";
        public static final String TCA_APP_CONFIG_JSON = "data/json/config/tca_app_config.json";
        public static final String TCA_ALERT_JSON = "data/json/facade/tca_alert.json";


        public static final String CONFIG_SERVICE_BINDINGS_JSON =
                "data/json/configservice/config_service_bindings.json";


        public static final String TEST_JSON_MESSAGE = "data/json/test/test_message.json";
    }


}

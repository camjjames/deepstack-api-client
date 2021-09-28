package org.flaad.deepstack.client;

import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class TestUtilities {

    public static MultipartFile getImage(String imageName) throws IOException, URISyntaxException {
        File file;
        URL resource = Thread.currentThread().getContextClassLoader().getResource(imageName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            file = new File(resource.toURI());
        }

        FileInputStream input = new FileInputStream(file);
        return new MockMultipartFile("image", file.getName(), "text/plain", IOUtils.toByteArray(input));
    }

}

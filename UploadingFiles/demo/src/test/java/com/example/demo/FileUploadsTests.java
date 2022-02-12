package com.example.demo;

import com.example.demo.storage.StorageFileNotFoundException;
import com.example.demo.storage.StorageServices;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadsTests {
    @Autowired
    MockMvc mvc;

    @MockBean
    StorageServices storageServices;

    @Test
    public void shouldListAllFiles() throws Exception{
        given(this.storageServices.loadAll())
                .willReturn(Stream.of(Paths.get("first.txt"), Paths.get("second.txt")));
        this.mvc.perform(get("/")).andExpect(status().isOk())
                .andExpect(model().attribute("files",
                        Matchers.contains("http://localhost/files?filename=first.txt", "http://localhost/files?filename=second.txt")));
    }

    @Test
    public void shouldSaveUploadedFile() throws Exception{
        MockMultipartFile multipartFile=new MockMultipartFile("file",
                "test2.txt","text/plain","Spring Framework".getBytes());
        this.mvc.perform(multipart("/").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/"));
        then(this.storageServices).should().store(multipartFile);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void should404WhenMissingFile() throws Exception{
        given(this.storageServices.loadAsResource("test.txt"))
                .willThrow(StorageFileNotFoundException.class);
        this.mvc.perform(get("/files?filename=test.txt"))
                .andExpect(status().isNotFound());
    }
}

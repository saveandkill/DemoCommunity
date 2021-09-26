package com.example.learningcase;

import com.example.learningcase.controller.IndexController;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
class LearningcaseApplicationTests {

    private MockMvc mvc;

    public void setup() throws Exception{
        mvc= MockMvcBuilders.standaloneSetup(new IndexController()).build();
    }

}

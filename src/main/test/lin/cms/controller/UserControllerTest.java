//package io.github.xucancould.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.PropertyNamingStrategies;
//import io.github.talelin.latticy.dto.user.RegisterDTO;
//import io.github.xucancould.dto.user.LoginDTO;
//import io.github.xucancould.mapper.GroupMapper;
//import io.github.xucancould.model.GroupDO;
//import io.github.xucancould.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.Arrays;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * created by Xu on 2024/3/24 20:07.
// */
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//public class UserControllerTest {
//
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private GroupMapper groupMapper;
//
//    @Autowired
//    private UserService userService;
//
//    private final String email = "13129982604@qq.com";
//
//    private final String password = "123456";
//
//    private final String username = "root";
//
//    /**
//     * getPermissions
//     */
//    @Test
//    public void getPermissions() throws Exception {
//
//        LoginDTO dto1 = new LoginDTO();
//        dto1.setUsername(username);
//        dto1.setPassword(password);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        String content = mapper.writeValueAsString(dto1);
//
//        mvc.perform(post("/cms/user/login")
//                        .contentType(MediaType.APPLICATION_JSON).content(content))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").isNotEmpty())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.refreshToken").isNotEmpty());
//    }
//}

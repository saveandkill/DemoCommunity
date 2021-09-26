package com.example.learningcase.controller;

import com.example.learningcase.dto.AccessTokenDTO;
import com.example.learningcase.dto.GithubUserDTO;
import com.example.learningcase.mapper.UserMapper;
import com.example.learningcase.model.User;
import com.example.learningcase.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.url}")
    private String redirectUri;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest httpServletRequest) {
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setState(state);
        String accesstoken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUserDTO githubuserdto = githubProvider.getUser(accesstoken);
        if (githubuserdto == null) {
//            返回失败，重新登陆
            return "redirect:/";
        } else {
            User user=new User();
            user.setAccount_id(githubuserdto.getId());
            user.setName(githubuserdto.getName());
            user.setToken(UUID.randomUUID().toString());
            user.setBio(githubuserdto.getBio());
            user.setGmt_create(System.currentTimeMillis());
            user.setGmt_modified(user.getGmt_create());
            userMapper.add(user);
//            设置cookie和session,返回成功
            httpServletRequest.getSession().setAttribute("user",githubuserdto);
            return "redirect:/";
        }

    }
}

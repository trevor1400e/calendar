package com.example.service

import com.example.model.SlackUser
import com.example.repository.RoleRepository
import com.example.repository.SlackUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service("slackUserService")
public class SlackUserServiceImpl implements SlackUserService {

    @Autowired
    public SlackUserRepository slackUserRepository;
    @Qualifier("roleRepository")
    @Autowired
    public RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public SlackUser findUserByEmail(String email) {
        return slackUserRepository.findByEmail(email);
    }

    @Override
    public SlackUser findUserById(int id) {
        return slackUserRepository.findById(id);
    }

    @Override
    void saveSlackUser(SlackUser slackUser) {
        slackUser.setToken(bCryptPasswordEncoder.encode(slackUser.getToken()))
        //Role userRole = roleRepository.findByRole("USER")
        //slackUser.setRoles(new HashSet<Role>(Arrays.asList(userRole)))
        slackUserRepository.save(slackUser)

    }


}

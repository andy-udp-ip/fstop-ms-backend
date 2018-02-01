package tw.com.fstop;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.fstop.app.mongodb.entity.User;
import tw.com.fstop.app.spring.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FstopMsBackendApplicationTests
{
    @ClassRule
    public static OutputCapture outputCapture = new OutputCapture();
    
    @Autowired
    private UserRepository userRepository;
    
    
    @Test
    public void testMongoDBLoadData()
    {
        String output = FstopMsBackendApplicationTests.outputCapture.toString();
        assertThat(output).contains("firstName=Angel, lastName=Smith");
    }
    
    @Test
    public void testUserRepositoryFunctions() throws JsonProcessingException
    {
        List<User> users = userRepository.findAll();
        assertNotNull(users);
        
        String uid = "A1234567890";
        String uidSeq = "A";
        String firstName = "Andy10";
        String lastName = "Lee";
        Integer age = 30;
        User user = new User(uid, uidSeq, firstName, lastName);
        user = userRepository.save(user);        
        assertNotNull(user.getId());
        
        user = userRepository.findByFirstName(firstName);
        assertNotNull(user);
        
        users = userRepository.findByLastName(lastName);
        assertNotNull(users);

        Sort sort = new Sort(Sort.Direction.DESC, "uidSeq");
        Page<User> p = userRepository.findByLastName(lastName, new PageRequest(1,3, sort));
        assertNotNull(p);
        
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);
        System.out.println(">>>>>>>>>>>>>>>>>>>"+json);
        
        user = userRepository.findUserByUidAndUidSeq(uid, uidSeq);
        assertNotNull(user);
        
    }

}

package tw.com.fstop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import tw.com.fstop.app.mongodb.entity.User;
import tw.com.fstop.app.spring.repository.UserRepository;

@SpringBootApplication
public class FstopMsBackendApplication implements CommandLineRunner
{

    @Autowired
    private UserRepository repository;

    public static void main(String[] args)
    {
        SpringApplication.run(FstopMsBackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        this.repository.deleteAll();
        
        // save a couple of customers
        this.repository.save(new User("A123456789", "", "Andy", "Lee"));
        this.repository.save(new User("A123456789", "1", "Andy1", "Lee"));
        this.repository.save(new User("A123456789", "2", "Andy2", "Lee"));
        this.repository.save(new User("A123456789", "3", "Andy3", "Lee"));
        this.repository.save(new User("A123456789", "4", "Andy4", "Lee"));
        this.repository.save(new User("A123456789", "5", "Andy5", "Lee"));

        this.repository.save(new User("A234567890", "", "Angel", "Smith"));
        this.repository.save(new User("A234567890", "1", "Angel1", "Smith"));
        this.repository.save(new User("A234567890", "2", "Angel2", "Smith"));
        this.repository.save(new User("A234567890", "3", "Angel3", "Smith"));
        this.repository.save(new User("A234567890", "4", "Angel4", "Smith"));
        this.repository.save(new User("A234567890", "5", "Angel5", "Smith"));
        this.repository.save(new User("A234567890", "6", "Angel6", "Smith"));

        // fetch all customers
        System.out.println("User found with findAll():");
        System.out.println("-------------------------------");
        for (User user : this.repository.findAll())
        {
            System.out.println(user);
        }
        System.out.println();

        // fetch an individual customer
        System.out.println("User found with findByFirstName('Andy'):");
        System.out.println("--------------------------------");
        System.out.println(this.repository.findByFirstName("Andy"));

        System.out.println("User found with findByLastName('Smith'):");
        System.out.println("--------------------------------");
        for (User user : this.repository.findByLastName("Smith"))
        {
            System.out.println(user);
        }
        System.out.println("--------------------------------");

        System.out.println(this.repository.findUserByUidAndUidSeq("A123456789", "2"));
        
        System.out.println("--------------------------------");
        Sort sort = new Sort(Sort.Direction.DESC, "uidSeq");
        Page<User> p = this.repository.findByLastName("Smith", new PageRequest(2,3, sort));
        for (User user : p)
        {
            System.out.println(">>" + user);
        }
    }
}

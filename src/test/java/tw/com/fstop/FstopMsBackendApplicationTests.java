package tw.com.fstop;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.rule.OutputCapture;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FstopMsBackendApplicationTests
{
    @ClassRule
    public static OutputCapture outputCapture = new OutputCapture();
    
    @Test
    public void testMongoDBLoadData()
    {
        String output = FstopMsBackendApplicationTests.outputCapture.toString();
        assertThat(output).contains("firstName=Angel, lastName=Smith");
    }

}

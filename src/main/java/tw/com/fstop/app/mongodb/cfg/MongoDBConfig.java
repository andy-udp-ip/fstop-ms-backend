
/*
 * Copyright (c) 2017, FSTOP, Inc. All Rights Reserved.
 *
 * You may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tw.com.fstop.app.mongodb.cfg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

// https://github.com/flapdoodle-oss/de.flapdoodle.embed.mongo
@Configuration
@EnableConfigurationProperties(MongoProperties.class)
//@AutoConfigureAfter(MongoAutoConfiguration.class)
//@AutoConfigureBefore(MongoDataAutoConfiguration.class)
public class MongoDBConfig
{

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.database}")
    private String dbName;
    
    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.user}")
    private String mongoUser;

    @Value("${spring.data.mongodb.password}")
    private String mongoPassword;
    

    private static MongodExecutable mongodExecutable = null;
    
    //private static MongodProcess mongodProcess = null;

    private MongoClient client = null;
    
    @Autowired
    private MongoProperties properties;

    @Autowired(required = false)
    private MongoClientOptions options;

    
//會有錯誤
//    @Bean(destroyMethod = "close")
//    public Mongo mongo(MongodProcess mongodProcess) throws IOException
//    {
//        System.out.println("mongo");
//        Net net = mongodProcess.getConfig().net();
//        properties.setHost(net.getServerAddress().getHostName());
//        properties.setPort(net.getPort());
//        return properties.createMongoClient(this.options, null);
//    }

    @Bean(destroyMethod = "close")
    public Mongo mongo(MongoClient mongoClient) throws IOException
    {
        //System.out.println("=========================mongo================================");
        client = mongoClient;
        return client;
    }
    
//會有錯誤    
//    @Bean(name = "mongoClient", destroyMethod = "close")
//    public MongoClient mongoClient(MongodProcess mongodProcess) throws IOException
//    {
//        System.out.println("=========================mongoClient================================");
//        Net net = mongodProcess.getConfig().net();
//        properties.setHost(net.getServerAddress().getHostName());
//        properties.setPort(net.getPort());
//        return properties.createMongoClient(this.options, null);
//    }

    @Bean(name = "mongoClient", destroyMethod = "close")
    public MongoClient mongoClient() throws IOException
    {
        //System.out.println("=========================mongoClient================================");

        Integer port = Integer.parseInt(this.port);
        List<ServerAddress> seeds = new ArrayList<>();
        seeds.add(new ServerAddress(host, port));
        
        List<MongoCredential> credentials = new ArrayList<>();
        MongoCredential.createMongoCRCredential(mongoUser, dbName, mongoPassword.toCharArray());
        
        options = getOptions();
        
        MongoClient mongoClient = new MongoClient(seeds, credentials, options);
        
        return mongoClient;                
    }
    
    private MongoClientOptions getOptions()
    {
        
        MongoClientOptions.Builder optionsBuilder = new MongoClientOptions.Builder();
        
        Integer serverSelectionTimeout = 30;
        Integer connectTimeout = 15;
        Integer socketTimeout = 10;
        Integer connectionsPerHost = 3;
        Integer maxWaitTime = 1;
        Boolean socketKeepAlive = true;
        optionsBuilder.serverSelectionTimeout(serverSelectionTimeout * 1000);
        optionsBuilder.connectTimeout(connectTimeout * 1000);
        optionsBuilder.socketTimeout(socketTimeout * 1000);
        optionsBuilder.connectionsPerHost(connectionsPerHost);
        optionsBuilder.socketKeepAlive(socketKeepAlive);
        //Number of ms a thread will wait for a connection to become available on the connection pool
        optionsBuilder.maxWaitTime(maxWaitTime * 1000);
        optionsBuilder.readPreference(ReadPreference.secondaryPreferred());
        MongoClientOptions options = optionsBuilder.build();        
        return options;
    }
    
    // @Bean
    // @DependsOn("embeddedMongoServer")
    // public MongoDbFactory mongoDbFactory() throws Exception
    // {
    // return new SimpleMongoDbFactory(new MongoClient(host + ":" + port),
    // dbName);
    // }

    // @Autowired
    @Bean(name = "mongoDbFactory")
    public MongoDbFactory mongoDbFactory(MongoClient mongoClient)
    {
        //System.out.println("=========================mongoDbFactory================================");
        return new SimpleMongoDbFactory(mongoClient, dbName);
    }

    // @Autowired
    @Bean(name = "mongoTemplate")
    public MongoTemplate mongoTemplate(MongoClient mongoClient)
    {
        //System.out.println("=========================mongoTemplate================================");
        return new MongoTemplate(mongoClient, dbName);
    }

// 本來是需要 start，但是實際上不需要 start 否則會重複執行
//    @Bean(destroyMethod = "stop")
//    public MongodProcess mongodProcess(MongodExecutable embeddedMongoServer) throws IOException
//    {
//        if (mongodProcess == null)
//        {
//            System.out.println("=========================mongodProcess================================");
//            mongodProcess = embeddedMongoServer.start();    
//        }
//        return mongodProcess;
//    }
   
    @Bean(name = "embeddedMongoServer", initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public MongodExecutable embeddedMongoServer(MongodStarter mongodStarter, IMongodConfig mongodConfig) throws IOException
    {
        //MongodStarter mongodStarter = getMongodStarter(this.runtimeConfig);
        //System.out.println("=========================embeddedMongoServer================================");
        if (mongodExecutable == null)
        {
            mongodExecutable = mongodStarter.prepare(mongodConfig);   
        }
        return mongodExecutable;
    }

    
    @Bean
    public IMongodConfig mongodConfig() throws IOException
    {
        //System.out.println("=========================mongodConfig================================");
        Integer p = Integer.parseInt(port);
        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
                .net(new Net(host, p, Network.localhostIsIPv6())).build();
        return mongodConfig;
    }

    @Bean
    public MongodStarter mongodStarter()
    {
        //System.out.println("=========================mongodStarter================================");
        return MongodStarter.getDefaultInstance();
    }

    @PreDestroy
    public void shutdownEmbeddedMongoDB()
    {
        //System.out.println("=========================shutdownEmbeddedMongoDB================================");
        if (this.mongodExecutable != null)
        {
            this.mongodExecutable.stop();
        }
    }
}

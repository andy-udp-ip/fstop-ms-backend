
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

package tw.com.fstop.app.spring.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.core.query.Query.query;

import com.mongodb.WriteResult;

import tw.com.fstop.app.mongodb.entity.User;

public class UserRepositoryImpl implements CustomUserRepository
{
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public int updateAge(String uid, String uidSeq, Integer age)
    {
        
        Query query = new Query(Criteria.where("uid").is(uid).and("uidSeq").is(uidSeq));
        Update update = new Update();
        update.set("age", age);

        WriteResult result = mongoTemplate.updateFirst(query, update, User.class);

        if(result!=null)
            return result.getN();
        else
            return 0;
    }
    
    private void operations()
    {
        MongoOperations op = mongoTemplate;
        op.updateFirst(new Query(Criteria.where("lastName").is("Lee")), update("age", 35), User.class);
        User u = op.findOne(query(where("lastName").is("Lee")), User.class);
        op.remove(u);
        op.dropCollection(User.class);
    }
    

}


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

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import tw.com.fstop.app.mongodb.entity.User;

public interface UserRepository extends MongoRepository<User, String>, CustomUserRepository
{
    User findByFirstName(String firstName);

    List<User> findByLastName(String lastName);

    Page<User> findByLastName(String lastName, Pageable pageable);
    
    //native JSON based query methods and field restriction
    // "?0" 表示方法參數中的第一個參數
    //傳回  id 以及 fields 中有指定的欄位
    @Query(value="{uid:'?0', uidSeq:'?1'}", fields="{'uid':1, 'uidSeq':1, 'firstName':1}")
    User findUserByUidAndUidSeq(String uid, String uidSeq);    
}

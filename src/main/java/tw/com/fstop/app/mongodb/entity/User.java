
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

package tw.com.fstop.app.mongodb.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-info")  //指定 mongodb 儲存的 collection  
@CompoundIndexes({  //指定複合索引，可以加快查詢速度
    @CompoundIndex(name = "uid_idx", def = "{'uid': 1, 'uidSeq': -1}", unique = true)  //uid ascending, uidSeq descending
})
public class User
{
    @Id
    private String id;        
    private String firstName;
    private String lastName;   
    private String uid;
    private String uidSeq;
    private Integer age;

    //@Indexed(unique = true)  //指定要使用索引
    private String ssn;
    
    public User()
    {
        
    }
    
    public User(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public User(String uid, String uidSeq, String firstName, String lastName)
    {
        this.uid = uid;
        this.uidSeq = uidSeq;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }    
    public String getUid()
    {
        return uid;
    }
    public void setUid(String uid)
    {
        this.uid = uid;
    }
    public Integer getAge()
    {
        return age;
    }
    public void setAge(Integer age)
    {
        this.age = age;
    }
    public String getUidSeq()
    {
        return uidSeq;
    }
    public void setUidSeq(String uidSeq)
    {
        this.uidSeq = uidSeq;
    }
    public String getSsn()
    {
        return ssn;
    }
    public void setSsn(String ssn)
    {
        this.ssn = ssn;
    }

    @Override
    public String toString()
    {
        return "User [id=" + id + ", uid=" + uid + ", uidSeq=" + uidSeq + ", firstName=" + firstName + ", lastName=" + lastName + "]";
    }    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        User other = (User) obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    
    
}

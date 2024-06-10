package edu.examples.todos.persistence.repositories.users;

import edu.examples.todos.common.config.profiles.DefaultProfile;
import edu.examples.todos.domain.resources.users.User;
import edu.examples.todos.domain.resources.users.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@DefaultProfile
public interface UserRepository extends JpaRepository<User, UserId>
{

}

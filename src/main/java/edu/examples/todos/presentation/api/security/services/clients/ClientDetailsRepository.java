package edu.examples.todos.presentation.api.security.services.clients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientDetailsRepository extends JpaRepository<ClientDetails, String>
{
}

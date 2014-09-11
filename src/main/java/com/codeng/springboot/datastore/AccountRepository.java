package com.codeng.springboot.datastore;

import com.codeng.springboot.domain.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, String> {
}

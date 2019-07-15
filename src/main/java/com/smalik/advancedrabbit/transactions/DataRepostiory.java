package com.smalik.advancedrabbit.transactions;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("transactions")
public interface DataRepostiory extends CrudRepository<Data, String> { }

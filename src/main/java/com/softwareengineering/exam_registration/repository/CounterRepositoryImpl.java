package com.softwareengineering.exam_registration.repository;

import com.softwareengineering.exam_registration.model.Counter;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CounterRepositoryImpl implements CounterRepository {

    private final MongoOperations mongoOperations;

    public CounterRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public long getNextSequence(String counterName) {

        Query query = new Query(
                Criteria.where("_id").is(counterName)
        );

        Update update = new Update()
                .inc("sequence", 1);

        FindAndModifyOptions options = FindAndModifyOptions
                .options()
                .returnNew(true)
                .upsert(true);

        Counter counter = mongoOperations.findAndModify(
                query,
                update,
                options,
                Counter.class
        );

        return counter.getSequence();
    }
}
package com.likg.dao.cassandraimpl.client;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.NoHostAvailableException;
import com.datastax.driver.core.exceptions.QueryExecutionException;
import com.datastax.driver.core.exceptions.QueryValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by lixusign on 15-2-3.
 * 处理BatchStatement
 */
public class BatchClient extends AbstractClient {

    private static final Logger log = LoggerFactory.getLogger(BatchClient.class);

    public boolean executeBatch(List<BoundStatement> list) throws Exception {

        BatchStatement batchStatement = new BatchStatement();
        if (!list.isEmpty()) {
            Iterator<BoundStatement> iterator = list.iterator();
            for (; iterator.hasNext(); ) {
                BoundStatement statement = iterator.next();
                batchStatement.add(statement);
            }
        }
        boolean isException = false;
        try {
            getSessionHot().executeAsync(batchStatement);
        } catch (NoHostAvailableException e) {
            log.error("No host in the {} cluster can be contacted to execute the query.", getSessionHot().getCluster());
            isException = true;
        } catch (QueryExecutionException e) {
            log.error("An exception was thrown by Cassandra because it cannot successfully execute the query with the specified consistency level.");
            isException = true;
        } catch (QueryValidationException e) {
            Collection<Statement> exceptionList = batchStatement.getStatements();
            if (!exceptionList.isEmpty()) {
                Iterator<Statement> iterator = exceptionList.iterator();
                for (; iterator.hasNext(); ) {
                    log.error("The query {} is not valid, for example, incorrect syntax.", iterator.next().toString());
                }

            }
            isException = true;
        } catch (IllegalStateException e) {
            log.error("The BoundStatement is not ready.");
            isException = true;
        } catch (Exception e) {
            throw new Exception();
        } finally {
            if (isException) {
                log.error("lost message");
                //重试也挂了，我应该存储下来，稍后完善
                return false;
            }
        }
        return true;
    }
}

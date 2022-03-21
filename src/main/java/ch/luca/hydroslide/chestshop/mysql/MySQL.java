package ch.luca.hydroslide.chestshop.mysql;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MySQL {

    private ExecutorService pool;

    private BasicDataSource sqlPool;

    public MySQL( String host, String database, String user, String password, int port ) {
        // Init async pool
        this.pool = Executors.newCachedThreadPool();

        // Create mysql connection
        this.sqlPool = new BasicDataSource();

        this.sqlPool.setDriverClassName( "com.mysql.jdbc.Driver" );
        this.sqlPool.setUrl( "jdbc:mysql://" + host + ":" + port + "/" + database );
        this.sqlPool.setUsername( user );
        this.sqlPool.setPassword( password );

        this.sqlPool.setMaxIdle( 30 );
        this.sqlPool.setMinIdle( 5 );
        this.sqlPool.setDriverClassLoader( MySQL.class.getClassLoader() );
    }

    /**
     * Update async a statement
     *
     * @param statement to update
     * @param objects   as parameters for the statement
     */
    public void updateAsync( String statement, Object... objects ) {
        this.pool.execute( () -> {
            Connection connection = null;
            try {
                // Open connection
                connection = this.sqlPool.getConnection();

                // Open statement
                PreparedStatement preparedStatement = connection.prepareStatement( statement );

                // Replace parameters
                for ( int i = 0; i < objects.length; i++ ) {
                    int id = i + 1;
                    preparedStatement.setObject( id, objects[i] );
                }

                // Execute the statement
                preparedStatement.executeUpdate();

                // Close
                preparedStatement.close();
            } catch ( SQLException e ) {
                e.printStackTrace();
            } finally {
                if ( connection != null ) {
                    try {
                        // Close connection
                        connection.close();
                    } catch ( SQLException e ) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }

    /**
     * Query async a statement
     *
     * @param statement to query
     * @param consumer  with the result
     * @param objects   as parameters to replace
     */
    public void queryAsync( String statement, Consumer<ResultSet> consumer, Object... objects ) {
        this.pool.execute( () -> {
            Connection connection = null;
            try {
                // Open connection
                connection = this.sqlPool.getConnection();

                // Open statement
                PreparedStatement preparedStatement = connection.prepareStatement( statement );

                // Replace parameters
                for ( int i = 0; i < objects.length; i++ ) {
                    int id = i + 1;
                    preparedStatement.setObject( id, objects[i] );
                }

                // Execute the statement
                ResultSet resultSet = preparedStatement.executeQuery();

                consumer.accept( resultSet );
            } catch ( SQLException e ) {
                e.printStackTrace();
            } finally {
                if ( connection != null ) {
                    try {
                        // Close connection
                        connection.close();
                    } catch ( SQLException e ) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }
}

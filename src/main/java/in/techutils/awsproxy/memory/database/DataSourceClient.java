package in.techutils.awsproxy.memory.database;

import java.sql.*;

import javax.sql.DataSource;

import javax.naming.Context;
import javax.naming.InitialContext;

import java.util.*;

import java.io.*;

import javax.naming.NamingException;

import java.sql.Connection;


public class DataSourceClient {
    private Connection conn = null;
    private DataSource ds = null;
    private Context ctx = null;

    private static DataSourceClient dsc;

    public static DataSourceClient getInstance() {
        if (dsc == null) {
            dsc = new DataSourceClient();
        }
        return dsc;
    }

    private DataSourceClient() {
        super();
        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        env.put(Context.PROVIDER_URL, "t3://localhost:7101");
        try {
            ctx = new InitialContext(env);
            ds = (DataSource) ctx.lookup("jdbc\\awsproxy");
            conn = ds.getConnection();
            System.out.println("Connection Establised");
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public DataSource getDataSource() {
        return ds;
    }
}

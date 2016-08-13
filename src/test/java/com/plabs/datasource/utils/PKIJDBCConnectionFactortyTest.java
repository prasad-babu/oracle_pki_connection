package com.plabs.datasource.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.junit.Assert;
import org.junit.Test;

public class PKIJDBCConnectionFactortyTest {
		
	@Test
	public void testGetConnection() throws Exception {
		PKIJDBCConnectionFactory factory = PKIJDBCConnectionFactory.instance(null);
		Connection connection = null;
		Connection connection1 = null;
		Statement stmt = null;
		ResultSet rset = null;
		try {
			connection = factory.getConnection();
			Assert.assertNotNull(connection);
			
			stmt = connection.createStatement();
		    rset = stmt.executeQuery("select 'TEST' from dual");

		    Assert.assertTrue(rset.next());
		    Assert.assertEquals("TEST", rset.getString(1));
		    
		    rset.close();
		    rset = stmt.executeQuery("select user from dual");

		    Assert.assertTrue(rset.next());
		    Assert.assertEquals("USER_NAME", rset.getString(1));
		    
		    connection1 = factory.getConnection();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		finally {
			rset.close();
		    stmt.close();
		    connection.close();
		    connection1.close();
		}	    
	}

}

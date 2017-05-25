package com.plabs.datasource.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Hashtable;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.Name;

import org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory;

/**
 * <a>http://feijiangnan.blogspot.in/2013/09/oracle-database-11g-jdbc-connection.html</a>
 * <a>http://blog.c2b2.co.uk/2016/05/how-to-configure-oracle-wallet-with.html</a>
 * <Resource name="jdbc/oracle/data" auth="Container" type="javax.sql.DataSource"
                                                factory="com.plabs.datasource.util.PKITomcatDataSourceFactory"
                                                driverClassName="oracle.jdbc.driver.OracleDriver" url="jdbc:oracle:thin:@localhost:1521:db11gr2"
                                                username="schema_user" password="encryptedpassword"  maxActive="500" maxIdle="30" maxWait="-1" minPoolSize="50" maxPoolSize="500"
                                                logAbandoned="true"  removeAbandoned="true"  removeAbandonedTimeout="60"/>

 * http://blog.guntram.de/?p=81
 * @author prasadbabu
 *
 */

public class PKITomcatDataSourceFactory extends BasicDataSourceFactory {
	
	private static String PKI_PROVIDER = "PKI_PROVIDER";//"oracle.security.pki.OraclePKIProvider"
	
	private Properties properties;
	
	@Override
	public Object getObjectInstance(Object obj, Name name, Context nameCtx, Hashtable environment) throws Exception {
		if(properties == null || properties.isEmpty()) {
			loadProperties();
		}
		ClassLoader clsLoader = PKITomcatDataSourceFactory.class.getClassLoader();
		Class<?> providerCls = clsLoader.loadClass(properties.getProperty(PKI_PROVIDER));
		Security.insertProviderAt((Provider) providerCls.newInstance(), 3);
		/*
		if (o != null) {
		      BasicDataSource ds = (BasicDataSource)o;
		      if ((ds.getPassword() != null) && (ds.getPassword().length() > 0)) {
		        String pwd = EncryptionProvider.getInstance().decrypt(ds.getPassword()).toString();
		        ds.setPassword(pwd);
		      }
		      return ds;
		    }
		*/
		return super.getObjectInstance(obj, name, nameCtx, environment);
	}
	
	private synchronized void loadProperties() throws Exception {
		properties = new Properties();
		String fileName = getClass().getSimpleName() + ".properties";
		InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
		if(in != null)
			properties.load(in);
		else
			throw new FileNotFoundException("File " + fileName + " is not accessible");
	}
}

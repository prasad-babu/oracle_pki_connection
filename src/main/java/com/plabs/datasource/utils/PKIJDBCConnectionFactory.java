package com.plabs.datasource.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.Provider;
import java.security.Security;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

public class PKIJDBCConnectionFactory {
	
	private static PKIJDBCConnectionFactory instance;
	
	private static String DB_URL = "msi.db_url";
	private static String DB_DRIVER = "msi.db_driver";//oracle.jdbc.driver.OracleDriver
	private static String PKI_PROVIDER = "msi.pki_provider";//"oracle.security.pki.OraclePKIProvider"
	private static String PKI_PROVIDER_POSITION = "msi.pki_provider_position";//3
	
	private Properties properties;
	private String url;
	
	private PKIJDBCConnectionFactory(Properties _properties) throws Exception {		
		properties = _properties;
		if(properties == null || properties.isEmpty()) {
			loadProperties();
		}		
		url = properties.getProperty(DB_URL);
	}
	
	public static PKIJDBCConnectionFactory instance(Properties _properties) throws Exception {
		if(instance == null) {
			instance = new PKIJDBCConnectionFactory(_properties);		
			instance.setup();
		}
		return instance;
	}
	
	public Connection getConnection() throws Exception {
		return DriverManager.getDriver(url).connect(url, properties);
	}
	
	private void setup() throws Exception {
		ClassLoader clsLoader = PKIJDBCConnectionFactory.class.getClassLoader();
	
		Class<?> driverCls = clsLoader.loadClass(properties.getProperty(DB_DRIVER));
		DriverManager.registerDriver((Driver) driverCls.newInstance());
		
		if(properties.getProperty(PKI_PROVIDER) != null) {
			Class<?> providerCls = clsLoader.loadClass(properties.getProperty(PKI_PROVIDER));
			int position = Integer.parseInt(properties.getProperty(PKI_PROVIDER_POSITION, "3"));
			Security.insertProviderAt((Provider) providerCls.newInstance(), position);
		}
		
	}
	
	private void loadProperties() throws Exception {
		properties = new Properties();
		File file = new File("D:\\EDRIVE\\workspace - Copy\\plabs_datasource\\src\\main\\resources\\PKIJDBCConnectionFactory.properties");
		properties.load(new FileInputStream(file));
	}

}

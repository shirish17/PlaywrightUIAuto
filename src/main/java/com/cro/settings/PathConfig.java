/*
 * This class will load projectpath.properties to check the project path is configured or not.
 */

package com.cro.settings;

import java.io.InputStream;
import java.util.Properties;

public class PathConfig {
	
	private static final Properties properties = new Properties();
	private PathConfig() {
		//intentionally left blank to avoid constructor overloading
	}
	static {
		try (InputStream inputstream= PathConfig.class.getClassLoader()
				                                .getResourceAsStream("projectpath.properties")){
			if(inputstream==null) {
				throw new RuntimeException("projectpath.properties are not found");
			}
			properties.load(inputstream);
		} catch (Exception e) {
			throw new RuntimeException("Failed to load projectpath.properties",e);
		}
	}
	
	
	public static String get(String key) {
		return properties.getProperty(key);
	}

}

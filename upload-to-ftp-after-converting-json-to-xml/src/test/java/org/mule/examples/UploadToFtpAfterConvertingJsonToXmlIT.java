/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

public class UploadToFtpAfterConvertingJsonToXmlIT extends FunctionalTestCase
{
	private static final String PATH_TO_TEST_PROPERTIES = "./src/test/resources/mule.test.properties";
	private static final Logger log = LogManager.getLogger(UploadToFtpAfterConvertingJsonToXmlIT.class); 
	
	private static String PASSWORD;
	private static String USER;
	private static String HOST;
	private static String PORT;
	private static String HOME;
	private static String PATH;
	
	private static String MESSAGE = "";
	private static String REPLY = "";
    @Override
    protected String getConfigResources()
    {
        return "upload-to-ftp.xml";
    }

    @BeforeClass
    public static void init() throws IOException{
    	final Properties props = new Properties();
    	try {
    	props.load(new FileInputStream(PATH_TO_TEST_PROPERTIES));
    	} catch (Exception e) {
    		log.error("Error occured while reading mule.test.properties", e);
    	}
    	try {
			MESSAGE = FileUtils.readFileToString(new File("./src/test/resources/message.json"));
			REPLY = FileUtils.readFileToString(new File("./src/test/resources/reply.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}    	    
    	USER = props.getProperty("ftp.user");
    	PASSWORD = props.getProperty("ftp.password");
    	PORT = props.getProperty("ftp.port");
    	HOME = props.getProperty("ftp.home");		
    	PATH = props.getProperty("ftp.path");
    	HOST = props.getProperty("ftp.host");
    	
    	System.setProperty("ftp.user", USER);
    	System.setProperty("ftp.password", PASSWORD);
    	System.setProperty("ftp.port", PORT);
    	System.setProperty("ftp.path", PATH);
    	System.setProperty("ftp.host", HOST);
		File dataDirectory = new File(HOME);
		if (dataDirectory.exists()) {
		    FileUtils.deleteDirectory(dataDirectory);
		}
		dataDirectory.mkdirs();    			
    }
    
    

    @Test
    public void testDataWeave() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        props.put("Content-Type", "application/json");
        MuleMessage result = client.send("http://0.0.0.0:8081/", MESSAGE, props);
        assertEquals(REPLY.replaceAll("\\s",""), result.getPayloadAsString().replaceAll("\\s",""));
        
    }


	@Override
	protected Properties getStartUpProperties() {
		return new Properties(super.getStartUpProperties());
	}	    
}

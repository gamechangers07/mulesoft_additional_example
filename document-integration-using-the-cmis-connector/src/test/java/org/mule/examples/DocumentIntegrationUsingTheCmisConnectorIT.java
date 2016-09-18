/**
 * MuleSoft Examples
 * Copyright 2014 MuleSoft, Inc.
 *
 * This product includes software developed at
 * MuleSoft, Inc. (http://www.mulesoft.com/).
 */

package org.mule.examples;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;
import org.mule.transport.NullPayload;

public class DocumentIntegrationUsingTheCmisConnectorIT extends FunctionalTestCase
{
	
    private static final String MESSAGE = "{\"id\":\"/okm:root/pic";

	@Override
    protected String getConfigResources()
    {
        return "cmis-document-integration.xml";
    }

    @Test
    public void httpGetToFlowUrlSentMessage() throws Exception
    {
        MuleClient client = new MuleClient(muleContext);
        Map<String, Object> props = new HashMap<String, Object>();
        props.put("http.method", "POST");
        MuleMessage result = client.send("http://localhost:8081/cmis", "", props);
        Thread.sleep(5000);
        assertNotNull(result);
        assertFalse(result.getPayload() instanceof NullPayload);
        assertTrue(result.getPayloadAsString().startsWith(MESSAGE));
                
    }
    
}

/**
 * Copyright 2010 Västra Götalandsregionen
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of version 2.1 of the GNU Lesser General Public
 *   License as published by the Free Software Foundation.
 *
 *   This library is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public
 *   License along with this library; if not, write to the
 *   Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *   Boston, MA 02111-1307  USA
 *
 */

package se.vgregion.alfrescoclient.test;

/**
 * 
 */

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import se.vgregion.alfrescoclient.domain.Events;
import se.vgregion.alfrescoclient.domain.Events.Event;
import se.vgregion.alfrescoclient.domain.Site;
import se.vgregion.alfrescoclient.service.AlfrescoService;

/**
 * Tests for Alfresco Client.
 * 
 * @author Simon Göransson
 * @author Björn Ryding
 * 
 */
public class AlfrescoServiceTest {
    private static final Log LOG = LogFactory.getLog(AlfrescoServiceTest.class);

    AlfrescoService alfrescoService;

    private static final int port = 8899;
    private static final String HEADER_FIELD = "X-Alfresco-Remote-User";
    private static final String USER_ID = "admin";
    private static Server server = new Server(port);
    private static Boolean correctSSOHeader = false;

    private static final String csIframPage = "test-page";
    private static final String portletInstance = "x12345";

    @Before
    public void before() {

        alfrescoService = new AlfrescoService("http://localhost:" + port, HEADER_FIELD);

        server.setHandler(new AbstractHandler() {

            @Override
            public void handle(String target, Request baseRequest, HttpServletRequest httpServletRequest,
                    HttpServletResponse httpServletResponse) throws IOException, ServletException {

                if (httpServletRequest.getHeader(HEADER_FIELD).equals(USER_ID)) {
                    correctSSOHeader = true;
                }

                String request = httpServletRequest.getRequestURI();

                httpServletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = httpServletResponse.getWriter();

                if (request.contains("calendar")) {

                    writer.append("{\"events\": [ { \"name\": \"1332762617639-415.ics\", \"title\": \"Test\", "
                            + "\"where\": \"\", \"when\": \"2012-03-26T12:00:00.000+02:00\", \"url\":"
                            + " \"page/site/redpilllinpro/calendar?date=2012-03-26\", \"start\": \"12:00\","
                            + " \"end\": \"13:00\", \"endDate\" : \"2012-03-26T13:00:00.000+02:00\", \"site\":"
                            + " \"redpilllinpro\", \"siteTitle\": \"Redpill Linpro\", \"allday\": \"false\","
                            + " \"tags\": \"\", \"duration\": \"PT1H\", \"isoutlook\": \"false\" } ] }");

                    writer.close();

                } else {
                    writer.append("[{"
                            + " \"url\" : \"\\/alfresco\\/wcservice\\/api\\/sites\\/apa\","
                            + " \"sitePreset\" : \"site-dashboard\","
                            + " \"shortName\" : \"apa\","
                            + " \"title\" : \"Apa\","
                            + " \"description\" : \"\","
                            + "  \"node\" : \"\\/alfresco\\/wcservice\\/api\\/node\\/workspace\\/SpacesStore\\/b0548376-4678-45bf-962b-7169b13550d3\","
                            + " \"tagScope\" : \"\\/alfresco\\/wcservice\\/api\\/tagscopes\\/workspace\\/SpacesStore\\/b0548376-4678-45bf-962b-7169b13550d3\","
                            + " \"isPublic\" : false,"
                            + " \"visibility\" : \"MODERATED\", "
                            + "\"siteManagers\" : [  \"susro3\"  , \"admin\" ]"
                            + " }"
                            + ","
                            + "{"
                            + " \"url\" : \"\\/alfresco\\/wcservice\\/api\\/sites\\/bepa\","
                            + " \"sitePreset\" : \"site-dashboard\","
                            + " \"shortName\" : \"bepa\","
                            + " \"title\" : \"Bepa\","
                            + " \"description\" : \"\","
                            + "  \"node\" : \"\\/alfresco\\/wcservice\\/api\\/node\\/workspace\\/SpacesStore\\/b0548376-4678-45bf-962b-7169b13550d3\","
                            + " \"tagScope\" : \"\\/alfresco\\/wcservice\\/api\\/tagscopes\\/workspace\\/SpacesStore\\/b0548376-4678-45bf-962b-7169b13550d3\","
                            + " \"isPublic\" : false," + " \"visibility\" : \"MODERATED\", "
                            + "\"siteManagers\" : [  \"susro3\"  , \"admin\" ]" + " }" + " ]");

                    writer.close();
                }

                httpServletResponse.setStatus(HttpServletResponse.SC_OK);

            }

        });

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void verifyNumberOfSites() {

        List<Site> sites = alfrescoService.getSitesByUser(USER_ID, csIframPage, portletInstance);

        assertEquals(2, sites.size());

    }

    @Test
    public void VerifySitesMarshalling() {

        List<Site> sites = alfrescoService.getSitesByUser(USER_ID, csIframPage, portletInstance);

        assertEquals(sites.get(0).getShortName(), "apa");
        assertEquals(sites.get(1).getShortName(), "bepa");

        assertEquals(sites.get(0).getUrl(), "/alfresco/wcservice/api/sites/apa");
        assertEquals(sites.get(1).getUrl(), "/alfresco/wcservice/api/sites/bepa");

    }

    @Test
    public void VerifySitesAlfrescoShareURLs() {

        List<Site> sites = alfrescoService.getSitesByUser(USER_ID, csIframPage, portletInstance);

        assertEquals(
                "test-page/-/autologin/x12345/aHR0cDovL2xvY2FsaG9zdDo4ODk5L3NoYXJlL3BhZ2Uvc2l0ZS9hcGEvZGFzaGJvYXJk",
                sites.get(0).getShareUrl());
        assertEquals(
                "test-page/-/autologin/x12345/aHR0cDovL2xvY2FsaG9zdDo4ODk5L3NoYXJlL3BhZ2Uvc2l0ZS9iZXBhL2Rhc2hib2FyZA",
                sites.get(1).getShareUrl());

    }

    @Test
    public void VerifySSOUserHeader() {

        List<Site> sites = alfrescoService.getSitesByUser(USER_ID, csIframPage, portletInstance);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        assertEquals(true, correctSSOHeader);

    }

    @Test
    public void testGetUserEvents() {

        Events events = alfrescoService.getUserEvents("testUser", new DateTime());

        assertEquals("Test", events.getEvents().get(0).getTitle());

    }

    @Test
    public void testJsonFormating() {

        Event event = new Event();
        event.setTitle("Hej");

        Events events = new Events();

        events.setEvents(Arrays.asList(new Event[]{event}));

        ObjectMapper mapper = new ObjectMapper();
        StringWriter stringWriter = new StringWriter();
        try {
            mapper.writeValue(stringWriter, events);

            assertEquals("{\"events\":[{\"name\":null,\"title\":\"Hej\",\"where\":null,\"when\":null,"
                    + "\"url\":null,\"start\":null,\"end\":null,\"endDate\":null,\"site\":null,"
                    + "\"siteTitle\":null,\"allday\":false,\"tags\":null,\"duration\":null,"
                    + "\"isoutlook\":false}]}", stringWriter.getBuffer().toString());

        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

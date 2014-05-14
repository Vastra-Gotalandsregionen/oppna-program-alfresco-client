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

package se.vgregion.alfrescoclient.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import se.vgregion.alfrescoclient.domain.Document;
import se.vgregion.alfrescoclient.domain.Events;
import se.vgregion.alfrescoclient.domain.RecentModifiedDocuments;
import se.vgregion.alfrescoclient.domain.Site;

/**
 * This service is responsible for the communication with Alfresco server using it´s REST API.
 *
 * @author Simon Göransson
 * @author Björn Ryding
 */
public class AlfrescoService {

    private static final String API_PEOPLE = "/alfresco/wcservice/api/people/";
    private static final String SITES = "/sites";
    private static final String DASHBOARD = "/dashboard";
    private static final String PAGE_SITES = "/share/page/site/";
    private static final String PUBLIC_SITES = "/alfresco/wcservice/vgr/public-sites";
    private static final String RECENTLY_MODIFIED =
            "/alfresco/wcservice/slingshot/doclib/doclist/documents/site/";
    private static final String RECENTLY_MODIFIED_2 = "/documentLibrary?filter=recentlyModified&max=10";
    private static final String EVENT_URL = "/alfresco/wcservice/calendar/events/user?from=";

    private final String server;
    private final String ssoUserHeaderField;

    /**
     * The constructor for the service.
     *
     * @param server Alfresco server base URL.
     * @param ssoUserHeaderField the name of the header to pass to Alfresco for single sign on
     */
    public AlfrescoService(String server, String ssoUserHeaderField) {
        this.server = server;
        this.ssoUserHeaderField = ssoUserHeaderField;
    }

    /**
     * Initialises the RestTemplate.
     *
     * @return the RestTemplate
     */
    private RestTemplate initJsonRestTemplate() {
        RestTemplate template = new RestTemplate();
        CommonsClientHttpRequestFactory requestFactory = new CommonsClientHttpRequestFactory();
        final int timeout = 5000; // Five seconds
        requestFactory.setReadTimeout(timeout);
        template.setRequestFactory(requestFactory);
        HttpMessageConverter<?>[] httpMessageConverters =
                new HttpMessageConverter<?>[]{new MappingJacksonHttpMessageConverter()};
        template.setMessageConverters(Arrays.asList(httpMessageConverters));

        return template;
    }

    /**
     * Sets up the Http request entity.
     *
     * @param ssoUser user id for the user that the API calls should performed as.
     * @return Http entity
     */
    private HttpEntity<String> setUpHttpEntity(String ssoUser) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ssoUserHeaderField, ssoUser);
        HttpEntity<String> httpEntity = new HttpEntity<String>(httpHeaders);

        return httpEntity;

    }

    /**
     * Adds urls to the Alfresco share dashboard for each site.
     *
     * @param list
     * @return sites with dashboard URLs
     */
    private List<Site> addAlfrescoShareDashboardURLs(List<Site> sites, String csIframPage,
                                                     String portletInstance) {
        for (Site site : sites) {
            String shareLink = server + PAGE_SITES + site.getShortName() + DASHBOARD;

            String encodeUrl = Base64.encodeBase64URLSafeString(shareLink.getBytes());

            site.setShareUrl(csIframPage + "/-/autologin/" + portletInstance + "/" + encodeUrl);

        }
        return sites;
    }

    /**
     * This method returns the Alfresco sites that a user is a member of.
     *
     * @param user user id
     * @param csIframePage the url to the page holding the Alfresco CSIframe
     * @param portletInstance the id of the CSIframe portletInstance
     * @return Alfresco sites for a user.
     */
    public List<Site> getSitesByUser(String user, String csIframePage, String portletInstance) {
        RestTemplate template = initJsonRestTemplate();
        String apiURL = server + API_PEOPLE + user + SITES;
        HttpEntity<String> httpEntity = setUpHttpEntity(user);

        ResponseEntity<Site[]> httpResponseEntity =
                template.exchange(apiURL, HttpMethod.GET, httpEntity, Site[].class,
                        new HashMap<String, String>());

        List<Site> sites = Arrays.asList(httpResponseEntity.getBody());
        List<Site> sitesWithDashboardURLs =
                addAlfrescoShareDashboardURLs(sites, csIframePage, portletInstance);

        return sitesWithDashboardURLs;
    }

    /**
     * This method returns the Alfresco sites that a user is a member of.
     *
     * @param user - user id
     * @param csIframePage the url to the page holding the Alfresco CSIframe
     * @param portletInstance the id of the CSIframe portletInstance
     * @return Alfresco sites for a user.
     */
    public List<Site> getPublicSites(String user, String csIframePage, String portletInstance) {
        RestTemplate template = initJsonRestTemplate();
        String apiURL = server + PUBLIC_SITES;
        HttpEntity<String> httpEntity = setUpHttpEntity(user);

        ResponseEntity<Site[]> httpResponseEntity =
                template.exchange(apiURL, HttpMethod.GET, httpEntity, Site[].class,
                        new HashMap<String, String>());

        List<Site> sites = Arrays.asList(httpResponseEntity.getBody());
        List<Site> sitesWithDashboardURLs =
                addAlfrescoShareDashboardURLs(sites, csIframePage, portletInstance);

        return sitesWithDashboardURLs;
    }

    /**
     * Get a list of recently modified {@link Document}s which the given user is allowed to see and which is filtered
     * so that only documents from the given site is returned.
     *
     * @param user the user
     * @param site the site which the {@link Document}s should exist in
     * @return a list of {@link Document}s
     */
    public List<Document> getRecentlyModified(String user, String site) {

        RestTemplate template = initJsonRestTemplate();
        String apiURL = server + RECENTLY_MODIFIED + site + RECENTLY_MODIFIED_2;

        HttpEntity<String> httpEntity = setUpHttpEntity(user);

        ResponseEntity<RecentModifiedDocuments> recentModifiedDocuments =
                template.exchange(apiURL, HttpMethod.GET, httpEntity, RecentModifiedDocuments.class,
                        new HashMap<String, String>());

        return recentModifiedDocuments.getBody().getItems();

    }

    /**
     * The method populates a list of {@link Site}s with recently modified {@link Document}s. The {@link Document}s
     * can then be accessed by {@link se.vgregion.alfrescoclient.domain.Site#getRecentModifiedDocuments()}.
     *
     * @param userId the userId
     * @param sites list of {@link Site}s
     * @return a list of the same {@link Site}s which were given as an argument, populated with recently modified
     * {@link Document}s
     */
    public List<Site> addRecentlyModifiedToSites(String userId, List<Site> sites) {

        for (Site site : sites) {
            site.setRecentModifiedDocuments(getRecentlyModified(userId, site.getShortName()));

        }

        return sites;
    }

    /**
     * Get {@link Events} for a user from a given date.
     * @param user the user
     * @param fromDate the earliest point in time the events should start
     * @return the {@link Events}
     */
    public Events getUserEvents(String user, DateTime fromDate) {

        RestTemplate template = initJsonRestTemplate();

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        String fromDateString = fmt.print(fromDate);

        String apiURL = server + EVENT_URL + fromDateString;
        HttpEntity<String> httpEntity = setUpHttpEntity(user);

        ResponseEntity<Events> httpResponseEntity =
                template.exchange(apiURL, HttpMethod.GET, httpEntity, Events.class,
                        new HashMap<String, String>());

        Events events = httpResponseEntity.getBody();

        return events;

    }
}

package se.vgregion.alfrescoclient.controller;

import static javax.portlet.PortletRequest.USER_INFO;
import static javax.portlet.PortletRequest.P3PUserInfos.USER_LOGIN_ID;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import se.vgregion.alfrescoclient.domain.Document;
import se.vgregion.alfrescoclient.service.AlfrescoService;

import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;

/**
 * The controller for the alfresco sites portlet.
 *
 * @author Simon Göransson
 * @author Björn Ryding
 */

@Controller
@RequestMapping(value = "VIEW")
public class SitesController {

    @Autowired
    private AlfrescoService alfrescoService;

    /**
     * Renders the Alfresco site list view.
     *
     * @param model   the model
     * @param request the request
     * @return the view to render
     */
    @RenderMapping
    public String viewSites(Model model, RenderRequest request) {

        PortletPreferences prefs = request.getPreferences();

        String csIframePage = prefs.getValue("csIframePage", "");
        String portletInstance = prefs.getValue("portalInstance", "");

        String userId = lookupUid(request);

        if (!"".equals(userId) && userId != null) {
            model.addAttribute("userSites", alfrescoService.addRecentlyModifiedToSites(userId,
                    alfrescoService.getSitesByUser(userId, csIframePage, portletInstance)));
            model.addAttribute("publicSites",
                    alfrescoService.getPublicSites(userId, csIframePage, portletInstance));
            return "view";
        }

        return "notloggedin";
    }

    /**
     * The method writes recently modified {@link Document}s as JSON to the response.
     *
     * @param site     the site in which the documents should exist
     * @param request  request
     * @param response response
     */
    @ResourceMapping
    public void serveRecentlyModified(@RequestParam("site") String site, ResourceRequest request,
                                      ResourceResponse response) {

        try {
            String userId = lookupUid(request);

            List<Document> documents = alfrescoService.getRecentlyModified(userId, site);

            JSONArray jsonResult = com.liferay.portal.kernel.json.JSONFactoryUtil.createJSONArray();

            for (Document document : documents) {
                JSONObject jsonRow = JSONFactoryUtil.createJSONObject();
                jsonRow.put("modified", document.getModifiedOn());
                jsonRow.put("name", document.getFileName());
                jsonResult.put(jsonRow);
            }

            JSONObject jsonResponse = JSONFactoryUtil.createJSONObject();
            jsonResponse.put("results", jsonResult);

            response.getWriter().append(jsonResponse.toString());

            response.setContentType("application/json");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Retrieves the user id for the logged in user.
     *
     * @param req the portlet request
     * @return the user id
     */
    @SuppressWarnings("unchecked")
    private String lookupUid(PortletRequest req) {
        Map<String, ?> userInfo = (Map<String, ?>) req.getAttribute(USER_INFO);
        String userId = "";
        if (userInfo != null && userInfo.get(USER_LOGIN_ID.toString()) != null) {
            userId = (String) userInfo.get(USER_LOGIN_ID.toString());
        }
        return userId;
    }

    public void setAlfrescoService(AlfrescoService alfrescoService) {
        this.alfrescoService = alfrescoService;
    }
}

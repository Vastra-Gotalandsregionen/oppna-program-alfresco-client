package se.vgregion.alfrescoclient.controller;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.util.ParamUtil;

/**
 * Controller class for the edit mode.
 *
 * @author simongoransson
 */

@Controller
@RequestMapping(value = "EDIT")
public class EditController {
    private static final Log LOG = LogFactory.getLog(EditController.class);

    /**
     * Default mapping method.
     *
     * @param renderRequest  renderRequest
     * @param renderResponse renderRequest
     * @return the edit view
     * @throws IOException      IOException
     * @throws PortletException PortletException
     */
    @RenderMapping
    public String doEdit(RenderRequest renderRequest, RenderResponse renderResponse) throws IOException,
            PortletException {

        PortletPreferences prefs = renderRequest.getPreferences();

        renderRequest.setAttribute("csIframePage", prefs.getValue("csIframePage", ""));
        renderRequest.setAttribute("portalInstance", prefs.getValue("portalInstance", ""));

        return "edit";
    }

    /**
     * Save preferences.
     *
     * @param request  request
     * @param response response
     */
    @ActionMapping(params = "action=savePref")
    public void savePref(ActionRequest request, ActionResponse response) {

        String csIframePage = ParamUtil.getString(request, "csIframePage");
        String portalInstance = ParamUtil.getString(request, "portalInstance");

        try {
            PortletPreferences prefs = request.getPreferences();
            prefs.setValue("csIframePage", csIframePage);
            prefs.setValue("portalInstance", portalInstance);
            prefs.store();
        } catch (ReadOnlyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ValidatorException e) {
            e.printStackTrace();
        }
    }

}

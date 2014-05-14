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

package se.vgregion.alfrescoclient.domain;

import java.util.List;

/**
 * This class represents an Alfresco site.
 * 
 * @author Simon Göransson
 * @author Björn Ryding
 * 
 */
public class Site {

    private String url;
    private String sitePreset;
    private String shortName;
    private String title;
    private String description;
    private String node;
    private String tagScope;
    private List<String> siteManagers;
    private String isPublic;
    private String visibility;
    private String shareUrl;
    private List<Document> recentModifiedDocuments;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSitePreset() {
        return sitePreset;
    }

    public void setSitePreset(String sitePreset) {
        this.sitePreset = sitePreset;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
    }

    public String getTagScope() {
        return tagScope;
    }

    public void setTagScope(String tagScope) {
        this.tagScope = tagScope;
    }

    public List<String> getSiteManagers() {
        return siteManagers;
    }

    public void setSiteManagers(List<String> siteManagers) {
        this.siteManagers = siteManagers;
    }

    public String getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(String isPublic) {
        this.isPublic = isPublic;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public List<Document> getRecentModifiedDocuments() {
        return recentModifiedDocuments;
    }

    public void setRecentModifiedDocuments(List<Document> recentModifiedDocuments) {
        this.recentModifiedDocuments = recentModifiedDocuments;
    }

}

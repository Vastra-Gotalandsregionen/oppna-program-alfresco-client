/**
 * 
 */
package se.vgregion.alfrescoclient.domain;

import java.util.List;

/**
 * Domain class representing Alfresco events.
 *
 * @author simongoransson
 */
public class Events {

    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public static class Event {

        private String name;
        private String title;
        private String where;
        private String when;
        private String url;
        private String start;
        private String end;
        private String endDate;
        private String site;
        private String siteTitle;
        private boolean allday;
        private String tags;
        private String duration;
        private boolean isoutlook;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getWhere() {
            return where;
        }

        public void setWhere(String where) {
            this.where = where;
        }

        public String getWhen() {
            return when;
        }

        public void setWhen(String when) {
            this.when = when;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public String getSiteTitle() {
            return siteTitle;
        }

        public void setSiteTitle(String siteTitle) {
            this.siteTitle = siteTitle;
        }

        public boolean isAllday() {
            return allday;
        }

        public void setAllday(boolean allday) {
            this.allday = allday;
        }

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public boolean isIsoutlook() {
            return isoutlook;
        }

        public void setIsoutlook(boolean isoutlook) {
            this.isoutlook = isoutlook;
        }

    }
}

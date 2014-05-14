/**
 * 
 */
package se.vgregion.alfrescoclient.domain;

import java.util.ArrayList;

/**
 * Domain class representing recently modified {@link Document}s and related metadata.
 *
 * @author simongoransson
 */
public class RecentModifiedDocuments {

    private String totalRecords;
    private String startIndex;
    private Object metadata;
    private ArrayList<Document> items;

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    public String getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(String startIndex) {
        this.startIndex = startIndex;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public ArrayList<Document> getItems() {
        return items;
    }

    public void setItems(ArrayList<Document> items) {
        this.items = items;
    }

}

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0"%>

<portlet:defineObjects />
<div class="alfresco-sites-wrap">

  <div class="header-bar">
    <h2>Mina webbplatser i Alfresco</h2>
  </div>
  
  <ul class="alfresco-sites-results-list clearfix">
    <c:forEach items="${userSites}" var="site" varStatus="rowCounter">
      <li class="${rowCounter.count==1?'first':''}">
        <div class="icon"><a href="${site.shareUrl}"><img src="<%=request.getContextPath()%>/images/folder-32.png"></a></div>
        <div class="details"> 
            <h4>
                <a  href="${site.shareUrl}" title="${site.description}">${site.title}</a>
            </h4>
        </div>
        <table class="alfresco-site-table">
          <thead>
            <tr>
              <th colspan="3">Senast uppdaterade dokument</th>
              </tr>
          </thead>
          <tbody>
          
            <c:forEach items="${site.recentModifiedDocuments}" var="document" varStatus="rowCounter">
              <tr class="${rowCounter.count%2==0?'even':'odd'}">           
                <td>${document.modifiedDate} </td>
                <td>${document.fileName}</td>
                <td>ver: ${document.version}</td>
                </tr>
             </c:forEach>
          </tbody>
        </table>
        <!-- <a class="show-more-documents-private" href="#" data-site="${site.shortName}">Visa fler</a>  -->
      </li>
    </c:forEach>
  </ul>


  <div class="header-bar">
    <h2>Publika webbplatser i Alfresco</h2>
  </div>
  <ul class="alfresco-sites-results-list clearfix">
    <c:forEach items="${publicSites}" var="site" varStatus="rowCounter">
      <li class="${rowCounter.count==1?'first':''}">
        <div class="icon"><a href="${site.shareUrl}"><img src="<%=request.getContextPath()%>/images/folder-32.png"></a></div>
        <div class="details"> 
            <h4>
                <a href="${site.shareUrl}" title="${site.description}">${site.title}</a>
            </h4>
        </div>
        <!-- <a class="show-more-documents-public" href="#" data-site="${site.shortName}">Visa senaste dokument</a>  -->
      </li>
    </c:forEach>
  </ul>
</div>



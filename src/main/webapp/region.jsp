<%@ include file="header.jsp" %>


   <h1><fmt:message key="msg.region.title" /></h1>
   <a href="regions?action=new"><fmt:message key="msg.region.add" /></a>
   <table border="1">
       <thead>
           <tr>
               <th><fmt:message key="msg.region.id" /></th>
               <th><fmt:message key="msg.region.code" /></th>
               <th><fmt:message key="msg.region.name" /></th>
               <th><fmt:message key="msg.region.actions" /></th>
           </tr>
       </thead>
       <tbody>
           <c:forEach var="region" items="${listRegions}">
               <tr>
                   <td>${region.id}</td>
                   <td>${region.code}</td>
                   <td>${region.name}</td>
                   <td>
                       <a href="regions?action=edit&id=${region.id}"><fmt:message key="msg.region.edit" /></a>
                       <form action="regions" method="post" style="display:inline;">
                           <input type="hidden" name="action" value="delete" />
                           <input type="hidden" name="id" value="${region.id}" />
                           <!-- Cuando una cadena de caracteres incluye a otra esta debe llevar comillas simples -->
                           <input type="submit" value="<fmt:message key='msg.region.delete' />" onclick="return confirm('<fmt:message key='msg.region.confirm' />')" />
                       </form>
                   </td>
               </tr>
           </c:forEach>
       </tbody>
   </table>
<%@ include file="footer.jsp" %>

<%@ include file="header.jsp" %>

   <h1>
       <c:choose>
           <c:when test="${region == null}">
               <fmt:message key="msg.region-form.add" />
           </c:when>
           <c:otherwise>
               <fmt:message key="msg.region-form.edit" />
           </c:otherwise>
       </c:choose>
   </h1>


   <form action="regions" method="post">
       <input type="hidden" name="id" value="${region != null ? region.id : ''}" />
       <input type="hidden" name="action" value="${region == null ? 'insert' : 'update'}" />


       <label for="code"><fmt:message key='msg.region-form.code' />:</label>
       <input type="text" name="code" id="code" value="${region != null ? region.code : ''}" required />

       <select name="id_region">
           <option value="1">Andalucía</option>
           <option value="2">Cataluña</option>
           <option value="3">Madrid</option>
       </select>


       <!-- Cuando una cadena de caracteres incluye a otra esta debe llevar comillas simples -->
       <c:choose>
           <c:when test="${region == null}">
               <input type="submit" value="<fmt:message key='msg.region-form.create' />" />
           </c:when>
           <c:otherwise>
               <input type="submit" value="<fmt:message key='msg.region-form.update' />" />
           </c:otherwise>
       </c:choose>

   </form>


   <a href="regions"><fmt:message key="msg.region-form.returnback" /></a>


<%@ include file="footer.jsp" %>

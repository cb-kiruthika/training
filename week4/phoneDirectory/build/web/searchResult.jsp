<%-- 
    Document   : searchResult
    Created on : May 24, 2018, 11:34:56 AM
    Author     : cb-kiruthika
--%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    HashMap<String, ArrayList<String>> contacts = (HashMap<String, ArrayList<String>>) request.getAttribute("contacts");
    String find = (String) request.getAttribute("find");
%>
<!DOCTYPE html>
<html>
    <head>
        <link rel="stylesheet" href="style.css" type="text/css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Result</title>
    </head>
    <body>
        <div class="header">
            <div class="headerText">
                Phone Directory
            </div>
                
        </div>

        <%if (contacts != null) {%>
        <h2>Contacts matching "<%= find%>"</h2>
        <div class="resultTable">
            <table>
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Address</th>
                        <th>Mobile</th>
                        <th>Home</th>
                        <th>Work</th>
                    </tr>
                </thead>
                <tbody>
                    <%

                        for (int i = 0; i < contacts.get("name").size(); i++) {

                    %>

                    <tr>
                        <td><%=contacts.get("name").get(i)%></td>
                        <td><%=contacts.get("address").get(i)%></td>
                        <td><%=contacts.get("mobile").get(i)%></td>
                        <td><%=contacts.get("home").get(i)%></td>
                        <td><%=contacts.get("work").get(i)%></td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>

        </div>
        <%} else {%>
        <div>
            <h2>No Matches Found for<%=find%></h2>
        </div>
        <%}%>
        <div>
            <form action="index.html">
                <button class="submit" id="back">GO BACK</button>
            </form>
        </div>
    </body>
</html>

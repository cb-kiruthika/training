<%-- 
    Document   : info
    Created on : May 22, 2018, 11:06:30 AM
    Author     : cb-kiruthika
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%

    HttpSession sn = request.getSession();
    String firstName = (String) sn.getAttribute("firstName");
    String lastName = (String) sn.getAttribute("lastName");
    String email = (String) sn.getAttribute("email");
    String address = (String) sn.getAttribute("address");
    if (address== null) {
        address = "NA";
    }

%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css_1.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
        <title>Info</title>
    </head>
    <body>
        <div class='header'>
            <div class='headerText'>
                Self Service Portal
            </div>
            <div>
                <button class='headerButton' id="deactivate" >Deactivate</button>
            </div>
            <div >
                <form action="logout">
                    <input type="hidden" name="type" value="logout">
                <button class='headerButton' id="logout">Log Out</button>
                </form>
            </div>
            
        </div>
        <div class='centerBlockParent'>
            <div class='centerBlock'>
        <h1>Hello!</h1>
        <table>
        <tr class="details">
            <td class="col1">Name:</td><td class="col2"> <%= firstName + " " + lastName%></td>
        </tr>
        <tr class="details">
            <td class="col1">Email:</td><td class="col2"> <%= email%></td>
        </tr>
        <tr class="details">
            <td class="col1">Address: </td><td class="col2"><%= address%></td>
        </tr>
        </table>
        <hr>
        <div class="buttons">
            <div >
                <button class ='button' id="addDetail">ADD DETAILS</button>
            </div>
            <div >
                <button class='button' id="editDetail">EDIT DETAILS</button>
            </div>
            
        </div>
        <div id="addPopup" style="display: none">
            <form action="update">
                <input type="hidden" name="type" value="add">
                <label for="newAddress">Address: </label>
                <input type="text" name="newAddress">
                <input class='button' type="submit" value="UPDATE">
            </form>
                <button class="close">CANCEL</button>
        </div>
        <div id="editPopup" style="display: none">
            <form action="update">
                <input type="hidden" name="type" value="edit">
                <label for="newFirstName">First Name:  </label>
                <input type="text" name="newFirstName">
                <label for="newLastName">Last Name:  </label>
                <input type="text" name="newLastName">
                <label for="newAddress">Address:  </label>
                <input type="text" name="newAddress">
                <input class='button' type="submit" value="UPDATE">
            </form>
                <button class="close">CANCEL</button>
        </div>
        <div id="deactivatePopup" style="display: none">
            <form action="logout">
                <input type="hidden" name="type" value="deactivate">
                Are you sure???
                <input class='button' type="submit" value="DEACTIVATE">
            </form>
                <button class="close">CLOSE</button>
            
        </div>
        </div>
        </div>
        <script>
            $("#addDetail").click(function () {
                $(".buttons").hide();
                $("#addPopup").show();
            });
            $("#editDetail").click(function () {
                $(".buttons").hide();
                $("#editPopup").show();
            });
            $("#deactivate").click(function () {
                $(".buttons").hide();
                $("#deactivatePopup").show();
            });
            $(".close").click(function () {
                $(".buttons").show();
                $(this).parent().hide();
            });
        </script>
    </body>
</html>

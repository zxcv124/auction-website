<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <title>Rating</title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="/css/skeleton.css" rel="stylesheet">
    <link href="/css/organism.css" rel="stylesheet">
</head>
<body>

<div class="container">
    <c:if test="${empty user}">
        <c:redirect url="/"/>
    </c:if>
    <c:if test="${not empty param.aid}">
        <!-- HEADER STUFF -->
        <div class="row">
            <div class="one column">
                <a href="/user/homepage.jsp">
                    <img class="u-max-full-width" src="/images/logo.png">
                </a>
            </div>
            <div class="offset-by-seven four columns">
                <ul class="nav u-full-width row">
                    <li class="offset-by-one-third one-third column newMessage tooltip"><span class="tooltipFire">Messages</span>
                        <div class="tooltipText"><div class="tooltipMargin"></div>
                            <a href="/message.do?action=listInbox">Inbox</a>
                            <br/>
                            <a href="/message.do?action=listSent">Sent</a>
                        </div>
                    </li>
                    <li class="one-third column">
                        <a href="/logout.do"><span class="delete">Logout</span></a>
                    </li>
                </ul>
            </div>
        </div>
        <!-- end of header row -->

        <!-- Rating -->
        <div class="row">
            <main class="u-full-width ">
                <div class="row u-full-width">
                    <c:if test="${not empty rating}">
                        <h6>You have already rated ${to_user.firstname} ${to_user.lastname} with <b>${rating}</b>.</h6>
                        <form action="/rate.do" method="POST">
                            <input type="hidden" name="to_id" value=${to_id} />
                            <input type="hidden" name="aid" value=${aid} />
                            <input type="number" min="0" max="10" value=${rating} name="rating" required>
                            <button class="button-primary" type="submit" name="action" value="updateRating">Update rating</button>
                        </form>
                    </c:if>
                    <c:if test="${empty rating}">
                        <h6>You have not rated ${to_user.firstname} ${to_user.lastname} yet</h6>
                        <form action="/rate.do" method="POST">
                            <input type="hidden" name="to_id" value=${to_id} />
                            <input type="hidden" name="aid" value=${aid} />
                            <input type="number" min="0" max="10" name="rating" required>
                            <button class="button-primary" type="submit" name="action" value="addRating">Rate</button>
                        </form>
                    </c:if>
                </div>
            </main>
        </div>
        <!-- end of rating row -->
    </c:if>
</div>

</body>
</html>

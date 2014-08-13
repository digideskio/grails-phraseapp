<%--
  User: tamer
  Date: 13/08/14
  Time: 19:51
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Phraseapp Grails Plugin Console</title>
</head>

<body>

<g:if test="${flash.message}">
    <div class="info">
        ${flash.message}
    </div>
</g:if>

<g:if test="${flash.error}">
    <div class="error">
        ${flash.error}
    </div>
</g:if>

<div id="downloadAndUpdate">
    <g:link controller="phraseappConsole" action="update">
        Download
    </g:link> latest Translations from Phraseapp and update Grails
</div>

<br/>

<div id="createSnapshot">
    <g:link controller="phraseappConsole" action="create">
        Create
    </g:link> a new Snapshot
</div>

<br/>

<div id="restoreSnapshot">
    <g:if test="${snapshots}">
        These are the Snapshot taken so far:
        <ul>
            <g:each in="${snapshots}" var="item">
                <li>Snapshot created at ${new Date(item as Long)}
                <g:link controller="phraseappConsole" action="restore" id="${item}">
                    | restore
                </g:link>
                <g:link controller="phraseappConsole" action="delete" id="${item}">
                    | delete
                </g:link>
                </li>
            </g:each>
        </ul>
    </g:if>
    <g:else>
        No Snapshot available.
    </g:else>
</div>

</body>
</html>
<!DOCTYPE html>
<html>
<head lang="en">
    <title>Spring Boot Demo - FreeMarker</title>
</head>
<body>
<h2>首页<h2>
<#--<#include "${path}.ftl"/>-->
    <#--<font>-->
    <#--<#list userList as item>-->
    <#--${item!}<br />-->
    <#--</#list>-->
        <#--<@-->
    <#--</font>-->

    <#--<button class="a"> click me</button>-->
    <@common tpl="head.ftl" defaultTpl="defaultHead.ftl"/>
    <@includeX path="${path}"/>
    <@common tpl="footer.ftl"  defaultTpl="defaultFooter.ftl"/>
</body>
</html>
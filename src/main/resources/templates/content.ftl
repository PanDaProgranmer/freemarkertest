<div>
<@portal_fn name="test" service="testService" method="test" param_a="a" cacheMin="1">
    <#list result as val>
       <div>内容1${val.a}</div>
    </#list>
</@portal_fn>
</div>